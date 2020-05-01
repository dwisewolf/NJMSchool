package com.wisewolf.njmschool

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.bumptech.glide.Glide

class SignUpActivity : AppCompatActivity() {
    lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        val actionBar = supportActionBar!!
        actionBar!!.hide()
        imageView  =findViewById<ImageView>(R.id.child_gif2)
        Glide.with(this).asGif().load(R.raw.child).into(imageView)
    }
}
