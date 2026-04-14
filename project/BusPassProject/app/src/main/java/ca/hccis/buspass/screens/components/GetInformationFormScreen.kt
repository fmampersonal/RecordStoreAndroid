/**
 * BusPassCard.kt
 *
 * Description: Enhanced the GetInformationForm with additional fields for address, city, preferred route,
 * rural route validity, length of pass, and start date, with error handling and validation for each.
 *
 * @Author: Anthony Odu
 * @Date: 2025-01-11
 */
package ca.hccis.buspass.screens.components

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import ca.hccis.buspass.utility.Dimensions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import ca.hccis.buspass.entity.BusPass
import ca.hccis.buspass.utility.CisUtility


/**
 * Composable function to display a form for collecting information about a bus pass.
 *
 * The form includes fields for name, address, city, preferred route, pass type, rural route validity,
 * length of pass, and start date. A submit button triggers the `onSubmit` callback with the collected data.
 *
 * @param paddingValues Optional padding values to be applied to the form.
 * @param onSubmit Callback function triggered when the form is submitted. It receives the collected data
 *                 as parameters including `id`, `name`, `address`, `city`, `preferredRoute`,
 *                 `passType`, `validForRuralRoute`, `lengthOfPass`, and `startDate`.
 */
@Composable
fun GetInformationForm(
    paddingValues: PaddingValues,
    bussPass: BusPass? = null,
    id: Int = 0,
    onSubmit: (
        name: String,
        address: String,
        city: String,
        preferredRoute: String,
        passType: Int,
        validForRuralRoute: Boolean,
        lengthOfPass: Int,
        startDate: String
    ) -> Unit
) {
    // State variables for the form fields
    var name by remember { mutableStateOf(bussPass?.name ?: "") }
    var address by remember { mutableStateOf(bussPass?.address ?: "") }
    var city by remember { mutableStateOf(bussPass?.city ?: "") }
    var preferredRoute by remember { mutableStateOf(bussPass?.preferredRoute ?: "") }
    var passType by remember { mutableStateOf((bussPass?.passType ?: 0).toString()) }
    var validForRuralRoute by remember { mutableStateOf(bussPass?.validForRuralRoute ?: false) }
    var lengthOfPass by remember { mutableStateOf((bussPass?.lengthOfPass ?: 0).toString()) }
    var startDate by remember { mutableStateOf(bussPass?.startDate ?: "") }


    // State variables for validation errors
    var nameError by remember { mutableStateOf(false) }
    var addressError by remember { mutableStateOf(false) }
    var cityError by remember { mutableStateOf(false) }
    var preferredRouteError by remember { mutableStateOf(false) }
    var passTypeError by remember { mutableStateOf(false) }
    var lengthOfPassError by remember { mutableStateOf(false) }
    var startDateError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxWidth()
            .padding(Dimensions.space16)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(Dimensions.space16)
    ) {


        // Name input field with error handling
        OutlinedTextField(
            value = name,
            onValueChange = { name = it; nameError = it.isEmpty() },
            label = { Text("Name") },
            isError = nameError,
            modifier = Modifier.fillMaxWidth()
        )
        if (nameError) Text("Name is required", color = MaterialTheme.colorScheme.error)

        // Address input field with error handling
        OutlinedTextField(
            value = address,
            onValueChange = { address = it; addressError = it.isEmpty() },
            label = { Text("Address") },
            isError = addressError,
            modifier = Modifier.fillMaxWidth()
        )
        if (addressError) Text("Address is required", color = MaterialTheme.colorScheme.error)

        // City input field with error handling
        OutlinedTextField(
            value = city,
            onValueChange = { city = it; cityError = it.isEmpty() },
            label = { Text("City") },
            isError = cityError,
            modifier = Modifier.fillMaxWidth()
        )
        if (cityError) Text("City is required", color = MaterialTheme.colorScheme.error)

        // Preferred Route input field with error handling
        OutlinedTextField(
            value = preferredRoute,
            onValueChange = { preferredRoute = it; preferredRouteError = it.isEmpty() },
            label = { Text("Preferred Route") },
            isError = preferredRouteError,
            modifier = Modifier.fillMaxWidth()
        )
        if (preferredRouteError) Text(
            "Enter a valid preferred route",
            color = MaterialTheme.colorScheme.error
        )


        //**********************************************
        //Pass Type
        //https://developer.android.com/develop/ui/compose/components/radio-button
        //**********************************************

        val radioOptions = listOf("Regular", "K12", "Student", "Senior")
        val busPassTypeIndex = (bussPass!!.passType) - 3;
        val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[busPassTypeIndex]) }

        radioOptions.forEach { text ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .selectable(
                        selected = (text == selectedOption),
                        onClick = {
                            onOptionSelected(text)
                            val passTypeIndex =radioOptions.indexOf(text) +3;
                            passType = passTypeIndex.toString()
                            CisUtility.log("BJM Radio Button", "passType=" + passType)
                            CisUtility.log("BJM Radio Button", "Selected=" + selectedOption)
                        },
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (text == selectedOption),
                    onClick = null // null recommended for accessibility with screen readers
                )
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }


//        // Pass Type input field with error handling
//        OutlinedTextField(
//            value = passType,
//            onValueChange = { passType = it; passTypeError = it.toIntOrNull() == null },
//            label = { Text("Pass Type (3-Regular, 4-K12, 5-Student, 6-Senior)") },
//            isError = passTypeError,
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//            modifier = Modifier.fillMaxWidth()
//        )
//        if (passTypeError) Text("Enter a valid pass type", color = MaterialTheme.colorScheme.error)
//


        // Length of Pass input field with error handling
        OutlinedTextField(
            value = lengthOfPass,
            onValueChange = { lengthOfPass = it; lengthOfPassError = it.toIntOrNull() == null },
            label = { Text("Length of Pass (in days)") },
            isError = lengthOfPassError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        if (lengthOfPassError) Text("Enter a valid length", color = MaterialTheme.colorScheme.error)

        // Start Date input field with error handling
        DatePickerField(
            selectedDate = startDate,
            isError = startDateError
        ) { date ->
            startDate = date
            startDateError = date.isEmpty()
        }

        if (startDateError) Text("Start Date is required", color = MaterialTheme.colorScheme.error)

        // Checkbox for Rural Route Validity
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = validForRuralRoute,
                onCheckedChange = { validForRuralRoute = it }
            )
            Text("Valid for rural route")
        }

        Button(
            onClick = {
                // Validate each field before submission
                val nameIsValid = name.isNotEmpty()
                val addressIsValid = address.isNotEmpty()
                val cityIsValid = city.isNotEmpty()
                val preferredRouteIsValid = preferredRoute.isNotEmpty()
                CisUtility.log("BJM validaing onclick","passType:***"+passType+"***")
                val passTypeIsValid = passType.toIntOrNull() != null
                val lengthOfPassIsValid = lengthOfPass.toIntOrNull() != null
                val startDateIsValid = startDate.isNotEmpty()

                // Set the error flags accordingly
                nameError = !nameIsValid
                addressError = !addressIsValid
                cityError = !cityIsValid
                preferredRouteError = !preferredRouteIsValid
                passTypeError = !passTypeIsValid
                lengthOfPassError = !lengthOfPassIsValid
                startDateError = !startDateIsValid

                // Check if all fields are valid before submitting
                val formIsValid = nameIsValid && addressIsValid && cityIsValid &&
                        preferredRouteIsValid && passTypeIsValid && lengthOfPassIsValid && startDateIsValid

                // If all fields are valid, submit the form
                if (formIsValid) {
                    onSubmit(
                        name,
                        address,
                        city,
                        preferredRoute,
                        passType.toIntOrNull() ?: 0,
                        validForRuralRoute,
                        lengthOfPass.toIntOrNull() ?: 0,
                        startDate
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Submit")
        }
        Spacer(modifier = Modifier.height(Dimensions.space16))
    }
}

/**
 * Preview composable to show the GetInformationForm in both dark and light modes.
 */
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
fun GetInformationFormPreview() {
    // Preview with a function to print the form details when submitted
    GetInformationForm(paddingValues = PaddingValues(), bussPass = BusPass()) { name,
                                                                                address,
                                                                                city,
                                                                                preferredRoute,
                                                                                passType,
                                                                                validForRuralRoute,
                                                                                lengthOfPass,
                                                                                startDate ->

        val result = "Name=$name\n" +
                "Address=$address\n" +
                "City=$city\n" +
                "Preferred Route=$preferredRoute\nPass Type=$passType\n" +
                "Valid For Rural Route=$validForRuralRoute\nLength Of Pass=$lengthOfPass\n" +
                "Start Date=$startDate"
        println(result)
    }
}

