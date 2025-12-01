package com.example.quoteapp

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.MaterialToolbar

class AddQuoteFragment : Fragment(R.layout.fragment_add_quote) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = view.findViewById<MaterialToolbar>(R.id.toolbar_add_quote)
        val editQuote = view.findViewById<EditText>(R.id.edit_quote_text)
        val editAuthor = view.findViewById<EditText>(R.id.edit_quote_author)
        val spinnerCategory = view.findViewById<Spinner>(R.id.spinner_category)
        val btnSave = view.findViewById<Button>(R.id.btn_save_quote)

        // 設定返回按鈕
        toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        // 設定分類下拉選單
        val categories = DataManager.categories.map { it.name }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, categories)
        spinnerCategory.adapter = adapter

        btnSave.setOnClickListener {
            val text = editQuote.text.toString().trim()
            val author = editAuthor.text.toString().trim()
            val category = spinnerCategory.selectedItem.toString()

            if (text.isEmpty()) {
                Toast.makeText(requireContext(), "請輸入名言內容", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val finalAuthor = if (author.isEmpty()) "匿名" else author

            // 儲存資料
            DataManager.addQuote(text, finalAuthor, category)

            Toast.makeText(requireContext(), "新增成功！", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }
    }
}