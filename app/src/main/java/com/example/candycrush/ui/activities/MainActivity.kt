package com.example.candycrush.ui.activities


import android.graphics.Bitmap
import android.graphics.Insets
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowInsets
import android.view.WindowMetrics
import android.widget.GridLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.candycrush.OnSwipeTouchListener
import com.example.candycrush.R
import com.example.candycrush.databinding.ActivityMainBinding
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.floor


class MainActivity : AppCompatActivity() {

  private val  candies = arrayOf(
      R.drawable.bluecandy,
      R.drawable.greencandy,
      R.drawable.orangecandy,
      R.drawable.purplecandy,
      R.drawable.redcandy,
      R.drawable.yellowcandy
  )

    private var candy:ArrayList<ImageView> = ArrayList<ImageView>()
    private var candiesFirst = ArrayList<Drawable?>()
    private var  widthOfBlockSecond = 8
    private var heightOfScreen : Int = 0
    private var widthOfBlock = 8
    private val noOfBlock = 8
    private var widthOfScreen:Int = 0
    private  var candyToBeDragged: Int = 0
    private var  candyToBeReplaced :Int = 0
    private val notCandy :Int = R.drawable.ic_not_candy
    private var mHandler = Handler()
    private val interval = 100L
    private var score = 0
    private var moves = 0
    private lateinit var mPlayerCrack:MediaPlayer
    private lateinit var mPlayerDown:MediaPlayer
    private lateinit var mPlayerPress:MediaPlayer
    private lateinit var mPlayerRelease:MediaPlayer
    private lateinit var mPlayerDelicious:MediaPlayer
    private lateinit var mPlayerDivine:MediaPlayer
    private lateinit var mPlayerCandyCrushIntro:MediaPlayer





    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the text to speech

        // Initialize the media player
        mPlayerCrack = MediaPlayer.create(this, R.raw.crack)
        mPlayerDivine = MediaPlayer.create(this, R.raw.divine)
        mPlayerDelicious = MediaPlayer.create(this, R.raw.delicious)
        mPlayerPress = MediaPlayer.create(this, R.raw.button_press)
        mPlayerDown = MediaPlayer.create(this, R.raw.button_down)
        mPlayerRelease = MediaPlayer.create(this, R.raw.button_release)
        mPlayerCandyCrushIntro = MediaPlayer.create(this, R.raw.candy_crush_intro1)

        mPlayerCandyCrushIntro.setVolume(0.17F, 0.17F)
        mPlayerCandyCrushIntro.isLooping = true
        mPlayerCandyCrushIntro.start()


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
          val  windowMetrics : WindowMetrics = windowManager.currentWindowMetrics
          val  insets:Insets = windowMetrics.getWindowInsets()
              .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars());
            widthOfScreen = windowMetrics.bounds.width() - insets.left - insets.right
            heightOfScreen = windowMetrics.bounds.height() - insets.left - insets.right;

            Log.e("widthPixels" ,"${windowMetrics.bounds.width() - insets.left - insets.right}")
        }else{
            val displayMetrics: DisplayMetrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            widthOfScreen = displayMetrics.widthPixels
            heightOfScreen = displayMetrics.heightPixels

            Log.e("widthPixels" ,"${displayMetrics.widthPixels}")
        }

        widthOfBlock = widthOfScreen/ noOfBlock
        widthOfBlockSecond = (widthOfBlock/1.6).toInt()



        createBoardForFirstPlayer()

        for (imageView in candy){

            imageView.setOnTouchListener(object : OnSwipeTouchListener(this@MainActivity){

                override fun onSwipeLeft() {
                    super.onSwipeLeft()
                    // play sound
                    mPlayerPress.start()
                    candyToBeDragged = imageView.id
                    candyToBeReplaced = candyToBeDragged -1
                    candyInterChange()
                    moves += 1
                    binding.tvFirstPlayerMove.text = "$moves"
                    // play sound Release
                    mPlayerRelease.start()
                }

                override fun onSwipeRight() {
                    super.onSwipeRight()
                    // play sound
                    mPlayerPress.start()

                    candyToBeDragged = imageView.id
                    candyToBeReplaced = candyToBeDragged +1
                    candyInterChange()
                    moves += 1
                    binding.tvFirstPlayerMove.text = "$moves"
                    // play sound Release
                    mPlayerRelease.start()
                }

                override fun onSwipeTop() {
                    super.onSwipeTop()
                    // play sound
                    mPlayerPress.start()

                    candyToBeDragged = imageView.id
                    candyToBeReplaced = candyToBeDragged - noOfBlock
                    candyInterChange()
                    moves += 1
                    binding.tvFirstPlayerMove.text = "$moves"
                    // play sound Release
                    mPlayerRelease.start()
                }

                override fun onSwipeBottom() {
                    super.onSwipeBottom()
                    // play sound
                    mPlayerDown.start()

                    candyToBeDragged = imageView.id
                    candyToBeReplaced = candyToBeDragged + noOfBlock
                    candyInterChange()
                    moves += 1
                    binding.tvFirstPlayerMove.text = "$moves"

                    // play sound Release
                    mPlayerRelease.start()
                }
            } )

        }


       mHandler = Handler()
        startRepeat()


        addResizeImageToList()
        createBoardForSecondPlayer()
    }

    private fun checkRowAndColumn(){
        var max = 0
        var indexMax = -1
        var Rvt=0 // which stores similar vertical candies
        var Rht=0
        var n = noOfBlock
        for (i in 0 until (n*n-1)){
            var vt=1 // for vertical
            var ht=1 // for horizontal
            //    checking upper 2 candy
            if(i-n>=0 && candy[i].tag == candy[i-n].tag){
                vt++
                if(i-(2*n)>=0 && candy[i].tag == candy[i-(2*n)].tag) {
                    vt++
                }
            }
            //    checking downward 2 candy
            if(i+n<(n*n) && candy[i].tag == candy[i+n].tag){
                vt++
                if(i+(2*n)<(n*n) && candy[i].tag == candy[i+(2*n)].tag) {
                    vt++
                }
            }
            if(i-1>=0 && candy[i].tag == candy[i-1].tag){
                ht++
                if(i-2>=0 && candy[i].tag == candy[i-2].tag) {
                    ht++
                }
            }
            if(i+1<(n*n) && candy[i].tag == candy[i+1].tag){
                ht++
                if(i+2<(n*n) && candy[i].tag == candy[i+2].tag) {
                    ht++
                }
            }
            if(ht>2 && vt>2) {

                if (ht + vt - 1 > max) {
                    max = ht + vt
                    indexMax = i
                    Rvt = vt
                    Rht = ht
                }
            }
            else if ( ht >2 ){
                if(ht > max) {
                    max = ht
                    indexMax=i
                    Rvt = 0
                    Rht = ht
                }
            }
            else if ( vt > 2 ){
                if(vt > max) {
                    max = vt
                    indexMax=i
                    Rvt = vt
                    Rht = 0
                }
            }

        }
        if(Rvt>2 || Rht>2){
            if ( Rvt>2){

                if(indexMax-n>=0 && candy[indexMax].tag == candy[indexMax-n].tag){
                    candy[indexMax-n].tag= notCandy
                    candy[indexMax-n].setImageResource(notCandy)
                    if(indexMax-(2*n)>=0 && candy[indexMax].tag == candy[indexMax-(2*n)].tag) {
                        candy[indexMax-(2*n)].tag= notCandy
                        candy[indexMax-(2*n)].setImageResource(notCandy)
                    }
                }
                if(indexMax+n<(n*n) && candy[indexMax].tag == candy[indexMax+n].tag){
                    candy[indexMax+n].tag= notCandy
                    candy[indexMax+n].setImageResource(notCandy)
                    if(indexMax+(2*n)<(n*n) && candy[indexMax].tag == candy[indexMax+(2*n)].tag) {
                        candy[indexMax+(2*n)].tag= notCandy
                        candy[indexMax+(2*n)].setImageResource(notCandy)
                    }
                }
            }
            if ( Rht>2){

                if(indexMax-1>=0 && candy[indexMax].tag == candy[indexMax-1].tag){
                    candy[indexMax-1].tag= notCandy
                    candy[indexMax-1].setImageResource(notCandy)
                    if(indexMax-2>=0 && candy[indexMax].tag == candy[indexMax-2].tag) {
                        candy[indexMax-2].tag= notCandy
                        candy[indexMax-2].setImageResource(notCandy)
                    }
                }

                if(indexMax+1<(n*n) && candy[indexMax].tag == candy[indexMax+1].tag){
                    candy[indexMax+1].tag= notCandy
                    candy[indexMax+1].setImageResource(notCandy)
                    if(indexMax+2<(n*n) && candy[indexMax].tag == candy[indexMax+2].tag) {
                        candy[indexMax+2].tag= notCandy
                        candy[indexMax+2].setImageResource(notCandy)
                    }
                }
            }
            candy[indexMax].tag= notCandy
            candy[indexMax].setImageResource(notCandy)
            if(Rht>2 && Rvt >2) {
                score += (Rht+Rvt-1+ 10)
                binding.tvPlayerOneScore.text = "$score"

                // start sound divine
                mPlayerDivine.start()
            }
            else if ( Rht==5 || Rvt ==5 ){
                score += (5 + 10)
                binding.tvPlayerOneScore.text = "$score"
                // start sound divine
                mPlayerDivine.start()
            }
            else if ( Rht==4 || Rvt ==4 ){
                score += (4 + 5)
                binding.tvPlayerOneScore.text = "$score"
                // start sound
                mPlayerDelicious.start()
            }
            else{
                score += 3
                binding.tvPlayerOneScore.text = "$score"
                // start sound
                mPlayerCrack.start()
            }
        }
    }



    private fun moveDownCandies():Boolean{
       var bool = false   //  check how many down needs
       val firstRow = arrayOf(0,1,2,3,4,5,6,7)
       val list: List<Int> = firstRow.toList()

        for (i in 55 downTo 0 step 1){

            if (candy[i+noOfBlock].tag == notCandy){

                candy[i+noOfBlock].setImageResource((candy[i].tag) as Int)
                candy[i + noOfBlock].tag = candy[i].tag
                candy[i].setImageResource(notCandy)
                candy[i].tag = notCandy

                if (list.contains(i) && candy[i].tag == notCandy){
                    bool = true
                    val randomColor = floor(Math.random()*candies.size).toInt()
                    candy[i].setImageResource(candies[randomColor])
                    candy[i].tag = candies[randomColor]
                }
            }
        }
        for (i in 0 until 8){
              if (candy[i].tag == notCandy){
                  bool = true
                  val randomColor = Math.floor(Math.random()*candies.size).toInt()
                  candy[i].setImageResource(candies[randomColor])
                  candy[i].tag = candies[randomColor]
              }
        }
   return bool }


      // repeat function every 100 milli seconds

    private val repeatChecker: Runnable = object : Runnable{


        override fun run() {

            try {
                checkRowAndColumn()
             //   checkRowForAll()
             //   checkColumnForAll()
                var bool = moveDownCandies()

                while(bool){
                    // all empty candy going to down
                bool = moveDownCandies()
                }
            }
            finally {
                mHandler.postDelayed( this, interval)
            }
        }
        //runnable

    }

       // start Repeat
    private fun startRepeat(){
        repeatChecker.run()
    }





    private fun candyInterChange(){
        val background:Int = candy[candyToBeReplaced].tag as Int
        val background1:Int = candy[candyToBeDragged].tag as Int
        candy[candyToBeDragged].setImageResource(background)
        candy[candyToBeReplaced].setImageResource(background1)
        candy[candyToBeDragged].tag = background
        candy[candyToBeReplaced].tag = background1
    }

    // this is for second player
    private fun addResizeImageToList(){
        candiesFirst.add(resize(ContextCompat.getDrawable(this, R.drawable.bluecandy)))
        candiesFirst.add(resize(ContextCompat.getDrawable(this, R.drawable.greencandy)))
        candiesFirst.add(resize(ContextCompat.getDrawable(this, R.drawable.orangecandy)))
        candiesFirst.add(resize(ContextCompat.getDrawable(this, R.drawable.purplecandy)))
        candiesFirst.add(resize(ContextCompat.getDrawable(this, R.drawable.yellowcandy)))
        candiesFirst.add(resize(ContextCompat.getDrawable(this, R.drawable.redcandy)))

    }

    private fun resize(image: Drawable?): Drawable{
        val b = (image as BitmapDrawable).bitmap
        val bitmapResized = Bitmap.createScaledBitmap(b, 200, 200, false)
        return BitmapDrawable(resources, bitmapResized)
    }



    private  fun  createBoardForFirstPlayer(){
        val gridLayout: GridLayout = binding.glBoardFirstPlayer
        gridLayout.rowCount = noOfBlock

        gridLayout.columnCount = noOfBlock
        gridLayout.layoutParams.width = widthOfScreen
        gridLayout.layoutParams.height= widthOfScreen

        for (i in 0 until noOfBlock*noOfBlock){
            val imageView = ImageView(this)
            imageView.id = i
            imageView.layoutParams = android.view.ViewGroup.LayoutParams(widthOfBlock,widthOfBlock)
            imageView.maxHeight = widthOfBlock
            imageView.maxWidth  = widthOfBlock
            val randomCandy = (Math.floor(Math.random()*candies.size)).toInt()
            imageView.setImageResource(candies[randomCandy])
            imageView.tag = candies[randomCandy]
            candy.add(imageView)
            gridLayout.addView(imageView)

     //  Log.i("proparties of imageview" , "id ${imageView.id} tag ${imageView.tag} ")
        }

    }

    private  fun  createBoardForSecondPlayer(){
        val gridLayout: GridLayout = binding.glBoardMain
        gridLayout.rowCount = noOfBlock
        gridLayout.columnCount = noOfBlock
    //    gridLayout.layoutParams.width = widthOfScreen
    //    gridLayout.layoutParams.height= widthOfScreen


        for (i in 0 until noOfBlock*noOfBlock){
            val imageView = ImageView(this)
            imageView.id = i
            imageView.layoutParams = android.view.ViewGroup.LayoutParams(widthOfBlockSecond,widthOfBlockSecond)
            imageView.maxHeight = widthOfBlockSecond
            imageView.maxWidth  = widthOfBlockSecond
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            val randomCandy = (Math.floor(Math.random()*candiesFirst.size)).toInt()
            imageView.setImageDrawable(candiesFirst[randomCandy])

            // change background color of second player
            if(i%2==0){
                imageView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
            }else{
                imageView.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_200))
            }


            gridLayout.addView(imageView)
        }

    }

    override fun onPause() {


        if(mPlayerCandyCrushIntro.isPlaying){
            mPlayerCandyCrushIntro.stop()
        }
        super.onPause()
    }

    override fun onResume() {

        if(!mPlayerCandyCrushIntro.isPlaying) {
            mPlayerCandyCrushIntro = MediaPlayer.create(this, R.raw.candy_crush_intro1)
            mPlayerCandyCrushIntro.setVolume(0.17F, 0.17F)
            mPlayerCandyCrushIntro.isLooping = true
            mPlayerCandyCrushIntro.start()

        }
        super.onResume()

    }

    override fun onDestroy() {
        if (mPlayerCandyCrushIntro.isPlaying){
            mPlayerCandyCrushIntro.stop()
        }
        super.onDestroy()
    }

}
