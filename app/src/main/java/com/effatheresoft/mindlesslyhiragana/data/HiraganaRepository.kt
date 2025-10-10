package com.effatheresoft.mindlesslyhiragana.data

import com.effatheresoft.mindlesslyhiragana.util.Result
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

data class HiraganaCategory(
    val id: String,
    val hiraganaList: List<Hiragana>
)

class HiraganaRepository(private val hiraganaDataSource: HiraganaDataSource) {
    fun getHiraganaCategories(): Flow<Result<List<HiraganaCategory>>> = flow {
        emit(Result.Loading)
        val hiraganaCategories = hiraganaDataSource.fetchHiraganaCategories()
        emit(Result.Success(hiraganaCategories))
    }.catch { e ->
        emit(Result.Error(e))
    }

    fun getHiraganaCategoryById(id: String): Flow<Result<HiraganaCategory>> = flow {
        emit(Result.Loading)
        val hiraganaCategory = hiraganaDataSource.fetchHiraganaCategoryById(id)
        emit(Result.Success(hiraganaCategory))
    }.catch { e ->
        emit(Result.Error(e))
    }
}

class HiraganaDataSource() {
    suspend fun fetchHiraganaCategories(): List<HiraganaCategory> {
        delay(2000)
        return hiraganaCategories
    }

    suspend fun fetchHiraganaCategoryById(id: String): HiraganaCategory {
        delay(1000)
        return hiraganaCategories.first { it.id == id }
    }

    val hiraganaCategories = listOf(
        HiraganaCategory("0", listOf(Hiragana.HI, Hiragana.MI, Hiragana.KA, Hiragana.SE)),
        HiraganaCategory("1", listOf(Hiragana.FU, Hiragana.WO, Hiragana.YA)),
        HiraganaCategory("2", listOf(Hiragana.A, Hiragana.O, Hiragana.E, Hiragana.N)),
        HiraganaCategory("3", listOf(Hiragana.U, Hiragana.TSU, Hiragana.KU, Hiragana.HE)),
        HiraganaCategory("4", listOf(Hiragana.KE, Hiragana.RI, Hiragana.KO, Hiragana.NI)),
        HiraganaCategory("5", listOf(Hiragana.SU, Hiragana.MU, Hiragana.NA, Hiragana.TA, Hiragana.RU, Hiragana.RO)),
        HiraganaCategory("6", listOf(Hiragana.SHI, Hiragana.I, Hiragana.MO)),
        HiraganaCategory("7", listOf(Hiragana.TO, Hiragana.TE, Hiragana.SO)),
        HiraganaCategory("8", listOf(Hiragana.NE, Hiragana.WA, Hiragana.RE)),
        HiraganaCategory("9", listOf(Hiragana.NU, Hiragana.ME, Hiragana.NO, Hiragana.YU)),
        HiraganaCategory("10", listOf(Hiragana.HA, Hiragana.HO, Hiragana.YO, Hiragana.MA)),
        HiraganaCategory("11", listOf(Hiragana.KI, Hiragana.SA, Hiragana.CHI, Hiragana.RA))
    )
}