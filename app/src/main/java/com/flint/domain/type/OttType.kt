package com.flint.domain.type

import com.flint.R

enum class OttType(
    val imageRes: Int,
    val iconRes: Int,
    val ottName: String,
) {
    Netflix(R.drawable.img_netflix, R.drawable.ic_small_netflix, "넷플릭스"),
    Tving(R.drawable.img_tving, R.drawable.ic_small_tving, "티빙"),
    Wavve(R.drawable.img_wave, R.drawable.ic_small_wave, "웨이브"),
    CoupangPlay(R.drawable.img_coupang, R.drawable.ic_small_coupang, "쿠팡플레이"),
    Disney(R.drawable.img_disney, R.drawable.ic_small_disney, "디즈니플러스"),
    Watcha(R.drawable.img_watcha, R.drawable.ic_small_watcha, "왓챠피디아"),
}
