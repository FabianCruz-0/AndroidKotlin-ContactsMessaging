package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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
        view.emailTextView.text = mensaje.emisor
        view.MensajeTextView.text = mensaje.mensaje
        view.fechaTextView.text = mensaje.fechaMostrar
    }

    }
}