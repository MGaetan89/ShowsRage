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
import com.mgaetan89.showsrage.databinding.AdapterEpisodesListBinding;
import com.mgaetan89.showsrage.model.Episode;
import com.mgaetan89.showsrage.presenter.EpisodePresenter;

import java.util.Collections;
import java.util.List;

public class EpisodesAdapter extends RecyclerView.Adapter<EpisodesAdapter.ViewHolder> {
	@NonNull
	private List<Episode> episodes = Collections.emptyList();

	private final int seasonNumber;

	public interface OnEpisodeActionSelectedListener {
		void onEpisodeActionSelected(int seasonNumber, int episodeNumber, MenuItem action);
	}

	public interface OnEpisodeSelectedListener {
		void onEpisodeSelected(int seasonNumber, int episodeNumber, @NonNull Episode episode, int episodesCount);
	}

	public EpisodesAdapter(@Nullable List<Episode> episodes, int seasonNumber) {
		if (episodes == null) {
			this.episodes = Collections.emptyList();
		} else {
			this.episodes = episodes;
		}

		this.seasonNumber = seasonNumber;
	}

	@Override
	public int getItemCount() {
		return this.episodes.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		Episode episode = this.episodes.get(position);
		episode.setNumber(this.getItemCount() - position);

		holder.binding.setEpisode(new EpisodePresenter(episode, holder.binding.getRoot().getContext()));
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		AdapterEpisodesListBinding binding = AdapterEpisodesListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

		return new ViewHolder(binding);
	}

	public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
		@NonNull
		public final AdapterEpisodesListBinding binding;

		public ViewHolder(@NonNull AdapterEpisodesListBinding binding) {
			super(binding.getRoot());

			binding.getRoot().setOnClickListener(this);

			this.binding = binding;

			if (this.binding.listContent.episodeActions != null) {
				this.binding.listContent.episodeActions.setOnClickListener(this);
			}
		}

		@Override
		public void onClick(View view) {
			Context context = view.getContext();

			if (view.getId() == R.id.episode_actions) {
				PopupMenu popupMenu = new PopupMenu(context, view);
				popupMenu.inflate(R.menu.episode_action);
				popupMenu.setOnMenuItemClickListener(this);
				popupMenu.show();
			} else {
				EpisodesAdapter adapter = EpisodesAdapter.this;
				Episode episode = adapter.episodes.get(this.getAdapterPosition());

				if (context instanceof OnEpisodeSelectedListener && episode != null) {
					int itemCount = adapter.getItemCount();

					((OnEpisodeSelectedListener) context).onEpisodeSelected(adapter.seasonNumber, itemCount - this.getAdapterPosition(), episode, itemCount);
				}
			}
		}

		@Override
		public boolean onMenuItemClick(MenuItem item) {
			if (this.binding.listContent.episodeActions != null) {
				Context context = this.binding.listContent.episodeActions.getContext();

				if (context instanceof OnEpisodeActionSelectedListener) {
					EpisodesAdapter adapter = EpisodesAdapter.this;

					((OnEpisodeActionSelectedListener) context).onEpisodeActionSelected(adapter.seasonNumber, adapter.getItemCount() - this.getAdapterPosition(), item);

					return true;
				}
			}

			return false;
		}
	}
}
