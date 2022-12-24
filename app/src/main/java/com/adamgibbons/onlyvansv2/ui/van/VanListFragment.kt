package com.adamgibbons.onlyvansv2.ui.van

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adamgibbons.onlyvansv2.R
import com.adamgibbons.onlyvansv2.adapters.VanAdapter
import com.adamgibbons.onlyvansv2.adapters.VanListener
import com.adamgibbons.onlyvansv2.databinding.FragmentVanListBinding
import com.adamgibbons.onlyvansv2.helpers.SwipeToDeleteCallback
import com.adamgibbons.onlyvansv2.helpers.SwipeToEditCallback
import com.adamgibbons.onlyvansv2.models.VanModel
import com.adamgibbons.onlyvansv2.ui.login.LoggedInViewModel
import com.google.android.material.snackbar.Snackbar

class VanListFragment : Fragment(), VanListener {

    private var _binding: FragmentVanListBinding? = null
    private val binding get() = _binding!!
    private val vanListViewModel : VanListViewModel by activityViewModels()
    private val loggedInViewModel: LoggedInViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVanListBinding.inflate(inflater, container, false)
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

        return binding.root
    }

    private fun render(vanList: ArrayList<VanModel>) {
        binding.recyclerView.adapter = VanAdapter(vanList,this)
        val swipeDeleteHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = binding.recyclerView.adapter as VanAdapter
                val van = vanList[viewHolder.adapterPosition]
                adapter.removeAt(viewHolder.adapterPosition)
                vanListViewModel.delete(van)
                view?.let { Snackbar.make(it, "Van deleted!", Snackbar.LENGTH_SHORT).show() }
            }
        }

        val swipeEditHandler = object : SwipeToEditCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = binding.recyclerView.adapter as VanAdapter
                val van = vanList[viewHolder.adapterPosition]
                adapter.removeAt(viewHolder.adapterPosition)
                editVan(van)
                view?.let { Snackbar.make(it, "Editing Van: ${van.title}", Snackbar.LENGTH_SHORT).show() }
            }
        }

        val itemTouchHelper1 = ItemTouchHelper(swipeDeleteHandler)
        val itemTouchHelper2 = ItemTouchHelper(swipeEditHandler)
        itemTouchHelper1.attachToRecyclerView(binding.recyclerView)
        itemTouchHelper2.attachToRecyclerView(binding.recyclerView)

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

    fun editVan(van: VanModel) {
        val action = VanListFragmentDirections.actionHomeFragmentToVanEditFragment(van.id)
        findNavController().navigate(action)
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

    override fun onResume() {
        super.onResume()
        loggedInViewModel.liveFirebaseUser.observe(viewLifecycleOwner, Observer { firebaseUser ->
            if (firebaseUser != null) {
                vanListViewModel.user.value = firebaseUser
                vanListViewModel.load()
            }
        })
    }
}