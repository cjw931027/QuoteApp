package com.example.quoteapp

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CategoryAdapter(
    private val categories: List<Category>,
    private val onItemClick: (Category) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img: ImageView = itemView.findViewById(R.id.img_category)
        val title: TextView = itemView.findViewById(R.id.text_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = categories.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = categories[position]

        // --- 修改顯示邏輯 ---
        if (item.imageUri != null) {
            // 如果有自訂圖片 URI，就解析並顯示
            holder.img.setImageURI(Uri.parse(item.imageUri))
            // 為了避免圖片變形，通常建議設為 CenterCrop
            holder.img.scaleType = ImageView.ScaleType.CENTER_CROP
        } else {
            // 否則顯示內建資源圖示
            holder.img.setImageResource(item.imageRes)
            holder.img.scaleType = ImageView.ScaleType.CENTER_CROP
        }
        // ------------------

        holder.title.text = item.name

        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }
}