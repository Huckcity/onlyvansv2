package com.adamgibbons.onlyvansv2.ui.van

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.adamgibbons.onlyvansv2.R
import com.adamgibbons.onlyvansv2.databinding.FragmentVanDetailBinding
import com.adamgibbons.onlyvansv2.helpers.customTransformation
import com.squareup.picasso.Picasso

class VanDetailFragment : Fragment() {

    private val args by navArgs<VanDetailFragmentArgs>()

    private var _binding: FragmentVanDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: VanDetailViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        viewModel = ViewModelProvider(this)[VanDetailViewModel::class.java]
        viewModel.observableVan.observe(viewLifecycleOwner) {
            binding.vm = viewModel
        }
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_van_detail, container, false)
        binding.vm = viewModel

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.getVan(args.vanid)
    }

}