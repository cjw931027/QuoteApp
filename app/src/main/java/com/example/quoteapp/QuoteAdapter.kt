package com.example.quoteapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class QuoteAdapter(
    private val quotes: List<Quote>,
    private val onFavoriteClick: (() -> Unit)? = null
) : RecyclerView.Adapter<QuoteAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textCategory: TextView = itemView.findViewById(R.id.text_category_label)
        val textQuote: TextView = itemView.findViewById(R.id.text_quote)
        val textAuthor: TextView = itemView.findViewById(R.id.text_author)
        val btnFavorite: ImageView = itemView.findViewById(R.id.btn_favorite)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_quote, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = quotes.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val quote = quotes[position]

        holder.textCategory.text = quote.category
        holder.textQuote.text = quote.text
        holder.textAuthor.text = quote.author
        holder.textQuote.textSize = DataManager.fontSize

        // 直接讀取 quote 物件的屬性
        updateFavoriteIcon(holder.btnFavorite, quote.isFavorite)

        holder.btnFavorite.setOnClickListener {
            // 切換收藏狀態 (DataManager 會負責更新資料庫)
            DataManager.toggleFavorite(quote)

            // 更新 UI
            updateFavoriteIcon(holder.btnFavorite,  quote.isFavorite)

            // 通知外部 (例如收藏頁面需要刷新)
            onFavoriteClick?.invoke()
        }
    }

    private fun updateFavoriteIcon(imageView: ImageView, isFavorite: Boolean) {
        if (isFavorite) {
            imageView.setImageResource(R.drawable.ic_heart_filled)
        } else {
            imageView.setImageResource(R.drawable.ic_heart_outline)
        }
    }
}