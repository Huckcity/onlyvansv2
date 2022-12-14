package com.adamgibbons.onlyvansv2.firebase

import androidx.lifecycle.MutableLiveData
import com.adamgibbons.onlyvansv2.models.VanModel
import com.adamgibbons.onlyvansv2.models.VanStore
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import timber.log.Timber

object FirebaseDBManager : VanStore {

    var database: DatabaseReference = FirebaseDatabase.getInstance().reference

    override fun findAll(vanList: MutableLiveData<List<VanModel>>) {
        database.child("vans")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Timber.i("Firebase error : ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val localList = ArrayList<VanModel>()
                    val children = snapshot.children
                    children.forEach {
                        val van = it.getValue(VanModel::class.java)
                        localList.add(van!!)
                    }
                    database.child("vans")
                        .removeEventListener(this)

                    vanList.value = localList
                }
            })
    }

    override fun findAllByUser(
        vanList: MutableLiveData<List<VanModel>>,
        userId: String
    ) {

        database.child("vans").orderByChild("userid").equalTo(userId)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Timber.i("Firebase error : ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val localList = ArrayList<VanModel>()
                    val children = snapshot.children
                    children.forEach {
                        val van = it.getValue(VanModel::class.java)
                        localList.add(van!!)
                    }
                    database.child("vans").child(userId)
                        .removeEventListener(this)

                    vanList.value = localList
                }
            })
    }

    override fun findById(van: MutableLiveData<VanModel>, id: String) {

        database.child("vans").child(id).get().addOnSuccessListener {
            van.value = it.getValue(VanModel::class.java)
            Timber.i("Van Found: ${it.value}")
        }.addOnFailureListener{
            Timber.e("Could not find van: $it")
        }
    }

    override fun create(van: VanModel, user: MutableLiveData<FirebaseUser>) {
        Timber.i("Firebase DB Reference : $database")

        val uid = user.value!!.uid
        val key = database.child("vans").push().key
        if (key == null) {
            Timber.i("Firebase Error : Key Empty")
            return
        }
        van.id = key
        van.userid = uid
        FirebaseImageManager.updateVanImage(van, van.imageUri)

        val vanValues = van.toMap()
        val childAdd = HashMap<String, Any>()
        childAdd["/vans/$key"] = vanValues

        database.updateChildren(childAdd)
    }

    override fun delete(van: VanModel) {
        val childDelete : MutableMap<String, Any?> = HashMap()
        childDelete["/vans/${van.id}"] = null
        database.updateChildren(childDelete)
    }

    override fun findAllImages(images: MutableLiveData<List<String>>) {
        database.child("vans")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Timber.i("Firebase error : ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val localList = ArrayList<String>()
                    val children = snapshot.children
                    children.forEach {
                        val van = it.getValue(VanModel::class.java)
                        localList.add(van!!.imageUri)
                    }
                    database.child("vans")
                        .removeEventListener(this)

                    images.value = localList
                }
            })
    }

    override fun update(van: VanModel) {
        FirebaseImageManager.updateVanImage(van, van.imageUri)

        val vanValues = van.toMap()
        val childUpdate : MutableMap<String, Any?> = HashMap()
        childUpdate["vans/${van.id}"] = vanValues
        database.updateChildren(childUpdate)
    }

}