package com.adamgibbons.onlyvansv2.ui.van

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adamgibbons.onlyvansv2.firebase.FirebaseDBManager
import com.adamgibbons.onlyvansv2.models.VanModel
import com.google.firebase.auth.FirebaseUser

class VanAddViewModel : ViewModel() {

    private val status = MutableLiveData<Boolean>()

    val observableStatus: LiveData<Boolean>
        get() = status

    fun addVan(van: VanModel, user: MutableLiveData<FirebaseUser>) {
        println("adding van...")
        status.value = try {
            FirebaseDBManager.create(van, user)
            true
        } catch (e: IllegalArgumentException) {
            println("Failed to add van: $e.message")
            false
        }
    }

    fun editVan(van: VanModel, user: MutableLiveData<FirebaseUser>) {
        println("Editing van: $van")
        status.value = try {
            if (van.userid == user.value?.uid) {
                FirebaseDBManager.update(van)
            }
            true
        } catch (e: IllegalArgumentException) {
            println("Failed to edit van: $e.message")
            false
        }
    }
}