package com.evanadwyer.simplevisitortracker

import android.text.TextUtils
import android.util.Patterns
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.evanadwyer.simplevisitortracker.ui.theme.LightGreen
import com.evanadwyer.simplevisitortracker.ui.theme.LightOrange
import com.evanadwyer.simplevisitortracker.ui.theme.LightYellow

@Composable
fun GuestSignIn(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onGuestEmailEntered: () -> Unit,
    viewModel: BarCodeScannerViewModel = viewModel(),
) {
    var guestName by remember {
        mutableStateOf("")
    }
    var isGuestNameValid by remember {
        mutableStateOf(true)
    }
    var guestEmail by remember {
        mutableStateOf("")
    }
    var isValidEmail by remember {
        mutableStateOf(true)
    }
    var selectedOption by remember {
        mutableStateOf("")
    }
    var isValidOption by remember {
        mutableStateOf(true)
    }
    val localFocusManager = LocalFocusManager.current
    BackHandler(onBack = onBack)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(top = 18.dp)
            .background(LightOrange)
//            .verticalScroll(rememberScrollState())
    ) {
        DiscoverySurvey(
            localFocusManager
        ) {
            selectedOption = if (it.startsWith("Other:")) {
                it.drop(6)
            } else {
                it
            }
            isValidOption = it.isNotBlank()
        }
        if (!isValidOption) {
            Text(
                text = "Please select an option",
                fontSize = 18.sp,
                color = Color.Red,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
        // first name field
        OutlinedTextField(
            value = guestName,
            onValueChange = {
                isGuestNameValid = true
                guestName = it
            },
            singleLine = true,
            label = { Text(text = "first name") },
//            placeholder = { Text(text = "jane_smith96@gmail.com") },
            isError = !isGuestNameValid,
            leadingIcon = {
                Icon(imageVector = Icons.Default.Email, contentDescription = "email icon")
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                backgroundColor = LightYellow.copy(alpha = 0.25f),
                cursorColor = LightGreen,
                focusedBorderColor = LightYellow,
                unfocusedBorderColor = LightYellow,
                disabledBorderColor = LightYellow,
                errorBorderColor = Color.Red,
                focusedLabelColor = LightGreen,
                unfocusedLabelColor = LightYellow,
                disabledLabelColor = LightGreen,
                errorLabelColor = Color.Red,
                textColor = LightGreen,
                errorCursorColor = LightGreen,
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Words,
                autoCorrect = false,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    isGuestNameValid = guestName.isNotBlank()
                    if (isGuestNameValid) {
                        localFocusManager.moveFocus(FocusDirection.Down)
                    }
                }
            )
        )
        if (!isGuestNameValid) {
            Text(
                text = "Please enter your name",
                fontSize = 18.sp,
                color = Color.Red,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )
        } else {
            Spacer(modifier = Modifier.height(18.dp))
        }
        // email field
        OutlinedTextField(
            value = guestEmail,
            onValueChange = {
                isValidEmail = true
                guestEmail = it
            },
            singleLine = true,
            label = { Text(text = "email") },
//            placeholder = { Text(text = "jane_smith96@gmail.com") },
            isError = !isValidEmail,
            leadingIcon = {
                Icon(imageVector = Icons.Default.Email, contentDescription = "email icon")
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                backgroundColor = LightYellow.copy(alpha = 0.25f),
                cursorColor = LightGreen,
                focusedBorderColor = LightYellow,
                unfocusedBorderColor = LightYellow,
                disabledBorderColor = LightYellow,
                errorBorderColor = Color.Red,
                focusedLabelColor = LightGreen,
                unfocusedLabelColor = LightYellow,
                disabledLabelColor = LightGreen,
                errorLabelColor = Color.Red,
                textColor = LightGreen,
                errorCursorColor = LightGreen
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                autoCorrect = false,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    isValidEmail = guestEmail.isValidEmail()
                    isGuestNameValid = guestName.isNotBlank()
                    isValidOption = selectedOption.isNotBlank()
                    if (isGuestNameValid && isValidEmail && isValidOption) {
                        viewModel.setBarcodeValueForGuestSignIn(
                            BarcodeValue(
                                id = "0",
                                firstName = guestName,
                                email = guestEmail,
                                discoveryOption = selectedOption
                            )
                        )
                        onGuestEmailEntered.invoke()
                    }
                }
            )
        )
        if (!isValidEmail) {
            Text(
                text = "Please enter a valid email",
                fontSize = 18.sp,
                color = Color.Red,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
        Button(
            onClick = {
                isValidEmail = guestEmail.isValidEmail()
                isGuestNameValid = guestName.isNotBlank()
                isValidOption = selectedOption.isNotBlank()
                if (isGuestNameValid && isValidEmail && isValidOption) {
                    viewModel.setBarcodeValueForGuestSignIn(
                        BarcodeValue(
                            id = "0",
                            firstName = guestName,
                            email = guestEmail,
                            discoveryOption = selectedOption
                        )
                    )
                    onGuestEmailEntered.invoke()
                }
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = LightYellow),
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = "Submit", color = LightGreen)
        }
    }
}

@Composable
fun DiscoverySurvey(
    localFocusManager: FocusManager,
    onOptionStateSelected: (String) -> Unit
) {

    var otherOption by remember {
        mutableStateOf("")
    }
    var isOtherOptionValid by remember {
        mutableStateOf(true)
    }

    Text(
        text = "How did you hear about us?",
        fontSize = 24.sp,
        color = LightGreen,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )

    val radioOptions = listOf(
        "Instagram",
        "Facebook",
        "Maps",
        "Meetup",
        "Friend (non-member)",
        "Friend (member)",
        "Sidewalk Sign",
        "Flyer/Poster",
        "Newsletter",
        "Other:"
    )
    val (selectedOption, onOptionSelected) = remember {
        mutableStateOf("")
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2)
    ) {
        items(radioOptions) { text ->
//            Column {
//                radioOptions.forEach { text ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (text == selectedOption),
                        onClick = {
                            onOptionSelected(text)
                            onOptionStateSelected.invoke(text)
                        }
                    ),
//                        .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
//                        modifier = Modifier.padding(8.dp),
                    selected = (selectedOption.contains(text)),
                    onClick = {
                        onOptionSelected(text)
                    },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = LightGreen,
                        unselectedColor = LightYellow
                    )
                )
                Text(
//                        modifier = Modifier.padding(start = 16.dp),
                    text = text,
                    color = if (text == selectedOption) LightGreen else LightYellow
                )
                if (text == "Other:" && selectedOption.contains("Other:")) {
                    OutlinedTextField(
                        value = otherOption,
                        onValueChange = {
                            isOtherOptionValid = true
                            otherOption = it
                        },
                        singleLine = true,
                        label = { Text(text = "please specify") },
//            placeholder = { Text(text = "jane_smith96@gmail.com") },
                        isError = !isOtherOptionValid,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            backgroundColor = LightYellow.copy(alpha = 0.25f),
                            cursorColor = LightGreen,
                            focusedBorderColor = LightYellow,
                            unfocusedBorderColor = LightYellow,
                            disabledBorderColor = LightYellow,
                            errorBorderColor = Color.Red,
                            focusedLabelColor = LightGreen,
                            unfocusedLabelColor = LightYellow,
                            disabledLabelColor = LightGreen,
                            errorLabelColor = Color.Red,
                            textColor = LightGreen,
                            errorCursorColor = LightGreen,
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            capitalization = KeyboardCapitalization.Words,
                            autoCorrect = false,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = {
                                isOtherOptionValid = otherOption.isNotBlank()
                                if (selectedOption.contains("Other:") && isOtherOptionValid) {
                                    onOptionStateSelected.invoke("Other:$otherOption")
                                    localFocusManager.moveFocus(FocusDirection.Down)
                                }
                            }
                        )
                    )
                }
            }
//                }
//            }
        }
    }
}

private fun String.isValidEmail(): Boolean {
    return !TextUtils.isEmpty(this.trim()) && Patterns.EMAIL_ADDRESS.matcher(this.trim()).matches()
}