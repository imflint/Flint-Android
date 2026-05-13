package com.flint.domain.model.terms

data class TermModel(
    val id: String,
    val type: String,
    val version: Int,
    val title: String,
    val content: String,
    val required: Boolean,
    val activeAt: String,
)
