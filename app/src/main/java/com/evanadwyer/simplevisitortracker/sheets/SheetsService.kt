package com.evanadwyer.simplevisitortracker.sheets

import android.content.Context
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.SheetsScopes
import com.google.api.services.sheets.v4.model.ValueRange
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun appendValues(
    context: Context,
    scope: CoroutineScope,
    spreadsheetId: String,
    range: String,
    valueInputOption: String,
    values: List<List<Any>>
) {
    val scopes = listOf(SheetsScopes.SPREADSHEETS)
    val credential = GoogleAccountCredential.usingOAuth2(context, scopes)
    credential.selectedAccount = GoogleSignIn.getLastSignedInAccount(context)?.account
    val service = Sheets.Builder(
        NetHttpTransport(),
        GsonFactory.getDefaultInstance(),
        credential
    )
        .setApplicationName("SimpleVisitorTracker")
        .build()

    try {
        val body = ValueRange().setValues(values)

        scope.launch(Dispatchers.IO) {
            service.spreadsheets().values().append(spreadsheetId, range, body)
                .setValueInputOption(valueInputOption)
                .execute()
        }
    } catch (e: GoogleJsonResponseException) {
        Log.e("Sheets Service", e.message, e)
    }
}