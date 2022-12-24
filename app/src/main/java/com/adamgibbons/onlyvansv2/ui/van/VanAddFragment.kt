package com.adamgibbons.onlyvansv2.ui.van

import android.annotation.SuppressLint
import android.graphics.ImageDecoder
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import com.adamgibbons.onlyvansv2.R
import com.adamgibbons.onlyvansv2.databinding.FragmentVanAddBinding
import com.adamgibbons.onlyvansv2.helpers.checkLocationPermissions
import com.adamgibbons.onlyvansv2.helpers.showImagePicker
import com.adamgibbons.onlyvansv2.models.Location
import com.adamgibbons.onlyvansv2.models.VanModel
import com.adamgibbons.onlyvansv2.ui.login.LoggedInViewModel
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import timber.log.Timber
import java.util.*

class VanAddFragment : Fragment() {

    private val args by navArgs<VanAddFragmentArgs>()
    private var _fragBinding: FragmentVanAddBinding? = null
    private val binding get() = _fragBinding!!
    private lateinit var imageIntentLauncher: ActivityResultLauncher<PickVisualMediaRequest>
    private lateinit var vanAddViewModel: VanAddViewModel
    private lateinit var vanEditViewModel: VanDetailViewModel
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()

    private var location = Location(52.245696, -7.139102, 15f)
    private lateinit var oldImageUri: String
    private var newImageUri: String = ""
    lateinit var locationService: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationService = LocationServices.getFusedLocationProviderClient(requireContext())
        if (checkLocationPermissions(requireActivity())) {
            doSetCurrentLocation()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragBinding = FragmentVanAddBinding.inflate(inflater, container, false)
        setupMenu()

        val thisYear: Int = Calendar.getInstance().get(Calendar.YEAR)
        val years: IntArray = (1900..thisYear).toList().toIntArray()
        val stringArray = years.map { it.toString() }.toTypedArray()
        (_fragBinding!!.yearPickerTextField.editText as? MaterialAutoCompleteTextView)?.setSimpleItems(stringArray)

        vanAddViewModel = ViewModelProvider(this)[VanAddViewModel::class.java]
        vanEditViewModel = ViewModelProvider(this)[VanDetailViewModel::class.java]
        if (args.vanid == "") {
            Timber.i("No van id, render empty form")
        } else {
            vanEditViewModel.observableVan.observe(viewLifecycleOwner) { van ->
                oldImageUri = van.imageUri
                binding.vanTitle.setText(van.title)
                binding.vanDescription.setText(van.description)
                binding.btnAdd.setText(R.string.update_van)
                binding.chooseImage.setText(R.string.change_image)
                if (newImageUri != "") {
                    Glide.with(this).load(newImageUri).into(binding.vanImage)
                } else {
                    Glide.with(this).load(van.imageUri).into(binding.vanImage)
                }
                binding.colorPicker.setText(van.color, false)
                binding.enginePicker.setText(van.engine.toString(), false)
                binding.yearPicker.setText(van.year.toString(), false)
                location = van.location
            }
        }
        vanAddViewModel.observableStatus.observe(viewLifecycleOwner, Observer {
                status ->
                status?.let { render(status) }
        })

        registerImagePickerCallback()

        binding.btnAdd.setOnClickListener {
            if (args.vanid != "") {
                editVan()
            } else {
                addVan()
            }
        }

        binding.chooseImage.setOnClickListener {
            addVanImage()
        }

        return binding.root
    }


    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_add_van, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return NavigationUI.onNavDestinationSelected(menuItem,
                    requireView().findNavController())
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun render(status: Boolean) {
        when (status) {
            true -> {
                view?.let {
                    findNavController().popBackStack()
                }
            }
            false -> Toast.makeText(context,getString(R.string.addVanError), Toast.LENGTH_LONG).show()
        }
    }

    private fun addVan() {
        try {
            vanAddViewModel.addVan(VanModel(
                title = binding.vanTitle.text.toString(),
                description = binding.vanDescription.text.toString(),
                color = binding.colorPicker.text.toString(),
                engine = binding.enginePicker.text.toString().toDouble(),
                year = binding.yearPicker.text.toString().toInt(),
                location = location,
                imageUri = newImageUri
            ),
            loggedInViewModel.liveFirebaseUser)

        } catch (exception: java.lang.Exception) {
            Timber.i("All fields are required $exception")
            view?.let { Snackbar.make(it, "All fields are required!", Snackbar.LENGTH_LONG).show() }
        }

    }

    private fun editVan() {
        try {
            vanAddViewModel.editVan(VanModel(
                id = args.vanid,
                userid = loggedInViewModel.liveFirebaseUser.value!!.uid,
                title = binding.vanTitle.text.toString(),
                description = binding.vanDescription.text.toString(),
                color = binding.colorPicker.text.toString(),
                engine = binding.enginePicker.text.toString().toDouble(),
                year = binding.yearPicker.text.toString().toInt(),
                location = location,
                imageUri = if (newImageUri != "") newImageUri else oldImageUri
            ),
                loggedInViewModel.liveFirebaseUser)

        } catch (exception: java.lang.Exception) {
            Timber.i("All fields are required $exception")
            view?.let { Snackbar.make(it, "All fields are required!", Snackbar.LENGTH_LONG).show() }
        }

    }

    private fun addVanImage() {
        showImagePicker(imageIntentLauncher)
    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia())
            { uri ->
                if (uri != null) {
                    Timber.i("PhotoPicker Selected URI: $uri")
                    newImageUri = uri.toString()
                    val source = ImageDecoder.createSource(requireActivity().contentResolver, uri)
                    val bitmap = ImageDecoder.decodeBitmap(source)
                    binding.vanImage.setImageBitmap(bitmap)
                } else {
                    Timber.i("PhotoPicker No media selected")
                }
            }
    }

    @SuppressLint("MissingPermission")
    private fun doSetCurrentLocation() {
        Timber.i("setting location from doSetLocation")
        locationService.lastLocation.addOnSuccessListener {
            locationUpdate(it.latitude, it.longitude)
        }
    }

    private fun locationUpdate(lat: Double, lng: Double) {
        location.lat = lat
        location.lng = lng
        location.zoom = 15f
    }

    override fun onResume() {
        super.onResume()
        if (args.vanid != "") {
            vanEditViewModel.getVan(args.vanid)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

}