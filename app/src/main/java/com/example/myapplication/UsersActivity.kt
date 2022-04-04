package com.example.myapplication

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Html
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.activity_users.*
import kotlinx.android.synthetic.main.item_user.*
import java.util.jar.Manifest

class UsersActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()
    var usuarios = ArrayList<User>()

    var cols = listOf<String>(
        ContactsContract.CommonDataKinds.Phone.NUMBER,
        ContactsContract.CommonDataKinds.Phone._ID
        ).toTypedArray()

    var contactos = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)

        //Cambia el color de la barra del título y el texto del título
        val actionBar: ActionBar?
        actionBar = supportActionBar
        val colorDrawable = ColorDrawable(Color.parseColor("#59656f"))
        actionBar?.setBackgroundDrawable(colorDrawable)
        actionBar?.setTitle(Html.fromHtml("<font color='#ffffff'>Chats</font>"));


        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,Array(1){android.Manifest.permission.READ_CONTACTS},111)
        } else {
            readContacts()
        }


        auth = Firebase.auth

        db.collection("usuarios").get().addOnSuccessListener { result ->
            for (user in result)
            {
                var nombre = user.get("Nombre").toString()
                var email = user.get("Email").toString()
                var tel = user.get("Numero").toString()

                if(!email.equals(auth.currentUser?.email) && !email.equals("void"))
                {
                    var telConEspacios = tel.substring(0,2)+" "+tel.substring(2,6)+" "+tel.substring(6,10)
                    var telConLada0 = "+52"+tel
                    var telConLada1 = "+521"+tel
                    var telConLadaYEspacios0 = "+52 "+telConEspacios
                    var telConLadaYEspacios1 = "+52 1 "+telConEspacios
                    var telConGuiones0 = tel.substring(0,3)+"-"+tel.substring(3,6)+"-"+tel.substring(6,10)
                    var telConGuiones1 = tel.substring(0,2)+"-"+tel.substring(2,6)+"-"+tel.substring(6,10)


                    if(contactos.contains(tel)|| contactos.contains(telConLada0) || contactos.contains(telConLada1)
                        || contactos.contains(telConEspacios) || contactos.contains(telConLadaYEspacios0)
                        || contactos.contains(telConLadaYEspacios1) || contactos.contains(telConGuiones0)
                        || contactos.contains(telConGuiones1))
                    {
                        var user = User(nombre, email)
                        usuarios.add(user)
                    }
                }
            }
            initRecycler()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==111 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            readContacts()
        }
    }

    private fun readContacts() {
        var rs = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                        cols,
                                        null,
                                        null,
                                        ContactsContract.CommonDataKinds.Phone.NUMBER
            )
        while(rs?.moveToNext()!!)
        {
            contactos.add(rs.getString(0))
        }
    }

    fun initRecycler(){
        usersRV.layoutManager = LinearLayoutManager(this)
        val adapter = UserAdapter(usuarios)
        usersRV.adapter = adapter
        adapter.setOnItemClickListener(object:UserAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                val intent = Intent(this@UsersActivity,ChatActivity::class.java).apply{
                    putExtra("email",usuarios[position].email)
                }
                startActivity(intent)
            }
        })
    }
}