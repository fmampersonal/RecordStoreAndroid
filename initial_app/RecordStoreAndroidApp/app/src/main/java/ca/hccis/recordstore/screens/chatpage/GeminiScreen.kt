package ca.hccis.recordstore.screens.chatpage

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ca.hccis.recordstore.BuildConfig // <-- Make sure this matches your package
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.launch

@Composable
fun MainMenuUI(
    mainMenuInput: String,
    onValueChange: (String) -> Unit,
    onProcess: () -> Unit,
    result: String
) {
    Column(
        modifier = Modifier.fillMaxWidth(0.95f),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            "Ask AI about Record Store inventory and music recommendations!",
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(6.dp))
    }
    Spacer(modifier = Modifier.height(16.dp))

    OutlinedTextField(
        value = mainMenuInput,
        onValueChange = onValueChange,
        label = { Text("e.g., Recommend me a good rock album") },
        modifier = Modifier.fillMaxWidth(0.95f),
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Text
        ),
        keyboardActions = KeyboardActions(onDone = { onProcess() })
    )

    Button(
        onClick = onProcess,
        modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
    ) {
        Text("Ask Gemini", style = MaterialTheme.typography.labelLarge)
    }

    Text(result)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeminiScreen(
    modifier: Modifier = Modifier,
    back: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ask AI Assistant") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                navigationIcon = {
                    IconButton(onClick = back) {
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, "Back")
                    }
                },
            )
        }
    ) { paddingValues ->

        // Fetching key exactly how your professor expects
        val apiKey = "AQ.Ab8RN6K0OALCiA8_w0Es2IrlfuWb91T5PEoNdlodrnu8lXLKow"

        val generativeModel = GenerativeModel(
            modelName = "gemini-2.5-flash", //
            apiKey = apiKey
        )

//        val mapsApiKey = properties.getProperty("MAPS_API_KEY", "")
//        manifestPlaceholders["MAPS_API_KEY"] = mapsApiKey

        val coroutineScope = rememberCoroutineScope()
        val mainMenuInput = remember { mutableStateOf("") }
        val resultState = remember { mutableStateOf("") }

        val processUserInput = {
            coroutineScope.launch {
                // Tailored prompt for your Record Store
                val prompt = """
                    A user asked: '${mainMenuInput.value}'. 
                    Answer them as a helpful, knowledgeable assistant working at a retro Record Store. 
                    Keep your answer concise and helpful.
                """.trimIndent()

                try {
                    val response = generativeModel.generateContent(prompt)
                    val responseText: String? = response.text?.replace("```json", "")?.replace("```", "")?.trim()
                    mainMenuInput.value = ""
                    if (responseText != null) {
                        resultState.value = responseText
                    }
                } catch (e: Exception) {
                    resultState.value = "Error connecting to AI: ${e.localizedMessage}"
                }
            }
        }

        Column(
            modifier = modifier.padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MainMenuUI(
                mainMenuInput = mainMenuInput.value,
                onValueChange = { mainMenuInput.value = it },
                onProcess = { processUserInput() },
                result = resultState.value
            )
        }
    }
}