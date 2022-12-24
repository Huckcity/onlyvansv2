package com.adamgibbons.onlyvansv2.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adamgibbons.onlyvansv2.firebase.FirebaseDBManager
import com.adamgibbons.onlyvansv2.models.VanModel
import com.google.firebase.auth.FirebaseUser
import timber.log.Timber

class MapsViewModel : ViewModel() {

    val van = MutableLiveData<VanModel>()
    var observableVan: LiveData<VanModel>
        get() = van
        set(value) {
            van.value = value.value
        }

    fun getVan(id: String) {
        try {
            FirebaseDBManager.findById(van, id)
            Timber.i("Fetching Van Details : ${
                van.value.toString()}")
        }
        catch (e: Exception) {
            Timber.i("Failed to retrieve van details : $e.message")
        }
    }

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