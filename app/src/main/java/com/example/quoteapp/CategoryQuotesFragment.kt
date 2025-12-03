package com.example.quoteapp

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import androidx.navigation.fragment.findNavController

class   CategoryQuotesFragment : Fragment(R.layout.fragment_category_quotes) {

    private lateinit var recycler: RecyclerView
    private lateinit var adapter: QuoteAdapter
    private lateinit var title: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = view.findViewById<MaterialToolbar>(R.id.toolbar_category) // 原本是 toolbar_quotes
        recycler = view.findViewById(R.id.recycler_category) // 原本是 recycler_quotes

        title = arguments?.getString("category_title") ?: "分類內容"

        // 設定 Toolbar 顯示分類名稱
        toolbar.title = title

        // 左上返回按鈕
        toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        setupRecyclerView()
    }

    // 當回到此頁面時 (例如從設定頁回來)，重新整理列表以套用新的字體大小
    override fun onResume() {
        super.onResume()
        if (::adapter.isInitialized) {
            adapter.notifyDataSetChanged()
        }
    }

    private fun setupRecyclerView() {
        // 篩選該分類名言
        val filteredQuotes = DataManager.quotes.filter { it.category == title }

        adapter = QuoteAdapter(filteredQuotes)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter
    }
}