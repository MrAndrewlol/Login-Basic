package com.example.login_basic

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit


class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    lateinit var user: EditText
    lateinit var pass: EditText
    private lateinit var clickiniciar: Button
    private lateinit var clickregistrar: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        user = findViewById(R.id.editTextText)
        pass = findViewById(R.id.editTextTextPassword)
        clickiniciar = findViewById(R.id.button)
        clickregistrar = findViewById(R.id.button2)

        auth = FirebaseAuth.getInstance()





        clickregistrar.setOnClickListener{
            openregistrar()
        }

        clickiniciar.setOnClickListener{ //Iniciar Sesion
            signin()

        }

    }

    private fun openregistrar() {
        val registry = Intent(this, registraractivity::class.java)

        startActivity(registry)

    }
    private fun openmainmenu() { //Iniciar Sesion

        val compactJws: String = Jwts.builder()
            .claim("email", user.text.toString())
            .claim("password", pass.text.toString())
            .signWith(SignatureAlgorithm.HS256, "secret".toByteArray())
            .compact()

        val currentDate = LocalDateTime.now()
        val futureDateTime = currentDate.plus(15, ChronoUnit.MINUTES)
        // Convert LocalDateTime to Instant
        val instant = futureDateTime.toInstant(ZoneOffset.UTC)
        // Get the seconds since the epoch
        val secondsSinceEpoch = instant.epochSecond

        val menu = Intent(this, MainmenuActivity::class.java).also {
            it.putExtra("token", compactJws)
            it.putExtra("expiracy",secondsSinceEpoch.toString())
        }
        startActivity(menu)

    }

    private fun signin(){

        val user = user.text.toString()
        val pass = pass.text.toString()

        if(user.isBlank() || pass.isBlank()){
            Toast.makeText(this, "Usuario y Contraseña estan en blanco", Toast.LENGTH_SHORT).show()
            return
        }

        auth.signInWithEmailAndPassword(user, pass).addOnCompleteListener(this) {
            if (it.isSuccessful) {
                Toast.makeText(this, "Successfully Logged In", Toast.LENGTH_SHORT).show()




                openmainmenu()
            } else
                Toast.makeText(this, "Log In failed", Toast.LENGTH_SHORT).show()
        }
    }




}