package com.mgaetan89.showsrage.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mgaetan89.showsrage.databinding.AdapterShowsListBinding;
import com.mgaetan89.showsrage.model.Show;
import com.mgaetan89.showsrage.presenter.ShowPresenter;

import java.util.Collections;
import java.util.List;

public class ShowsAdapter extends RecyclerView.Adapter<ShowsAdapter.ViewHolder> {
	@NonNull
	private List<Show> shows = Collections.emptyList();

	public interface OnShowSelectedListener {
		void onShowSelected(@NonNull Show show);
	}

	public ShowsAdapter(@Nullable List<Show> shows) {
		if (shows == null) {
			this.shows = Collections.emptyList();
		} else {
			this.shows = shows;
		}
	}

	@Override
	public int getItemCount() {
		return this.shows.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		Show show = this.shows.get(position);

		holder.binding.setShow(new ShowPresenter(show, holder.binding.getRoot().getContext()));
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		AdapterShowsListBinding binding = AdapterShowsListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

		return new ViewHolder(binding);
	}

	public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		@NonNull
		public final AdapterShowsListBinding binding;

		public ViewHolder(@NonNull AdapterShowsListBinding binding) {
			super(binding.getRoot());

			binding.getRoot().setOnClickListener(this);

			this.binding = binding;
		}

		@Override
		public void onClick(View view) {
			Context context = view.getContext();
			Show show = ShowsAdapter.this.shows.get(this.getAdapterPosition());

			if (context instanceof OnShowSelectedListener && show != null) {
				((OnShowSelectedListener) context).onShowSelected(show);
			}
		}
	}
}
