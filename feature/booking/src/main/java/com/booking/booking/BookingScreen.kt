package com.booking.booking

import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import com.booking.designsystem.component.ComponentButton
import com.booking.ui.CustomCalendar
import java.time.LocalDate
import java.util.Calendar

@Composable
fun BookingScreen() {
    var expanded by remember {
        mutableStateOf(false)
    }
    var showCalendar by remember {
        mutableStateOf(false)
    }
    var showRoomPicker by remember {
        mutableStateOf(false)
    }
    var selectedDate by remember {
        mutableStateOf(LocalDate.now())
    }
    var selectedMeetingRoom by remember {
        mutableStateOf("")
    }
    val mCities = listOf("Meeting Room 1", "Meeting Room 2", "Meeting Room 3")
    val mCalendar = Calendar.getInstance()
    val mHour = mCalendar[Calendar.HOUR_OF_DAY]
    val mMinute = mCalendar[Calendar.MINUTE]
    val mTime = remember { mutableStateOf("") }
    val timePickerDialog = TimePickerDialog(
        LocalContext.current,
        { _, hour: Int, minute: Int ->
            mTime.value = "$hour:$minute"
        },
        mHour,
        mMinute,
        true
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onTertiary),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row {
            TextField(
                value = stringResource(R.string.meeting_title),
                onValueChange = {},
                modifier = Modifier.width(250.dp)
            )
        }
        TimePickerDialog(
            titleText = "Date : ",
            pickerText = selectedDate.toString(),
            onClick = { showCalendar = true},
            modifier = Modifier.padding(top = 20.dp)
        )
        OutlinedTextField(
            value = selectedMeetingRoom,
            onValueChange = {
                selectedMeetingRoom = it},
            label = { Text(text = "Select Meeting Room")},
            modifier = Modifier.clickable {
                expanded = !expanded
            }
        )
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            mCities.forEach {
                DropdownMenuItem(text = { Text(text = it) },
                    onClick =
                    {
                        selectedMeetingRoom = it
                        expanded = false
                    })
            }
            
        }
        TimePickerDialog(
            titleText = stringResource(R.string.from_time),
            onClick = { timePickerDialog.show() },
            modifier = Modifier.padding(top = 20.dp)
        )
        TimePickerDialog(
            titleText = stringResource(R.string.to_time),
            onClick = { timePickerDialog.show() },
            modifier = Modifier.padding(top = 20.dp)
        )
//        SearchUsers()
        ComponentButton(
            onClick = { /*TODO*/ },
            text = stringResource(R.string.book_meeting),
            modifier = Modifier.padding(top = 40.dp)
        )
        if (showCalendar) {
            CustomCalendar(
                onDateSelected = { selectedDate =  it },
                onDismissRequest = { showCalendar = false },
                selectedDate = selectedDate
            )
        }
    }
}

@Composable
fun SearchUsers() {
    //TODO: While creating viewmodel
}

@Composable
private fun TimePickerDialog(
    titleText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    pickerText : String = stringResource(id = R.string.select_time)
) {
    Row(modifier = modifier) {
        Text(
            text = titleText,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
        Text(
            text = pickerText,
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
    BookingScreen()
}