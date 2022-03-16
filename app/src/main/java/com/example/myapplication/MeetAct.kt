package com.example.myapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.facebook.react.modules.core.PermissionListener
import kotlinx.android.synthetic.main.activity_meet.*
import org.jitsi.meet.sdk.*
import timber.log.Timber
import java.net.URL


class MeetAct : AppCompatActivity(), JitsiMeetActivityInterface {
    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            onBroadcastReceived(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meet)

        var edtNameMeet = findViewById<EditText>(R.id.edt_name_meet)
        var btnStartMeet = findViewById<Button>(R.id.btn_start_meet)

        // Initialize default options for Jitsi Meet conferences.

        // Initialize default options for Jitsi Meet conferences.
        val options = JitsiMeetConferenceOptions.Builder()
            .setServerURL(URL("https://meet.jit.si"))
            .setRoom("fime20391")
            .setAudioMuted(false)
            .setVideoMuted(false)
            .setAudioOnly(false)
            .setWelcomePageEnabled(false)
            .setConfigOverride("requireDisplayName", true)
            .build()

        btnStartMeet.setOnClickListener(View.OnClickListener { onStartMeet() })
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)
        super.onDestroy()
    }

    private fun registerForBroadcastMessages() {
        val intentFilter = IntentFilter()

        /* This registers for every possible event sent from JitsiMeetSDK
           If only some of the events are needed, the for loop can be replaced
           with individual statements:
           ex:  intentFilter.addAction(BroadcastEvent.Type.AUDIO_MUTED_CHANGED.getAction());
                intentFilter.addAction(BroadcastEvent.Type.CONFERENCE_TERMINATED.getAction());
                ... other events
         */for (type in BroadcastEvent.Type.values()) {
            intentFilter.addAction(type.action)
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter)
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

    private fun onStartMeet() {
        val meetName: String = edt_name_meet.getText().toString().trim { it <= ' ' }
        if (meetName.length > 0) {
            val options = JitsiMeetConferenceOptions.Builder()
                .setRoom(edt_name_meet.toString()) // Settings for audio and video
                //.setAudioMuted(true)
                //.setVideoMuted(true)
                .build()
            // Launch the new activity with the given options. The launch() method takes care
            // of creating the required Intent and passing the options.
            JitsiMeetActivity.launch(this, options)
        } else {
            Toast.makeText(applicationContext, "Name invalid!", Toast.LENGTH_SHORT).show()
        }
    }

    // Example for sending actions to JitsiMeetSDK
    private fun hangUp() {
        val hangupBroadcastIntent = BroadcastIntentHelper.buildHangUpIntent()
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(hangupBroadcastIntent)
    }
    override fun requestPermissions(p0: Array<out String>?, p1: Int, p2: PermissionListener?) {
        TODO("Not yet implemented")
    }
}