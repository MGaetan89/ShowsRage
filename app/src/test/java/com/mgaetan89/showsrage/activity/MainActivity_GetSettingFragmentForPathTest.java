package com.mgaetan89.showsrage.activity;

import com.mgaetan89.showsrage.fragment.SettingsAboutFragment;
import com.mgaetan89.showsrage.fragment.SettingsAboutLicensesFragment;
import com.mgaetan89.showsrage.fragment.SettingsAboutShowsRageFragment;
import com.mgaetan89.showsrage.fragment.SettingsBehaviorFragment;
import com.mgaetan89.showsrage.fragment.SettingsDisplayFragment;
import com.mgaetan89.showsrage.fragment.SettingsExperimentalFeaturesFragment;
import com.mgaetan89.showsrage.fragment.SettingsFragment;
import com.mgaetan89.showsrage.fragment.SettingsServerApiKeyFragment;
import com.mgaetan89.showsrage.fragment.SettingsServerFragment;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class MainActivity_GetSettingFragmentForPathTest {
	@Parameterized.Parameter(1)
	public Class<SettingsFragment> fragmentClass;

	@Parameterized.Parameter(0)
	public String path;

	@Test
	public void getSettingFragmentForPath() {
		if (this.fragmentClass == null) {
			assertThat(MainActivity.getSettingFragmentForPath(this.path)).isNull();
		} else {
			assertThat(MainActivity.getSettingFragmentForPath(this.path)).isExactlyInstanceOf(this.fragmentClass);
		}
	}

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][]{
				{null, null},
				{"", null},
				{" ", null},
				{"/", SettingsFragment.class},
				{"/wrong_path", null},
				{"/about", SettingsAboutFragment.class},
				{"/about/licenses", SettingsAboutLicensesFragment.class},
				{"/about/showsrage", SettingsAboutShowsRageFragment.class},
				{"/about", SettingsAboutFragment.class},
				{"/behavior", SettingsBehaviorFragment.class},
				{"/display", SettingsDisplayFragment.class},
				{"/experimental_features", SettingsExperimentalFeaturesFragment.class},
				{"/server", SettingsServerFragment.class},
				{"/server/api_key", SettingsServerApiKeyFragment.class},
		});
	}
}
