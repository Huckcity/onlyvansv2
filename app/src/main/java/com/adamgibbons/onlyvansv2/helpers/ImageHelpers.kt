package com.adamgibbons.onlyvansv2.helpers

import android.graphics.Color
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.makeramen.roundedimageview.RoundedTransformationBuilder
import com.squareup.picasso.Transformation

fun showImagePicker(intentLauncher: ActivityResultLauncher<PickVisualMediaRequest>) {
    intentLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
}

fun customTransformation() : Transformation =
    RoundedTransformationBuilder()
        .borderColor(Color.WHITE)
        .borderWidthDp(2F)
        .cornerRadiusDp(35F)
        .oval(false)
        .build()