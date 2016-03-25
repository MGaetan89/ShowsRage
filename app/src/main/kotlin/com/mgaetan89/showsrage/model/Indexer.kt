package com.mgaetan89.showsrage.model

class Indexer private constructor(val paramName: String) {
    companion object {
        val TVDB = Indexer("tvdbid")
        val TVRAGE = Indexer("tvrageid")
    }
}
