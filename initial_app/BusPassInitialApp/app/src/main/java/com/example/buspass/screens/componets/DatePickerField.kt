package com.example.buspass.screens.componets


import android.app.DatePickerDialog
import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.widget.DatePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import java.util.Calendar

@Preview(
    name = "Dark Mode",
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES
)
@Preview(
    name = "Light Mode",
    showBackground = true,
    uiMode = UI_MODE_NIGHT_NO
)
@Composable
fun DatePickerFieldPreview() {
    DatePickerField()
}

@Composable
fun DatePickerField(
    selectedDate: String = "",
    isError: Boolean = false,
    onDateSelected: (String) -> Unit = { _ -> },
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                val date = "$dayOfMonth/${month + 1}/$year"
                onDateSelected(date)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH),
        )
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()

    ) {
        OutlinedTextField(
            value = selectedDate,
            onValueChange = {  },
            label = { Text("Start Date (YYYY-MM-DD)") },
            enabled = false,
            isError = isError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier.fillMaxWidth()
                .clickable { datePickerDialog.show() },
        )
    }
}
