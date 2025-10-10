package com.effatheresoft.mindlesslyhiragana

import android.app.Application
import com.effatheresoft.mindlesslyhiragana.data.HiraganaDataSource
import com.effatheresoft.mindlesslyhiragana.data.HiraganaRepository

class DefaultApplication : Application() {
    val hiraganaDataSource by lazy { HiraganaDataSource() }
    val hiraganaRepository by lazy { HiraganaRepository(hiraganaDataSource) }
}

