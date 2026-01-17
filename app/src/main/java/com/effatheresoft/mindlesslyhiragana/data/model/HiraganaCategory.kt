package com.effatheresoft.mindlesslyhiragana.data.model

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

    val kanaWithNakaguro: String
        get() {
            val hiraganaBeforeNakaguroSet = setOf(Hiragana.U, Hiragana.HE, Hiragana.NI, Hiragana.MU)
            return hiraganaList.joinToString("") { if (it in hiraganaBeforeNakaguroSet) "${it.kana}・" else it.kana }
        }

    companion object {
        fun progressToCategoryList(progress: String): List<HiraganaCategory> {
            if (progress.isEmpty()) return emptyList()
            val progressCategory = entries.first { it.id == progress }
            return entries.filter { it.ordinal <= progressCategory.ordinal }
        }

        fun getAllHiraganaUntilCategory(category: HiraganaCategory): List<Hiragana> {
            return entries.filter { it.ordinal <= category.ordinal }.flatMap { it.hiraganaList }
        }
    }
}