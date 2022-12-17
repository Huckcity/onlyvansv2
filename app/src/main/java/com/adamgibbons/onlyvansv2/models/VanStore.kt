package com.adamgibbons.onlyvansv2.models

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser

interface VanStore {
    fun findById(van: MutableLiveData<VanModel>, id: String)
    fun findAll(vanList: MutableLiveData<List<VanModel>>)
    fun findAllByUser(vanList: MutableLiveData<List<VanModel>>, user: String)
    fun create(van: VanModel, user: MutableLiveData<FirebaseUser>)
    fun update(van: VanModel)
    fun delete(van: VanModel)
}