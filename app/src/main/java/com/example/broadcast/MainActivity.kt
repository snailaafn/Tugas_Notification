package com.example.broadcast

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.broadcast.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var prefManager: PrefManager
    private val channelId = "LOGOUT_NOTIF"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefManager = PrefManager.getInstance(this)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        checkLoginStatus()

        with(binding){
            txtUsername.text = prefManager.getUsername()
            btnLogout.setOnClickListener {

                val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    PendingIntent.FLAG_IMMUTABLE
                } else{
                    0
                }

                val intentToLoginAct = Intent(this@MainActivity, LoginActivity::class.java).apply {
                    putExtra("LOGOUT_ACTIVITY", true)
                }

                val pendingIntent = PendingIntent.getActivity(
                    this@MainActivity, 0, intentToLoginAct, flag
                )

                val builder = NotificationCompat.Builder(this@MainActivity, channelId)
                    .setSmallIcon(R.drawable.baseline_notifications_24)
                    .setContentTitle("Logout Notification")
                    .setContentText("Do you want to logout?")
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .addAction(0, "Logout", pendingIntent)


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    val notifChannel = NotificationChannel(
                        channelId,
                        "Logout Notification",
                        NotificationManager.IMPORTANCE_DEFAULT
                    )
                    with(notificationManager) {
                        createNotificationChannel(notifChannel)
                        notify(32, builder.build())
                    }
                }
                else {
                    notificationManager.notify(32, builder.build())
                }
            }
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun checkLoginStatus() {
        if (prefManager.getUsername().isEmpty()){
            startActivity(
                Intent(this@MainActivity, LoginActivity::class.java)
            )
            finish()
        }
    }
}