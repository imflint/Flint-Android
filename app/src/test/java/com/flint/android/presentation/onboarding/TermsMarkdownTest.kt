package com.flint.android.presentation.onboarding

import com.flint.android.presentation.onboarding.component.TermsBlock
import com.flint.android.presentation.onboarding.component.parseTermsMarkdown
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TermsMarkdownTest {

    @Test
    fun `### 로 시작하면 제목 블록이 되고 기호는 남지 않는다`() {
        val blocks = parseTermsMarkdown("### 제2조. 약관의 게시와 효력, 개정")

        assertEquals(1, blocks.size)
        assertTrue(blocks[0] is TermsBlock.Heading)
        assertEquals("제2조. 약관의 게시와 효력, 개정", blocks[0].text.text)
    }

    @Test
    fun `제목 안의 굵게 표기는 별표 없이 본문만 남는다`() {
        val blocks = parseTermsMarkdown("### **제1조. 목적**")

        assertTrue(blocks[0] is TermsBlock.Heading)
        assertEquals("제1조. 목적", blocks[0].text.text)
    }

    @Test
    fun `번호 목록은 번호를 기호로 분리한다`() {
        val blocks = parseTermsMarkdown("1. 사이트는 서비스의 가입 과정에 본 약관을 게시합니다.")

        val item = blocks[0] as TermsBlock.ListItem
        assertEquals("1.", item.marker)
        assertEquals("사이트는 서비스의 가입 과정에 본 약관을 게시합니다.", item.text.text)
    }

    @Test
    fun `불릿 목록은 가운데점 기호로 바뀐다`() {
        val blocks = parseTermsMarkdown("- 계정 정보")

        val item = blocks[0] as TermsBlock.ListItem
        assertEquals("•", item.marker)
        assertEquals("계정 정보", item.text.text)
    }

    @Test
    fun `문장 중간의 굵게 표기는 별표가 제거된다`() {
        val blocks = parseTermsMarkdown("본 약관은 **중요**한 내용입니다.")

        assertTrue(blocks[0] is TermsBlock.Paragraph)
        assertEquals("본 약관은 중요한 내용입니다.", blocks[0].text.text)
    }

    @Test
    fun `빈 줄은 블록으로 만들지 않는다`() {
        val blocks = parseTermsMarkdown("첫 문단\n\n\n둘째 문단")

        assertEquals(2, blocks.size)
        assertEquals("첫 문단", blocks[0].text.text)
        assertEquals("둘째 문단", blocks[1].text.text)
    }

    @Test
    fun `실제 약관 형태의 문서를 블록 순서대로 변환한다`() {
        val content = """
            ### **제1조. 목적**

            본 이용약관은 서비스 이용에 관한 사항을 규정합니다.

            ### 제2조. 약관의 게시와 효력

            1. 사이트는 본 약관을 게시합니다.
            2. 사이트는 약관을 변경할 수 있습니다.

            - 계정 정보
            - 취향 정보
        """.trimIndent()

        val blocks = parseTermsMarkdown(content)

        assertEquals(7, blocks.size)
        assertTrue(blocks[0] is TermsBlock.Heading)
        assertTrue(blocks[1] is TermsBlock.Paragraph)
        assertTrue(blocks[2] is TermsBlock.Heading)
        assertEquals("1.", (blocks[3] as TermsBlock.ListItem).marker)
        assertEquals("2.", (blocks[4] as TermsBlock.ListItem).marker)
        assertEquals("•", (blocks[5] as TermsBlock.ListItem).marker)
        assertEquals("취향 정보", blocks[6].text.text)
    }
}
