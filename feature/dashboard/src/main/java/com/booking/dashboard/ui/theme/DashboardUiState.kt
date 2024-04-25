package com.booking.dashboard.ui.theme

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import java.util.stream.Collectors
import java.util.stream.Stream

//interface DashboardUiState {
data class CalendarDates(
    var selectedDate: Date,
    var visibleDates: List<Date>
) {
    var firstDate = visibleDates.first()
    var lastDate = visibleDates.last()

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
    startDate: LocalDate = LocalDate.now(),
    lastSelectedDate: LocalDate
): CalendarDates {
    val firstDayOfWeek = startDate.with(DayOfWeek.MONDAY)
    val endDayOfWeek = firstDayOfWeek.plusDays(5)
    val visibleDates = getDatesBetween(firstDayOfWeek, endDayOfWeek)
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

private fun getDatesBetween(startDate: LocalDate, endDate: LocalDate): List<LocalDate> {
    return Stream.iterate(startDate) { date ->
        date.plusDays(/* daysToAdd = */ 1)
    }
        .limit(5)
        .collect(Collectors.toList())
}
//}