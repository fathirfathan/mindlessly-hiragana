package com.kaishijak.mindlesslyhiragana.analytics

interface Analytics {
    fun logEvent(eventName: String, params: Map<String, String> = emptyMap())
}

