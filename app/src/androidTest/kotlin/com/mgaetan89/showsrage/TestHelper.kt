package com.mgaetan89.showsrage

import android.content.Context
import com.mgaetan89.showsrage.model.RealmString
import io.realm.RealmList
import org.assertj.core.api.Assertions.assertThat
import java.util.*

fun <T> buildComparator(valueExtractor: (T) -> String?, descending: Boolean = false): Comparator<T> {
    return Comparator { first, second ->
        val firstValue = valueExtractor(first)
        val secondValue = valueExtractor(second)
        val descendingOffset = if (descending) -1 else 1

        if (firstValue == secondValue) {
            0
        } else if (firstValue == null) {
            descendingOffset * -1
        } else if (secondValue == null) {
            descendingOffset * 1
        } else {
            descendingOffset * firstValue.compareTo(secondValue)
        }
    }
}

fun initRealm(context: Context, testContext: Context) {
    /*
    Realm.init(context)

    val configuration = RealmConfiguration.Builder().let {
        it.assetFile("test.realm")
        it.schemaVersion(Constants.DATABASE_VERSION)
        it.migration(Migration())
        it.build()
    }

    Realm.deleteRealm(configuration)
    Realm.setDefaultConfiguration(configuration)
    */
}

fun validateRealmList(actual: RealmList<RealmString>?, expected: RealmList<RealmString>?) {
    if (expected == null) {
        assertThat(actual).isNull()
    } else {
        assertThat(actual).isNotNull()
        assertThat(actual).hasSize(expected.size)

        actual!!.forEachIndexed { i, item ->
            assertThat(item.value).isEqualTo(expected[i].value)
        }
    }
}
