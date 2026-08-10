package com.flint.presentation.collectioncreate

import android.content.Context
import android.content.Intent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.Companion.ACTION_SYSTEM_FALLBACK_PICK_IMAGES

/**
 * Android 12 이하에서 Play 서비스가 제공하는 폴백 포토피커를 쓸 때,
 * androidx 가 붙여주는 max extra 를 피커가 읽지 못해 단일 선택으로 열리는 경우가 있다.
 * GMS 네임스페이스의 extra 를 함께 실어 다중 선택이 유지되도록 한다.
 *
 * 폴백 경로(ACTION_SYSTEM_FALLBACK_PICK_IMAGES)일 때만 extra 를 추가하므로
 * OS 포토피커(ACTION_PICK_IMAGES)나 SAF 폴백 동작에는 영향이 없다.
 */
class GmsCompatPickMultipleVisualMedia(
    private val maxItems: Int,
) : ActivityResultContracts.PickMultipleVisualMedia(maxItems) {

    override fun createIntent(context: Context, input: PickVisualMediaRequest): Intent =
        super.createIntent(context, input).apply {
            if (action == ACTION_SYSTEM_FALLBACK_PICK_IMAGES) {
                putExtra(GMS_EXTRA_PICK_IMAGES_MAX, maxItems)
            }
        }

    private companion object {
        const val GMS_EXTRA_PICK_IMAGES_MAX = "com.google.android.gms.provider.extra.PICK_IMAGES_MAX"
    }
}
