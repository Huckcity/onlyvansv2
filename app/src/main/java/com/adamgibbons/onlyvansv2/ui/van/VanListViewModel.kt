package com.adamgibbons.onlyvansv2.ui.van

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adamgibbons.onlyvansv2.firebase.FirebaseDBManager
import com.adamgibbons.onlyvansv2.models.VanModel
import com.google.firebase.auth.FirebaseUser
import timber.log.Timber

class VanListViewModel : ViewModel() {

    private val vanList = MutableLiveData<List<VanModel>>()
    var user = MutableLiveData<FirebaseUser>()

    val observableVansList: LiveData<List<VanModel>>
        get() = vanList

    init {
        load()
    }

    fun load() {
        try {
            FirebaseDBManager.findAllByUser(vanList, user.value?.uid!!)
        } catch(e: Exception) {
            Timber.i("Error fetching vans : ${e.message}")
        }
    }

    fun delete(van: VanModel) {
        try {
            FirebaseDBManager.delete(van)
        } catch(e: Exception) {
            Timber.i("Error deleting van: ${e.message}")
        }
    }

    fun edit(van: VanModel) {

    }
}