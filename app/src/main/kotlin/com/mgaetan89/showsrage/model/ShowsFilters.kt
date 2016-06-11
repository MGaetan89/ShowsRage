package com.mgaetan89.showsrage.model

import com.mgaetan89.showsrage.R

object ShowsFilters {
    enum class State {
        ACTIVE, ALL, PAUSED;

        companion object {
            fun getStateForViewId(id: Int?): State {
                return when (id) {
                    R.id.filter_active -> ACTIVE
                    R.id.filter_all -> ALL
                    R.id.filter_paused -> PAUSED
                    else -> ALL
                }
            }

            fun getViewIdForState(state: State): Int {
                return when (state) {
                    ACTIVE -> R.id.filter_active
                    ALL -> R.id.filter_all
                    PAUSED -> R.id.filter_paused
                }
            }
        }
    }

    enum class Status(val status: Int) {
        CONTINUING(1),
        ENDED(2),
        UNKNOWN(4),
        ALL(CONTINUING.status or ENDED.status or UNKNOWN.status);

        companion object {
            fun getStatusForStates(all: Boolean?, continuing: Boolean?, ended: Boolean?, unknown: Boolean?): Int {
                if (all ?: false) {
                    return ALL.status
                }

                var status = 0

                if (continuing ?: false) {
                    status = status or CONTINUING.status
                }

                if (ended ?: false) {
                    status = status or ENDED.status
                }

                if (unknown ?: false) {
                    status = status or UNKNOWN.status
                }

                return if (status == 0) ALL.status else status
            }

            fun isAll(status: Int): Boolean {
                return (status and ALL.status) == ALL.status
            }

            fun isContinuing(status: Int): Boolean {
                return (status and CONTINUING.status) == CONTINUING.status
            }

            fun isEnded(status: Int): Boolean {
                return (status and ENDED.status) == ENDED.status
            }

            fun isUnknown(status: Int): Boolean {
                return (status and UNKNOWN.status) == UNKNOWN.status
            }
        }
    }
}
