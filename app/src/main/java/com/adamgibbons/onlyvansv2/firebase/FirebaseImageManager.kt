package com.adamgibbons.onlyvansv2.firebase

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.adamgibbons.onlyvansv2.helpers.customTransformation
import com.adamgibbons.onlyvansv2.models.VanModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.util.*

object FirebaseImageManager {

    var storage = FirebaseStorage.getInstance().reference
    var database: DatabaseReference = FirebaseDatabase.getInstance().reference
    var imageUri = MutableLiveData<Uri>()

    fun checkStorageForExistingProfilePic(userid: String) {
        val imageRef = storage.child("photos").child("${userid}.jpg")

        imageRef.metadata.addOnSuccessListener { //File Exists
            imageRef.downloadUrl.addOnCompleteListener { task ->
                imageUri.value = task.result!!
            }
        }.addOnFailureListener {
            imageUri.value = Uri.EMPTY
        }
    }

    fun uploadImageToFirebase(van: VanModel, bitmap: Bitmap, updating : Boolean) {

        val imageRef = storage.child("photos").child("${van.id}/${UUID.randomUUID()}")
        val baos = ByteArrayOutputStream()
        lateinit var uploadTask: UploadTask

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        uploadTask = imageRef.putBytes(data)

        uploadTask.addOnSuccessListener { taskSnapshot ->
            taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                van.imageUri = it.toString()
                database.child("vans").child(van.id).setValue(van)
            }
        }.addOnFailureListener{
            val errorMessage = it.message
            Timber.i("Failure: $errorMessage")
        }
    }

    fun updateVanImage(van: VanModel, imageUri: String, updating: Boolean) {
        Picasso.get().load(imageUri)
//            .resize(200, 200)
//            .transform(customTransformation())
//            .memoryPolicy(MemoryPolicy.NO_CACHE)
//            .centerCrop()
            .into(object : Target {
                override fun onBitmapLoaded(bitmap: Bitmap?,
                                            from: Picasso.LoadedFrom?
                ) {
                    Timber.i("DX onBitmapLoaded $bitmap")
                    uploadImageToFirebase(van, bitmap!!,updating)
                }

                override fun onBitmapFailed(e: java.lang.Exception?,
                                            errorDrawable: Drawable?) {
                    Timber.i("DX onBitmapFailed $e")
                }

                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
            })
    }
}