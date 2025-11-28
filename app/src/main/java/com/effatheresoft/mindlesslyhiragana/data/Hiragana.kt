package com.effatheresoft.mindlesslyhiragana.data

data class HiraganaCategory(
    val id: String,
    val hiraganaList: List<Hiragana>
) {
    fun toHiraganaStringWithNakaguro(): String {
        val hiraganaBeforeNakaguroSet = setOf(Hiragana.U, Hiragana.HE, Hiragana.NI, Hiragana.MU)
        return hiraganaList.joinToString("") { if (it in hiraganaBeforeNakaguroSet) "${it.kana}・" else it.kana }
    }
}

enum class Hiragana(val kana: String) {
    // Vowels
    A("あ"),
    I("い"),
    U("う"),
    E("え"),
    O("お"),

    // K-line
    KA("か"),
    KI("き"),
    KU("く"),
    KE("け"),
    KO("こ"),

    // S-line
    SA("さ"),
    SHI("し"),
    SU("す"),
    SE("せ"),
    SO("そ"),

    // T-line
    TA("た"),
    CHI("ち"),
    TSU("つ"),
    TE("て"),
    TO("と"),

    // N-line
    NA("な"),
    NI("に"),
    NU("ぬ"),
    NE("ね"),
    NO("の"),

    // H-line
    HA("は"),
    HI("ひ"),
    FU("ふ"),
    HE("へ"),
    HO("ほ"),

    // M-line
    MA("ま"),
    MI("み"),
    MU("む"),
    ME("め"),
    MO("も"),

    // Y-line
    YA("や"),
    YU("ゆ"),
    YO("よ"),

    // R-line
    RA("ら"),
    RI("り"),
    RU("る"),
    RE("れ"),
    RO("ろ"),

    // W-line + N
    WA("わ"),
    WO("を"),
    N("ん"),

    // G-line
    GA("が"),
    GI("ぎ"),
    GU("ぐ"),
    GE("げ"),
    GO("ご"),

    // Z-line
    ZA("ざ"),
    JI("じ"),
    ZU("ず"),
    ZE("ぜ"),
    ZO("ぞ"),

    // D-line
    DA("だ"),
    DJI("ぢ"),
    DZU("づ"),
    DE("で"),
    DO("ど"),

    // B-line
    BA("ば"),
    BI("び"),
    BU("ぶ"),
    BE("べ"),
    BO("ぼ"),

    // P-line
    PA("ぱ"),
    PI("ぴ"),
    PU("ぷ"),
    PE("ぺ"),
    PO("ぽ");

    companion object {
        fun getCategories(): List<HiraganaCategory> {
            return listOf(
                HiraganaCategory("himikase",  listOf(HI,  MI, KA,  SE)),
                HiraganaCategory("fuwoya",    listOf(FU,  WO, YA)),
                HiraganaCategory("ao",        listOf(A,   O)),
                HiraganaCategory("tsuune",    listOf(TSU, U,  N,   E)),
                HiraganaCategory("kuherike",  listOf(KU,  HE, RI,  KE)),
                HiraganaCategory("konitana",  listOf(KO,  NI, TA,  NA)),
                HiraganaCategory("sumuroru",  listOf(SU,  MU, RO,  RU)),
                HiraganaCategory("shiimo",    listOf(SHI, I,  MO)),
                HiraganaCategory("toteso",    listOf(TO,  TE, SO)),
                HiraganaCategory("wanere",    listOf(WA,  NE, RE)),
                HiraganaCategory("noyumenu",  listOf(NO,  YU, ME,  NU)),
                HiraganaCategory("yohamaho",  listOf(YO,  HA, MA,  HO)),
                HiraganaCategory("sakichira", listOf(SA,  KI, CHI, RA)),
            )
        }
    }
}
