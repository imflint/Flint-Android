package com.flint.android.core.common.extension

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri

fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("no activity")
}

// 이 앱의 플레이스토어 상세 페이지를 연다.
// Play 스토어 앱이 없는 기기(에뮬레이터 등)에서는 웹 페이지로 대신 연다.
fun Context.openPlayStore() {
    val marketIntent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName"))
    try {
        startActivity(marketIntent)
    } catch (e: ActivityNotFoundException) {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$packageName"),
            ),
        )
    }
}

// 서버가 내려준 스토어 주소로 이동한다. 주소가 비었거나 열 수 있는 앱이 없으면 이 앱의 플레이스토어 페이지를 연다.
fun Context.openStoreUrl(storeUrl: String) {
    if (storeUrl.isBlank()) return openPlayStore()

    try {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(storeUrl)))
    } catch (e: ActivityNotFoundException) {
        openPlayStore()
    }
}
