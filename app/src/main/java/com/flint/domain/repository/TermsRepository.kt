package com.flint.domain.repository

import com.flint.core.common.util.suspendRunCatching
import com.flint.data.api.TermsApi
import com.flint.domain.mapper.terms.toModel
import com.flint.domain.model.terms.TermModel
import javax.inject.Inject

class TermsRepository @Inject constructor(
    private val api: TermsApi,
) {
    suspend fun getTermsList(type: String? = null): Result<List<TermModel>> =
        suspendRunCatching {
            api.getTermsList(type).data.terms.toModel()
        }
}
