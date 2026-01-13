package com.flint.domain.type

import com.flint.R

enum class OttType(val imageRes: Int, val iconRes: Int) {
    Netflix(R.drawable.img_netflix, R.drawable.ic_small_netflix),
    Tving(R.drawable.img_tving, R.drawable.ic_small_tving),
    Wave(R.drawable.img_wave, R.drawable.ic_small_wave),
    Coupang(R.drawable.img_coupang, R.drawable.ic_small_coupang),
    Disney(R.drawable.img_disney, R.drawable.ic_small_disney)
}
