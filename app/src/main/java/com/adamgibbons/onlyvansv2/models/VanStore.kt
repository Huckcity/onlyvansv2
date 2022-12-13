package com.adamgibbons.onlyvansv2.models

import androidx.lifecycle.MutableLiveData

interface VanStore {
    fun findById(van: MutableLiveData<VanModel>, id: String)
    fun findAll(vanList: MutableLiveData<List<VanModel>>)
    fun create(van: VanModel)
    fun update(van: VanModel)
    fun delete(van: VanModel)
}