package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_settings2.*

class SettingsActivity : AppCompatActivity() {

    lateinit var myPreference: MyPreference

    val languageList = arrayOf("es", "de", "en", "fr")

    override fun onCreate(savedInstanceState: Bundle?) {
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