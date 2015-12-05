package com.mgaetan89.showsrage.model;

import com.google.gson.annotations.SerializedName;

public class Serie {
	@SerializedName("Actors")
	private String actors;

	@SerializedName("Awards")
	private String awards;

	@SerializedName("Country")
	private String country;

	@SerializedName("Director")
	private String director;

	@SerializedName("Genre")
	private String genre;

	@SerializedName("imdbID")
	private String imdbId;

	private String imdbRating;

	private String imdbVotes;

	@SerializedName("Language")
	private String language;

	@SerializedName("Metascore")
	private String metascore;

	@SerializedName("Plot")
	private String plot;

	@SerializedName("Poster")
	private String poster;

	@SerializedName("Rated")
	private String rated;

	@SerializedName("Released")
	private String released;

	@SerializedName("Response")
	private String response;

	@SerializedName("Runtime")
	private String runtime;

	@SerializedName("Title")
	private String title;

	@SerializedName("Type")
	private String type;

	@SerializedName("Writer")
	private String writer;

	@SerializedName("Year")
	private String year;

	public String getActors() {
		return this.actors;
	}

	public String getAwards() {
		return this.awards;
	}

	public String getCountry() {
		return this.country;
	}

	public String getDirector() {
		return this.director;
	}

	public String getGenre() {
		return this.genre;
	}

	public String getImdbId() {
		return this.imdbId;
	}

	public String getImdbRating() {
		return this.imdbRating;
	}

	public String getImdbVotes() {
		return this.imdbVotes;
	}

	public String getLanguage() {
		return this.language;
	}

	public String getMetascore() {
		return this.metascore;
	}

	public String getPlot() {
		return this.plot;
	}

	public String getPoster() {
		return this.poster;
	}

	public String getRated() {
		return this.rated;
	}

	public String getReleased() {
		return this.released;
	}

	public String getResponse() {
		return this.response;
	}

	public String getRuntime() {
		return this.runtime;
	}

	public String getTitle() {
		return this.title;
	}

	public String getType() {
		return this.type;
	}

	public String getWriter() {
		return this.writer;
	}

	public String getYear() {
		return this.year;
	}
}
