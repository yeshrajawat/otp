package com.codingblocks.otp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.activity_o_t_p_verify.*

class OTPVerify : AppCompatActivity() {
   private lateinit var  firebaseAuth :FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_o_t_p_verify)
        firebaseAuth = FirebaseAuth.getInstance()
        val mVerificationId = intent.getStringExtra("verificationId")
       confirm_button.setOnClickListener {
           var otp = input1.text.toString().trim() + input2.text.toString().trim()  + input3.text.toString().trim() + input4.text.toString().trim() + input5.text.toString().trim() + input6.text.toString().trim()
           if(input1.text.isEmpty()|| input2.text.isEmpty()|| input3.text.isEmpty()|| input4.text.isEmpty()|| input5.text.isEmpty()|| input6.text.isEmpty()){
               Toast.makeText(this,"Enter Complete OTP",Toast.LENGTH_SHORT).show()
           }
           else{
               val credential : PhoneAuthCredential = PhoneAuthProvider.getCredential(
                   mVerificationId.toString(),otp)
               signInWithPhoneAuthCredential(credential)

           }
       }
    }


    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this){task ->
                if(task.isSuccessful) {
                    startActivity(Intent(applicationContext, Home::class.java))
                    finish()
                }
                else{
                    if(task.exception is FirebaseAuthInvalidCredentialsException)
                    {
                        Toast.makeText(this,"Invalid OTP",Toast.LENGTH_SHORT).show()
                    }
                }
            }

    }

}