package com.effatheresoft.mindlesslyhiragana.data



enum class Hiragana(val hiragana: String, val romaji: String) {
    A("あ", "a"),
    I("い", "i"),
    U("う", "u"),
    E("え", "e"),
    O("お", "o"),
    KA("か", "ka"),
    KI("き", "ki"),
    KU("く", "ku"),
    KE("け", "ke"),
    KO("こ", "ko"),
    SA("さ", "sa"),
    SHI("し", "shi"),
    SU("す", "su"),
    SE("せ", "se"),
    SO("そ", "so"),
    TA("た", "ta"),
    CHI("ち", "chi"),
    TSU("つ", "tsu"),
    TE("て", "te"),
    TO("と", "to"),
    NA("な", "na"),
    NI("に", "ni"),
    NU("ぬ", "nu"),
    NE("ね", "ne"),
    NO("の", "no"),
    HA("は", "ha"),
    HI("ひ", "hi"),
    FU("ふ", "fu"),
    HE("へ", "he"),
    HO("ほ", "ho"),
    MA("ま", "ma"),
    MI("み", "mi"),
    MU("む", "mu"),
    ME("め", "me"),
    MO("も", "mo"),
    YA("や", "ya"),
    YU("ゆ", "yu"),
    YO("よ", "yo"),
    RA("ら", "ra"),
    RI("り", "ri"),
    RU("る", "ru"),
    RE("れ", "re"),
    RO("ろ", "ro"),
    WA("わ", "wa"),
    WO("を", "wo"),
    N("ん", "n"),
    GA("が", "ga"),
    GI("ぎ", "gi"),
    GU("ぐ", "gu"),
    GE("げ", "ge"),
    GO("ご", "go"),
    ZA("ざ", "za"),
    JI("じ", "ji"),
    ZU("ず", "zu"),
    ZE("ぜ", "ze"),
    ZO("ぞ", "zo"),
    DA("だ", "da"),
    DJI("ぢ", "dji"),
    DZU("づ", "dzu"),
    DE("で", "de"),
    DO("ど", "do"),
    BA("ば", "ba"),
    BI("び", "bi"),
    BU("ぶ", "bu"),
    BE("べ", "be"),
    BO("ぼ", "bo"),
    PA("ぱ", "pa"),
    PI("ぴ", "pi"),
    PU("ぷ", "pu"),
    PE("ぺ", "pe"),
    PO("ぽ", "po");

    companion object {
        val categories: List<HiraganaCategory> = listOf(
            HiraganaCategory("0", listOf(HI, MI, KA, SE)),
            HiraganaCategory("1", listOf(FU, WO, YA)),
            HiraganaCategory("2", listOf(A, O, E, N)),
            HiraganaCategory("3", listOf(U, TSU, KU, HE)),
            HiraganaCategory("4", listOf(KE, RI, KO, NI)),
            HiraganaCategory("5", listOf(SU, MU, NA, TA, RU, RO)),
            HiraganaCategory("6", listOf(SHI, I, MO)),
            HiraganaCategory("7", listOf(TO, TE, SO)),
            HiraganaCategory("8", listOf(NE, WA, RE)),
            HiraganaCategory("9", listOf(NU, ME, NO, YU)),
            HiraganaCategory("10", listOf(HA, HO, YO, MA)),
            HiraganaCategory("11", listOf(KI, SA, CHI, RA))
        )
    }
}

data class HiraganaCategory(
    val id: String,
    val hiraganaList: List<Hiragana>
)

fun HiraganaCategory.generateQuestions(setCount: Int): List<Hiragana> {
    val multipliedList = mutableListOf<Hiragana>().apply {
        repeat(setCount) {
            addAll(hiraganaList)
        }
    }

    repeat(1000) {
        val questions = multipliedList.shuffled()
        if (questions.zipWithNext().all { it.first != it.second }) {
            return questions
        }
    }

    return multipliedList
}

fun List<HiraganaCategory>.getCategoryById(id: String): HiraganaCategory? {
    return this.find { it.id == id }
}

fun List<HiraganaCategory>.getLearnedHiraganaUpToId(id: String): List<Hiragana> {
    val categoryIndex = indexOfFirst { it.id == id }
    if (categoryIndex == -1) return emptyList()
    return this.take(categoryIndex + 1).flatMap { it.hiraganaList }
}
