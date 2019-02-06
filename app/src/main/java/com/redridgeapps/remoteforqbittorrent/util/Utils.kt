package com.redridgeapps.remoteforqbittorrent.util

private const val SI_UNITS = 1000
private const val BINARY_UNITS = 1024

fun Long.humanReadableByteCount(isSpeed: Boolean = false, si: Boolean = false): String {
    val bytes = this
    val unit = if (si) SI_UNITS else BINARY_UNITS
    val speed = if (isSpeed) "/s" else ""

    if (bytes < unit) return "$bytes B$speed"

    val exp = (Math.log(bytes.toDouble()) / Math.log(unit.toDouble())).toInt()
    val pre = (if (si) "kMGTPE" else "KMGTPE")[exp - 1] + if (si) "" else "i"

    return String.format(
            "%.1f %sB%s",
            bytes / Math.pow(unit.toDouble(), exp.toDouble()),
            pre,
            speed
    )
}

fun <T> lazyUnsynchronized(initializer: () -> T) = lazy(LazyThreadSafetyMode.NONE, initializer)
