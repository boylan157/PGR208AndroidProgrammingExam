package no.kristiania.pgr208androidprogrammingexam.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import no.kristiania.pgr208androidprogrammingexam.R

class SplashScreen : AppCompatActivity() {

    lateinit var handler: Handler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        // Creating splash screen
        handler = Handler()
        handler.postDelayed({

            var mainIntent: Intent = Intent(this, MainActivity::class.java)
            startActivity(mainIntent)
            finish()
        }, 3000) // 3 second to open second page

        }
    }

