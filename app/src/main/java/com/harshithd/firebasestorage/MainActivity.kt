package com.harshithd.firebasestorage

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.myhexaville.smartimagepicker.ImagePicker
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val imagePicker by lazy {
        ImagePicker(this, null) { uri ->
            ivSelectedImage.setImageURI(uri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnChoose.setOnClickListener {
            imagePicker.choosePicture(false)
        }
        ivSelectedImage.setOnClickListener {
            uploadImage()
        }
    }

    private fun uploadImage() {
        progressUpload.visibility = View.VISIBLE
        // TODO: add code for uploading the image here
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        imagePicker.handleActivityResult(resultCode, requestCode, data)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        imagePicker.handlePermission(requestCode, grantResults)
    }
}
