package com.mgaetan89.showsrage

import io.realm.RealmList
import org.assertj.core.api.Assertions.assertThat
import java.util.Comparator

fun <T> buildComparator(valueExtractor: (T) -> String?, descending: Boolean = false): Comparator<T> {
	return Comparator { first, second ->
		val firstValue = valueExtractor(first)
		val secondValue = valueExtractor(second)
		val descendingOffset = if (descending) -1 else 1

		when {
			firstValue == secondValue -> 0
			firstValue == null -> descendingOffset * -1
			secondValue == null -> descendingOffset * 1
			else -> descendingOffset * firstValue.compareTo(secondValue)
		}
	}
}

fun validateRealmList(actual: RealmList<String>?, expected: RealmList<String>?) {
	if (expected == null) {
		assertThat(actual).isNull()
	} else {
		assertThat(actual).isNotNull()
		assertThat(actual).hasSize(expected.size)

		actual!!.forEachIndexed { i, item ->
			assertThat(item).isEqualTo(expected[i])
		}
	}
}
