package com.wisewolf.njmschool

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class LoginActivity : AppCompatActivity() {
    lateinit var imageView: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        imageView  =findViewById<ImageView>(R.id.child_gif)
        Glide.with(this).asGif().load(R.raw.child).into(imageView)
        val actionBar = supportActionBar!!
        actionBar!!.hide()
    }
}
