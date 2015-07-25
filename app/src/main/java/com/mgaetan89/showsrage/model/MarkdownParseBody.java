package com.mgaetan89.showsrage.model;

import android.support.annotation.Nullable;

import java.io.Serializable;

public class MarkdownParseBody implements Serializable {
	private static final long serialVersionUID = -9001789380256669780L;

	private String context = "";
	private Mode mode = Mode.markdown;
	private String text = "";

	public enum Mode {
		gfm, markdown
	}

	public MarkdownParseBody() {
	}

	public MarkdownParseBody(@Nullable String text, @Nullable Mode mode, @Nullable String context) {
		if (text == null) {
			this.text = "";
		} else {
			this.text = text;
		}

		if (mode == null) {
			this.mode = Mode.markdown;
		} else {
			this.mode = mode;
		}

		if (context == null && Mode.gfm == this.mode) {
			this.context = "gollum/gollum";
		} else {
			this.context = context;
		}
	}
}
