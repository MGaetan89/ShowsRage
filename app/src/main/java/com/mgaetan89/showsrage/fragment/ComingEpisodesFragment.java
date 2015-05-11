package com.mgaetan89.showsrage.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.mgaetan89.showsrage.ShowsRageApplication;
import com.mgaetan89.showsrage.model.ComingEpisodes;
import com.mgaetan89.showsrage.network.SickRageApi;

import javax.inject.Inject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ComingEpisodesFragment extends Fragment implements Callback<ComingEpisodes> {
	@Inject
	public SickRageApi api;

	public ComingEpisodesFragment() {
	}

	@Override
	public void failure(RetrofitError error) {
		error.printStackTrace();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		this.api.getServices().getComingEpisodes(this);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		((ShowsRageApplication) this.getActivity().getApplication()).inject(this);
	}

	@Override
	public void success(ComingEpisodes comingEpisodes, Response response) {
	}
}
