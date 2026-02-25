package com.kaishijak.mindlesslyhiragana.analytics

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import com.google.firebase.analytics.logEvent

class DefaultAnalytics: Analytics {
    override fun logEvent(
        eventName: String,
        params: Map<String, String>
    ) {
        if (
            !eventName.onlyConsistOfAlphanumericsUnderscores() or
            params.any { !it.key.onlyConsistOfAlphanumericsUnderscores() } or
            params.any { !it.value.onlyConsistOfAlphanumericsUnderscores() }
        ) {
            Log.d("Analytics", "Invalid event name or parameter name or value")
            return
        }

        Log.d("Analytics", "Event: $eventName, Params: $params")
        Firebase.analytics.logEvent(eventName) {
            params.forEach { (key, value) ->
                param(key, value)
            }
        }
    }
}

fun String.onlyConsistOfAlphanumericsUnderscores(): Boolean {
    val validNameRegex = Regex("^[a-zA-Z0-9_]+$")
    return validNameRegex.matches(this)
}