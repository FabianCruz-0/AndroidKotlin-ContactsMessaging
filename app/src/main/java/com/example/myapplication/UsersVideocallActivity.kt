package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_users.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class UsersVideocallActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()
    var usuarios = ArrayList<User>()
    var nombre = ""
    var email = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)


        title = "Usuarios para videollamada"
        auth = Firebase.auth

        db.collection("usuarios").get().addOnSuccessListener { result ->
            for (user in result) {
                nombre = user.get("Nombre").toString()
                email = user.get("Email").toString()

                if (!email.equals(auth.currentUser?.email) && !email.equals("void")) {
                    var user = User(nombre, email)
                    usuarios.add(user)
                }
            }
            initRecycler()
        }
    }
    fun initRecycler(){
        usersRV.layoutManager = LinearLayoutManager(this)
        val adapter = UserAdapter(usuarios)
        usersRV.adapter = adapter
        adapter.setOnItemClickListener(object:UserAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                // -- DONE -- AQUI DEBE REDIRIGIR A LA VENTANA DE 'LLAMANDO, ESPERANDO RESPUESTA'.
                // TAMBIÉN DEBE LLAMAR AL USUARIO DESTINO A LA VENTANA 'LLAMADA ENTRANTE'

                var id = usuarios[position].email
                var room = ""+System.currentTimeMillis()+auth.currentUser?.email.toString()

                db.collection("videollamadas").document(id).set(
                    hashMapOf(
                        "EmisorEmail" to auth.currentUser?.email.toString(),
                        "LlamandoA" to usuarios[position].email,
                        "Fecha" to System.currentTimeMillis(),
                        "Room" to room,
                        "Id" to db.collection("videollamadas").document().id
                    )
                )

                db.collection("rooms").document(room).set(
                    hashMapOf(
                        "EmisorEmail" to auth.currentUser?.email.toString(),
                        "LlamandoA" to usuarios[position].email,
                        "Respuesta" to "",
                        "Fecha" to System.currentTimeMillis(),
                        "Room" to room,
                        "Id" to db.collection("videollamadas").document().id
                    )
                )

                val intent = Intent(this@UsersVideocallActivity,CallingActivity::class.java).apply{
                    putExtra("nombre",usuarios[position].nombre)
                    putExtra("email",usuarios[position].email)
                    putExtra("room",room)
                }
                startActivity(intent)
            }
        })
    }
}