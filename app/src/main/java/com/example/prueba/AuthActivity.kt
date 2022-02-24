package com.example.prueba

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.signOut

class AuthActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    setup()
    }
    private fun setup(){
        title="Autentificación"
        LoginButton.setOnClickListener {
            if(EmailEditText.text.isNotEmpty() && passwordEditText.text.isNotEmpty()) {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(EmailEditText.text.toString(), passwordEditText.text.toString()).addOnCompleteListener {
                if(it.isSuccessful){
                showHome(it.result?.user?.email ?:"", ProviderType.BASIC)
                } else {
                showAlert()
                }
            }

        }
        }
        //
        signOut.setOnClickListener {
            if(EmailEditText.text.isNotEmpty() && passwordEditText.text.isNotEmpty()) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(EmailEditText.text.toString(), passwordEditText.text.toString()).addOnCompleteListener {
                    if(it.isSuccessful){
                        showHome(it.result?.user?.email ?:"", ProviderType.BASIC)
                    } else {
                        showAlert()
                    }
                }

            }
        }




    }
private fun showAlert() {
    val builder=AlertDialog.Builder(this)
    builder.setTitle("Error")
    builder.setMessage("Se ha producido un error al autentificar")
    builder.setPositiveButton("Aceptar" , null)
    val dialog: AlertDialog= builder.create()
    dialog.show()
}
    private fun showHome(email: String, provider: ProviderType){
    val homeIntent= Intent(this, HomeActivity::class.java).apply {
    putExtra("email", email)
    putExtra("provider",provider.name)


    }
        startActivity(homeIntent)
    }

}