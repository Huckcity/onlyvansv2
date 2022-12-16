package com.adamgibbons.onlyvansv2.ui.van

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import com.adamgibbons.onlyvansv2.R
import com.adamgibbons.onlyvansv2.adapters.VanAdapter
import com.adamgibbons.onlyvansv2.adapters.VanListener
import com.adamgibbons.onlyvansv2.databinding.FragmentVanListBinding
import com.adamgibbons.onlyvansv2.models.VanModel

class VanListFragment : Fragment(), VanListener {

    private var _binding: FragmentVanListBinding? = null
    private val binding get() = _binding!!
    private val vanListViewModel : VanListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVanListBinding.inflate(inflater, container, false)
        val root: View = binding.root
        setupMenu()
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        binding.fab.setOnClickListener {
            val action = VanListFragmentDirections.actionHomeFragmentToVanAddFragment()
            findNavController().navigate(action)
        }
        vanListViewModel.observableVansList.observe(viewLifecycleOwner, Observer {
            vans ->
            vans?.let {
                render(vans as ArrayList<VanModel>)
            }
        })

        return root
    }

    private fun render(vanList: ArrayList<VanModel>) {
        binding.recyclerView.adapter = VanAdapter(vanList,this)
        if (vanList.isEmpty()) {
            binding.recyclerView.visibility = View.GONE
            binding.vansNotFound.visibility = View.VISIBLE
        } else {
            binding.recyclerView.visibility = View.VISIBLE
            binding.vansNotFound.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onVanClick(van: VanModel) {
        val action = VanListFragmentDirections.actionHomeFragmentToVanDetailFragment(van.id)
        findNavController().navigate(action)
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

    override fun onResume() {
        super.onResume()
        vanListViewModel.load()
    }
}