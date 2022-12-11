package com.adamgibbons.onlyvansv2.helpers

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.util.Base64
import androidx.activity.result.ActivityResultLauncher
import com.adamgibbons.onlyvansv2.R
import com.makeramen.roundedimageview.RoundedTransformationBuilder
import com.squareup.picasso.Transformation
import java.io.ByteArrayOutputStream

fun showImagePicker(intentLauncher : ActivityResultLauncher<Intent>) {

    var chooseFile = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
        type = "image/*"
        addCategory(Intent.CATEGORY_OPENABLE)
    }
    chooseFile = Intent.createChooser(chooseFile, R.string.select_van_image.toString())
    intentLauncher.launch(chooseFile)
}

fun encodeImage(bm: Bitmap): String? {
    val byteArrayOutputStream = ByteArrayOutputStream()
    bm.compress(Bitmap.CompressFormat.JPEG, 30, byteArrayOutputStream)
    val b: ByteArray = byteArrayOutputStream.toByteArray()
    return Base64.encodeToString(b, Base64.DEFAULT)
}

fun decodeImage(imageString: String): Bitmap {
    val decodedByte = Base64.decode(imageString, Base64.DEFAULT)
    val bitmap = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.size)
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
    return bitmap
}

fun customTransformation() : Transformation =
    RoundedTransformationBuilder()
        .borderColor(Color.WHITE)
        .borderWidthDp(2F)
        .cornerRadiusDp(35F)
        .oval(false)
        .build()