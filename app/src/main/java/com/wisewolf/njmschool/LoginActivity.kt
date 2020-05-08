package com.wisewolf.njmschool

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.bumptech.glide.Glide

class LoginActivity : AppCompatActivity() {
    lateinit var imageView: ImageView
    lateinit var login: CardView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        imageView  =findViewById<ImageView>(R.id.child_gif)
        login  =findViewById<CardView>(R.id.log_in)
        Glide.with(this).asGif().load(R.raw.child).into(imageView)



        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        val actionBar = supportActionBar!!
        actionBar!!.hide()

        login.setOnClickListener(View.OnClickListener {
            intent = Intent(this, VideoListing::class.java)

            startActivity(intent)
        })
    }

}
