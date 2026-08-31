package com.flint.android.core.analytics

/**
 * 기획 이벤트 로그 정의서 기준 Amplitude 이벤트 목록.
 *
 * 이벤트 이름과 파라미터 키를 문자열로 직접 넘기면 오타를 컴파일 시점에 못 잡고,
 * 잘못 쌓인 로그는 되돌릴 수 없으므로 여기서만 정의한다.
 * 화면 코드에서는 [AnalyticsTracker.track] 에 이 타입만 넘긴다.
 *
 * 새 이벤트는 정의서에 추가된 것만 넣는다. 코드에서 임의로 만들면
 * 기획이 보는 지표와 어긋난다.
 */
sealed class FlintEvent(
    val eventName: String,
    val properties: Map<String, Any> = emptyMap(),
) {
    // ────────── 앱 / 로그인 ──────────

    /** 로그인 페이지에서 회원가입 버튼 클릭 */
    data object ClickSignup : FlintEvent("click_signup")

    /** 기존 사용자 로그인 성공 */
    data object CompleteLogin : FlintEvent("complete_login")

    // ────────── 회원가입 / 온보딩 ──────────

    /** 약관 동의 페이지 진입 */
    data object ViewTos : FlintEvent("view_tos")

    /** 온보딩 작품 선택 페이지 진입 */
    data object ViewFilmSelect : FlintEvent("view_filmselect")

    /** 프로필(닉네임) 설정 페이지 진입 */
    data object ViewNickname : FlintEvent("view_nickname")

    /** 온보딩 완료 페이지 진입 */
    data object ViewOnboardingDone : FlintEvent("view_onboarding_done")

    /**
     * 온보딩 완료까지 걸린 시간.
     *
     * @param durationSec 약관 페이지 진입부터 완료까지의 초.
     *   온보딩이 여러 화면에 걸쳐 있어 중간에 앱이 종료될 수 있으므로,
     *   시작 시각은 메모리가 아니라 로컬 저장소에 남겨야 한다.
     */
    data class CompleteOnboarding(
        val durationSec: Long,
    ) : FlintEvent("complete_onboarding", mapOf(DURATION_SEC to durationSec))

    /** 회원 생성 성공 및 회원가입 최종 완료 */
    data object CompleteSignup : FlintEvent("complete_signup")

    // ────────── 홈 ──────────

    /** 홈 데이터 로딩 완료 후 정상 표시된 시점 */
    data object ViewHome : FlintEvent("view_home")

    /** 홈 내 콘텐츠 또는 기능 클릭 */
    data class ClickHomeContent(
        val contentType: HomeContentType,
    ) : FlintEvent("click_home_content", mapOf(CONTENT_TYPE to contentType.value))

    // ────────── 네비게이션 ──────────

    /** 하단 네비게이션 탭 클릭 */
    data class ClickBottomNavigation(
        val tab: BottomNavigationTab,
    ) : FlintEvent("click_bottom_navigation", mapOf(TAB_NAME to tab.value))

    // ────────── 컬렉션 ──────────

    /**
     * 컬렉션 상세 화면 진입.
     *
     * 정의서에는 collection_id 와 source 가 각각 다른 행으로 적혀 있으나,
     * 두 번 전송하면 진입 수가 2배로 집계되므로 한 이벤트에 두 속성을 함께 싣는다.
     */
    data class ViewCollection(
        val collectionId: String,
        val source: CollectionSource,
    ) : FlintEvent(
            "view_collection",
            mapOf(COLLECTION_ID to collectionId, SOURCE to source.value),
        )

    /** 컬렉션 내 작품 저장 */
    data class SaveContent(
        val contentId: String,
    ) : FlintEvent("save_content", mapOf(CONTENT_ID to contentId))

    /** 컬렉션 저장 */
    data class SaveCollection(
        val collectionId: String,
    ) : FlintEvent("save_collection", mapOf(COLLECTION_ID to collectionId))

    /** 컬렉션 생성 페이지 진입 */
    data object ViewCreateCollection : FlintEvent("view_create_collection")

    /** 컬렉션 생성 및 발행 완료 */
    data class CompleteCreateCollection(
        val collectionId: String,
    ) : FlintEvent("complete_create_collection", mapOf(COLLECTION_ID to collectionId))

    // ────────── 탐색 ──────────

    /**
     * 탐색에서 개별 작품 노출.
     *
     * 노출 이벤트는 스크롤할 때마다 발생해 볼륨이 커진다.
     * 노출 판정 기준(가시 비율·노출 시간)과 세션 내 중복 전송 여부를 기획과 확정한 뒤 호출할 것.
     */
    data class ViewExploreContent(
        val contentId: String,
    ) : FlintEvent("view_explore_content", mapOf(CONTENT_ID to contentId))

    /** 탐색에서 해당 작품이 포함된 컬렉션으로 이동 */
    data class ClickExploreCollection(
        val collectionId: String,
    ) : FlintEvent("click_explore_collection", mapOf(COLLECTION_ID to collectionId))

    /**
     * 탐색 페이지 이탈 시 체류시간 기록.
     *
     * @param durationSec 탐색 진입부터 이탈까지의 초.
     *   무엇을 "이탈"로 볼지(탭 전환 / 컬렉션 상세 진입 / 백그라운드)는 기획 확정 필요.
     *   앱이 강제 종료되면 전송 기회가 없어 일부 유실될 수 있다.
     */
    data class ExitExplore(
        val durationSec: Long,
    ) : FlintEvent("exit_explore", mapOf(DURATION_SEC to durationSec))

    // ────────── 마이 / 저장 ──────────

    /** 사용자 키워드 업데이트 완료 */
    data object UpdateKeyword : FlintEvent("update_keyword")

    /** 저장한 작품 목록에서 이전에 저장한 작품을 다시 확인 */
    data class ViewSavedContent(
        val contentId: String,
    ) : FlintEvent("view_saved_content", mapOf(CONTENT_ID to contentId))

    /** 저장한 컬렉션 목록에서 이전에 저장한 컬렉션을 다시 확인 */
    data class ViewSavedCollection(
        val collectionId: String,
    ) : FlintEvent("view_saved_collection", mapOf(COLLECTION_ID to collectionId))
}

/** 홈에서 클릭한 콘텐츠 영역 */
enum class HomeContentType(
    val value: String,
) {
    // 정의서의 content_type 은 "fliner", source 는 "home_flinner" 로 철자가 다르다.
    // 어느 쪽이 맞는지 기획 확인 후 한쪽으로 통일할 것.
    FLINER("fliner"),
    RECENTLY_SAVED("recently_saved"),
    POPULAR("popular"),
}

/** 하단 네비게이션 탭 */
enum class BottomNavigationTab(
    val value: String,
) {
    HOME("home"),
    EXPLORE("explore"),
    MY("my"),
}

/** 컬렉션 상세로 진입한 경로 */
enum class CollectionSource(
    val value: String,
) {
    HOME_FLINNER("home_flinner"),
    HOME_POPULAR("home_popular"),
    EXPLORE("explore"),
    MY_SAVED("my_saved"),
    MY_CREATED("my_created"),
    ;

    companion object {
        /**
         * 네비게이션 인자로 실어 보낸 문자열을 되돌린다.
         *
         * 타입 안전 라우트가 enum 을 그대로 담지 못해 value 문자열로 오간다.
         * 알 수 없는 값이면 잘못된 경로로 집계하는 대신 null 을 반환해 이벤트를 생략한다.
         */
        fun from(value: String): CollectionSource? = entries.find { it.value == value }
    }
}

private const val DURATION_SEC = "duration_sec"
private const val CONTENT_TYPE = "content_type"
private const val TAB_NAME = "tab_name"
private const val COLLECTION_ID = "collection_id"
private const val CONTENT_ID = "content_id"
private const val SOURCE = "source"
