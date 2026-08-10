package com.flint.presentation.onboarding.component

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

/**
 * 약관 전문에 실제로 쓰이는 문법만 다루는 최소 파서.
 * 서버 약관은 `### 제목`, 번호 목록, 불릿 목록, `**굵게**` 로만 구성돼 있어
 * 마크다운 라이브러리를 붙이지 않고 필요한 범위만 직접 변환한다.
 */
sealed interface TermsBlock {
    val text: AnnotatedString

    data class Heading(override val text: AnnotatedString) : TermsBlock
    data class Paragraph(override val text: AnnotatedString) : TermsBlock

    /** 번호/불릿 목록. [marker] 는 "1." 또는 "•" 처럼 앞에 붙는 기호다. */
    data class ListItem(val marker: String, override val text: AnnotatedString) : TermsBlock
}

private val HEADING_REGEX = Regex("""^#{1,6}\s+(.*)$""")
private val NUMBERED_REGEX = Regex("""^(\d+\.)\s+(.*)$""")
private val BULLET_REGEX = Regex("""^[-*•]\s+(.*)$""")
private val BOLD_REGEX = Regex("""\*\*(.+?)\*\*""")

fun parseTermsMarkdown(content: String): List<TermsBlock> =
    content.lines()
        .map { it.trim() }
        .filter { it.isNotEmpty() }
        .map { line ->
            HEADING_REGEX.matchEntire(line)?.let {
                return@map TermsBlock.Heading(parseInline(it.groupValues[1]))
            }
            NUMBERED_REGEX.matchEntire(line)?.let {
                return@map TermsBlock.ListItem(it.groupValues[1], parseInline(it.groupValues[2]))
            }
            BULLET_REGEX.matchEntire(line)?.let {
                return@map TermsBlock.ListItem("•", parseInline(it.groupValues[1]))
            }
            TermsBlock.Paragraph(parseInline(line))
        }

/** `**굵게**` 만 처리한다. 나머지 기호는 원문 그대로 둔다. */
private fun parseInline(text: String): AnnotatedString = buildAnnotatedString {
    var lastIndex = 0
    BOLD_REGEX.findAll(text).forEach { match ->
        append(text.substring(lastIndex, match.range.first))
        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
            append(match.groupValues[1])
        }
        lastIndex = match.range.last + 1
    }
    append(text.substring(lastIndex))
}
