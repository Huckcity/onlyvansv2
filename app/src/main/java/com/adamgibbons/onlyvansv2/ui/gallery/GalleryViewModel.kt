package com.adamgibbons.onlyvansv2.ui.gallery

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adamgibbons.onlyvansv2.firebase.FirebaseDBManager
import timber.log.Timber

class GalleryViewModel : ViewModel() {

    val images = MutableLiveData<List<String>>()

    fun loadImages() {
        try {
            FirebaseDBManager.findAllImages(images)
            println(images)
        } catch(e: Exception) {
            Timber.i("Error fetching vans : ${e.message}")
        }
    }

}