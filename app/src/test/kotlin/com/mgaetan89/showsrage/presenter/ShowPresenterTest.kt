package com.mgaetan89.showsrage.presenter

// TODO Move to Android Test
/*
@RunWith(Parameterized::class)
class ShowPresenterTest {
    @Parameterized.Parameter(1)
    var bannerUrl: String? = null

    @Parameterized.Parameter(2)
    var downloaded: Int = 0

    @Parameterized.Parameter(3)
    var episodesCount: Int = 0

    @Parameterized.Parameter(4)
    var network: String? = null

    @Parameterized.Parameter(5)
    var paused: Boolean = false

    @Parameterized.Parameter(6)
    var posterUrl: String? = null

    @Parameterized.Parameter(7)
    var quality: String? = null

    @Parameterized.Parameter(0)
    var show: Show? = null

    @Parameterized.Parameter(8)
    var showName: String? = null

    @Parameterized.Parameter(9)
    var snatched: Int = 0

    private lateinit var presenter: ShowPresenter

    @Before
    fun before() {
        this.presenter = ShowPresenter(this.show)
    }

    @Test
    fun getBannerUrl() {
        assertThat(this.presenter.getBannerUrl()).isEqualTo(this.bannerUrl)
    }

    @Ignore
    @Test
    fun getDownloaded() {
        assertThat(this.presenter.getDownloaded()).isEqualTo(this.downloaded)
    }

    @Ignore
    @Test
    fun getEpisodesCount() {
        assertThat(this.presenter.getEpisodesCount()).isEqualTo(this.episodesCount)
    }

    @Test
    fun getNetwork() {
        assertThat(this.presenter.getNetwork()).isEqualTo(this.network)
    }

    @Test
    fun getPosterUrl() {
        assertThat(this.presenter.getPosterUrl()).isEqualTo(this.posterUrl)
    }

    @Test
    fun getQuality() {
        assertThat(this.presenter.getQuality()).isEqualTo(this.quality)
    }

    @Test
    fun getShowName() {
        assertThat(this.presenter.getShowName()).isEqualTo(this.showName)
    }

    @Ignore
    @Test
    fun getSnatched() {
        assertThat(this.presenter.getSnatched()).isEqualTo(this.snatched)
    }

    @Test
    fun isPaused() {
        assertThat(this.presenter.isPaused()).isEqualTo(this.paused)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<Any?>> {
            val gson = SickRageApi.gson

            return listOf(
                    arrayOf(null, "", 0, 0, "", false, "", "", "", 0),
                    arrayOf(gson.fromJson("{downloaded: 10, episodesCount: 20, network: \"ABC\", paused: 0, quality: \"HD\", show_name: \"Show 1\", snatched: 5, tvdbid: 123}", Show::class.java), "https://127.0.0.1:8083/api/apiKey/?cmd=show.getbanner&tvdbid=123", 10, 20, "ABC", false, "https://127.0.0.1:8083/api/apiKey/?cmd=show.getposter&tvdbid=123", "HD", "Show 1", 5),
                    arrayOf(gson.fromJson("{downloaded: 20, episodesCount: 30, network: \"CBS\", paused: 1, quality: \"HD1080p\", show_name: \"Show 2\", snatched: 10, tvdbid: 456}", Show::class.java), "https://127.0.0.1:8083/api/apiKey/?cmd=show.getbanner&tvdbid=456", 20, 30, "CBS", true, "https://127.0.0.1:8083/api/apiKey/?cmd=show.getposter&tvdbid=456", "HD1080p", "Show 2", 10)
            )
        }
    }
}
*/
