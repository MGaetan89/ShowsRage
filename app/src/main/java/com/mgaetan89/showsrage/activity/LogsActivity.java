package com.mgaetan89.showsrage.activity;

import android.os.Bundle;

import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.fragment.LogsFragment;

public class LogsActivity extends BaseActivity {
	@Override
	protected int getSelectedMenuId() {
		return R.id.menu_logs;
	}

	@Override
	protected int getTitleResourceId() {
		return R.string.logs;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null) {
			this.getSupportFragmentManager().beginTransaction()
					.replace(R.id.content, new LogsFragment())
					.commit();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		this.displayHomeAsUp(false);
	}
}
