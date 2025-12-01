package com.example.quoteapp

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
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
        // 每次回到頁面都刷新資料，因為可能在其他地方取消收藏了
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

            // 這裡傳入一個 callback，當使用者在收藏頁點擊愛心(取消收藏)時，
            // 列表需要馬上刷新以移除該項目
            adapter = QuoteAdapter(favorites) {
                // 當有項目被點擊切換時，重新檢查列表狀態
                setupRecyclerView()
            }
            recycler.adapter = adapter
        }
    }
}