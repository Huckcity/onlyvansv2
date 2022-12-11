package com.adamgibbons.onlyvansv2.firebase

import androidx.lifecycle.MutableLiveData
import com.adamgibbons.onlyvansv2.models.VanModel
import com.adamgibbons.onlyvansv2.models.VanStore
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import timber.log.Timber

object FirebaseDBManager : VanStore {

    var database: DatabaseReference = FirebaseDatabase.getInstance().reference
    override fun findById(id: String): VanModel {
        TODO("Not yet implemented")
    }

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
                        val donation = it.getValue(VanModel::class.java)
                        localList.add(donation!!)
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

//    override fun findById(userid: String, donationid: String, donation: MutableLiveData<DonationModel>) {
//
//        database.child("user-donations").child(userid)
//            .child(donationid).get().addOnSuccessListener {
//                donation.value = it.getValue(DonationModel::class.java)
//                Timber.i("firebase Got value ${it.value}")
//            }.addOnFailureListener{
//                Timber.e("firebase Error getting data $it")
//            }
//    }

    override fun create(van: VanModel) {
        Timber.i("Firebase DB Reference : $database")

//        val uid = firebaseUser.value!!.uid
        val key = database.child("vans").push().key
        if (key == null) {
            Timber.i("Firebase Error : Key Empty")
            return
        }
        van.id = key
        val vanValues = van.toMap()

        val childAdd = HashMap<String, Any>()
        childAdd["/vans/$key"] = vanValues
//        childAdd["/user-vans/$id/$key"] = donationValues

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

    fun updateImageRef(userid: String,imageUri: String) {

        val userVans = database.child("user-vans").child(userid)
        val allVans = database.child("vans")

        userVans.addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {}
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach {
                        //Update Users imageUri
                        it.ref.child("profilepic").setValue(imageUri)
                        //Update all donations that match 'it'
                        val van = it.getValue(VanModel::class.java)
                        allVans.child(van!!.id!!)
                            .child("profilepic").setValue(imageUri)
                    }
                }
            })
    }
}