package com.effatheresoft.mindlesslyhiragana.data.model

import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana.A
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana.CHI
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana.E
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana.FU
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana.HA
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana.HE
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana.HI
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana.HO
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana.I
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana.KA
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana.KE
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana.KI
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana.KO
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana.KU
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana.MA
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana.ME
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana.MI
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana.MO
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana.MU
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana.N
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana.NA
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana.NE
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana.NI
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana.NO
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana.NU
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana.O
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana.RA
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana.RE
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana.RI
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana.RO
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana.RU
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana.SA
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana.SE
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana.SHI
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana.SO
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana.SU
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana.TA
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana.TE
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana.TO
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana.TSU
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana.U
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana.WA
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana.WO
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana.YA
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana.YO
import com.effatheresoft.mindlesslyhiragana.data.model.Hiragana.YU


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

    companion object {
        fun progressToCategoryList(progress: String): List<HiraganaCategory> {
            if (progress.isEmpty()) return emptyList()
            val progressCategory = HiraganaCategory.entries.first { it.id == progress }
            return HiraganaCategory.entries.filter { it.ordinal <= progressCategory.ordinal }
        }

        fun getAllHiraganaUntilCategory(category: HiraganaCategory): List<Hiragana> {
            return HiraganaCategory.entries.filter { it.ordinal <= category.ordinal }.flatMap { it.hiraganaList }
        }
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
