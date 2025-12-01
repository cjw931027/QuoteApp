package com.example.quoteapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class QuoteAdapter(private val quotes: List<Quote>) :
    RecyclerView.Adapter<QuoteAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textQuote: TextView = itemView.findViewById(R.id.text_quote)
        val textAuthor: TextView = itemView.findViewById(R.id.text_author)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_quote, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = quotes.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val quote = quotes[position]
        holder.textQuote.text = quote.text
        holder.textAuthor.text = "— ${quote.author}"
    }
}
