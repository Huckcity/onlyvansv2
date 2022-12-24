package com.adamgibbons.onlyvansv2.helpers

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import androidx.core.app.ActivityCompat
import com.adamgibbons.onlyvansv2.models.Location
import com.google.android.gms.maps.model.LatLng
import java.util.*

const val REQUEST_PERMISSIONS_REQUEST_CODE = 34

fun checkLocationPermissions(activity: Activity) : Boolean {
    return if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        true
    }
    else {
        ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_PERMISSIONS_REQUEST_CODE)
        false
    }
}

fun locationToLatLng(location: Location): LatLng {
    return LatLng(location.lat, location.lng)
}

// Yoinked from random Stack Overflow post.
fun getAddress(activity: Activity, latLng: Location): String {
    val geocoder = Geocoder(activity.applicationContext, Locale.getDefault())
    val address: Address?
    var addressText = ""

    val addresses: List<Address>? = geocoder.getFromLocation(latLng.lat, latLng.lng, 1)

    if (addresses != null) {
        if (addresses.isNotEmpty()) {
            address = addresses[0]
            addressText = address.getAddressLine(0)
        } else{
            addressText = "its not appear" // I'm not even going to change the poor grammar
        }
    }
    return addressText
}