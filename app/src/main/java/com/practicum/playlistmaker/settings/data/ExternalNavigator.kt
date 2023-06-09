package com.practicum.playlistmaker.settings.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.settings.domain.IExternalNavigator

class ExternalNavigator(private val context: Context): IExternalNavigator {

    override fun shareApplication(){
        val courseLink = context.getString(R.string.link_Yandex_AndroidDeveloper)
        val sendIntent : Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, courseLink)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        context.startActivity(shareIntent)
    }

    override fun writeToSupport(){
        val message = context.getString(R.string.write_support_message)
        val subject = context.getString(R.string.write_support_subject)
        val writeSupport = Intent(Intent.ACTION_SENDTO)
        writeSupport.data = Uri.parse("mailto:")
        writeSupport.putExtra(Intent.EXTRA_EMAIL, arrayOf(context.getString(R.string.my_email)))
        writeSupport.putExtra(Intent.EXTRA_SUBJECT, subject)
        writeSupport.putExtra(Intent.EXTRA_TEXT, message)
        context.startActivity(writeSupport)
    }


    override fun openUserAgreement(){
        val userAgreementLink = context.getString(R.string.yandex_offer)
        val openURL = Intent(Intent.ACTION_VIEW)
        openURL.data = Uri.parse(userAgreementLink)
        context.startActivity(openURL)
    }
}