package com.example.candycrush.ui.activities;

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.candycrush.R


@Suppress("DEPRECATION")

class SplashScreen :AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        // This is used to hide the status bar and make
        // the splash screen as a full screen activity.
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        // HERE WE ARE TAKING THE REFERENCE OF OUR IMAGE
        // SO THAT WE CAN PERFORM ANIMATION USING THAT IMAGE
        val tvCandyCrush: TextView = findViewById(R.id.tv_candy_crush)
        val slideAnimation = AnimationUtils.loadAnimation(this, R.anim.top_animation)
        tvCandyCrush.startAnimation(slideAnimation)

        val imageCenter: ImageView = findViewById(R.id.image_center)
        val hyperspaceJump: Animation = AnimationUtils.loadAnimation(this, R.anim.hyperspace_jump)
        imageCenter.startAnimation(hyperspaceJump)

        val imageLeft: ImageView = findViewById(R.id.image_left)
        val bottomAnimation : Animation = AnimationUtils.loadAnimation(this,
            R.anim.bottom_animation
        )
         imageLeft.startAnimation(bottomAnimation)


        val imageRight: ImageView = findViewById(R.id.image_right)
        val topAnimation: Animation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation)
        imageRight.startAnimation(topAnimation)


        val imageCenter1: ImageView = findViewById(R.id.image_center2)
        imageCenter1.startAnimation(bottomAnimation)

        val imageRight1: ImageView = findViewById(R.id.image_right2)
        imageRight1.startAnimation(bottomAnimation)

        val imageLeft1: ImageView = findViewById(R.id.image_left2)
        imageLeft1.startAnimation(bottomAnimation)



        // we used the postDelayed(Runnable, time) method
        // to send a message with a delayed time.
        Handler().postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000) // 3000 is the delayed time in milliseconds.
    }
}
