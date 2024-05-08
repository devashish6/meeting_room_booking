package com.booking.booking

import android.app.TimePickerDialog
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.booking.model.model.MeetingRoom
import com.booking.model.model.User
import com.booking.ui.CustomCalendar
import com.booking.ui.Loading
import java.time.LocalDate
import java.util.Calendar

private const val TAG = "BOOKING_SCREEN"

@Composable
fun BookingsRoute(
    navigateToConfirmation: () -> Unit,
    onBackClicked: () -> Unit
) {

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
        fetchAvailableUsersByEmail = { email -> viewModel.getUsersByEmail(email = email) },
        onBackClicked = onBackClicked
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(
    bookingUiState: BookingUiState = BookingUiState.None,
    navigateToConfirmation: () -> Unit = {},
    fetchAvailableMeetings: (String, String, String) -> Unit = { _, _, _ -> },
    fetchAvailableUsers: () -> Unit = {},
    availableMeetingRooms: List<MeetingRoom?> = emptyList(),
    availableUsers: List<User?> = emptyList(),
    fetchAvailableUsersByEmail: (String) -> Unit = { _ -> },
    onBackClicked: () -> Unit = {},
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Meeting Details",
                        modifier = Modifier.padding(16.dp),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .padding(paddingValues)
        ) {

            TitleCompose(setTitle = { title = it })

            SelectDateCompose(
                setDate = { date = it.toString() }
            )

            Spacer(modifier = Modifier.padding(4.dp))

            SelectTime(
                setStartTime = { startTime = it },
                setEndTime = { endTime = it }
            )

            Spacer(modifier = Modifier.padding(4.dp))

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
                searchBarFunction = fetchAvailableUsersByEmail,
                trailingIcon = Icons.Default.AddCircle
            )

            Spacer(modifier = Modifier.padding(4.dp))

            BottomSheet(
                clickableText = stringResource(R.string.click_here_to_see_available_meeting_rooms),
                bottomSheetList = availableMeetingRooms,
                fetchData = { fetchAvailableMeetings(startTime, endTime, date) },
                saveData = { selectedRoomID = it.first() },
                trailingIcon = Icons.Default.ArrowDropDown
            )

            Button(
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = ButtonColors(containerColor = Color.Black,
                    contentColor = Color.White,
                    disabledContainerColor = Color.Cyan,
                    disabledContentColor = Color.Cyan
                )
            ) {
                Text(
                    text = "Book",
                    style = MaterialTheme.typography.bodyLarge
                )

            }
        }

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
    searchBarFunction: (String) -> Unit = { _ -> },
    trailingIcon: ImageVector
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
//    Card(
//        modifier = Modifier
//            .padding(top = 16.dp)
//            .fillMaxWidth()
//    ) {
    OutlinedTextField(
        label = {
            Text(
                text = headerText,
                style = MaterialTheme.typography.bodySmall,
            )
        },
        value = headerText,
        onValueChange = {},
        trailingIcon = {
            Image(
                imageVector = trailingIcon,
                contentDescription = "add",
                Modifier.clickable {
                    fetchData.invoke()
                    showBottomSheet = true
                })
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
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
//    }
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

    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            label = {
                Text(
                    text = stringResource(id = R.string.from_time),
                    style = MaterialTheme.typography.bodySmall,
                )
            },
            value = startHeader,
            onValueChange = {},
            trailingIcon = {
                Image(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "add",
                    Modifier.clickable {
                        showTimePickerDialog("startTime")
                        startHeader = startTime
                    })
            },
            modifier = Modifier.width(160.dp)
        )
        OutlinedTextField(
            label = {
                Text(
                    text = stringResource(id = R.string.to_time),
                    style = MaterialTheme.typography.bodySmall,
                )
            },
            value = endHeader,
            onValueChange = {},
            trailingIcon = {
                Image(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "add",
                    Modifier.clickable {
                        showTimePickerDialog("endTime")
                        endHeader = endTime
                    })
            },
            modifier = Modifier.width(160.dp)
        )
    }

//    Card(
//        modifier = Modifier
//            .padding(start = 8.dp, top = 16.dp, end = 8.dp)
//            .fillMaxWidth()
//    ) {
//        ClickableText(
//            titleText = stringResource(R.string.from_time),
//            onClick = {
//                showTimePickerDialog("startTime")
//                startHeader = startTime
//            },
//            clickableText = startHeader,
//            modifier = Modifier.padding(top = 10.dp, start = 8.dp, bottom = 10.dp)
//        )
//    }
//    Card(
//        modifier = Modifier
//            .padding(start = 8.dp, top = 16.dp, end = 8.dp)
//            .fillMaxWidth(),
//    ) {
//        ClickableText(
//            titleText = stringResource(R.string.to_time),
//            onClick = {
//                showTimePickerDialog("endTime")
//                endHeader = endTime
//            },
//            clickableText = endHeader,
//            modifier = Modifier.padding(top = 10.dp, start = 8.dp, bottom = 10.dp)
//        )
//    }
}

@Composable
fun SelectDateCompose(setDate: (LocalDate) -> Unit) {
    var selectedDate by remember {
        mutableStateOf(LocalDate.now())
    }
    var showCalendar by remember {
        mutableStateOf(false)
    }

    OutlinedTextField(
        label = {
            Text(text = "Select Date")
        },
        value = selectedDate.toString(),
        onValueChange = {},
        trailingIcon = {
            Image(
                imageVector = Icons.Default.DateRange,
                contentDescription = "Date picker",
                Modifier.clickable { showCalendar = true })
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
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

    OutlinedTextField(
        value = placeHolder,
        onValueChange = {
            placeHolder = it
            setTitle(placeHolder)
        },
        label = {
            Text(
                text = stringResource(id = R.string.meeting_title),
                style = MaterialTheme.typography.bodyLarge
            )
        },
        trailingIcon = {
            Image(imageVector = Icons.Default.Edit, contentDescription = "Edit")
        },
        placeholder = {
            Text(text = "Enter meeting title")
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}

@Composable
private fun ClickableText(
    titleText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    clickableText: String = stringResource(id = R.string.select_time)
) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier
    ) {
        Text(
            text = titleText,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 10.dp)
                .weight(1f)
        )
        Text(
            text = clickableText,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .padding(horizontal = 12.dp)
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
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun Preview() {
    BookingScreen()
}