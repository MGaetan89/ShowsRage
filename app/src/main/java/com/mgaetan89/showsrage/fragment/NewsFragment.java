package com.mgaetan89.showsrage.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mgaetan89.showsrage.Constants;
import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.adapter.ReleasesAdapter;
import com.mgaetan89.showsrage.model.MarkdownParseBody;
import com.mgaetan89.showsrage.model.Release;
import com.mgaetan89.showsrage.model.ReleasesResponse;
import com.mgaetan89.showsrage.network.GitHubApi;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

public class NewsFragment extends Fragment implements Callback<ReleasesResponse> {
	@Nullable
	private ReleasesAdapter adapter = null;

	private GitHubApi gitHubApi = null;

	@NonNull
	private final List<Release> releases = new ArrayList<>();

	@Nullable
	private RecyclerView recyclerView = null;

	public NewsFragment() {
	}

	@Override
	public void failure(RetrofitError error) {
		error.printStackTrace();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		RestAdapter restAdapter = new RestAdapter.Builder()
				.setEndpoint("https://api.github.com/")
				.setLogLevel(Constants.NETWORK_LOG_LEVEL)
				.build();

		this.gitHubApi = restAdapter.create(GitHubApi.class);
		this.gitHubApi.getReleases(this);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_news, container, false);

		if (view != null) {
			this.recyclerView = (RecyclerView) view.findViewById(android.R.id.list);

			if (this.recyclerView != null) {
				this.adapter = new ReleasesAdapter(this.releases);

				this.recyclerView.setAdapter(this.adapter);
				this.recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
			}
		}

		return view;
	}

	@Override
	public void onDestroy() {
		this.releases.clear();

		super.onDestroy();
	}

	@Override
	public void onDestroyView() {
		this.recyclerView = null;

		super.onDestroyView();
	}

	@Override
	public void success(final ReleasesResponse releases, Response response) {
		this.releases.clear();

		final GitHubApi gitHubApiLocal = this.gitHubApi;

		new Thread(new Runnable() {
			@Override
			public void run() {
				final Runnable runnable = this;

				for (Release release : releases) {
					MarkdownParseBody markdownParseBody = new MarkdownParseBody(release.getBody(), MarkdownParseBody.Mode.gfm, null);

					gitHubApiLocal.parseMarkdown(markdownParseBody, new MarkdownParserCallback(NewsFragment.this, release, runnable));

					try {
						synchronized (runnable) {
							runnable.wait();
						}
					} catch (InterruptedException exception) {
						exception.printStackTrace();
					}
				}
			}
		}).start();
	}

	private static final class MarkdownParserCallback implements Callback<Response> {
		@NonNull
		private WeakReference<NewsFragment> fragmentReference = new WeakReference<>(null);

		private Release release = null;

		@NonNull
		private WeakReference<Runnable> runnableReference = new WeakReference<>(null);

		private MarkdownParserCallback(NewsFragment newsFragment, Release release, Runnable runnable) {
			this.fragmentReference = new WeakReference<>(newsFragment);
			this.release = release;
			this.runnableReference = new WeakReference<>(runnable);
		}

		@Override
		public void failure(RetrofitError error) {
			error.printStackTrace();
		}

		@Override
		public void success(Response response, Response ignored) {
			this.release.setHtmlBody(new String(((TypedByteArray) response.getBody()).getBytes()));

			NewsFragment fragment = this.fragmentReference.get();
			Runnable runnable = this.runnableReference.get();

			if (fragment != null) {
				fragment.releases.add(this.release);

				if (fragment.adapter != null) {
					fragment.adapter.notifyItemInserted(fragment.releases.size() - 1);
				}
			}

			if (runnable != null) {
				synchronized (runnable) {
					runnable.notify();
				}
			}
		}
	}
}
