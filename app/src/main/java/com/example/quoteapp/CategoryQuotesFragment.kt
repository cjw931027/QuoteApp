package com.example.quoteapp

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import androidx.navigation.fragment.findNavController

class CategoryQuotesFragment : Fragment(R.layout.fragment_category_quotes) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = view.findViewById<MaterialToolbar>(R.id.toolbar_quotes)
        val recycler = view.findViewById<RecyclerView>(R.id.recycler_quotes)

        val title = arguments?.getString("category_title") ?: "分類內容"

        // 設定 Toolbar 顯示分類名稱
        toolbar.title = title

        // 左上返回按鈕
        toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        // 篩選該分類名言
        val filteredQuotes = DataManager.quotes.filter { it.category == title }

        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = QuoteAdapter(filteredQuotes)
    }
}
