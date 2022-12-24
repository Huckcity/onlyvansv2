package com.adamgibbons.onlyvansv2.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.adamgibbons.onlyvansv2.databinding.CardImageBinding
import com.squareup.picasso.Picasso


class ImagesAdapter constructor(private var images: List<String>) : RecyclerView.Adapter<ImagesAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val image = images[holder.adapterPosition]
        holder.bind(image)
    }

    class MainHolder(private val binding : CardImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(image: String) {
            Picasso.get().load(image.toUri())
//                .resize(200, 200)
//                .transform(customTransformation())
//                .centerCrop()
                .into(binding.vanThumbnail)
            binding.executePendingBindings()
        }
    }

    override fun getItemCount(): Int = images.size

}