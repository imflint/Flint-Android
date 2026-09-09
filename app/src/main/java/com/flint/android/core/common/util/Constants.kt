package com.flint.android.core.common.util

object DataStoreKey {
    const val ACCESS_TOKEN = "accessToken"
    const val REFRESH_TOKEN = "refreshToken"
    const val USER_ID = "userId"
    const val USER_NAME = "userName"
    const val USER_EMAIL = "userEmail"

    /** 온보딩 소요시간 측정용 시작 시각(밀리초). 온보딩 완료 시 지운다. */
    const val ONBOARDING_STARTED_AT = "onboardingStartedAt"
}

object ExternalLinks {
    const val PRIVACY_POLICY_URL = "https://artistic-bacon-a40.notion.site/35650cdb714e804cb642eb2cc576a62c"
    const val TERMS_OF_SERVICE_URL = "https://artistic-bacon-a40.notion.site/35650cdb714e8054a69bcbd110ed19dd?source=copy_link"
}

/**
 * 약관 목록 API 는 전문(content)만 내려주므로, 펼침 영역에 노출할 안내 문구는 앱이 갖는다.
 * 전문은 "자세히 보기" 웹뷰에서 확인한다.
 */
object TermGuides {
    private const val TYPE_SERVICE = "SERVICE"
    private const val TYPE_PRIVACY = "PRIVACY"

    private const val SERVICE_SUMMARY =
        "본 약관은 서비스 이용과 관련한 기본적인 권리·의무 및 책임사항을 규정합니다."

    private const val PRIVACY_SUMMARY =
        "서비스 제공을 위해 개인정보를 수집 · 이용합니다.\n" +
            "콘텐츠 추천, 컬렉션 생성 및 공유, 맞춤형 탐색 경험 제공을 위한 이용 기록 및 취향 정보 처리 내용이 포함됩니다.\n\n" +
            "수집 항목 : 계정 정보, 취향 정보, 컬렉션 및 콘텐츠 활동, 서비스 이용 기록 등\n\n" +
            "수집 목적: 개인화 추천 제공, 컬렉션 생성 및 공유, 서비스 운영 및 이용자 보호"

    /** 안내 문구가 준비되지 않은 유형은 null 을 반환해 호출부가 전문으로 대체하도록 한다. */
    fun summaryOf(type: String): String? = when (type) {
        TYPE_SERVICE -> SERVICE_SUMMARY
        TYPE_PRIVACY -> PRIVACY_SUMMARY
        else -> null
    }
}