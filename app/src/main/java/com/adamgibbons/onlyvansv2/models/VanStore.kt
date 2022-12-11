package com.adamgibbons.onlyvansv2.models

import androidx.lifecycle.MutableLiveData

interface VanStore {
    fun findById(id: String) : VanModel
    fun findAll(vanList: MutableLiveData<List<VanModel>>)
    fun create(van: VanModel)
    fun update(van: VanModel)
    fun delete(van: VanModel)
}