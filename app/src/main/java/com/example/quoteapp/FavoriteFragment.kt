package com.example.quoteapp

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FavoriteFragment : Fragment(R.layout.fragment_favorite) {

    private lateinit var recycler: RecyclerView
    private lateinit var emptyStateLayout: LinearLayout
    private lateinit var adapter: QuoteAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler = view.findViewById(R.id.recycler_favorites)
        emptyStateLayout = view.findViewById(R.id.layout_empty_state)

        recycler.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onResume() {
        super.onResume()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val favorites = DataManager.favorites

        if (favorites.isEmpty()) {
            recycler.visibility = View.GONE
            emptyStateLayout.visibility = View.VISIBLE
        } else {
            recycler.visibility = View.VISIBLE
            emptyStateLayout.visibility = View.GONE

            adapter = QuoteAdapter(
                favorites,
                onFavoriteClick = {
                    // 取消收藏後要馬上刷新列表
                    setupRecyclerView()
                },
                // [新增] 長按刪除
                onItemLongClick = { quote ->
                    showDeleteConfirmDialog(quote)
                }
            )
            recycler.adapter = adapter
        }
    }

    private fun showDeleteConfirmDialog(quote: Quote) {
        AlertDialog.Builder(requireContext())
            .setTitle("刪除名言")
            .setMessage("確定要永久刪除這則名言嗎？\n(這會將它從資料庫移除，不只是取消收藏)")
            .setPositiveButton("刪除") { _, _ ->
                DataManager.deleteQuote(quote)
                Toast.makeText(requireContext(), "已刪除名言", Toast.LENGTH_SHORT).show()
                setupRecyclerView()
            }
            .setNegativeButton("取消", null)
            .show()
    }
}