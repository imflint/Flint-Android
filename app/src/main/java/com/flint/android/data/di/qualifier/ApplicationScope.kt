package com.flint.android.data.di.qualifier

import javax.inject.Qualifier

/**
 * 앱 프로세스가 살아 있는 동안 유지되는 CoroutineScope.
 *
 * 화면을 벗어나도 끝까지 보내야 하는 서버 쓰기(북마크 토글 등)에만 쓴다.
 * viewModelScope 는 ViewModel 이 정리될 때 취소되므로, 디바운스 대기 중이던 요청이
 * 조용히 사라진다.
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ApplicationScope
