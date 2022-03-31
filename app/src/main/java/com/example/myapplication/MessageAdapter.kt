package com.example.myapplication

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.oAuthCredential
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.item_message.view.*

class MessageAdapter(val mensajes:ArrayList<Message>):RecyclerView.Adapter<MessageAdapter.MessageHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageHolder {
        val layoutinflater = LayoutInflater.from(parent.context)
        return MessageHolder(layoutinflater.inflate(R.layout.item_message, parent, false))
    }

    override fun onBindViewHolder(holder: MessageHolder, position: Int) {
        holder.render(mensajes[position])
    }

    override fun getItemCount(): Int {
        return mensajes.size
    }

    class MessageHolder(val view: View):RecyclerView.ViewHolder(view){

    fun render(mensaje:Message)
    {
        var auth: FirebaseAuth
        auth = Firebase.auth

        view.emailTextView.text = mensaje.emisor
        view.MensajeTextView.text = mensaje.mensaje
        view.fechaTextView.text = mensaje.fechaMostrar

        if(mensaje.emisor != auth.currentUser?.email)
        {
            val colorDrawable = ColorDrawable(Color.parseColor("#C4761F"))
            view.backgroundMsg.background = colorDrawable
        }
    }

    }
}