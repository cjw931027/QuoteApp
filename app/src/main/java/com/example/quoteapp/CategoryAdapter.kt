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
    private val onItemClick: (Category) -> Unit,
    // [新增] 長按事件的回呼函式
    private val onItemLongClick: (Category) -> Unit
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

        if (item.imageUri != null) {
            holder.img.setImageURI(Uri.parse(item.imageUri))
            holder.img.scaleType = ImageView.ScaleType.CENTER_CROP
        } else {
            holder.img.setImageResource(item.imageRes)
            holder.img.scaleType = ImageView.ScaleType.CENTER_CROP
        }

        holder.title.text = item.name

        // 點擊：進入分類
        holder.itemView.setOnClickListener {
            onItemClick(item)
        }

        // [新增] 長按：刪除
        holder.itemView.setOnLongClickListener {
            onItemLongClick(item)
            true // 回傳 true 表示事件已被處理，不會觸發普通點擊
        }
    }
}