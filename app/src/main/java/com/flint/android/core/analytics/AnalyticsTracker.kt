package com.flint.android.core.analytics

/**
 * 분석 이벤트 전송 창구.
 *
 * 화면·ViewModel 은 이 인터페이스에만 의존한다.
 * Amplitude 를 다른 도구로 바꾸거나 테스트에서 가짜 구현을 끼울 때
 * 호출부를 건드리지 않기 위한 경계다.
 */
interface AnalyticsTracker {
    /** 정의서에 있는 이벤트를 전송한다. */
    fun track(event: FlintEvent)

    /**
     * 로그인한 사용자를 식별한다. 로그인 성공 직후 한 번 호출한다.
     *
     * null 을 넘기면 식별을 해제한다.
     */
    fun setUserId(userId: String?)

    /**
     * 사용자 식별 정보를 초기화한다. 로그아웃·회원탈퇴 시 호출한다.
     *
     * 호출하지 않으면 같은 기기에서 다음에 로그인한 사람의 행동이
     * 이전 사용자에 붙어 집계된다.
     */
    fun reset()
}
