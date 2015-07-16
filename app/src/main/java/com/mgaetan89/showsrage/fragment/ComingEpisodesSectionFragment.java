package com.mgaetan89.showsrage.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mgaetan89.showsrage.Constants;
import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.adapter.ComingEpisodesAdapter;
import com.mgaetan89.showsrage.model.ComingEpisode;

import java.util.ArrayList;
import java.util.List;

public class ComingEpisodesSectionFragment extends Fragment {
	@Nullable
	private ComingEpisodesAdapter adapter = null;

	@Nullable
	private TextView emptyView = null;

	@NonNull
	private final List<ComingEpisode> comingEpisodes = new ArrayList<>();

	@Nullable
	private RecyclerView recyclerView = null;

	public ComingEpisodesSectionFragment() {
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		Bundle arguments = this.getArguments();

		if (arguments != null) {
			ArrayList<ComingEpisode> comingEpisodes = (ArrayList<ComingEpisode>) arguments.getSerializable(Constants.Bundle.COMING_EPISODES);

			if (comingEpisodes != null) {
				this.comingEpisodes.addAll(comingEpisodes);
			}
		}

		if (this.comingEpisodes.isEmpty()) {
			if (this.emptyView != null) {
				this.emptyView.setVisibility(View.VISIBLE);
			}

			if (this.recyclerView != null) {
				this.recyclerView.setVisibility(View.GONE);
			}
		} else {
			if (this.emptyView != null) {
				this.emptyView.setVisibility(View.GONE);
			}

			if (this.recyclerView != null) {
				this.recyclerView.setVisibility(View.VISIBLE);
			}
		}

		if (this.adapter != null) {
			this.adapter.notifyDataSetChanged();
		}
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_coming_episodes_section, container, false);

		if (view != null) {
			this.emptyView = (TextView) view.findViewById(android.R.id.empty);
			this.recyclerView = (RecyclerView) view.findViewById(android.R.id.list);

			if (this.recyclerView != null) {
				int columnCount = this.getResources().getInteger(R.integer.shows_column_count);
				this.adapter = new ComingEpisodesAdapter(this.comingEpisodes);

				this.recyclerView.setAdapter(this.adapter);
				this.recyclerView.setLayoutManager(new GridLayoutManager(this.getActivity(), columnCount));
			}
		}

		return view;
	}

	@Override
	public void onDestroy() {
		this.comingEpisodes.clear();

		super.onDestroy();
	}

	@Override
	public void onDestroyView() {
		this.emptyView = null;
		this.recyclerView = null;

		super.onDestroyView();
	}
}
