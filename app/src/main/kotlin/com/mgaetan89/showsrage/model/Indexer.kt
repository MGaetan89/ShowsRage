package com.mgaetan89.showsrage.model

sealed class Indexer(val paramName: String) {
	object TMDB : Indexer("tmdbid")
	object TVDB : Indexer("tvdbid")
	object TVMAZE : Indexer("tvmazeid")
	object TVRAGE : Indexer("tvrageid")
}
