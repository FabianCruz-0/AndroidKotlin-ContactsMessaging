package com.example.myapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import androidx.appcompat.app.ActionBar
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.facebook.react.modules.core.PermissionListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_calling.*
import org.jitsi.meet.sdk.BroadcastEvent
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetActivityInterface
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import timber.log.Timber

class CallingActivity : AppCompatActivity(), JitsiMeetActivityInterface {
    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            onBroadcastReceived(intent)
        }
    }

    private fun onBroadcastReceived(intent: Intent?) {
        if (intent != null) {
            val event = BroadcastEvent(intent)
            when (event.type) {
                BroadcastEvent.Type.CONFERENCE_JOINED -> Timber.i(
                    "Conference Joined with url%s",
                    event.data["url"]
                )
                BroadcastEvent.Type.PARTICIPANT_JOINED -> Timber.i(
                    "Participant joined%s",
                    event.data["name"]
                )
            }
        }
    }

    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {

        //Cambia el color de la barra del título y el texto del título
        val actionBar: ActionBar?
        actionBar = supportActionBar
        val colorDrawable = ColorDrawable(Color.parseColor("#59656f"))
        actionBar?.setBackgroundDrawable(colorDrawable)
        actionBar?.setTitle(Html.fromHtml("<font color='#ffffff'>Llamando</font>"));

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calling)

        var nameCall:String = intent.getStringExtra("nombre").toString()
        var emailCall:String = intent.getStringExtra("email").toString()
        var room:String = intent.getStringExtra("room").toString()

        callNameTextView.text = nameCall
        callEmailTextView.text = emailCall

        db.collection("rooms").document(room).addSnapshotListener{ doc,e ->
            var respuesta = doc?.get("Respuesta").toString()
            if (respuesta.equals("Aceptada"))
            {
                // SE VA LA VIDEOLLAMADA
                System.out.println("SE ACEPTÓ LA VIDEOLLAMADA")
                val options = JitsiMeetConferenceOptions.Builder()
                    .setRoom(room)
                    .setAudioMuted(true)
                    .setVideoMuted(true)
                    .build()
                JitsiMeetActivity.launch(this, options)

            } else if (respuesta.equals("Rechazada"))
            {
                //SE REGRESA A MENU
                System.out.println("SE RECHAZÓ LA VIDEOLLAMADA")
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                System.out.println("???????")
            }
        }

        cancelCallButton.setOnClickListener{
            //SE TIENE QUE CANCELAR LA LLAMADA
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onRestart() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        super.onRestart()
    }

    override fun requestPermissions(p0: Array<out String>?, p1: Int, p2: PermissionListener?) {
        TODO("Not yet implemented")
        }
}