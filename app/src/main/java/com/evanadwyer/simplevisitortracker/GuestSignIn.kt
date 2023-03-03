package com.evanadwyer.simplevisitortracker

import android.text.TextUtils
import android.util.Patterns
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
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
    val localFocusManager = LocalFocusManager.current
    BackHandler(onBack = onBack)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(top = 18.dp)
            .background(LightOrange)
    ) {
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
                    if (isGuestNameValid && isValidEmail) {
                        viewModel.setBarcodeValueForGuestSignIn(
                            BarcodeValue(
                                id = "0",
                                firstName = guestName,
                                email = guestEmail
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
    }
}

private fun String.isValidEmail(): Boolean {
    return !TextUtils.isEmpty(this.trim()) && Patterns.EMAIL_ADDRESS.matcher(this.trim()).matches()
}