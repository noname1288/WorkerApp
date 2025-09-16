package com.example.workerapp.utils.cached

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object UserSession {
    private var _isLoggedIn = MutableStateFlow(true)
    val isLoggedIn : StateFlow<Boolean> = _isLoggedIn

    var uid: String? = "snpmuantm0Ucx1mkJLFNde8RuM82"
    var displayName: String? = null
    var userEmail: String? = null
    var userProfilePicUrl: String? = null
    var token: String? = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjUwMDZlMjc5MTVhMTcwYWIyNmIxZWUzYjgxZDExNjU0MmYxMjRmMjAiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vam9icy00YzllMyIsImF1ZCI6ImpvYnMtNGM5ZTMiLCJhdXRoX3RpbWUiOjE3NTc4Njc5MDQsInVzZXJfaWQiOiJzbnBtdWFudG0wVWN4MW1rSkxGTmRlOFJ1TTgyIiwic3ViIjoic25wbXVhbnRtMFVjeDFta0pMRk5kZThSdU04MiIsImlhdCI6MTc1Nzg2NzkwNCwiZXhwIjoxNzU3ODcxNTA0LCJlbWFpbCI6ImxtMUBnbWFpbC5jb20iLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsImZpcmViYXNlIjp7ImlkZW50aXRpZXMiOnsiZW1haWwiOlsibG0xQGdtYWlsLmNvbSJdfSwic2lnbl9pbl9wcm92aWRlciI6InBhc3N3b3JkIn19.OasybG1EQxcLxauSkcK-J4esDwhanTYw33KoW78ATEVnczK36qwrpcuZFN1jMyYSxnOlPHNDQTlc9wM0S6iBTaYglY2MVNIZmvWVABAT_GKOPNg2stLH1Q5MfwHTcPjDXS-DIaEvJDoXinMQkl2E5V48XIUptghC3pMYfrgGRxjpweeekBKTsGMHgcPDDQovz7NVZ_AAyzZ8g_QAGpwDZs_yA4o1FFiaSiLOmnA-kMZplobqsN3-MTd3_0cRqldTc1iV-41iG0-9EPdwrDQLE1iiUbsK-TAWkkdKDZCUnlhOVcdRh_LVteUp03PopgWuWZ8rR4fmStnrkRoJiNDuYA"
    fun saveState(uid: String, displayName: String?, email: String?, profilePicUrl: String?, token: String?) {
        _isLoggedIn.value = true

        this.uid = uid
        this.displayName = displayName
        this.userEmail = email
        this.userProfilePicUrl = profilePicUrl
        this.token = token
    }

    fun logOut() {
        _isLoggedIn.value = false

        uid = null
        displayName = null
        userEmail = null
        userProfilePicUrl = null
        token = null
    }
}