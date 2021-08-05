package com.codingblocks.otp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.ScriptGroup
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    //Go to google cloud and enable api for safetyNet instead of recaptcha and check implementations
    //codestromex
    //sirf or sirf text format ki dikkat thi , trim karna or .text karna

    private  var forceResendToken: PhoneAuthProvider.ForceResendingToken? = null
private lateinit var resendToken:PhoneAuthProvider.ForceResendingToken
    private var mBaseCallback:PhoneAuthProvider.OnVerificationStateChangedCallbacks?= null
    private var mVerificationId: String?=null
    private lateinit var firebaseAuth: FirebaseAuth
    private val TAG = "MAIN_TAG"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        firebaseAuth = FirebaseAuth.getInstance()

        mBaseCallback = object :PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            override fun onVerificationCompleted(credential:  PhoneAuthCredential) {
                startActivity(Intent(applicationContext,Home::class.java))
                finish()
            }

            override fun onVerificationFailed(e: FirebaseException) {
               Toast.makeText(applicationContext,"Failed",Toast.LENGTH_SHORT).show()
            }

            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                   mVerificationId = verificationId
                resendToken = token

                var intent = Intent(applicationContext,OTPVerify::class.java)
                intent.putExtra("verificationId",mVerificationId)
                startActivity(intent)
            }

        }

        sendOTP.setOnClickListener{
         startVerification()


        }


    }
    private fun startVerification(){
        var number = phoneNo.text.toString().trim()
        if(!number.isEmpty())
        {
            number = "+91"+number
            startVerification(number)

        }
        else{
            Toast.makeText(this,"Please Enter Mobile NO.",Toast.LENGTH_SHORT).show()
        }
    }
    private fun startVerification(phone:String){

        val options =
            PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(phone)
                .setTimeout(60,TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(mBaseCallback!!)
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)
        }

}