package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_message.view.*
import kotlinx.android.synthetic.main.item_user.view.*

class UserAdapter(val usuarios:ArrayList<User>):RecyclerView.Adapter<UserAdapter.UserHolder>(){

    private lateinit var mListener:onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener)
    {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        val layoutinflater = LayoutInflater.from(parent.context)
        return UserHolder(layoutinflater.inflate(R.layout.item_user, parent, false),mListener)
    }

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        holder.render(usuarios[position])
    }

    override fun getItemCount(): Int {
        return usuarios.size
    }

    class UserHolder(val view: View,listener:onItemClickListener): RecyclerView.ViewHolder(view){

        fun render(usuario:User) {
            view.usr_nameTextView.text = usuario.nombre
            view.usr_emailTextView.text = usuario.email
        }

        init { //constructor
            itemView.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
        }
    }
}