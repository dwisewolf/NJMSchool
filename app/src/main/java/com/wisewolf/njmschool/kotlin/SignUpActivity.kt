package com.wisewolf.njmschool.kotlin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.bumptech.glide.Glide
import com.wisewolf.njmschool.R

class SignUpActivity : AppCompatActivity() {
    lateinit var imageView: ImageView
    lateinit var check: TextView
    lateinit var phone: EditText
    lateinit var password: CardView
    lateinit var signup: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        val actionBar = supportActionBar!!
        actionBar.hide()
        imageView  =findViewById<ImageView>(R.id.child_gif2)
        check  =findViewById<TextView>(R.id.check)
        phone  =findViewById<EditText>(R.id.phone)
        password  =findViewById<CardView>(R.id.password)
        signup  =findViewById<CardView>(R.id.button)
        Glide.with(this).asGif().load(R.raw.child).into(imageView)

        check.setOnClickListener(View.OnClickListener {
            signup.visibility= View.VISIBLE
            password.visibility=View.VISIBLE
            val toast = Toast.makeText(applicationContext, "Number present", Toast.LENGTH_LONG)
            toast.show()
        })

        signup.setOnClickListener(View.OnClickListener {
            intent = Intent(this, LoginActivity::class.java)

            startActivity(intent)
        })
    }
}
