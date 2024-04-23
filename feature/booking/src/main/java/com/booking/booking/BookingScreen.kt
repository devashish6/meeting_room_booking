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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.booking.designsystem.component.ComponentButton
import java.util.Calendar

@Composable
fun BookingScreen() {
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
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Text(
            text = titleText,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
        Text(
            text = stringResource(R.string.select_time),
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