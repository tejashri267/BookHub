package com.example.bookhub.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.bookhub.PreferenceManager
import com.example.bookhub.R
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth


class SignIn : AppCompatActivity() {
    private lateinit var inputEmail: EditText
    private lateinit var inputPassword:EditText
    private lateinit var buttonSignIn: MaterialButton
    private lateinit var pbar: ProgressBar
    private lateinit var auth: FirebaseAuth
    private lateinit var preferenceManager: PreferenceManager

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {

        preferenceManager = PreferenceManager(this@SignIn)
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this@SignIn, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        inputEmail = findViewById(R.id.inputEmail)
        inputPassword = findViewById(R.id.inputPassword)
        buttonSignIn = findViewById(R.id.signinbtn)
        pbar = findViewById(R.id.pbar)

       findViewById<TextView>(R.id.signup).setOnClickListener {
           val intent = Intent(this@SignIn, SignUp::class.java)
           startActivity(intent)
       }

        buttonSignIn.setOnClickListener {
            if (inputEmail.text.toString().trim { it <= ' ' }.isEmpty()) {
                Toast.makeText(this@SignIn, "Enter Email", Toast.LENGTH_SHORT).show()
            } else if (inputPassword.text.toString().trim { it <= ' ' }.isEmpty()) {
                Toast.makeText(this@SignIn, "Enter Password", Toast.LENGTH_SHORT).show()
            } else if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail.text.toString()).matches()) {
                Toast.makeText(this@SignIn, "Enter Valid Email", Toast.LENGTH_SHORT).show()
            } else if (inputPassword.text.toString().length < 6) {
                Toast.makeText(this@SignIn, "password must have 6 letters", Toast.LENGTH_SHORT)
                    .show()
            } else {
                signIn()
            }
        }
        FirebaseAuth.getInstance().signOut()
    }

    private fun signIn() {
        buttonSignIn.visibility = View.INVISIBLE
        pbar.visibility = View.VISIBLE

        val email = inputEmail.text.toString()
        val pass = inputPassword.text.toString()

        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {

//                preferenceManager.putString("email",email)
//                val name1 = preferenceManager.getString("FName")
//                val name2 = preferenceManager.getString("LName")
//                preferenceManager.putString("FName", name1)
//                preferenceManager.putString("LName", name2)



                val intent = Intent(this@SignIn, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            } else {
                pbar.visibility = View.INVISIBLE
                buttonSignIn.visibility = View.VISIBLE
                Toast.makeText(
                    this@SignIn, task.exception!!.localizedMessage, Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

}