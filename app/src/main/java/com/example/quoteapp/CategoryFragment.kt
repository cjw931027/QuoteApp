package com.example.quoteapp

import android.os.Bundle
import android.view.View
import android.widget.Toast
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

        val toolbar = view.findViewById<MaterialToolbar>(R.id.toolbar_category)

        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_add -> {
                    showAddDialog()
                    true
                }
                else -> false
            }
        }

        recycler = view.findViewById(R.id.recycler_category)

        setupRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        // 為了確保刪除後列表刷新，我們重新建立 Adapter
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val categoryList = DataManager.categories

        adapter = CategoryAdapter(
            categoryList,
            onItemClick = { category ->
                val bundle = Bundle().apply {
                    putString("category_title", category.name)
                }
                findNavController().navigate(
                    R.id.categoryQuotesFragment,
                    bundle
                )
            },
            // [新增] 處理長按刪除
            onItemLongClick = { category ->
                showDeleteConfirmDialog(category)
            }
        )

        recycler.layoutManager = GridLayoutManager(requireContext(), 2)
        recycler.adapter = adapter
    }

    // [新增] 刪除確認對話框
    private fun showDeleteConfirmDialog(category: Category) {
        AlertDialog.Builder(requireContext())
            .setTitle("刪除分類")
            .setMessage("確定要刪除「${category.name}」嗎？\n\n注意：該分類下的所有名言也會一併被刪除！")
            .setPositiveButton("刪除") { _, _ ->
                DataManager.deleteCategory(category)
                Toast.makeText(requireContext(), "已刪除分類", Toast.LENGTH_SHORT).show()
                setupRecyclerView() // 刷新列表
            }
            .setNegativeButton("取消", null)
            .show()
    }

    private fun showAddDialog() {
        val options = arrayOf("新增名言", "新增種類")

        AlertDialog.Builder(requireContext())
            .setTitle("請選擇新增項目")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> findNavController().navigate(R.id.addQuoteFragment)
                    1 -> findNavController().navigate(R.id.addCategoryFragment)
                }
            }
            .show()
    }
}