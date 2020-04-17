package com.harshithd.firebasestorage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_row.view.*

class ImageAdapter(private val images: List<UploadedImage>) :
    RecyclerView.Adapter<ImageAdapter.ImageHolder>() {

    inner class ImageHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row, parent, false)
        return ImageHolder(view)
    }

    override fun getItemCount() = images.size

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        val currentImage = images[position]
        holder.itemView.rvImageName.text = currentImage.name
        Picasso.get().load(currentImage.url).into(holder.itemView.rvImage)
    }
}