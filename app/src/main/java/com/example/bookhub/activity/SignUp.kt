package com.example.bookhub.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.bookhub.PreferenceManager
import com.example.bookhub.R
import com.example.bookhub.SharedPreferenceManager
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth

class SignUp : AppCompatActivity() {
    private lateinit var inputFirst: EditText
    private lateinit var inputLast: EditText
    private lateinit var inputEmail: EditText
    private lateinit var inputPass: EditText
    private lateinit var inputConfirm: EditText
    private lateinit var signUpBtn: MaterialButton
    private lateinit var imageView: ImageView
    private lateinit var pbar1: ProgressBar
    private lateinit var auth: FirebaseAuth
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this@SignUp, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        preferenceManager = PreferenceManager(this@SignUp)
        setContentView(R.layout.activity_sign_up)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "SIGN UP"

        //auth = FirebaseAuth.getInstance()

        inputFirst = findViewById(R.id.InputFirstName)
        inputLast = findViewById(R.id.InputLastName)
        inputEmail = findViewById(R.id.InputEmail)
        inputPass = findViewById(R.id.InputPassword)
        inputConfirm = findViewById(R.id.InputConfirmPass)
        signUpBtn = findViewById(R.id.signUpBtn)
        pbar1 = findViewById(R.id.signupbar)
        imageView = findViewById(R.id.imgback)

        imageView.setOnClickListener{
            startActivity(Intent(this@SignUp, SignIn::class.java))
            finishAffinity()
        }

        findViewById<TextView>(R.id.signin).setOnClickListener { onBackPressed() }
        signUpBtn.setOnClickListener {
            if (inputFirst.text.toString().trim { it <= ' ' }.isEmpty()) {
                Toast.makeText(this@SignUp, "Enter First Name", Toast.LENGTH_SHORT).show()
            } else if (inputLast.text.toString().trim { it <= ' ' }.isEmpty()) {
                Toast.makeText(this@SignUp, "Enter Last Name", Toast.LENGTH_SHORT).show()
            } else if (inputEmail.text.toString().trim { it <= ' ' }.isEmpty()) {
                Toast.makeText(this@SignUp, "Enter Email Address", Toast.LENGTH_SHORT).show()
            } else if (inputPass.text.toString().trim { it <= ' ' }.isEmpty()) {
                Toast.makeText(this@SignUp, "Enter Password", Toast.LENGTH_SHORT).show()
            } else if (inputPass.text.toString() != inputConfirm.text.toString()) {
                Toast.makeText(this@SignUp, "Enter Valid Password", Toast.LENGTH_SHORT).show()
            } else if (inputConfirm.text.toString().trim { it <= ' ' }.isEmpty()) {
                Toast.makeText(this@SignUp, "Confirm Your Password", Toast.LENGTH_SHORT).show()
            } else if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail.text.toString()).matches()) {
                Toast.makeText(this@SignUp, "Enter Valid Email", Toast.LENGTH_SHORT).show()
            } else if (inputPass.text.toString().length < 6) {
                Toast.makeText(this@SignUp, "Password must be 6 letters", Toast.LENGTH_SHORT).show()
            } else {
                signUp()
            }
        }
    }
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        super.onOptionsItemSelected(item)
//
//        when (item.itemId) {
//            android.R.id.home -> {
//                onBackPressed()
//            }
//        }
//        return true
//    }

    private fun signUp() {
        signUpBtn.visibility = View.INVISIBLE
        pbar1.visibility = View.VISIBLE
        val email = inputEmail.text.toString()
        val pass = inputPass.text.toString()

        //val user = HashMap<String, Any>()
        //user[com.example.bookhub.Constants.toString()] = inputFirst.text.toString()
        //user[com.example.bookhub.Constants.toString()] = inputLast.text.toString()
        //user[com.example.bookhub.Constants.toString()] = inputEmail.text.toString()
        //user[com.example.bookhub.Constants.toString()] = inputPass.text.toString()

        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {

                preferenceManager.putString("email",email)
                preferenceManager.putString("FName",inputFirst.text.toString())
                preferenceManager.putString("LName",inputLast.text.toString())

                val intent = Intent(this@SignUp, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            } else {
                pbar1.visibility = View.INVISIBLE
                signUpBtn.visibility = View.VISIBLE
                Toast.makeText(this@SignUp, task.exception!!.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }

    }
}