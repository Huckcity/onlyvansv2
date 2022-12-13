package com.adamgibbons.onlyvansv2.ui.van

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adamgibbons.onlyvansv2.firebase.FirebaseDBManager
import com.adamgibbons.onlyvansv2.models.VanModel
import timber.log.Timber

class VanListViewModel : ViewModel() {

    private val vanList = MutableLiveData<List<VanModel>>()

    val observableVansList: LiveData<List<VanModel>>
        get() = vanList

    init {
        load()
    }

    fun load() {
        try {
            FirebaseDBManager.findAll(vanList)
        } catch(e: Exception) {
            Timber.i("Error fetching vans : $e.message")
        }
    }
}