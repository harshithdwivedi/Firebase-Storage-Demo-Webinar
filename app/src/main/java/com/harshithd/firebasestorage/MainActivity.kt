package com.harshithd.firebasestorage

import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.storage.FirebaseStorage
import com.myhexaville.smartimagepicker.ImagePicker
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    val rootReference = FirebaseStorage.getInstance().reference
    val uploadReference = rootReference.child("upload")

    lateinit var selectedImage: Uri
    val images = arrayListOf<UploadedImage>()
    val imageAdapter = ImageAdapter(images)
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
        ivRefresh.setOnClickListener {
            fetchImagesFromStorage()
        }

        rvItems.layoutManager = GridLayoutManager(this, 2)
        rvItems.adapter = imageAdapter
    }

    private fun fetchImagesFromStorage() {
        images.clear()
        imageAdapter.notifyDataSetChanged()

        uploadReference.listAll()
            .addOnSuccessListener {
                val files = it.items
                files.forEach { currentFile ->
                    val name = currentFile.name
                    currentFile.downloadUrl
                        .addOnSuccessListener {
                            Log.e("Name Tag", "The file name is $name")
                            Log.e("URL Tag", "The URL is $it")

                            val currentImage = UploadedImage(it, name)
                            images.add(currentImage)
//                            imageAdapter.notifyDataSetChanged()

                            imageAdapter.notifyItemInserted(images.size - 1)
                        }
                }
            }
            .addOnFailureListener {

            }
    }

    // upload the selected image to Firebase Storage
    private fun uploadImage(selectedImage: Uri) {
        progressUpload.visibility = View.VISIBLE

        val fileName = displayName(selectedImage)

        val uploadTask = uploadReference
            .child(fileName ?: "")
            .putFile(selectedImage)
            .addOnSuccessListener {
                Toast.makeText(baseContext, "Upload complete!!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(baseContext, "Upload failed, please retry!!", Toast.LENGTH_SHORT)
                    .show()
            }
            .addOnCompleteListener {
                progressUpload.visibility = View.GONE
            }

//        uploadTask.cancel()
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
