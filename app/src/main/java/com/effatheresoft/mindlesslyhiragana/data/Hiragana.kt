package com.effatheresoft.mindlesslyhiragana.data

import com.effatheresoft.mindlesslyhiragana.data.Hiragana.HI
import com.effatheresoft.mindlesslyhiragana.data.Hiragana.KA
import com.effatheresoft.mindlesslyhiragana.data.Hiragana.MI
import com.effatheresoft.mindlesslyhiragana.data.Hiragana.SE
import com.effatheresoft.mindlesslyhiragana.data.Hiragana.FU
import com.effatheresoft.mindlesslyhiragana.data.Hiragana.WO
import com.effatheresoft.mindlesslyhiragana.data.Hiragana.YA
import com.effatheresoft.mindlesslyhiragana.data.Hiragana.A
import com.effatheresoft.mindlesslyhiragana.data.Hiragana.O
import com.effatheresoft.mindlesslyhiragana.data.Hiragana.TSU
import com.effatheresoft.mindlesslyhiragana.data.Hiragana.U
import com.effatheresoft.mindlesslyhiragana.data.Hiragana.N
import com.effatheresoft.mindlesslyhiragana.data.Hiragana.E
import com.effatheresoft.mindlesslyhiragana.data.Hiragana.KU
import com.effatheresoft.mindlesslyhiragana.data.Hiragana.HE
import com.effatheresoft.mindlesslyhiragana.data.Hiragana.RI
import com.effatheresoft.mindlesslyhiragana.data.Hiragana.KE
import com.effatheresoft.mindlesslyhiragana.data.Hiragana.KO
import com.effatheresoft.mindlesslyhiragana.data.Hiragana.NI
import com.effatheresoft.mindlesslyhiragana.data.Hiragana.TA
import com.effatheresoft.mindlesslyhiragana.data.Hiragana.NA
import com.effatheresoft.mindlesslyhiragana.data.Hiragana.SU
import com.effatheresoft.mindlesslyhiragana.data.Hiragana.MU
import com.effatheresoft.mindlesslyhiragana.data.Hiragana.RO
import com.effatheresoft.mindlesslyhiragana.data.Hiragana.RU
import com.effatheresoft.mindlesslyhiragana.data.Hiragana.SHI
import com.effatheresoft.mindlesslyhiragana.data.Hiragana.I
import com.effatheresoft.mindlesslyhiragana.data.Hiragana.MO
import com.effatheresoft.mindlesslyhiragana.data.Hiragana.TO
import com.effatheresoft.mindlesslyhiragana.data.Hiragana.TE
import com.effatheresoft.mindlesslyhiragana.data.Hiragana.SO
import com.effatheresoft.mindlesslyhiragana.data.Hiragana.WA
import com.effatheresoft.mindlesslyhiragana.data.Hiragana.NE
import com.effatheresoft.mindlesslyhiragana.data.Hiragana.RE
import com.effatheresoft.mindlesslyhiragana.data.Hiragana.NO
import com.effatheresoft.mindlesslyhiragana.data.Hiragana.YU
import com.effatheresoft.mindlesslyhiragana.data.Hiragana.ME
import com.effatheresoft.mindlesslyhiragana.data.Hiragana.NU
import com.effatheresoft.mindlesslyhiragana.data.Hiragana.YO
import com.effatheresoft.mindlesslyhiragana.data.Hiragana.HA
import com.effatheresoft.mindlesslyhiragana.data.Hiragana.MA
import com.effatheresoft.mindlesslyhiragana.data.Hiragana.HO
import com.effatheresoft.mindlesslyhiragana.data.Hiragana.SA
import com.effatheresoft.mindlesslyhiragana.data.Hiragana.KI
import com.effatheresoft.mindlesslyhiragana.data.Hiragana.CHI
import com.effatheresoft.mindlesslyhiragana.data.Hiragana.RA


enum class HiraganaCategory(
    val id: String,
    val hiraganaList: List<Hiragana>
) {
    HIMIKASE ("himikase",  listOf(HI,  MI, KA,  SE)),
    FUWOYA   ("fuwoya",    listOf(FU,  WO, YA)),
    AO       ("ao",        listOf(A,   O)),
    TSUUNE   ("tsuune",    listOf(TSU, U,  N,   E)),
    KUHERIKE ("kuherike",  listOf(KU,  HE, RI,  KE)),
    KONITANA ("konitana",  listOf(KO,  NI, TA,  NA)),
    SUMURORU ("sumuroru",  listOf(SU,  MU, RO,  RU)),
    SHIIMO   ("shiimo",    listOf(SHI, I,  MO)),
    TOTESO   ("toteso",    listOf(TO,  TE, SO)),
    WANERE   ("wanere",    listOf(WA,  NE, RE)),
    NOYUMENU ("noyumenu",  listOf(NO,  YU, ME,  NU)),
    YOHAMAHO ("yohamaho",  listOf(YO,  HA, MA,  HO)),
    SAKICHIRA("sakichira", listOf(SA,  KI, CHI, RA));

    fun toHiraganaStringWithNakaguro(): String {
        val hiraganaBeforeNakaguroSet = setOf(U, HE, NI, MU)
        return hiraganaList.joinToString("") { if (it in hiraganaBeforeNakaguroSet) "${it.kana}・" else it.kana }
    }

    val kanaWithNakaguro: String
        get() {
            val hiraganaBeforeNakaguroSet = setOf(U, HE, NI, MU)
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
}
