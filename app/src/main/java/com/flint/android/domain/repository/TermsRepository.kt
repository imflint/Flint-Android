package com.flint.android.domain.repository

import com.flint.android.core.common.util.suspendRunCatching
import com.flint.android.data.api.TermsApi
import com.flint.android.domain.mapper.terms.toModel
import com.flint.android.domain.model.terms.TermModel
import javax.inject.Inject

class TermsRepository @Inject constructor(
    private val api: TermsApi,
) {
    suspend fun getTermsList(type: String? = null): Result<List<TermModel>> =
        suspendRunCatching {
            api.getTermsList(type).data.terms.toModel()
        }
}
