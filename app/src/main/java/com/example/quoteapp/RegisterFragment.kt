package com.example.quoteapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import androidx.activity.result.contract.ActivityResultContracts
import java.io.File
import java.io.FileOutputStream

class RegisterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    private var selectedAvatarResId: Int = R.drawable.ic_avatar_1 // Default
    private var selectedAvatarUri: String? = null // For Gallery Selection

    // Gallery Launcher
    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            try {
                // Copy to internal storage
                val context = requireContext()
                val inputStream = context.contentResolver.openInputStream(uri)
                val avatarDir = File(context.filesDir, "avatars")
                if (!avatarDir.exists()) avatarDir.mkdirs()
                
                val fileName = "avatar_${System.currentTimeMillis()}.jpg"
                val file = File(avatarDir, fileName)
                val outputStream = FileOutputStream(file)
                
                inputStream?.copyTo(outputStream)
                inputStream?.close()
                outputStream.close()
                
                selectedAvatarUri = file.absolutePath
                selectedAvatarResId = 0 // Reset preset selection
                
                // Update UI to show "Selected"
                view?.findViewById<Button>(R.id.btn_select_gallery)?.text = "已選擇圖片!"
                
                // Clear preset highlights
                val avatars = listOf(
                    view?.findViewById<android.widget.ImageView>(R.id.iv_avatar_1),
                    view?.findViewById<android.widget.ImageView>(R.id.iv_avatar_2),
                    view?.findViewById<android.widget.ImageView>(R.id.iv_avatar_3),
                    view?.findViewById<android.widget.ImageView>(R.id.iv_avatar_4)
                )
                avatars.forEach { 
                    it?.alpha = 0.5f 
                    it?.background = null
                }
                
                Toast.makeText(context, "已選擇自訂頭貼", Toast.LENGTH_SHORT).show()
                
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "載入圖片失敗", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etUsername = view.findViewById<TextInputEditText>(R.id.et_register_username)
        val etEmail = view.findViewById<TextInputEditText>(R.id.et_register_email)
        val etPassword = view.findViewById<TextInputEditText>(R.id.et_register_password)
        val btnRegister = view.findViewById<Button>(R.id.btn_register)
        val tvGoToLogin = view.findViewById<TextView>(R.id.tv_go_to_login)
        val btnSelectGallery = view.findViewById<Button>(R.id.btn_select_gallery) // New Button

        // Avatar Selection Logic
        val avatars = listOf(
            view.findViewById<android.widget.ImageView>(R.id.iv_avatar_1) to R.drawable.ic_avatar_1,
            view.findViewById<android.widget.ImageView>(R.id.iv_avatar_2) to R.drawable.ic_avatar_2,
            view.findViewById<android.widget.ImageView>(R.id.iv_avatar_3) to R.drawable.ic_avatar_3,
            view.findViewById<android.widget.ImageView>(R.id.iv_avatar_4) to R.drawable.ic_avatar_4
        )

        fun highlightAvatar(selectedResId: Int) {
            avatars.forEach { (imageView, resId) ->
                if (resId == selectedResId) {
                    imageView.alpha = 1.0f
                    imageView.setBackgroundResource(R.drawable.circle_background) 
                    selectedAvatarUri = null // Clear gallery selection
                    btnSelectGallery.text = "從相簿選擇" // Reset button text
                } else {
                    imageView.alpha = 0.5f
                    imageView.background = null
                }
            }
        }

        // Initialize default
        highlightAvatar(selectedAvatarResId)

        avatars.forEach { (imageView, resId) ->
            imageView.setOnClickListener {
                selectedAvatarResId = resId
                highlightAvatar(resId)
            }
        }
        
        // Gallery Button Click
        btnSelectGallery.setOnClickListener {
            galleryLauncher.launch("image/*")
        }

        btnRegister.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(context, "請填寫所有欄位", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Pass both. DataManager logic should prioritize URI if present, or handle both.
            // My User entity has both fields.
            val success = DataManager.register(username, email, password, selectedAvatarResId, selectedAvatarUri)
            if (success) {
                Toast.makeText(context, "註冊成功", Toast.LENGTH_SHORT).show()
                // Navigate to home, clearing mostly everything
                // We rely on the nav graph action to handle pop behavior
                findNavController().navigate(R.id.action_registerFragment_to_homeFragment)
            } else {
                Toast.makeText(context, "Email 已存在", Toast.LENGTH_SHORT).show()
            }
        }

        tvGoToLogin.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}
