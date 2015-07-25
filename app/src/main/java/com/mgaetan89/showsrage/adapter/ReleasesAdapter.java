package com.mgaetan89.showsrage.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.model.Release;

import java.util.Collections;
import java.util.List;

public class ReleasesAdapter extends RecyclerView.Adapter<ReleasesAdapter.ViewHolder> {
	@NonNull
	private List<Release> releases = Collections.emptyList();

	public ReleasesAdapter(@Nullable List<Release> releases) {
		if (releases == null) {
			this.releases = Collections.emptyList();
		} else {
			this.releases = releases;
		}
	}

	@Override
	public int getItemCount() {
		return this.releases.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		Release release = this.releases.get(position);

		if (holder.body != null) {
			holder.body.setText(Html.fromHtml(release.getHtmlBody()));
		}

		if (holder.date != null) {
			holder.date.setText(release.getPublishedAt());
		}

		if (holder.name != null) {
			holder.name.setText(release.getName());
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_releases_list, parent, false);

		return new ViewHolder(view);
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {
		@Nullable
		public final TextView body;

		@Nullable
		public final TextView date;

		@Nullable
		public final TextView name;

		public ViewHolder(View view) {
			super(view);

			this.body = (TextView) view.findViewById(R.id.release_body);
			this.date = (TextView) view.findViewById(R.id.release_date);
			this.name = (TextView) view.findViewById(R.id.release_name);
		}
	}
}
