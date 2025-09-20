package com.effatheresoft.mindlesslyhiragana.data

data class HiraganaCategory(
    val id: String,
    val hiragana: String,
    val hiraganaRomaji: String
)

object HiraganaRepository {
    private val hiraganaCategories = listOf(
        HiraganaCategory("0", "ひみかせ", "ひみかせ Hi Mi Ka Se"),
        HiraganaCategory("1", "ふをや", "ふをや Fu Wo Ya"),
        HiraganaCategory("2", "あお・えん", "あお・えん A O E N"),
        HiraganaCategory("3", "うつ・くへ", "うつ・くへ U Tsu Ku He"),
        HiraganaCategory("4", "けり・こに", "けり・こに Ke Ri Ko Ni"),
        HiraganaCategory("5", "すむ・なた・るろ", "すむ・なた・るろ Su Mu Na Ta Ru Ro"),
        HiraganaCategory("6", "しいも", "しいも Shi I Mo"),
        HiraganaCategory("7", "とてそ", "とてそ To Te So"),
        HiraganaCategory("8", "ねわれ", "ねわれ Ne Wa Re"),
        HiraganaCategory("9", "ぬめのゆ", "ぬめのゆ Nu Me No Yu"),
        HiraganaCategory("10", "はほよま", "はほよま Ha Ho Yo Ma"),
        HiraganaCategory("11", "きさちら", "きさちら Ki Sa Chi Ra"),
        HiraganaCategory("12", "Test All Learned", "Test All Learned")
    )

    fun getHiraganaCategories(): List<HiraganaCategory> {
        return hiraganaCategories
    }

    fun getHiraganaCategoryById(id: String): HiraganaCategory? {
        return when(id) {
            "0" -> HiraganaCategory("0", "ひみかせ", "ひみかせ Hi Mi Ka Se")
            "1" -> HiraganaCategory("1", "ふをや", "ふをや Fu Wo Ya")
            "2" -> HiraganaCategory("2", "あお・えん", "あお・えん A O E N")
            "3" -> HiraganaCategory("3", "うつ・くへ", "うつ・くへ U Tsu Ku He")
            "4" -> HiraganaCategory("4", "けり・こに", "けり・こに Ke Ri Ko Ni")
            "5" -> HiraganaCategory("5", "すむ・なた・るろ", "すむ・なた・るろ Su Mu Na Ta Ru Ro")
            "6" -> HiraganaCategory("6", "しいも", "しいも Shi I Mo")
            "7" -> HiraganaCategory("7", "とてそ", "とてそ To Te So")
            "8" -> HiraganaCategory("8", "ねわれ", "ねわれ Ne Wa Re")
            "9" -> HiraganaCategory("9", "ぬめのゆ", "ぬめのゆ Nu Me No Yu")
            "10" -> HiraganaCategory("10", "はほよま", "はほよま Ha Ho Yo Ma")
            "11" -> HiraganaCategory("11", "きさちら", "きさちら Ki Sa Chi Ra")
            "12" -> HiraganaCategory("12", "Test All Learned", "Test All Learned")
            else -> null
        }
    }
}