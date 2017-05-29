package com.mgaetan89.showsrage.model

sealed class Indexer(val paramName: String) {
	object TVDB : Indexer("tvdbid")
	object TVRAGE : Indexer("tvrageid")
}
