package com.mgaetan89.showsrage.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.mgaetan89.showsrage.Constants;
import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.fragment.ShowsFragment;
import com.mgaetan89.showsrage.model.RootDir;
import com.mgaetan89.showsrage.model.RootDirs;
import com.mgaetan89.showsrage.network.SickRageApi;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ShowsActivity extends BaseActivity {
	@Override
	protected boolean displayHomeAsUp() {
		return false;
	}

	@Override
	protected Fragment getFragment() {
		return new ShowsFragment();
	}

	@Override
	protected int getSelectedMenuId() {
		return R.id.menu_shows;
	}

	@Override
	protected int getTitleResourceId() {
		return R.string.shows;
	}

	@Override
	protected void onResume() {
		super.onResume();

		SickRageApi.getInstance().getServices().getRootDirs(new RootDirsCallback(this));
	}

	private static final class RootDirsCallback implements Callback<RootDirs> {
		@NonNull
		private WeakReference<Activity> activityReference = new WeakReference<>(null);

		private RootDirsCallback(Activity activity) {
			this.activityReference = new WeakReference<>(activity);
		}

		@Override
		public void failure(RetrofitError error) {
			error.printStackTrace();
		}

		@Override
		public void success(RootDirs rootDirs, Response response) {
			Activity activity = this.activityReference.get();

			if (activity == null || rootDirs == null) {
				return;
			}

			List<RootDir> data = rootDirs.getData();
			Set<String> rootPaths = new HashSet<>(data.size());

			for (RootDir rootDir : data) {
				rootPaths.add(rootDir.getLocation());
			}

			SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(activity).edit();
			editor.putStringSet(Constants.Preferences.Fields.ROOT_DIRS, rootPaths);
			editor.apply();
		}
	}
}
