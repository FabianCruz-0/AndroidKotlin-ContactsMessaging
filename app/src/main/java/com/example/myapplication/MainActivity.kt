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
    var initialSnap = true

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
            val intent = Intent(this, UsersVideocallActivity::class.java)
            startActivity(intent)
        }

        auth.currentUser?.email?.let {
            db.collection("videollamadas").document(it).addSnapshotListener{ doc, e ->
                if (doc != null) {
                    if(initialSnap == true) {
                        initialSnap = false
                    }else {

                        var room = doc.get("Room").toString()
                        var emisor = doc.get("EmisorEmail").toString()

                        val intent = Intent(this@MainActivity, ResCallActivity::class.java).apply {
                            putExtra("emisor",emisor)
                            putExtra("room",room)
                        }
                        startActivity(intent)
                    }
                }
            }
        }
    }

    private fun signOut() {
        Firebase.auth.signOut()
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
    }
}