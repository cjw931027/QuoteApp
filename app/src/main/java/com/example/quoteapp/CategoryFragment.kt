package com.example.quoteapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import androidx.navigation.fragment.findNavController

class CategoryFragment : Fragment(R.layout.fragment_category) {

    private lateinit var recycler: RecyclerView
    private lateinit var adapter: CategoryAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 尋找 Toolbar
        val toolbar = view.findViewById<MaterialToolbar>(R.id.toolbar_category)

        // 設定 Toolbar 菜單按鈕事件
        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_add -> {
                    showAddDialog() // 改成呼叫顯示對話框的方法
                    true
                }
                else -> false
            }
        }

        recycler = view.findViewById(R.id.recycler_category)

        setupRecyclerView()
    }

    // 當從新增頁面回來時，刷新列表以顯示新種類
    override fun onResume() {
        super.onResume()
        if (::adapter.isInitialized) {
            adapter.notifyDataSetChanged()
        }
    }

    private fun setupRecyclerView() {
        val categoryList = DataManager.categories

        adapter = CategoryAdapter(categoryList) { category ->
            val bundle = Bundle().apply {
                putString("category_title", category.name)
            }

            findNavController().navigate(
                R.id.categoryQuotesFragment,
                bundle
            )
        }

        recycler.layoutManager = GridLayoutManager(requireContext(), 2)
        recycler.adapter = adapter
    }

    // 顯示「新增」對話框
    private fun showAddDialog() {
        val options = arrayOf("新增名言", "新增種類")

        AlertDialog.Builder(requireContext())
            .setTitle("請選擇新增項目")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> { // 點擊 "新增名言"
                        findNavController().navigate(R.id.addQuoteFragment)
                    }
                    1 -> { // 點擊 "新增種類"
                        findNavController().navigate(R.id.addCategoryFragment)
                    }
                }
            }
            .show()
    }
}