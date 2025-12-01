package com.example.quoteapp

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
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

    private var selectedIconRes = R.drawable.cat_others // 預設選中 ID
    private var selectedImageUri: Uri? = null // 選中的自訂圖片 URI

    private lateinit var imgPreview: ImageView

    // --- 這裡是你原本缺少的部分：設定相簿選取器 ---
    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            // 嘗試取得權限，避免 App 重啟後無法讀取圖片
            try {
                requireContext().contentResolver.takePersistableUriPermission(
                    it,
                    android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }

            // 更新選擇狀態：有 URI 就清空 iconRes (或不處理，依賴 save 邏輯判斷)
            selectedImageUri = it

            // 更新預覽圖
            imgPreview.setImageURI(it)
            imgPreview.scaleType = ImageView.ScaleType.CENTER_CROP
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = view.findViewById<MaterialToolbar>(R.id.toolbar_add_category)
        val editName = view.findViewById<EditText>(R.id.edit_category_name)
        imgPreview = view.findViewById(R.id.img_preview)
        val btnSave = view.findViewById<Button>(R.id.btn_save_category)
        // 綁定 XML 中的按鈕
        val btnPickGallery = view.findViewById<Button>(R.id.btn_pick_gallery)
        val recyclerIcons = view.findViewById<RecyclerView>(R.id.recycler_icons)

        // 設定返回按鈕
        toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        // --- 這裡是你原本缺少的部分：設定按鈕點擊事件 ---
        btnPickGallery.setOnClickListener {
            // 開啟系統相簿，限制選擇 image 類型
            pickImageLauncher.launch("image/*")
        }

        // 設定圖示選擇器 Adapter (內建圖示)
        recyclerIcons.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerIcons.adapter = IconAdapter(availableIcons) { iconRes ->
            // 當使用者點選內建圖示時，清除自訂圖片選擇
            selectedIconRes = iconRes
            selectedImageUri = null

            imgPreview.setImageResource(iconRes)
            imgPreview.scaleType = ImageView.ScaleType.CENTER_INSIDE // 內建圖示保持完整
        }

        btnSave.setOnClickListener {
            val name = editName.text.toString().trim()

            if (name.isEmpty()) {
                Toast.makeText(requireContext(), "請輸入種類名稱", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (DataManager.categories.any { it.name == name }) {
                Toast.makeText(requireContext(), "此種類已存在", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // --- 這裡也需要更新：判斷是否有 imageUri ---
            val uriString = selectedImageUri?.toString()

            // 呼叫 DataManager 新增 (如果 uriString 不為 null，就會優先顯示圖片)
            DataManager.addCategory(name, selectedIconRes, uriString)

            Toast.makeText(requireContext(), "新增成功！", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }
    }

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
                setBackgroundResource(R.drawable.bg_input_area)
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