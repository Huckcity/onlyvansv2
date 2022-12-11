package com.adamgibbons.onlyvansv2.ui.van

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.adamgibbons.onlyvansv2.R
import com.adamgibbons.onlyvansv2.databinding.FragmentVanAddBinding
import com.adamgibbons.onlyvansv2.helpers.encodeImage
import com.adamgibbons.onlyvansv2.helpers.showImagePicker
import com.adamgibbons.onlyvansv2.models.Location
import com.adamgibbons.onlyvansv2.models.VanModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import java.io.InputStream
import java.util.*

class VanAddFragment : Fragment() {

    private var _fragBinding: FragmentVanAddBinding? = null
    private val binding get() = _fragBinding!!
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var vanAddViewModel: VanAddViewModel
    private var location = Location(52.245696, -7.139102, 15f)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        vanAddViewModel.observableStatus.observe(viewLifecycleOwner, Observer {
                status -> status?.let { render(status) }
        })

        registerImagePickerCallback()
//        registerMapCallback()

        binding.btnAdd.setOnClickListener {
            addVan()
        }

        binding.chooseImage.setOnClickListener {
            addVanImage()
        }

//        binding.vanLocation.setOnClickListener {
//            val launcherIntent = Intent(context, MapsActivity::class.java)
//                .putExtra("location", location)
//            mapIntentLauncher.launch(launcherIntent)
//        }

        return binding.root
    }


    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                // Handle for example visibility of menu items
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_add_van, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Validate and handle the selected menu item
                return NavigationUI.onNavDestinationSelected(menuItem,
                    requireView().findNavController())
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun render(status: Boolean) {
        when (status) {
            true -> {
                println("POPPING THAT FAT BACKSTACK!!!!!!!!!!!!!!!!!!!!!!!!!!")
                view?.let {
                    //Uncomment this if you want to immediately return to Report
                    findNavController().popBackStack()
                }
            }
            false -> Toast.makeText(context,getString(R.string.addVanError), Toast.LENGTH_LONG).show()
        }
    }

    private fun addVan() {
        try {
            vanAddViewModel.addVan(VanModel(
                title = binding.vanDescription.text.toString(),
                description = binding.vanDescription.text.toString(),
                color = binding.colorPicker.text.toString(),
                engine = binding.enginePicker.text.toString().toDouble(),
                year = binding.yearPicker.text.toString().toInt(),
                location = location
            ))

//            if (van.title.isEmpty() || van.description.isEmpty() || van.color.isEmpty() || van.engine.toString().isEmpty() || van.year.toString().isEmpty()) {
//                view?.let { Snackbar.make(it, "All fields are required!", Snackbar.LENGTH_LONG).show() }
//            } else {
//                vanAddViewModel.addVan(van)
//
//                if (edit) {
//                    app.vans.update(van.copy())
//                } else {
//                    app.vans.create(van.copy())
//                }
//                view?.let { Snackbar.make(it, "Van Updated!", Snackbar.LENGTH_LONG).show() }
//            }
        } catch (exception: java.lang.Exception) {
            view?.let { Snackbar.make(it, "All fields are required!", Snackbar.LENGTH_LONG).show() }
        }

    }

    private fun addVanImage() {
        showImagePicker(imageIntentLauncher)
    }

//    private fun registerMapCallback() {
//        mapIntentLauncher =
//            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
//            { result ->
//                when (result.resultCode) {
//                    Activity.RESULT_OK -> {
//                        if (result.data != null) {
//                            location = result.data!!.extras?.getParcelable("location")!!
//                        }
//                    }
//                    Activity.RESULT_CANCELED -> { } else -> { }
//                }
//            }
//    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    Activity.RESULT_OK -> {
                        if (result.data != null) {
                            val imageStream: InputStream? =
                                requireActivity().contentResolver.openInputStream(result.data!!.data!!)
                            val selectedImage = BitmapFactory.decodeStream(imageStream)
                            val encodedImage: String? = encodeImage(selectedImage)

                            binding.vanImage.setImageBitmap(selectedImage)
                            binding.chooseImage.setText(R.string.change_image)
                            println(encodedImage)
//                            if (encodedImage != null) {
//                                van.image64 = encodedImage
//                            }
                        } // end of if
                    }
                    Activity.RESULT_CANCELED -> { } else -> { }
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

}