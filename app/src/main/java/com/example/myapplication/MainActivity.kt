package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.databinding.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title = "Pantalla Principal"

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        val email= auth.currentUser?.email
        if (email != null) {
             db.collection("usuarios").document(email).get().addOnSuccessListener { document ->
                 var result = document.data

                 binding.NameTextView.text = result?.get("Nombre").toString()

                 if(result?.get("Cuidador").toString() =="true")
                 {binding.NivelTextView.text = "Cuidador"} else
                 {binding.NivelTextView.text = "Persona a cuidar"}
        }
        }

        binding.signOut.setOnClickListener {
            signOut()
        }
        binding.chatButton.setOnClickListener{
            val intent = Intent(this, UsersActivity::class.java)
            startActivity(intent)
        }
        binding.ingresarbtn.setOnClickListener {
            val intent = Intent(this, MeetAct::class.java)
            startActivity(intent)
        }
    }

    private fun signOut() {
        Firebase.auth.signOut()
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
    }
}