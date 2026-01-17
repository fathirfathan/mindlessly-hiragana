package com.effatheresoft.mindlesslyhiragana.data.model

import com.effatheresoft.mindlesslyhiragana.data.model.HiraganaCategory.entries

enum class HiraganaCategory(
    val id: String,
    val hiraganaList: List<Hiragana>
) {
    HIMIKASE ("himikase",  listOf(Hiragana.HI, Hiragana.MI, Hiragana.KA, Hiragana.SE)),
    FUWOYA   ("fuwoya",    listOf(Hiragana.FU, Hiragana.WO, Hiragana.YA)),
    AO       ("ao",        listOf(Hiragana.A, Hiragana.O)),
    TSUUNE   ("tsuune",    listOf(Hiragana.TSU, Hiragana.U, Hiragana.N, Hiragana.E)),
    KUHERIKE ("kuherike",  listOf(Hiragana.KU, Hiragana.HE, Hiragana.RI, Hiragana.KE)),
    KONITANA ("konitana",  listOf(Hiragana.KO, Hiragana.NI, Hiragana.TA, Hiragana.NA)),
    SUMURORU ("sumuroru",  listOf(Hiragana.SU, Hiragana.MU, Hiragana.RO, Hiragana.RU)),
    SHIIMO   ("shiimo",    listOf(Hiragana.SHI, Hiragana.I, Hiragana.MO)),
    TOTESO   ("toteso",    listOf(Hiragana.TO, Hiragana.TE, Hiragana.SO)),
    WANERE   ("wanere",    listOf(Hiragana.WA, Hiragana.NE, Hiragana.RE)),
    NOYUMENU ("noyumenu",  listOf(Hiragana.NO, Hiragana.YU, Hiragana.ME, Hiragana.NU)),
    YOHAMAHO ("yohamaho",  listOf(Hiragana.YO, Hiragana.HA, Hiragana.MA, Hiragana.HO)),
    SAKICHIRA("sakichira", listOf(Hiragana.SA, Hiragana.KI, Hiragana.CHI, Hiragana.RA));

    fun toHiraganaStringWithNakaguro(): String {
        val hiraganaBeforeNakaguroSet = setOf(Hiragana.U, Hiragana.HE, Hiragana.NI, Hiragana.MU)
        return hiraganaList.joinToString("") { if (it in hiraganaBeforeNakaguroSet) "${it.kana}・" else it.kana }
    }

    fun toRoomEntityProgress(): String = ordinal.plus(1).toString()

    fun getNextCategoryOrNull() = entries.getOrNull(ordinal + 1)

    val isLastCategory: Boolean get() = this == entries.last()
    val complementedHiraganaList: List<Hiragana> get() =
        entries.filter { it.ordinal <= this.ordinal }.flatMap { it.hiraganaList }
    val complementedHiraganaCategory: List<HiraganaCategory> get() = entries.filter { it.ordinal <= this.ordinal }

    val kanaWithNakaguro: String
        get() {
            val hiraganaBeforeNakaguroSet = setOf(Hiragana.U, Hiragana.HE, Hiragana.NI, Hiragana.MU)
            return hiraganaList.joinToString("") { if (it in hiraganaBeforeNakaguroSet) "${it.kana}・" else it.kana }
        }
}

fun String.toHiraganaCategoryOrNull(): HiraganaCategory? {
    return entries.firstOrNull { category -> category.id == this }
}