package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_chat.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ChatActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()
    private lateinit var binding: ActivityChatBinding

    var mensajes = ArrayList<Message>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = "Chat"

        auth = Firebase.auth
        val email= auth.currentUser?.email

        db.collection("chat").orderBy("Fecha").addSnapshotListener { result, e ->
            if (result != null) {
                mensajes.clear()
                for (document in result) {
                    var emisor = document.get("Emisor").toString()
                    var fecha = document.get("Fecha").toString()
                    var receptor = document.get("Receptor").toString()
                    var mensaje = document.get("Mensaje").toString()
                    var fechaMostrar = document.get("FechaMostrar").toString()
                    var msj = Message(emisor, receptor, mensaje, fecha, fechaMostrar)
                    mensajes.add(msj)
                }
                initRecycler()
            }
        }

            binding.SendButton.setOnClickListener {

                var mensaje = binding.MensajeEdiText.text.toString()
                val formatter = SimpleDateFormat("dd-MM-yyyy hh:mm a ")
                formatter.timeZone = TimeZone.getTimeZone("GMT-6:00")
                val cal = Calendar.getInstance()
                val timestamp = formatter.format(cal.time)

                db.collection("chat").document().set(
                    hashMapOf(
                        "Fecha" to System.currentTimeMillis(),
                        "Mensaje" to mensaje,
                        "Emisor" to email,
                        "FechaMostrar" to timestamp
                    )
                )
                binding.MensajeEdiText.text.clear()
            }
    }

    fun initRecycler(){
        MensajesRV.layoutManager = LinearLayoutManager(this)
        val adapter = MessageAdapter(mensajes)
        MensajesRV.adapter = adapter
        MensajesRV.scrollToPosition(mensajes.lastIndex);
    }
}