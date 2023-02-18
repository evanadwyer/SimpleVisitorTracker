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
import com.google.api.services.sheets.v4.model.AppendValuesResponse
import com.google.api.services.sheets.v4.model.ValueRange
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SheetsService(
    context: Context
) {

    private val service: Sheets

    init {
        val scopes = listOf(SheetsScopes.SPREADSHEETS)
        val credential = GoogleAccountCredential.usingOAuth2(context, scopes)
        credential.selectedAccount = GoogleSignIn.getLastSignedInAccount(context)?.account
        service = Sheets.Builder(
            NetHttpTransport(),
            GsonFactory.getDefaultInstance(),
            credential
        )
            .setApplicationName("SimpleVisitorTracker")
            .build()
    }

    suspend fun appendValues(
        spreadsheetId: String,
        range: String,
        valueInputOption: String,
        values: List<List<Any>>
    ): AppendValuesResponse? {
        var response: AppendValuesResponse? = null
        try {
            val body = ValueRange().setValues(values)

            withContext(Dispatchers.IO) {
                response = service.spreadsheets().values().append(spreadsheetId, range, body)
                    .setValueInputOption(valueInputOption)
                    .execute()
            }
        } catch (e: GoogleJsonResponseException) {
            Log.e("Sheets Service", "unable to submit checkin", e)
        }
        return response
    }

    suspend fun readIDsToNames(
        spreadsheetId: String,
        range: String,
    ): ValueRange? {
        var response: ValueRange? = null
        try {
            withContext(Dispatchers.IO) {
                response = service.spreadsheets().values()
                    .get(spreadsheetId, range)
                    .execute()
            }
        } catch (e: GoogleJsonResponseException) {
            Log.e("Sheets Service", "unable to load IDs to Names", e)
        }
        return response
    }

}