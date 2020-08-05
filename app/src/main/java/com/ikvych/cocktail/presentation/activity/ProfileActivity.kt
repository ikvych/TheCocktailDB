package com.ikvych.cocktail.presentation.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.ikvych.cocktail.R
import com.ikvych.cocktail.databinding.ActivityProfileBinding
import com.ikvych.cocktail.presentation.activity.base.BaseActivity
import com.ikvych.cocktail.viewmodel.ProfileActivityViewModel
import kotlinx.android.synthetic.main.activity_profile.*
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStream
import kotlin.reflect.KClass

class ProfileActivity : BaseActivity<ProfileActivityViewModel, ActivityProfileBinding>()  {

    override var contentLayoutResId: Int = R.layout.activity_profile
    override val viewModelClass: KClass<ProfileActivityViewModel>
        get() = ProfileActivityViewModel::class

    override fun configureView(savedInstanceState: Bundle?) {
        super.configureView(savedInstanceState)
        b_upload_photo.setOnClickListener(this)
    }

    override fun configureDataBinding(binding: ActivityProfileBinding) {
        super.configureDataBinding(binding)
        binding.viewModel = viewModel
    }

    override fun onClick(v: View?) {
        if (v == null) return
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            100)
            return
        }
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.type = "image/*"
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {

            val bitmaps = arrayListOf<Bitmap>()
            val clipData = data?.clipData

            if (clipData != null) {
                for (i in 0..clipData.itemCount) {
                    val imageUri = clipData.getItemAt(i).uri
                    try {
                        val inputStream = contentResolver.openInputStream(imageUri)
                        val imageBitmap = BitmapFactory.decodeStream(inputStream)
                        bitmaps.add(imageBitmap)
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    }

                }
            } else {
                val imageUri = data?.data
                if (imageUri != null) {

                    try {
                        val inputStream = contentResolver.openInputStream(imageUri)
                        val file = File.createTempFile("userAvatar", ".jpg")
                        file.copyInputStreamToFile(inputStream!!)
                        viewModel.uploadAvatar(file)
                        inputStream.close()
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    }
                } else {
                    return
                }
            }
        }
    }

    fun File.copyInputStreamToFile(inputStream: InputStream) {
        this.outputStream().use { fileOut ->
            inputStream.copyTo(fileOut)
        }
    }
}