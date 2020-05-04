package com.wisewolf.njmschool

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.cardview.widget.CardView
import com.bumptech.glide.Glide

class SignUpActivity : AppCompatActivity() {
    lateinit var imageView: ImageView
    lateinit var check: TextView
    lateinit var phone: EditText
    lateinit var password: EditText
    lateinit var signup: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        val actionBar = supportActionBar!!
        actionBar!!.hide()
        imageView  =findViewById<ImageView>(R.id.child_gif2)
        check  =findViewById<TextView>(R.id.check)
        phone  =findViewById<EditText>(R.id.phone)
        password  =findViewById<EditText>(R.id.password)
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
