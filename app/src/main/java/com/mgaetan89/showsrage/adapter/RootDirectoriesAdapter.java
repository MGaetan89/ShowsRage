package com.mgaetan89.showsrage.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Set;

public class RootDirectoriesAdapter extends ArrayAdapter<String> {
	public RootDirectoriesAdapter(Context context, Set<String> rootDirectories) {
		super(context, android.R.layout.simple_list_item_1, new ArrayList<>(rootDirectories));
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);

		if (view instanceof TextView) {
			((TextView) view).setText(this.getItem(position));
		}

		return view;
	}
}
