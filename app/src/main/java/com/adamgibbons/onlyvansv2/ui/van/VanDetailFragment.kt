package com.adamgibbons.onlyvansv2.ui.van

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.adamgibbons.onlyvansv2.R

class VanDetailFragment : Fragment() {

    companion object {
        fun newInstance() = VanDetailFragment()
    }

    private lateinit var viewModel: VanDetailViewModel
    private val args by navArgs<VanDetailFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_van_detail, container, false)

        Toast.makeText(context,"Donation ID Selected : ${args.vanid}",Toast.LENGTH_LONG).show()

        return view
    }


}