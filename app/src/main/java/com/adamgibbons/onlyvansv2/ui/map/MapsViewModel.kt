package com.adamgibbons.onlyvansv2.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adamgibbons.onlyvansv2.firebase.FirebaseDBManager
import com.adamgibbons.onlyvansv2.models.VanModel
import com.google.firebase.auth.FirebaseUser
import timber.log.Timber

class MapsViewModel : ViewModel() {

    private val vanList = MutableLiveData<List<VanModel>>()
    var user = MutableLiveData<FirebaseUser>()

    val observableVansList: LiveData<List<VanModel>>
        get() = vanList

    init {
        load()
    }

    fun load() {
        try {
            FirebaseDBManager.findAll(vanList)
        } catch(e: Exception) {
            Timber.i("Error fetching vans : ${e.message}")
        }
    }
}