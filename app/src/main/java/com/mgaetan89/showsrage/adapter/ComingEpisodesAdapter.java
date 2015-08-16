package com.mgaetan89.showsrage.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.databinding.AdapterComingEpisodesListBinding;
import com.mgaetan89.showsrage.model.ComingEpisode;
import com.mgaetan89.showsrage.presenter.ComingEpisodePresenter;

import java.util.Collections;
import java.util.List;

public class ComingEpisodesAdapter extends RecyclerView.Adapter<ComingEpisodesAdapter.ViewHolder> {
	@NonNull
	private List<ComingEpisode> comingEpisodes = Collections.emptyList();

	public interface OnEpisodeActionSelectedListener {
		void onEpisodeActionSelected(int seasonNumber, int episodeNumber, int indexerId, MenuItem action);
	}

	public ComingEpisodesAdapter(@Nullable List<ComingEpisode> comingEpisodes) {
		if (comingEpisodes == null) {
			this.comingEpisodes = Collections.emptyList();
		} else {
			this.comingEpisodes = comingEpisodes;
		}
	}

	@Override
	public int getItemCount() {
		return this.comingEpisodes.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		ComingEpisode comingEpisode = this.comingEpisodes.get(position);

		holder.binding.setComingEpisode(new ComingEpisodePresenter(comingEpisode));
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		AdapterComingEpisodesListBinding binding = AdapterComingEpisodesListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

		return new ViewHolder(binding);
	}

	public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
		@NonNull
		public final AdapterComingEpisodesListBinding binding;

		public ViewHolder(@NonNull AdapterComingEpisodesListBinding binding) {
			super(binding.getRoot());

			this.binding = binding;

			if (this.binding.listContent.episodeActions != null) {
				this.binding.listContent.episodeActions.setOnClickListener(this);
			}
		}

		@Override
		public void onClick(View view) {
			PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
			popupMenu.inflate(R.menu.episode_action);
			popupMenu.setOnMenuItemClickListener(this);
			popupMenu.show();
		}

		@Override
		public boolean onMenuItemClick(MenuItem item) {
			if (this.binding.listContent.episodeActions != null) {
				Context context = this.binding.listContent.episodeActions.getContext();

				if (context instanceof OnEpisodeActionSelectedListener) {
					ComingEpisode comingEpisode = ComingEpisodesAdapter.this.comingEpisodes.get(this.getAdapterPosition());

					if (comingEpisode != null) {
						((OnEpisodeActionSelectedListener) context).onEpisodeActionSelected(comingEpisode.getSeason(), comingEpisode.getEpisode(), comingEpisode.getIndexerId(), item);
					}

					return true;
				}
			}

			return false;
		}
	}
}
