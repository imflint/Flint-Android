package com.flint.android.core.common.extension

// 연도 정보가 없는 작품은 서버/매퍼에서 0 으로 내려온다. 이런 작품은 연도를 아예 그리지 않도록 null 을 돌려준다.
fun Int.toDisplayYear(): String? = takeIf { it > 0 }?.toString()
