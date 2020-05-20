package com.wisewolf.njmschool.kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.wisewolf.njmschool.R

class ProfileActivity : AppCompatActivity() {
    lateinit var prfile_gif: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        val actionBar = supportActionBar!!
        actionBar!!.hide()
        prfile_gif  =findViewById<ImageView>(R.id.profilepic)
        Glide.with(this).asGif().load(R.raw.profile).into(prfile_gif)
    }
}
