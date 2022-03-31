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
import com.facebook.react.modules.core.PermissionListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_meet.*
import kotlinx.android.synthetic.main.activity_res_call.*
import org.jitsi.meet.sdk.BroadcastEvent
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetActivityInterface
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import timber.log.Timber

class ResCallActivity : AppCompatActivity(), JitsiMeetActivityInterface {
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

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {

        //Cambia el color de la barra del título y el texto del título
        val actionBar: ActionBar?
        actionBar = supportActionBar
        val colorDrawable = ColorDrawable(Color.parseColor("#59656f"))
        actionBar?.setBackgroundDrawable(colorDrawable)
        actionBar?.setTitle(Html.fromHtml("<font color='#ffffff'>Llamada entrante</font>"));

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_res_call)

        var emisor = intent.getStringExtra("emisor").toString()
        var room = intent.getStringExtra("room").toString()
        var nombre=""

        db.collection("usuarios").document(emisor).get().addOnSuccessListener { doc ->
            nombre = doc.get("Nombre").toString()
            callNameTextView.text = nombre
        }

        callEmailTextView.text = emisor

        declineCallButton.setOnClickListener{
            //SE DECLINA LA LLAMADA

            db.collection("rooms").document(room).update("Respuesta","Rechazada")

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        acceptCallButton.setOnClickListener {
            //SE ACEPTA LLAMADA

            db.collection("rooms").document(room).update("Respuesta","Aceptada")

            val options = JitsiMeetConferenceOptions.Builder()
                .setRoom(room)
                .setAudioMuted(true)
                .setVideoMuted(true)
                .build()
            JitsiMeetActivity.launch(this, options)
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