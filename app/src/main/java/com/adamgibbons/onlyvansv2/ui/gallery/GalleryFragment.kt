package com.adamgibbons.onlyvansv2.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.adamgibbons.onlyvansv2.adapters.ImagesAdapter
import com.adamgibbons.onlyvansv2.adapters.VanAdapter
import com.adamgibbons.onlyvansv2.databinding.FragmentGalleryBinding
import com.adamgibbons.onlyvansv2.models.VanModel
import com.adamgibbons.onlyvansv2.ui.login.LoggedInViewModel

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    private val galleryViewModel: GalleryViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        galleryViewModel.images.observe(viewLifecycleOwner, Observer { images ->
            images?.let {
                binding.recyclerView.adapter = ImagesAdapter(images)
                if (images.isEmpty()) {
                    binding.recyclerView.visibility = View.GONE
                } else {
                    binding.recyclerView.visibility = View.VISIBLE
                }
            }
        })
        return root
    }

    override fun onResume() {
        super.onResume()
        galleryViewModel.loadImages()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}