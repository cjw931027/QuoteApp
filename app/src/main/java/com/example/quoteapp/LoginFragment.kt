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
import com.example.quoteapp.DataManager
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.CustomCredential
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.util.Log
import android.app.AlertDialog
import android.widget.LinearLayout
import com.google.android.material.textfield.TextInputEditText

class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Check if already logged in
        if (DataManager.isLoggedIn) {
            navigateToHome()
            return
        }

        val etEmail = view.findViewById<TextInputEditText>(R.id.et_login_email)
        val etPassword = view.findViewById<TextInputEditText>(R.id.et_login_password)
        val btnLogin = view.findViewById<Button>(R.id.btn_login)
        val tvGoToRegister = view.findViewById<TextView>(R.id.tv_go_to_register)

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(context, "請填寫所有欄位", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val user = DataManager.login(email, password)
            if (user != null) {
                navigateToHome()
            } else {
                Toast.makeText(context, "Email 或密碼錯誤", Toast.LENGTH_SHORT).show()
            }
        }



        tvGoToRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun navigateToHome() {
        // Navigate to Home and clear back stack so back button exits app
        try {
           findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
        } catch (e: Exception) {
            // Already navigated or issue
        }
    }

}
