package com.example.quoteapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar

class AddCategoryFragment : Fragment(R.layout.fragment_add_category) {

    // 預設可選的圖示資源
    private val availableIcons = listOf(
        R.drawable.cat_others,
        R.drawable.cat_motivation,
        R.drawable.cat_life,
        R.drawable.cat_love,
        R.drawable.cat_work,
        R.drawable.cat_friendship
    )

    private var selectedIconRes = R.drawable.cat_others // 預設選中

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = view.findViewById<MaterialToolbar>(R.id.toolbar_add_category)
        val editName = view.findViewById<EditText>(R.id.edit_category_name)
        val imgPreview = view.findViewById<ImageView>(R.id.img_preview)
        val btnSave = view.findViewById<Button>(R.id.btn_save_category)
        val recyclerIcons = view.findViewById<RecyclerView>(R.id.recycler_icons)

        // 設定返回按鈕
        toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        // 設定圖示選擇器 Adapter
        recyclerIcons.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerIcons.adapter = IconAdapter(availableIcons) { iconRes ->
            selectedIconRes = iconRes
            imgPreview.setImageResource(iconRes)
        }

        btnSave.setOnClickListener {
            val name = editName.text.toString().trim()

            if (name.isEmpty()) {
                Toast.makeText(requireContext(), "請輸入種類名稱", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 檢查是否重複
            if (DataManager.categories.any { it.name == name }) {
                Toast.makeText(requireContext(), "此種類已存在", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 新增分類
            DataManager.categories.add(Category(name, selectedIconRes))

            Toast.makeText(requireContext(), "新增成功！", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }
    }

    // 內部類別：圖示選擇列表的 Adapter
    inner class IconAdapter(
        private val icons: List<Int>,
        private val onIconClick: (Int) -> Unit
    ) : RecyclerView.Adapter<IconAdapter.IconViewHolder>() {

        inner class IconViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val img: ImageView = itemView as ImageView
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconViewHolder {
            val imageView = ImageView(parent.context).apply {
                layoutParams = ViewGroup.MarginLayoutParams(120, 120).apply {
                    setMargins(16, 0, 16, 0)
                }
                setPadding(20, 20, 20, 20)
                setBackgroundResource(R.drawable.bg_input_area) // 使用相同的背景
            }
            return IconViewHolder(imageView)
        }

        override fun getItemCount(): Int = icons.size

        override fun onBindViewHolder(holder: IconViewHolder, position: Int) {
            holder.img.setImageResource(icons[position])
            holder.itemView.setOnClickListener {
                onIconClick(icons[position])
            }
        }
    }
}