package com.practicum.playlistmaker

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val buttonGoBack = findViewById<ImageView>(R.id.button_go_back)
        val buttonShareApplication = findViewById<Button>(R.id.button_share_application)
        val buttonWriteSupport = findViewById<Button>(R.id.button_write_support)
        val buttonOpenUserAgreement = findViewById<Button>(R.id.button_user_agreement)

        buttonGoBack.setOnClickListener {
            val displayMainMenu = Intent(this, MainActivity::class.java)
            startActivity(displayMainMenu)
        }

        buttonShareApplication.setOnClickListener {
            val sendIntent : Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, getString(R.string.link_Yandex_AndroidDeveloper))
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

        buttonWriteSupport.setOnClickListener {
            val message = getString(R.string.write_support_message)
            val subject = getString(R.string.write_support_subject)
            val writeSupport = Intent(Intent.ACTION_SENDTO)
            writeSupport.data = Uri.parse("mailto:")
            writeSupport.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.my_email)))
            writeSupport.putExtra(Intent.EXTRA_SUBJECT, subject)
            writeSupport.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(writeSupport)
        }

        buttonOpenUserAgreement.setOnClickListener {
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse(getString(R.string.yandex_offer))
            startActivity(openURL)
        }
    }
}