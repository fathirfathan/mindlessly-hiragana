package com.kaishijak.mindlesslyhiragana.sharedtest.analytics

import android.util.Log
import com.kaishijak.mindlesslyhiragana.analytics.Analytics
import com.kaishijak.mindlesslyhiragana.analytics.onlyConsistOfAlphanumericsUnderscores

class FakeAnalytics: Analytics {
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
    }
}