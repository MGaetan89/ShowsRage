package com.mgaetan89.showsrage.fragment;

import com.mgaetan89.showsrage.R;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SettingsAboutLicensesFragmentTest {
	@Test
	public void getTitleResourceId() {
		assertThat(new SettingsAboutLicensesFragment().getTitleResourceId()).isEqualTo(R.string.licenses);
	}

	@Test
	public void getXmlResourceFile() {
		assertThat(new SettingsAboutLicensesFragment().getXmlResourceFile()).isEqualTo(R.xml.settings_about_licenses);
	}
}
