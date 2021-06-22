package com.example.candycrush


import android.graphics.Bitmap
import android.graphics.Insets
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
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

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



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
                    candyToBeDragged = imageView.id
                    candyToBeReplaced = candyToBeDragged -1
                    candyInterChange()
                    moves += 1
                    binding.tvFirstPlayerMove.text = "$moves"
                }

                override fun onSwipeRight() {
                    super.onSwipeRight()
                    candyToBeDragged = imageView.id
                    candyToBeReplaced = candyToBeDragged +1
                    candyInterChange()
                    moves += 1
                    binding.tvFirstPlayerMove.text = "$moves"
                }

                override fun onSwipeTop() {
                    super.onSwipeTop()
                    candyToBeDragged = imageView.id
                    candyToBeReplaced = candyToBeDragged - noOfBlock
                    candyInterChange()
                    moves += 1
                    binding.tvFirstPlayerMove.text = "$moves"
                }

                override fun onSwipeBottom() {
                    super.onSwipeBottom()
                    candyToBeDragged = imageView.id
                    candyToBeReplaced = candyToBeDragged + noOfBlock
                    candyInterChange()
                    moves += 1
                    binding.tvFirstPlayerMove.text = "$moves"
                }
            } )

        }

       mHandler = Handler()
        startRepeat()


        addResizeImageToList()
        createBoardForSecondPlayer()
    }



    private fun checkRowAndColumn(){

        for (i in 0 until 62){
            val isBlank: Boolean = candy[i].tag == notCandy

        }

    }


    private fun checkRowForAll() {

        for (i in 0 until 62) {

            val isBlank: Boolean = candy[i].tag == notCandy
            val notValid = intArrayOf(6, 7, 14, 15, 22, 23, 30, 31, 38, 39, 46, 47, 54, 55)
            val list: List<Int> = notValid.toList()

            if (!list.contains(i)) {




                   if (( i%8 in 0..3) && !isBlank && candy[i].tag == candy[i+1].tag &&
                       candy[i].tag == candy[i+2].tag && candy[i].tag == candy[i+3].tag &&
                       candy[i].tag == candy[i+4].tag
                            ){
                        // 10 bonus
                        score += (5 + 10)

                        binding.tvPlayerOneScore.text = "$score"
                        //4
                        candy[i].setImageResource(notCandy)
                        candy[i].tag = notCandy
                         // 3
                        candy[i+1].setImageResource(notCandy)
                        candy[i+1].tag = notCandy
                       //2
                        candy[i+2].setImageResource(notCandy)
                        candy[i+2].tag = notCandy
                       // 1
                        candy[i+3].setImageResource(notCandy)
                        candy[i+3].tag = notCandy
                          // 0
                        candy[i+4].setImageResource(notCandy)
                        candy[i+4].tag = notCandy
                   }
                   else if ( (i % 8 in 0..4) && !isBlank && candy[i].tag == candy[i+1].tag &&
                       candy[i].tag == candy[i+2].tag  && candy[i].tag == candy[i+3].tag
                            ){

                        //  5 bonus
                        score += (4 + 5 )

                        binding.tvPlayerOneScore.text = "$score"
                        // 3
                        candy[i].setImageResource(notCandy)
                        candy[i].tag = notCandy

                        candy[i+1].setImageResource(notCandy)
                        candy[i+1].tag = notCandy
                       // 1
                        candy[i+2].setImageResource(notCandy)
                        candy[i+2].tag = notCandy
                      // 0
                        candy[i+3].setImageResource(notCandy)
                        candy[i+3].tag = notCandy
                    }

                  else if  (  !isBlank && candy[i+1].tag == candy[i].tag && candy[i+2].tag == candy[i].tag ) {
                        score += 3
                        binding.tvPlayerOneScore.text = "$score"
                        candy[i].setImageResource(notCandy)
                        candy[i].tag = notCandy
                        Log.i("value of i", "$i")
                        candy[i+1].setImageResource(notCandy)
                        candy[i+1].tag = notCandy

                        candy[i+2].setImageResource(notCandy)
                        candy[i+2].tag = notCandy


                    }



                }
            }
            // moveDownCandies()
        }


    private fun checkColumnForAll(){

        for (i in 0 until 48){

            val isBlank:Boolean = candy[i].tag == notCandy




            if (i < 32 && !isBlank && candy[i + noOfBlock].tag == candy[i].tag &&
               candy[i + 2 * noOfBlock].tag == candy[i].tag && candy[i + 3 * noOfBlock].tag == candy[i].tag &&
               candy[i + 4 * noOfBlock].tag == candy[i].tag ){
                    // bonus 10
                    score += (5+10)
                    binding.tvPlayerOneScore.text = "$score"
               candy[i].setImageResource(notCandy)
               candy[i].tag = notCandy

               candy[i + noOfBlock].setImageResource(notCandy)
               candy[i + noOfBlock].tag = notCandy

               candy[i + 2 * noOfBlock].setImageResource(notCandy)
               candy[i + 2 * noOfBlock].tag = notCandy

               candy[i + 3 * noOfBlock].setImageResource(notCandy)
               candy[i + 3 * noOfBlock].tag = notCandy

               candy[i + 4 * noOfBlock].setImageResource(notCandy)
               candy[i + 4 * noOfBlock].tag = notCandy

            }
            else  if (i < 40 && !isBlank && candy[i + noOfBlock].tag == candy[i].tag &&
               candy[i + 2 * noOfBlock].tag == candy[i].tag && candy[i + 3 * noOfBlock].tag == candy[i].tag ){
                  // bonus
                    score += (4 +5)
                    binding.tvPlayerOneScore.text = "$score"
               candy[i].setImageResource(notCandy)
               candy[i].tag = notCandy

               candy[i + noOfBlock].setImageResource(notCandy)
               candy[i + noOfBlock].tag = notCandy

               candy[i + 2 * noOfBlock].setImageResource(notCandy)
               candy[i + 2 * noOfBlock].tag = notCandy

               candy[i + 3 * noOfBlock].setImageResource(notCandy)
               candy[i + 3 * noOfBlock].tag = notCandy



            }
            else if(!isBlank && candy[i + noOfBlock].tag == candy[i].tag &&
                candy[i + 2 * noOfBlock].tag == candy[i].tag){

                    score += 3
                    binding.tvPlayerOneScore.text = "$score"
                candy[i].setImageResource(notCandy)
                candy[i].tag = notCandy

                candy[i + noOfBlock].setImageResource(notCandy)
                candy[i + noOfBlock].tag = notCandy

                candy[i + 2 * noOfBlock].setImageResource(notCandy)
                candy[i + 2 * noOfBlock].tag = notCandy

             }
        }

     //   moveDownCandies()
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

                checkRowForAll()
                checkColumnForAll()
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
        candiesFirst.add(resize(ContextCompat.getDrawable(this,R.drawable.bluecandy)))
        candiesFirst.add(resize(ContextCompat.getDrawable(this,R.drawable.greencandy)))
        candiesFirst.add(resize(ContextCompat.getDrawable(this,R.drawable.orangecandy)))
        candiesFirst.add(resize(ContextCompat.getDrawable(this,R.drawable.purplecandy)))
        candiesFirst.add(resize(ContextCompat.getDrawable(this,R.drawable.yellowcandy)))
        candiesFirst.add(resize(ContextCompat.getDrawable(this,R.drawable.redcandy)))

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
                imageView.setBackgroundColor(ContextCompat.getColor(this,R.color.colorPrimary))
            }else{
                imageView.setBackgroundColor(ContextCompat.getColor(this,R.color.teal_200))
            }


            gridLayout.addView(imageView)
        }

    }

}