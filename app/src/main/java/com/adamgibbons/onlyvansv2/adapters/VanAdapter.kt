package com.adamgibbons.onlyvansv2.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.adamgibbons.onlyvansv2.databinding.CardVanBinding
import com.adamgibbons.onlyvansv2.helpers.customTransformation
import com.adamgibbons.onlyvansv2.models.VanModel
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso


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

    class MainHolder(private val binding : CardVanBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(van: VanModel, listener: VanListener) {
            binding.vanTitle.text = van.title
            binding.vanDescription.text = van.description
            Picasso.get().load(van.imageUri.toUri())
                .resize(200, 200)
                .transform(customTransformation())
                .centerCrop()
                .into(binding.vanThumbnail)
            binding.root.setOnClickListener { listener.onVanClick(van) }
            binding.executePendingBindings()
        }
    }

}