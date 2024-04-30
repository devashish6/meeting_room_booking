package com.booking.dashboard.ui.theme

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.booking.model.model.BookedMeetingRoom
import com.booking.ui.CustomCalendar
import com.booking.ui.Loading
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.math.roundToInt

private const val TAG = "DASHBOARD_SCREEN_DEBUG"

@Composable
fun DashboardRoute(
    viewModel: DashboardViewModel = hiltViewModel(),
    navigateToBooking: () -> Unit
) {
    val dashboardUiState = viewModel.dashboardUiState.collectAsStateWithLifecycle()
//    viewModel.createBookingUiState()
    val bookingsUiState = viewModel.bookingUiState.collectAsStateWithLifecycle(
        BookingsUiState("",
            listOf()
        )
    )
    val bookedMeetingRooms = viewModel.bookedMeetings.collectAsStateWithLifecycle()
    DashboardScreen(
        dashboardUiState = dashboardUiState.value,
        fetchMeetingsForTheDate = { viewModel.getBookedTimeslots() },
        updateSelectedDate = { viewModel.selectedDate.value = it },
        navigateToBooking = navigateToBooking,
        bookedMeetingRooms = bookingsUiState.value.bookedSlots
    )
}

@Composable
fun DashboardScreen(
    dashboardUiState: DashboardUiState = DashboardUiState.None,
    fetchMeetingsForTheDate: (String) -> Unit = { _ -> },
    updateSelectedDate: (String) -> Unit = { _ -> },
    navigateToBooking: () -> Unit = {},
    bookedMeetingRooms: List<BookedMeetingRoom>?
) {
    var dates by remember {
        mutableStateOf(getDates(lastSelectedDate = LocalDate.now()))
    }
    Log.d(TAG, "DashboardScreen: ${dates.selectedDate.date}")
    Log.d(TAG, "DashboardScreen: fetching slots from screen")
    updateSelectedDate(dates.selectedDate.date.toString())
    fetchMeetingsForTheDate(dates.selectedDate.date.toString())
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navigateToBooking.invoke() },
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(text = "Book")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            CalendarPickerBar(
                headerDate = dates.selectedDate.date,
            ) {
                updateSelectedDate(it.toString())
                dates.selectedDate.date = it
                Log.d(TAG, "DashboardScreen: fetching data after change in date from calendar")
                fetchMeetingsForTheDate(it.toString())
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
                    updateSelectedDate(date.date.toString())
                    Log.d(TAG, "DashboardScreen: fetching data from date picker click")
                    fetchMeetingsForTheDate(date.date.toString())
                }
            )
            Row {
                ScheduleSidebar(
                    hourHeight = 64.dp,
                    modifier = Modifier.padding(end = 24.dp),
                    label = { BasicSidebarLabel(time = it) }
                )
                when (dashboardUiState) {
                    is DashboardUiState.Success -> {
                        BasicMeetingSchedule(
                            bookedMeetingRoom = bookedMeetingRooms ?: emptyList(),
                            eventContent = {BasicEvent(bookedMeetingRoom = it)},
                            modifier = Modifier
                                .weight(1f)
                                .verticalScroll(rememberScrollState())
                        )
                    }
                    is DashboardUiState.Loading -> {Loading()}
                    is DashboardUiState.None -> {}
                }

            }
        }
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
            text = headerDate.toString(),
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier
                .clickable {
                    showDialog = true
                }
        )
    }
    if (showDialog) {
        CustomCalendar(
            onDateSelected = { updateSelectedDate(it) },
            onDismissRequest = { showDialog = false },
            selectedDate = headerDate
        )
    }
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
fun ScheduleSidebar(
    hourHeight: Dp,
    modifier: Modifier = Modifier,
    minTime: LocalTime = LocalTime.of(10, 0),
    maxTime: LocalTime = LocalTime.of(20, 0),
    label: @Composable (time: LocalTime) -> Unit
) {
    val numMinutes = ChronoUnit.MINUTES.between(minTime, maxTime).toInt() + 1
    val numHours = numMinutes / 60
    val firstHour = minTime.truncatedTo(ChronoUnit.HOURS)
    val firstHourOffsetMinutes =
        if (firstHour == minTime) 0 else ChronoUnit.MINUTES.between(minTime, firstHour.plusHours(1))
    val firstHourOffset = hourHeight * (firstHourOffsetMinutes / 60f)
    val startTime = if (firstHour == minTime) firstHour else firstHour.plusHours(1)
    Column(modifier = modifier) {
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
    modifier: Modifier = Modifier,
) {
    Text(
        text = time.format(DateTimeFormatter.ofPattern("h a")),
        modifier = modifier
            .fillMaxHeight()
            .padding(4.dp)
    )
}

@Composable
fun BasicEvent(
    bookedMeetingRoom: BookedMeetingRoom,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(end = 2.dp)
            .background(
                color = MaterialTheme.colorScheme.inversePrimary,
            )
            .padding(4.dp)
    ) {
        Text(
            text = "${bookedMeetingRoom.fromTime} - ${bookedMeetingRoom.toTime}",
            style = MaterialTheme.typography.labelSmall,
        )

        Text(
            text = bookedMeetingRoom.meetingTitle,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
fun BasicMeetingSchedule(
    bookedMeetingRoom: List<BookedMeetingRoom>,
    modifier: Modifier = Modifier,
    eventContent: @Composable (bookedMeetingRoom: BookedMeetingRoom) -> Unit
) {
    val hourHeight = 64.dp
    val positionedBookings = remember(bookedMeetingRoom) {
        arrangeEvents(events = bookedMeetingRoom)
    }
    Layout(
        content =
        {
            positionedBookings
                .forEach { bookedMeetingRoom ->
                    Box(modifier = Modifier.eventData(bookedMeetingRoom)) {
                        eventContent(bookedMeetingRoom)
                    }
                }
        },
        modifier = modifier
    ) { measurables, constraints ->
        val height = hourHeight.roundToPx() * 24
        val placeablesWithEvents = measurables.map { measurable ->
            val event = measurable.parentData as BookedMeetingRoom
            val eventDurationMinutes = ChronoUnit.MINUTES.between(
                LocalTime.of(event.fromTime.toInt(), 0),
                LocalTime.of(event.toTime.toInt(), 0)
            )
            val eventHeight = ((eventDurationMinutes / 60f) * hourHeight.toPx()).roundToInt()
            val eventWidth =
                ((event.colSpan.toFloat() / event.colTotal.toFloat()) * (256.dp).toPx()).roundToInt()
            val placeable = measurable.measure(
                constraints.copy(
                    minWidth = eventWidth,
                    maxWidth = eventWidth,
                    minHeight = eventHeight,
                    maxHeight = eventHeight
                )
            )
            Pair(placeable, event)
        }
        layout(constraints.minWidth, height) {
            placeablesWithEvents.forEach { (placeable, event) ->
                val eventOffsetMinutes = ChronoUnit.MINUTES.between(
                    LocalTime.of(10, 0),
                    LocalTime.of(event.fromTime.toInt(), 0)
                )
                val eventY = ((eventOffsetMinutes / 60f) * hourHeight.toPx()).roundToInt()
                val eventX = (event.col * ((256.dp).toPx() / event.colTotal.toFloat())).roundToInt()

                placeable.place(eventX, eventY)
            }
        }
    }
}

private fun arrangeEvents(events: List<BookedMeetingRoom>): List<BookedMeetingRoom> {
    val positionedEvents = mutableListOf<BookedMeetingRoom>()
    val groupEvents: MutableList<MutableList<BookedMeetingRoom>> = mutableListOf()

    fun resetGroup() {
        groupEvents.forEachIndexed { colIndex, col ->
            col.forEach { e ->
                positionedEvents.add(e.copy(col = colIndex, colTotal = groupEvents.size))
            }
        }
        groupEvents.clear()
    }

    events.forEach { event ->
        var firstFreeCol = -1
        var numFreeCol = 0
        for (i in 0 until groupEvents.size) {
            val col = groupEvents[i]
            if (col.timesOverlapWith(event)) {
                if (firstFreeCol < 0) continue else break
            }
            if (firstFreeCol < 0) firstFreeCol = i
            numFreeCol++
        }

        when {
            // Overlaps with all, add a new column
            firstFreeCol < 0 -> {
                groupEvents += mutableListOf(event)
                // Expand anything that spans into the previous column and doesn't overlap with this event
                for (ci in 0 until groupEvents.size - 1) {
                    val col = groupEvents[ci]
                    col.forEachIndexed { ei, e ->
                        if (ci + e.colSpan == groupEvents.size - 1 && !e.overlapsWith(event)) {
                            col[ei] = e.copy(colSpan = e.colSpan + 1)
                        }
                    }
                }
            }
            // No overlap with any, start a new group
            numFreeCol == groupEvents.size -> {
                resetGroup()
                groupEvents += mutableListOf(event)
            }
            // At least one column free, add to first free column and expand to as many as possible
            else -> {
                groupEvents[firstFreeCol] += event.copy(colSpan = numFreeCol)
            }
        }
    }
    resetGroup()
    return positionedEvents
}

private fun BookedMeetingRoom.overlapsWith(other: BookedMeetingRoom): Boolean {
    return date == other.date && fromTime < other.toTime && toTime > other.fromTime
}

private fun List<BookedMeetingRoom>.timesOverlapWith(event: BookedMeetingRoom): Boolean {
    return any { it.overlapsWith(event) }
}

@Preview(showBackground = true)
@Composable
fun SchedulePreview() {
    DashboardScreen(bookedMeetingRooms = emptyList())
}