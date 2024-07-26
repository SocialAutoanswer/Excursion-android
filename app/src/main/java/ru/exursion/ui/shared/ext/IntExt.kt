package ru.exursion.ui.shared.ext

import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter

fun Int.toTimeFormat(): String = DateTimeFormatter.ofPattern("mm:ss")
    .withZone(ZoneId.of("UTC"))
    .format(Instant.ofEpochSecond(this.toLong()))