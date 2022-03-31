package com.example.myapplication

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.widget.ArrayAdapter
import androidx.appcompat.app.ActionBar
import kotlinx.android.synthetic.main.activity_settings2.*

class SettingsActivity : AppCompatActivity() {

    lateinit var myPreference: MyPreference

    val languageList = arrayOf("es", "de", "en", "fr")

    override fun onCreate(savedInstanceState: Bundle?) {

        //Cambia el color de la barra del título y el texto del título
        val actionBar: ActionBar?
        actionBar = supportActionBar
        val colorDrawable = ColorDrawable(Color.parseColor("#59656f"))
        actionBar?.setBackgroundDrawable(colorDrawable)
        actionBar?.setTitle(Html.fromHtml("<font color='#ffffff'>Cambiar Idioma</font>"));

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings2)

        myPreference = MyPreference(this)

        spinner.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, languageList)

        val lang = myPreference.getLoginCount()
        val index = languageList.indexOf(lang)

        button.setOnClickListener{
            myPreference.setLoginCount(languageList[spinner.selectedItemPosition])
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }

    }
}