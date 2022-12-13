package com.adamgibbons.onlyvansv2.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adamgibbons.onlyvansv2.databinding.CardVanBinding
import com.adamgibbons.onlyvansv2.helpers.decodeImage
import com.adamgibbons.onlyvansv2.models.VanModel

interface VanListener {
    fun onVanClick(van: VanModel)
}

class VanAdapter constructor(private var vans: List<VanModel>,
                             private val listener: VanListener) : RecyclerView.Adapter<VanAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardVanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val van = vans[holder.adapterPosition]
        holder.bind(van, listener)
    }

    override fun getItemCount(): Int = vans.size

    fun removeAt(position: Int) {
        val result = vans.toMutableList()
        result.removeAt(position)
        notifyItemRemoved(position)
    }

    class MainHolder(val binding : CardVanBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(van: VanModel, listener: VanListener) {
            binding.vanTitle.text = van.title
            binding.vanDescription.text = van.description
            if (van.image64.isNotEmpty()) {
                binding.imageIcon.setImageBitmap(decodeImage(van.image64))
            }
            binding.root.setOnClickListener { listener.onVanClick(van) }
            binding.executePendingBindings()
        }
    }

}