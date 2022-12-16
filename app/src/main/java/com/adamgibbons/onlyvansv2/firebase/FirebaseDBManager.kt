package com.adamgibbons.onlyvansv2.firebase

import androidx.lifecycle.MutableLiveData
import com.adamgibbons.onlyvansv2.models.VanModel
import com.adamgibbons.onlyvansv2.models.VanStore
import com.google.firebase.database.*
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

//    override fun findAll(userid: String, donationsList: MutableLiveData<List<DonationModel>>) {
//
//        database.child("user-donations").child(userid)
//            .addValueEventListener(object : ValueEventListener {
//                override fun onCancelled(error: DatabaseError) {
//                    Timber.i("Firebase Donation error : ${error.message}")
//                }
//
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    val localList = ArrayList<DonationModel>()
//                    val children = snapshot.children
//                    children.forEach {
//                        val donation = it.getValue(DonationModel::class.java)
//                        localList.add(donation!!)
//                    }
//                    database.child("user-donations").child(userid)
//                        .removeEventListener(this)
//
//                    donationsList.value = localList
//                }
//            })
//    }

    override fun findById(van: MutableLiveData<VanModel>, id: String) {

        database.child("vans").child(id).get().addOnSuccessListener {
            van.value = it.getValue(VanModel::class.java)
            Timber.i("Van Found: ${it.value}")
        }.addOnFailureListener{
            Timber.e("Could not find van: $it")
        }
    }

    override fun create(van: VanModel) {
        Timber.i("Firebase DB Reference : $database")

        val key = database.child("vans").push().key
        if (key == null) {
            Timber.i("Firebase Error : Key Empty")
            return
        }
        van.id = key
        FirebaseImageManager.updateVanImage(van, van.imageUri, false)

        val vanValues = van.toMap()
        val childAdd = HashMap<String, Any>()
        childAdd["/vans/$key"] = vanValues

        database.updateChildren(childAdd)
    }

    override fun update(van: VanModel) {
        TODO("Not yet implemented")
    }

    override fun delete(van: VanModel) {
        TODO("Not yet implemented")
    }

//    override fun delete(userid: String, donationid: String) {
//
//        val childDelete : MutableMap<String, Any?> = HashMap()
//        childDelete["/donations/$donationid"] = null
//        childDelete["/user-donations/$userid/$donationid"] = null
//
//        database.updateChildren(childDelete)
//    }

//    override fun update(van: VanModel) {
//
//        val vanValues = van.toMap()
//
//        val childUpdate : MutableMap<String, Any?> = HashMap()
//        childUpdate["vans/$vanId"] = vanValues
//        childUpdate["user-vans/$userId/$vanId"] = vanValues
//
//        database.updateChildren(childUpdate)
//    }

}