package com.example.quoteapp

import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import androidx.navigation.fragment.findNavController

class CategoryFragment : Fragment(R.layout.fragment_category) {

    private lateinit var recycler: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 尋找 Toolbar
        val toolbar = view.findViewById<MaterialToolbar>(R.id.toolbar_category)

        // 設定 Toolbar 菜單按鈕事件
        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_add -> {
                    showAddMenu(toolbar)
                    true
                }
                else -> false
            }
        }

        recycler = view.findViewById(R.id.recycler_category)

        val categoryList = listOf(
            Category("友情", R.drawable.cat_friendship),
            Category("人生", R.drawable.cat_life),
            Category("愛情", R.drawable.cat_love),
            Category("勵志", R.drawable.cat_motivation),
            Category("工作", R.drawable.cat_work),
            Category("其他", R.drawable.cat_others)
        )

        val adapter = CategoryAdapter(categoryList) { category ->
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

    // 彈出右上角選單
    private fun showAddMenu(anchor: View) {
        val popup = PopupMenu(requireContext(), anchor)
        popup.menu.add("新增名言")
        popup.menu.add("新增分類")

        popup.setOnMenuItemClickListener { item ->
            when (item.title) {
                "新增名言" -> {
                    // TODO: 開啟新增名言頁面
                }

                "新增分類" -> {
                    // TODO: 開啟新增分類頁面
                }
            }
            true
        }

        popup.show()
    }
}
