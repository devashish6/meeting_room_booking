package com.booking.dashboard.ui.theme

import android.util.Log
import android.view.ContextThemeWrapper
import android.widget.CalendarView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

private const val TAG = "DASHBOARD_SCREEN_DEBUG"

@Composable
fun DashboardScreen() {
    var dates by remember {
        mutableStateOf(getDates(lastSelectedDate = LocalDate.now()))
    }
    Column {
        CalendarPickerBar(
            headerDate = dates.selectedDate.date
        ) {
            dates.selectedDate.date = it
            dates = getDates(lastSelectedDate = it)
        }
        DatePickerHeader(
            dates = dates,
            onDateClickListener = { date ->
                dates = dates.copy(
                    selectedDate = date,
                    visibleDates = dates.visibleDates.map {
                        it.copy(isSelected = it.date.isEqual(date.date))
                    }
                )
            }
        )
        SlotsBooked(hourHeight = 64.dp)
    }
}

@Composable
fun CalendarPickerBar(
    headerDate: LocalDate,
    updateSelectedDate: (LocalDate) -> Unit
) {
    var showDialog by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = headerDate.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy")),
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier
                .clickable {
                    showDialog = true
                }
        )
    }
    if (showDialog) {
        DatePicker(
            onDateSelected = { updateSelectedDate(it) },
            onDismissRequest = { showDialog = false },
            selectedDate = headerDate
        )
    }
}

@Composable
fun DatePicker(
    onDateSelected: (LocalDate) -> Unit,
    onDismissRequest: () -> Unit,
    selectedDate: LocalDate
) {
    val selDate = remember { mutableStateOf(selectedDate) }

    Dialog(onDismissRequest = { onDismissRequest() }, properties = DialogProperties()) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(size = 16.dp)
                )
        ) {
            Column(
                Modifier
                    .defaultMinSize(minHeight = 72.dp)
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                    )
                    .padding(16.dp)
            ) {
                Text(
                    text = "Select date".uppercase(Locale.ENGLISH),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )

                Spacer(modifier = Modifier.size(24.dp))

                Text(
                    text = selDate.value.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy")),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )

                Spacer(modifier = Modifier.size(16.dp))
            }

            CustomCalendarView(onDateSelected = {
                selDate.value = it
            })

            Spacer(modifier = Modifier.size(8.dp))

            Row(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(bottom = 16.dp, end = 16.dp)
            ) {
                TextButton(
                    onClick = onDismissRequest
                ) {
                    Text(
                        text = "Cancel",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                TextButton(
                    onClick = {
                        onDateSelected(selDate.value)
                        onDismissRequest()
                    }
                ) {
                    Text(
                        text = "OK",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

            }
        }
    }
}

@Composable
fun CustomCalendarView(onDateSelected: (LocalDate) -> Unit) {
    AndroidView(
        modifier = Modifier.wrapContentSize(),
        factory = {
            CalendarView(ContextThemeWrapper(it, androidx.appcompat.R.style.AlertDialog_AppCompat))
        },
        update = { view ->
            view.setOnDateChangeListener { _, year, month, dayOfMonth ->
                onDateSelected(
                    LocalDate
                        .now()
                        .withMonth(month + 1)
                        .withYear(year)
                        .withDayOfMonth(dayOfMonth)
                )
            }
        }
    )
}

@Composable
fun DatePickerHeader(
    dates: CalendarDates,
    onDateClickListener: (CalendarDates.Date) -> Unit
) {
    Column {
        Log.d(TAG, "DatePickerHeader-2: $dates")
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 60.dp),
            modifier = Modifier.padding(start = 70.dp)
        ) {
            items(dates.visibleDates.size) { index ->
                DateItem(
                    date = dates.visibleDates[index],
                    onDateClickListener = { onDateClickListener(it) }
                )
            }
        }
    }
}

@Composable
fun DateItem(
    date: CalendarDates.Date,
    onDateClickListener: (CalendarDates.Date) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .clickable {
                onDateClickListener(date)
            },
        colors = CardDefaults.cardColors(
            containerColor = if (date.isSelected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.secondary
            }
        ),
    ) {
        Column(
            modifier = Modifier
                .size(70.dp)
                .padding(5.dp)
        ) {
            Text(
                text = date.day,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(1.dp),
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = date.month,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(1.dp),
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = date.date.dayOfMonth.toString(),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(1.dp),
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }

}

@Composable
fun SlotsBooked(
    hourHeight: Dp,
    minTime: LocalTime = LocalTime.of(10, 0),
    maxTime: LocalTime = LocalTime.of(19, 0),
    label: @Composable (time: LocalTime) -> Unit = { BasicSidebarLabel(time = it) },
) {
    val numMinutes = ChronoUnit.MINUTES.between(minTime, maxTime).toInt() + 1
    val numHours = numMinutes / 60
    val firstHour = minTime.truncatedTo(ChronoUnit.HOURS)
    val firstHourOffsetMinutes =
        if (firstHour == minTime) 0 else ChronoUnit.MINUTES.between(minTime, firstHour.plusHours(1))
    val firstHourOffset = hourHeight * (firstHourOffsetMinutes / 60f)
    val startTime = if (firstHour == minTime) firstHour else firstHour.plusHours(1)
    Column {
        Spacer(modifier = Modifier.height(firstHourOffset))
        repeat(numHours) { i ->
            Box(modifier = Modifier.height(hourHeight)) {
                label(startTime.plusHours(i.toLong()))
            }
        }
    }
}

@Composable
fun BasicSidebarLabel(
    time: LocalTime,
) {
    Text(
        text = time.format(DateTimeFormatter.ofPattern("h a")),
        modifier = Modifier
            .fillMaxHeight()
            .padding(4.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    DashboardScreen()
}