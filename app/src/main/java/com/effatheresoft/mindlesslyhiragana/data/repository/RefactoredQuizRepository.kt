package com.effatheresoft.mindlesslyhiragana.data.repository

import com.effatheresoft.mindlesslyhiragana.data.local.UserDao
import com.effatheresoft.mindlesslyhiragana.data.local.toUser
import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


data class QuizCategory(
    val title: String,
    val isLocked: Boolean
)

fun List<QuizCategory>.isCategoryUnlocked(category: QuizCategory): Boolean {
    return !first { it.title == category.title }.isLocked
}

class RefactoredQuizRepository @Inject constructor(
    userLocalDataSource: UserDao
) {
    private var initJob: Job? = null

    init {
        initJob = CoroutineScope(Job() + Dispatchers.IO).launch {
            userLocalDataSource.observeLocalUser().collect { userRoomEntity ->
                val userProgress = userRoomEntity.toUser().progress
                val categories = mutableListOf<QuizCategory>()
                HiraganaCategory.entries.map { category ->
                    when(category.id) {
                        userProgress -> categories.add(QuizCategory(category.kanaWithNakaguro, false))
                        else -> categories.add(QuizCategory(category.kanaWithNakaguro, true))
                    }
                }
                val currentProgressCategoryIndex: Int = categories.indexOfLast { !it.isLocked }
                categories.add(currentProgressCategoryIndex + 1, QuizCategory("Test All Learned", false))
                quizCategories.value = categories
            }
        }
    }
    private val quizCategories = MutableStateFlow<List<QuizCategory>>(emptyList())
    private val selectedCategory = MutableStateFlow<QuizCategory?>(null)

    fun observeQuizCategories() = quizCategories
    fun observeSelectedCategory() = selectedCategory

    fun selectCategory(category: QuizCategory): Boolean {
        when(quizCategories.value.isCategoryUnlocked(category)) {
            true -> {
                selectedCategory.value = category
                return true
            }
            false -> return false
        }
    }

    fun cancelInitJob() = initJob?.cancel()
}
