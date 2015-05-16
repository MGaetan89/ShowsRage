package com.mgaetan89.showsrage.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.mgaetan89.showsrage.Constants;
import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.ShowsRageApplication;
import com.mgaetan89.showsrage.adapter.ShowPagerAdapter;
import com.mgaetan89.showsrage.model.Seasons;
import com.mgaetan89.showsrage.model.Show;
import com.mgaetan89.showsrage.network.SickRageApi;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ShowFragment extends Fragment implements Callback<Seasons> {
	@Inject
	public SickRageApi api;

	@Nullable
	private ShowPagerAdapter adapter = null;

	@NonNull
	private final List<Integer> seasons = new ArrayList<>();

	public ShowFragment() {
	}

	@Override
	public void failure(RetrofitError error) {
		error.printStackTrace();
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		Intent intent = this.getActivity().getIntent();
		Show show = (Show) intent.getSerializableExtra(Constants.Bundle.SHOW_MODEL);

		this.api.getServices().getSeasons(show.getIndexerId(), this);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		((ShowsRageApplication) this.getActivity().getApplication()).inject(this);
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_show, container, false);

		if (view != null) {
			MaterialViewPager viewPager = (MaterialViewPager) view.findViewById(R.id.show_pager);

			if (viewPager != null) {
				this.adapter = new ShowPagerAdapter(this.getChildFragmentManager(), this, this.seasons);

				viewPager.getToolbar().setVisibility(View.GONE);
				viewPager.getViewPager().setAdapter(this.adapter);
				viewPager.getPagerTitleStrip().setViewPager(viewPager.getViewPager());
			}
		}

		return view;
	}

	@Override
	public void onDestroy() {
		this.seasons.clear();

		super.onDestroy();
	}

	@Override
	public void success(Seasons seasons, Response response) {
		this.seasons.clear();
		this.seasons.addAll(seasons.getData());

		if (this.adapter != null) {
			this.adapter.notifyDataSetChanged();
		}
	}
}
