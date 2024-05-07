package com.booking.booking

import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.booking.designsystem.component.ComponentButton
import com.booking.model.model.MeetingRoom
import com.booking.model.model.User
import com.booking.ui.CustomCalendar
import com.booking.ui.Loading
import java.time.LocalDate
import java.util.Calendar

private const val TAG = "BOOKING_SCREEN"

@Composable
fun BookingsRoute(navigateToConfirmation: () -> Unit) {

    val viewModel: BookingsViewModel = hiltViewModel()
    val bookingUiState = viewModel.bookingUiState.collectAsStateWithLifecycle()
    val meetingRooms = viewModel.meetingRooms.collectAsStateWithLifecycle()
    val users = viewModel.users.collectAsStateWithLifecycle()

    BookingScreen(
        bookingUiState = bookingUiState.value,
        navigateToConfirmation = navigateToConfirmation,
        availableMeetingRooms = meetingRooms.value,
        availableUsers = users.value,
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
        },
        fetchAvailableUsers = { viewModel.getAvailableUsers() },
        fetchAvailableUsersByEmail = { email -> viewModel.getUsersByEmail(email = email) }
    )
}

@Composable
fun BookingScreen(
    bookingUiState: BookingUiState = BookingUiState.None,
    navigateToConfirmation: () -> Unit = {},
    fetchAvailableMeetings: (String, String, String) -> Unit,
    fetchAvailableUsers: () -> Unit = {},
    availableMeetingRooms: List<MeetingRoom?>,
    availableUsers: List<User?>,
    fetchAvailableUsersByEmail: (String) -> Unit = { _ -> },
    bookMeeting: (
        String,
        String,
        String,
        String,
        String,
        String,
        List<String>
    ) -> Unit = { _, _, _, _, _, _, _ -> },
) {
    var title by remember {
        mutableStateOf("")
    }
    var date by remember {
        mutableStateOf("")
    }
    var startTime by remember {
        mutableStateOf("")
    }
    var endTime by remember {
        mutableStateOf("")
    }
    var selectedRoomID by remember {
        mutableStateOf("")
    }
    var attendees by remember {
        mutableStateOf(emptyList<String>())
    }

    when (bookingUiState) {
        is BookingUiState.BookingSuccess -> navigateToConfirmation.invoke()
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
            TitleCompose(setTitle = { title = it })
        }

        SelectDateCompose(
            setDate = { date = it.toString() }
        )

        SelectTime(
            setStartTime = { startTime = it },
            setEndTime = { endTime = it }
        )

        BottomSheet(
            clickableText = stringResource(R.string.click_here_to_add_users),
            bottomSheetList = availableUsers.filter {
                it?.email?.isNotEmpty() == true
            },
            fetchData = { fetchAvailableUsers() },
            saveData = {
                attendees = it
            },
            showSearchBar = true,
            searchBarFunction = fetchAvailableUsersByEmail
        )

        BottomSheet(
            clickableText = stringResource(R.string.click_here_to_see_available_meeting_rooms),
            bottomSheetList = availableMeetingRooms,
            fetchData = { fetchAvailableMeetings(startTime, endTime, date) },
            saveData = { selectedRoomID = it.first() }
        )

        ComponentButton(
            onClick = {
                bookMeeting.invoke(
                    startTime,
                    endTime,
                    title,
                    selectedRoomID,
                    "host",
                    date,
                    attendees.filter {
                        it.isNotEmpty()
                    }
                )
            },
            text = stringResource(R.string.book_meeting),
            modifier = Modifier.padding(top = 20.dp)
        )

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BottomSheet(
    clickableText: String,
    bottomSheetList: List<*>,
    showSearchBar: Boolean = false,
    fetchData: () -> Unit,
    saveData: (List<String>) -> Unit = {},
    searchBarFunction: (String) -> Unit = { _ -> }
) {
    val selectedUsers = mutableListOf("")
    var showSaveButton by remember {
        mutableStateOf(false)
    }
    var headerText by remember {
        mutableStateOf(clickableText)
    }
    var showBottomSheet by remember {
        mutableStateOf(false)
    }
    Text(
        text = headerText,
        modifier = Modifier
            .padding(top = 24.dp)
            .clickable {
                fetchData.invoke()
                showBottomSheet = true
            },
        style = MaterialTheme.typography.labelLarge,
        color = Color.Blue
    )
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
        ) {
            if (showSearchBar) {
                CustomSearchBar(
                    onSearch = { searchBarFunction(it) }
                )
            }
            LazyColumn {
                items(bottomSheetList) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp, horizontal = 20.dp)
                    ) {
                        when (it) {
                            is MeetingRoom -> {
                                Text(
                                    text = it.meetingRoomName,
                                    style = MaterialTheme.typography.labelLarge,
                                    modifier = Modifier.clickable {
                                        saveData(listOf(it.meetingRoomID))
                                        headerText = it.meetingRoomName
                                        showBottomSheet = false
                                    }
                                )
                            }

                            is User -> {
                                CustomCheckBox(
                                    user = it,
                                    addUser = { selectedUsers.add(it) },
                                    removeUser = {
                                        if (selectedUsers.contains(it)) {
                                            selectedUsers.remove(it)
                                        }
                                    }
                                )
                                Text(
                                    text = it.email,
                                    style = MaterialTheme.typography.labelLarge,
                                    modifier = Modifier
                                        .padding(vertical = 12.dp)
                                )
                                showSaveButton = true
                            }

                        }
                    }
                }

            }
            if (showSaveButton) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick =
                        {
                            saveData(selectedUsers as List<String>)
                            showBottomSheet = false
                        },
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        Text(text = "Invite Users")
                    }
                }
            }
        }
    }
}

@Composable
fun CustomCheckBox(
    user: User,
    addUser: (String) -> Unit,
    removeUser: (String) -> Unit
) {
    var isSelected by remember {
        mutableStateOf(user.isSelected)
    }
    Checkbox(
        checked = isSelected,
        onCheckedChange = {
            isSelected = it
            user.isSelected = isSelected
            if (isSelected) {
                addUser(user.email)
            } else {
                removeUser(user.email)
            }
        }
    )
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
    TextField(
        value = placeHolder,
        onValueChange =
        {
            placeHolder = it
            setTitle.invoke(placeHolder)
        }
    )
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
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .clickable { onClick.invoke() }
        )
    }
}

@Composable
fun CustomSearchBar(onSearch: (String) -> Unit) {
    var searchText by remember { mutableStateOf("") }
    var isHintDisplayed by remember {
        mutableStateOf(true)
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color.White,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, top = 12.dp, bottom = 12.dp, end = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 18.dp, bottom = 18.dp, end = 16.dp)
            ) {
                BasicTextField(
                    value = searchText,
                    onValueChange = {
                        searchText = it
                        isHintDisplayed = searchText.isEmpty()
                        onSearch(it)
                    },
                    maxLines = 1,
                    singleLine = true,
                    textStyle = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                if (isHintDisplayed) {
                    Text(text = "Enter User's email")
                }
            }
        }

    }
}


@Preview(showBackground = true)
@Composable
fun Preview() {
    BookingScreen(
        availableMeetingRooms = emptyList(),
        fetchAvailableMeetings = { _, _, _ -> },
        availableUsers = listOf(
            User("hkecb@gma.com", "cec", "cece"),
            User("decece@gmcecea.com", "cewcw", "cqcw"),
            User("hkcececb@gma.com", "ceevewvc", "cecvqeve"),
            User("wvcc@gma.com", "cveqwrcec", "wqc"),
            User("wecw@gma.com", "cjwc", "cece"),
            User("kncewj@gma.com", "cekjcwejkcc", "cece")
        )
    )
}