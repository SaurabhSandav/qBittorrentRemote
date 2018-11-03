package com.redridgeapps.remoteforqbittorrent.ui.log.model

import com.redridgeapps.remoteforqbittorrent.R

data class FilterState(
        val normal: Boolean = true,
        val info: Boolean = true,
        val warning: Boolean = true,
        val critical: Boolean = true
)

enum class LogFilter(val resId: Int) {
    NORMAL(R.string.log_type_normal),
    INFO(R.string.log_type_info),
    WARNING(R.string.log_type_warning),
    CRITICAL(R.string.log_type_critical);
}

fun FilterState.asLogFilterList(): List<LogFilter> = ArrayList<LogFilter>().apply {
    if (normal) add(LogFilter.NORMAL)
    if (info) add(LogFilter.INFO)
    if (warning) add(LogFilter.WARNING)
    if (critical) add(LogFilter.CRITICAL)
}

fun List<LogFilter>.getFilterState() = FilterState(
        normal = contains(LogFilter.NORMAL),
        info = contains(LogFilter.INFO),
        warning = contains(LogFilter.WARNING),
        critical = contains(LogFilter.CRITICAL)
)
