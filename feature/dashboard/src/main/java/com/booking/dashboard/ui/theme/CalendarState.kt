package com.booking.dashboard.ui.theme

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import java.util.stream.Collectors
import java.util.stream.Stream

data class CalendarDates(
    var selectedDate: Date,
    var visibleDates: List<Date>
) {

    data class Date(
        var date: LocalDate,
        var isSelected: Boolean,
        val isToday: Boolean
    ) {
        var day: String = date.format(DateTimeFormatter.ofPattern("E"))
        var month: String =
            date.month.getDisplayName(TextStyle.SHORT_STANDALONE, Locale.ENGLISH)
    }
}

fun getDates(
    lastSelectedDate: LocalDate
): CalendarDates {
    val firstDayOfWeek = lastSelectedDate.with(DayOfWeek.MONDAY)
    firstDayOfWeek.plusDays(5)
    val visibleDates = getDatesBetween(firstDayOfWeek)
    return CalendarDates(
        selectedDate = CalendarDates.Date(
            date = lastSelectedDate,
            isToday = lastSelectedDate.isEqual(LocalDate.now()),
            isSelected = true
        ),
        visibleDates = visibleDates.map {
            CalendarDates.Date(
                date = it,
                isToday = it.isEqual(LocalDate.now()),
                isSelected = it.isEqual(lastSelectedDate)
            )
        }
    )
}

private fun getDatesBetween(startDate: LocalDate): List<LocalDate> {
    return Stream.iterate(startDate) { date ->
        date.plusDays(/* daysToAdd = */ 1)
    }
        .limit(5)
        .collect(Collectors.toList())
}

fun LocalDate.toIso(): String {
    return format(DateTimeFormatter.ISO_DATE).toString()
}
