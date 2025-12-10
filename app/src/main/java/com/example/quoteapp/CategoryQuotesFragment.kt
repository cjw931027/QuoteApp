package com.example.quoteapp

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import androidx.navigation.fragment.findNavController

class CategoryQuotesFragment : Fragment(R.layout.fragment_category_quotes) {

    private lateinit var recycler: RecyclerView
    private lateinit var adapter: QuoteAdapter
    private lateinit var title: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = view.findViewById<MaterialToolbar>(R.id.toolbar_category)
        recycler = view.findViewById(R.id.recycler_category)

        title = arguments?.getString("category_title") ?: "分類內容"

        toolbar.title = title

        toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        // [修改 3] 設定 Toolbar 選單點擊事件
        // 這樣在分類內點擊「+」號才有反應
        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_add -> {
                    // 點擊新增，導航至 AddQuoteFragment，並傳遞當前分類名稱
                    val bundle = Bundle().apply {
                        putString("preselected_category", title)
                    }
                    findNavController().navigate(R.id.addQuoteFragment, bundle)
                    true
                }
                else -> false
            }
        }

        setupRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val filteredQuotes = DataManager.quotes.filter { it.category == title }

        adapter = QuoteAdapter(
            filteredQuotes,
            onFavoriteClick = { /* 這裡不需要做特別的事，點愛心會自己變色 */ },
            onItemLongClick = { quote ->
                showDeleteConfirmDialog(quote)
            }
        )
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter
    }

    private fun showDeleteConfirmDialog(quote: Quote) {
        AlertDialog.Builder(requireContext())
            .setTitle("刪除名言")
            .setMessage("確定要刪除這則名言嗎？")
            .setPositiveButton("刪除") { _, _ ->
                DataManager.deleteQuote(quote)
                Toast.makeText(requireContext(), "已刪除名言", Toast.LENGTH_SHORT).show()
                setupRecyclerView() // 刷新列表
            }
            .setNegativeButton("取消", null)
            .show()
    }
}