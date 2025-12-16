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

class RegisterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etUsername = view.findViewById<TextInputEditText>(R.id.et_register_username)
        val etEmail = view.findViewById<TextInputEditText>(R.id.et_register_email)
        val etPassword = view.findViewById<TextInputEditText>(R.id.et_register_password)
        val btnRegister = view.findViewById<Button>(R.id.btn_register)
        val tvGoToLogin = view.findViewById<TextView>(R.id.tv_go_to_login)

        btnRegister.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val success = DataManager.register(username, email, password)
            if (success) {
                Toast.makeText(context, "Registration successful", Toast.LENGTH_SHORT).show()
                // Navigate to home, clearing mostly everything
                // We rely on the nav graph action to handle pop behavior
                findNavController().navigate(R.id.action_registerFragment_to_homeFragment)
            } else {
                Toast.makeText(context, "Email already exists", Toast.LENGTH_SHORT).show()
            }
        }

        tvGoToLogin.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}
