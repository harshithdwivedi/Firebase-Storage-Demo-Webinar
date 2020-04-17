package com.harshithd.firebasestorage

import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.storage.FirebaseStorage
import com.myhexaville.smartimagepicker.ImagePicker
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit var selectedImage: Uri
    val images = arrayListOf<UploadedImage>()

    private val imagePicker by lazy {
        ImagePicker(this, null) { uri ->
            ivSelectedImage.setImageURI(uri)
            selectedImage = uri
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnChoose.setOnClickListener {
            imagePicker.choosePicture(false)
        }
        ivSelectedImage.setOnClickListener {
            uploadImage(selectedImage)
        }
        val imageAdapter = ImageAdapter(images)
        rvItems.layoutManager = LinearLayoutManager(this)
        rvItems.adapter = imageAdapter
    }

    // upload the selected image to Firebase Storage
    private fun uploadImage(selectedImage: Uri) {
        progressUpload.visibility = View.VISIBLE
        val storageRef = FirebaseStorage.getInstance().reference
        val fileName = displayName(selectedImage)

        storageRef
            .child(fileName ?: "")
            .putFile(selectedImage)
            .addOnCompleteListener {
                progressUpload.visibility = View.GONE
            }
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

    private fun displayName(uri: Uri): String? {
        val cursor: Cursor =
            applicationContext.contentResolver.query(uri, null, null, null, null)!!
        val indexedname: Int = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        cursor.moveToFirst()
        val filename: String = cursor.getString(indexedname)
        cursor.close()
        return filename
    }

}
