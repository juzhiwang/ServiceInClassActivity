package edu.temple.myapplication

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var timerBinder: TimerService.TimerBinder
    private var timerServiceBound = false

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            timerBinder = service as TimerService.TimerBinder
            timerServiceBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            timerServiceBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.playButton).setOnClickListener {
            if (!timerBinder.isRunning) {
                timerBinder.start(10)
            }
        }

        findViewById<Button>(R.id.pauseButton).setOnClickListener {
            timerBinder.pause()
        }

        findViewById<Button>(R.id.stopButton).setOnClickListener {
            if (timerBinder.isRunning) {
                timerBinder.stop()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Intent(this, TimerService::class.java).also { intent ->
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        if (timerServiceBound) {
            unbindService(serviceConnection)
            timerServiceBound = false
        }
    }
}

