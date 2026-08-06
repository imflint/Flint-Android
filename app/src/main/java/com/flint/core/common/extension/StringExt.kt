package com.flint.core.common.extension

private const val ZERO_WIDTH_SPACE = '​'
private val HANGUL_SYLLABLE_RANGE = 0xAC00..0xD7A3

// 한글 음절 사이에 zero-width space(U+200B)를 삽입해 줄바꿈 기회를 추가
// → 공간이 남아있으면 단어 경계가 아닌 글자 경계에서 줄바꿈 가능
fun String.addKoreanLineBreaks(): String = buildString {
    for (char in this@addKoreanLineBreaks) {
        append(char)
        if (char.code in HANGUL_SYLLABLE_RANGE) append(ZERO_WIDTH_SPACE)
    }
}
