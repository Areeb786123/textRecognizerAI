package com.Areeb.textrecognizer

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.media.Image
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.provider.MediaStore
import android.view.View
import android.widget.*
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionText
import androidx.annotation.RequiresApi as RequiresApi1


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var textdisplay : EditText
    private lateinit  var img :ImageView
    private lateinit var btnselect: Button
    private lateinit var btnrecognize :Button



    @RequiresApi1(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        img =  findViewById(R.id.img)
        textdisplay = findViewById(R.id.textdisplay)
        btnselect = findViewById(R.id.btnSelect)
        btnrecognize = findViewById(R.id.btnrecognize)

        btnselect.setOnClickListener{
            val intent = Intent()
            intent.type="image/*"
            intent.action =Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent,"select Image "),1)

        }
        btnrecognize.setOnClickListener{
            val bitmap = (img.drawable as BitmapDrawable).bitmap
            val image = FirebaseVisionImage.fromBitmap(bitmap)
            val recognize = FirebaseVision.getInstance().onDeviceTextRecognizer
            textdisplay?.setText(" ")
            recognize.processImage(image).addOnSuccessListener {firebaseVisionText ->
                Recognizetext(firebaseVisionText)





            }.addOnFailureListener {
                textdisplay?.setText("failed")

            }
        }

    }

    private fun Recognizetext(resulttext : FirebaseVisionText){
        if (resulttext.textBlocks.size == 0 ){
            textdisplay?.setText("Data Not Found")
            return
        }
        for(block in resulttext.textBlocks){
            val text  = block.text
            textdisplay?.setText(textdisplay.text.toString() + text)
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 1 && resultCode ==Activity.RESULT_OK){
            if (data != null){
                img?.setImageURI(data.data)
            }

        }
    }
}

