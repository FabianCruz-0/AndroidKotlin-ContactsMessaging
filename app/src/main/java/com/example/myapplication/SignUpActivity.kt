package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.RadioButton
import android.widget.Toast
import com.example.myapplication.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.util.regex.Pattern

class SignUpActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title = "Registrarse"

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        binding.signUpButton.setOnClickListener {

            val mEmail = binding.emailEditText.text.toString()
            val mPassword = binding.passwordEditText.text.toString()
            val mRepeatPassword = binding.repeatPasswordEditText.text.toString()
            val name = binding.nameEditText.text.toString()
            var intNivelUser = binding.NivelRadioGroup.checkedRadioButtonId;
            var cuidador : Boolean = false;

            var radio = findViewById<RadioButton>(intNivelUser)

            if(radio.text.toString()=="Cuidador")
            {
                cuidador = true;
            }

            val passwordRegex = Pattern.compile("^" +
                     "(?=.*[-@#$%^&+=])" +  // Al menos 1 caracter especial
                     ".{6,}" +              // Al menos 6 caracteres
                    "$")

            if (mEmail.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()) {
                Toast.makeText(baseContext, "Ingrese un Email valido.",
                    Toast.LENGTH_SHORT).show()

            } else if(mPassword.isEmpty() || !passwordRegex.matcher(mPassword).matches()) {
                Toast.makeText(baseContext, "La contraseña es debil.",
                    Toast.LENGTH_SHORT).show()
            } else if (mPassword != mRepeatPassword) {
                Toast.makeText(baseContext, "Confirma la contraseña.",
                    Toast.LENGTH_SHORT).show()
            } else {
                createAccount(mEmail, mPassword, cuidador, name)
            }

        }

        binding.backImageView.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }


    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
            if(currentUser.isEmailVerified) {
                reload()
            } else {
                val intent = Intent(this, CheckEmailActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun createAccount(email : String, password : String, cuidador : Boolean, name : String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    db.collection("usuarios").document(email).set(hashMapOf(
                        "Id" to this.db.collection("usuarios").document().id,
                        "Nombre" to name,
                      "Email" to email,
                      "Cuidador" to cuidador
                    ))
                    val intent = Intent(this, CheckEmailActivity::class.java)
                    startActivity(intent)
                } else {
                    Log.w("TAG", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun reload() {
        val intent = Intent (this, MainActivity::class.java)
        this.startActivity(intent)
    }
}