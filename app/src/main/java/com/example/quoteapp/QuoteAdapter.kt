package com.example.quoteapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class QuoteAdapter(
    private val quotes: List<Quote>,
    // 增加一個 callback，讓 Fragment 知道資料變動了 (主要用於收藏頁移除項目時)
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

        // 檢查是否已收藏
        val isFavorite = DataManager.favorites.contains(quote)
        updateFavoriteIcon(holder.btnFavorite, isFavorite)

        // 點擊愛心
        holder.btnFavorite.setOnClickListener {
            // 切換收藏狀態
            DataManager.toggleFavorite(quote)

            // 更新圖示
            val newIsFavorite = DataManager.favorites.contains(quote)
            updateFavoriteIcon(holder.btnFavorite, newIsFavorite)

            // 如果有設定 callback (例如在收藏頁面)，就通知外部刷新
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