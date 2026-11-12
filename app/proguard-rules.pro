# 릴리스 빌드(R8) 전용 규칙.
# Retrofit·OkHttp·kotlinx.serialization·Hilt·Coil·Lottie·Amplitude 는 라이브러리에 규칙이 들어 있어서 따로 적지 않는다.
# 여기에는 라이브러리가 챙겨 주지 않는 것만 둔다.

# Play Console 크래시 스택트레이스를 매핑 파일로 되돌렸을 때 줄 번호가 보이도록 남긴다.
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# 타입 세이프 내비게이션은 라우트의 enum 인자를 직렬화 이름(클래스 전체 이름)으로 Class.forName 해서 찾는다.
# 이름이 바뀌면 해당 화면으로 이동할 때 크래시가 나므로 라우트에 쓰는 enum 은 이름을 유지한다.
-keepnames class com.flint.android.core.navigation.model.CollectionListRouteType

# 카카오 SDK 는 응답 모델을 Gson 으로 파싱하는데 자체 R8 규칙을 넣어 주지 않는다.
# 카카오 개발자 문서의 ProGuard 규칙을 그대로 옮겼다.
-keep class com.kakao.sdk.**.model.* { <fields>; }
-keep class * extends com.google.gson.TypeAdapter
-dontwarn org.bouncycastle.jsse.**
-dontwarn org.conscrypt.*
-dontwarn org.openjsse.**
