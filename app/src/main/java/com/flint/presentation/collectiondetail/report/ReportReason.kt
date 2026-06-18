package com.flint.presentation.collectiondetail.report

enum class ReportReason(val displayText: String, val code: String) {
    ABUSE("욕설·혐오 표현이 포함된 콘텐츠", "ABUSE"),
    OBSCENE("음란하거나 선정적인 콘텐츠", "OBSCENE"),
    SPAM("광고·홍보 또는 스팸성 콘텐츠", "SPAM"),
    COPYRIGHT("저작권을 침해한 콘텐츠", "COPYRIGHT"),
    OTHER("기타", "OTHER"),
}
