package com.booking.booking

import android.app.TimePickerDialog
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.booking.designsystem.component.ComponentButton
import com.booking.model.model.MeetingRoom
import com.booking.ui.CustomCalendar
import com.booking.ui.Loading
import java.time.LocalDate
import java.util.Calendar

private val TAG = "BOOKING_SCREEN"

@Composable
fun BookingsRoute(navigateToConfirmation: () -> Unit) {

    val viewModel: BookingsViewModel = hiltViewModel()
    val bookingUiState = viewModel.bookingUiState.collectAsStateWithLifecycle()
    val meetingRooms = viewModel.meetingRooms.collectAsStateWithLifecycle()

    BookingScreen(
        bookingUiState = bookingUiState.value,
        navigateToConfirmation = navigateToConfirmation,
        availableMeetingRooms = meetingRooms.value,
        bookMeeting = { startTime, endTime, title, meetingRoomID, host, date, attendees ->
            viewModel.bookMeetingRoom(
                startTime = startTime,
                endTime = endTime,
                title = title,
                meetingRoomId = meetingRoomID,
                host = host,
                date = date,
                attendees = attendees
            )
        },
        fetchAvailableMeetings =
        { startTime, endTime, date ->
            viewModel.getAvailableMeetingRooms(
                startTime = startTime,
                endTime = endTime,
                date = date
            )
        }
    )
}

@Composable
fun BookingScreen(
    bookingUiState: BookingUiState = BookingUiState.None,
    navigateToConfirmation: () -> Unit = {},
    availableMeetingRooms: List<MeetingRoom?>,
    bookMeeting: (
        String,
        String,
        String,
        String,
        String,
        String,
        List<String>
    ) -> Unit = { _, _, _, _, _, _, _ -> },
    fetchAvailableMeetings: (String, String, String) -> Unit
) {
    var title = ""
    var date = ""
    var startTime = ""
    var endTime = ""
    var selectedRoomID = ""
    var attendees = emptyList<String>()

    Log.d(TAG, "BookingScreen: recomposing screen $title")

    when (bookingUiState) {
        is BookingUiState.Success -> navigateToConfirmation.invoke()
        is BookingUiState.Loading -> Loading()
        is BookingUiState.NoMeetingRoomsAvailable -> {} //Show a toast
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onTertiary),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.meeting_title),
            style = MaterialTheme.typography.titleMedium
        )
        Row {
            Log.d(TAG, "BookingScreen: recomposing row $title")
            TitleCompose(setTitle = { title = it })
        }

        SelectDateCompose(setDate = { date = it.toString() })

        SelectTime(setStartTime = { startTime = it }, setEndTime = { endTime = it })

//        SearchUsers()

        MeetingRoomDropDown(
            promptText = stringResource(R.string.select_a_meeting_room),
            availableMeetingRooms = availableMeetingRooms.filterNotNull(),
            setMeetingRoom = { selectedRoomID = it },
            fetchAvailableMeetings = { fetchAvailableMeetings(startTime, endTime, date) }
        )
//        SearchUsers(stringResource(R.string.search_users))
        ComponentButton(
            onClick = {
                bookMeeting.invoke(
                    startTime,
                    endTime,
                    title,
                    selectedRoomID,
                    "host",
                    date,
                    attendees
                )
            },
            text = stringResource(R.string.book_meeting),
            modifier = Modifier.padding(top = 20.dp)
        )

    }
}

@Composable
fun SelectTime(
    setStartTime: (String) -> Unit,
    setEndTime: (String) -> Unit
) {
    var startTime by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }

    var startHeader by remember {
        mutableStateOf("Select Time")
    }
    var endHeader by remember {
        mutableStateOf("Select Time")
    }

    val context = LocalContext.current
    val calendar = remember { Calendar.getInstance() }
    val mHour = remember { calendar[Calendar.HOUR_OF_DAY] }
    val mMinute = remember { calendar[Calendar.MINUTE] }

    val showTimePickerDialog: (String) -> Unit = { initialTime ->
        val timePickerDialog = TimePickerDialog(
            context,
            { _, hour: Int, minute: Int ->
                if (initialTime == "startTime") {
                    startTime = "$hour:$minute"
                    setStartTime(hour.toString())
                    startHeader = startTime
                } else {
                    endTime = "$hour:$minute"
                    setEndTime(hour.toString())
                    endHeader = endTime
                }
            },
            mHour,
            mMinute,
            true
        )
        timePickerDialog.show()
    }

    ClickableText(
        titleText = stringResource(R.string.from_time),
        onClick = {
            showTimePickerDialog("startTime")
            startHeader = startTime
        },
        modifier = Modifier.padding(top = 20.dp),
        clickableText = startHeader
    )
    ClickableText(
        titleText = stringResource(R.string.to_time),
        onClick = {
            showTimePickerDialog("endTime")
            endHeader = endTime
        },
        modifier = Modifier.padding(top = 20.dp),
        clickableText = endHeader
    )
}


@Composable
fun SelectDateCompose(setDate: (LocalDate) -> Unit) {
    var selectedDate by remember {
        mutableStateOf(LocalDate.now())
    }
    var showCalendar by remember {
        mutableStateOf(false)
    }
    ClickableText(
        titleText = stringResource(R.string.date),
        clickableText = selectedDate.toString(),
        onClick = { showCalendar = true },
        modifier = Modifier.padding(top = 20.dp)
    )
    if (showCalendar) {
        CustomCalendar(
            onDateSelected =
            {
                selectedDate = it
                setDate(selectedDate)
            },
            onDismissRequest = { showCalendar = false },
            selectedDate = selectedDate
        )
    }
}

@Composable
fun TitleCompose(
    setTitle: (String) -> Unit
) {
    var placeHolder by remember {
        mutableStateOf("")
    }
    Log.d(TAG, "TitleCompose: recomposing $placeHolder")
    TextField(
        value = placeHolder,
        onValueChange =
        {
            placeHolder = it
            setTitle.invoke(placeHolder)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeetingRoomDropDown(
    promptText: String,
    availableMeetingRooms: List<MeetingRoom>,
    setMeetingRoom: (String) -> Unit,
    fetchAvailableMeetings: () -> Unit,
) {
    var selectedRoom by remember {
        mutableStateOf("")
    }
    var expandDropDown by remember {
        mutableStateOf(false)
    }
    ExposedDropdownMenuBox(
        expanded = expandDropDown,
        onExpandedChange = {
            fetchAvailableMeetings.invoke()
            expandDropDown = !expandDropDown
        },
        modifier = Modifier.padding(top = 20.dp, bottom = 20.dp)
    ) {
        TextField(
            value = selectedRoom,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandDropDown) },
            modifier = Modifier.menuAnchor(),
            label = { Text(text = promptText) }
        )
        ExposedDropdownMenu(
            expanded = expandDropDown,
            onDismissRequest = { expandDropDown = false }
        ) {
            if (availableMeetingRooms.isEmpty()) {
                Text(text = "No available meeting rooms")
            } else {
                availableMeetingRooms.forEachIndexed { index, meetingRoom ->
                    DropdownMenuItem(
                        text = { Text(text = meetingRoom.meetingRoomName) },
                        onClick = {
                            selectedRoom = availableMeetingRooms[index].meetingRoomName
                            setMeetingRoom(selectedRoom)
                            expandDropDown = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )

                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchUsers() {
    var promptText by remember {
        mutableStateOf("")
    }
    Text(text = "Enter first 3 character of the attendee's email-id")


}

@Composable
private fun ClickableText(
    titleText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    clickableText: String = stringResource(id = R.string.select_time)
) {
    Row(modifier = modifier) {
        Text(
            text = titleText,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
        Text(
            text = clickableText,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .clickable { onClick.invoke() }
        )
    }
}


@Preview(showBackground = true)
@Composable
fun Preview() {
    BookingScreen(
        availableMeetingRooms = emptyList(),
        fetchAvailableMeetings = { _, _, _ -> }
    )
}