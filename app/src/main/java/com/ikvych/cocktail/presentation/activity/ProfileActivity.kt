package com.ikvych.cocktail.presentation.activity

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import com.ikvych.cocktail.R
import com.ikvych.cocktail.databinding.ActivityProfileBinding
import com.ikvych.cocktail.extension.isAllPermissionGranted
import com.ikvych.cocktail.extension.log
import com.ikvych.cocktail.presentation.activity.base.BaseActivity
import com.ikvych.cocktail.presentation.dialog.bottom.RegularBottomSheetDialogFragment
import com.ikvych.cocktail.presentation.dialog.bottom.UploadAvatarBottomSheetDialogFragment
import com.ikvych.cocktail.presentation.dialog.type.*
import com.ikvych.cocktail.presentation.extension.convertMbToBinaryBytes
import com.ikvych.cocktail.presentation.extension.scaleToSize
import com.ikvych.cocktail.presentation.fragment.EditProfileFragment
import com.ikvych.cocktail.util.UploadAvatar
import com.ikvych.cocktail.viewmodel.ProfileActivityViewModel
import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.activity_profile.*
import java.io.*
import kotlin.reflect.KClass


class ProfileActivity : BaseActivity<ProfileActivityViewModel, ActivityProfileBinding>() {

    override var contentLayoutResId: Int = R.layout.activity_profile
    override val viewModelClass: KClass<ProfileActivityViewModel>
        get() = ProfileActivityViewModel::class
    private var currentPhotoPath: Uri? = null

    override fun configureView(savedInstanceState: Bundle?) {
        super.configureView(savedInstanceState)
        b_upload_photo.setOnClickListener(this)
        iv_menu.setOnClickListener(this)
    }

    override fun configureDataBinding(binding: ActivityProfileBinding) {
        super.configureDataBinding(binding)
        binding.viewModel = viewModel
    }

    override fun onClick(v: View?) {
        if (v == null) return
        when (v.id) {
            R.id.b_upload_photo -> {
                UploadAvatarBottomSheetDialogFragment.newInstance().show(
                    supportFragmentManager,
                    UploadAvatarBottomSheetDialogFragment::class.java.simpleName
                )
            }
            R.id.iv_menu -> {
                PopupMenu(this, v).apply {
                    inflate(R.menu.menu_profile)
                    setOnMenuItemClickListener {
                        when (it.itemId) {
                            R.id.menu_profile_setting -> {
                                val fragmentTransaction = supportFragmentManager.beginTransaction()
                                fragmentTransaction.add(
                                    R.id.fcv_profile,
                                    EditProfileFragment.newInstance(),
                                    EditProfileFragment::class.java.simpleName
                                )
                                fragmentTransaction.addToBackStack(EditProfileFragment::class.java.name)
                                fragmentTransaction.commit()
                                true
                            }
                            R.id.menu_profile_log_out -> {
                                RegularBottomSheetDialogFragment.newInstance {
                                    titleText = getString(R.string.profile_log_out_dialog_title)
                                    descriptionText =
                                        getString(R.string.profile_log_out_dialog_message)
                                    leftButtonText = getString(R.string.all_cancel_button)
                                    rightButtonText = getString(R.string.all_accept_button)
                                }.show(
                                    supportFragmentManager,
                                    RegularBottomSheetDialogFragment::class.java.simpleName
                                )
                                true
                            }
                            else -> false
                        }
                    }
                    show()
                }

            }
        }

    }

    private fun processGalleryImage(data: Intent) {
        val selectedImageUri = data.data ?: return
        val imageBitmap = getBitmapFromUri(this, selectedImageUri)!!
            .also { "LOG bitmap size before = ${it.byteCount}".log }
            .scaleToSize(convertMbToBinaryBytes(1))
            .also {
                "LOG bitmap size after = ${it.byteCount}, max= ${convertMbToBinaryBytes(
                    1
                )}, dif=${convertMbToBinaryBytes(1) - it.byteCount}".log
            }

        /*We can access getExternalFileDir() without asking any storage permission.*/
        val selectedImgFile = File(
            getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            System.currentTimeMillis().toString() + "_selectedImg.jpg"
        )

        convertBitmapToFile(selectedImgFile, imageBitmap)

        /*We have to again create a new file where we will save the cropped image. */
        val croppedImgFile = File(
            getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            System.currentTimeMillis().toString() + "_croppedImg.jpg"
        )

        openCropActivity(
            this,
            Uri.fromFile(selectedImgFile),
            Uri.fromFile(croppedImgFile)
        )
    }

    private fun processCameraImage() {
        if (currentPhotoPath == null) return
        val capturedBitmap = getBitmapFromUri(this, currentPhotoPath!!)!!
            .also { "LOG bitmap size before = ${it.byteCount}".log }
            .scaleToSize(convertMbToBinaryBytes(1))
            .also {
                "LOG bitmap size after = ${it.byteCount}, max= ${convertMbToBinaryBytes(
                    1
                )}, dif=${convertMbToBinaryBytes(1) - it.byteCount}".log
            }

        val capturedImgFile = File(
            getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            System.currentTimeMillis().toString() + "_capturedImg.jpg"
        )
        convertBitmapToFile(capturedImgFile, capturedBitmap)
        /*We have to again create a new file where we will save the processed image.*/
        val croppedImgFile = File(
            getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            System.currentTimeMillis().toString() + "_croppedImg.jpg"
        )
        openCropActivity(
            this,
            Uri.fromFile(capturedImgFile),
            Uri.fromFile(croppedImgFile)
        )
    }

    private fun saveCroppedImage(data: Intent) {
        try {
            /*After the cropping is done we will get the cropped image Uri here. We can use this Uri and create file and use it for other purpose like saving to cloude etc.*/
            val croppedImageUri = UCrop.getOutput(data) ?: return
            val croppedImageBitmap = getBitmapFromUri(this, croppedImageUri) ?: return

            val avatarImgFile = File(
                getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                System.currentTimeMillis().toString() + "_profileAvatar.jpg"
            )
            convertBitmapToFile(avatarImgFile, croppedImageBitmap)
            //Uploading the image to cloud.
            viewModel.uploadAvatar(avatarImgFile)
        } catch (e: Exception) {
            "Cannot load image. Unknown error".log()
            e.printStackTrace()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when {
            requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null -> {
                processGalleryImage(data)
            }
            resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_TAKE_PICTURE -> {
                processCameraImage()
            }
            resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP && data != null -> {
                saveCroppedImage(data)
            }
            resultCode == UCrop.RESULT_ERROR -> {
                if (data != null) {
                    val cropError = UCrop.getError(data)
                    cropError!!.message.log
                } else {
                    "Error on Crop".log()
                }
                "Cannot crop image. Unknown error!".log
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_IMAGE_CHOOSER_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                    // permissions granted
                    openGallery()
                } else {
                    // user does not grand permissions
                }
            }
            REQUEST_CODE_TAKE_CAMERA_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                    // permissions granted
                    takePicture()
                } else {
                    // user does not grand permissions
                }
            }
        }
    }

    private fun convertBitmapToFile(destinationFile: File, bitmap: Bitmap) {
        //create a file to write bitmap data
        destinationFile.createNewFile()
        //Convert bitmap to byte array
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
        val bitmapData = bos.toByteArray()
        //write the bytes in file
        val fos = FileOutputStream(destinationFile)
        fos.write(bitmapData)
        fos.flush()
        fos.close()
    }

    private fun getBitmapFromUri(context: Context, imageUri: Uri): Bitmap? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.decodeBitmap(
                ImageDecoder.createSource(
                    context.contentResolver,
                    imageUri
                )
            )
        } else {
            context
                .contentResolver
                .openInputStream(imageUri)
                .use { inputStream ->
                    BitmapFactory.decodeStream(inputStream)
                }

        }
    }

    private fun openCropActivity(context: Activity, sourceUri: Uri, destinationUri: Uri) {
        val options = UCrop.Options()
        options.setHideBottomControls(true)
        UCrop.of(sourceUri, destinationUri)
            .withAspectRatio(1f, 1f)
            .withOptions(options)
            .start(context as AppCompatActivity)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
    }

    private fun takePicture() {
        try {
            val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            // Create the File where the photo should go
            File.createTempFile(
                "JPEG_${System.currentTimeMillis()}_", /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
            ).apply {
                // Save a file: path for use with ACTION_VIEW intents
                currentPhotoPath = FileProvider.getUriForFile(
                    this@ProfileActivity,
                    applicationContext.packageName,
                    this
                )
            }.deleteOnExit()

            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
                intent.resolveActivity(packageManager)?.also {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, currentPhotoPath)
                    startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onBottomSheetDialogFragmentClick(
        dialog: DialogFragment,
        buttonType: DialogButton,
        type: DialogType<DialogButton>,
        data: Any?
    ) {
        super.onBottomSheetDialogFragmentClick(dialog, buttonType, type, data)
        when (type) {
            RegularDialogType -> {
                when (buttonType) {
                    RightDialogButton -> {
                        viewModel.removeUser()
                        val intent = Intent(this, AuthActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    LeftDialogButton -> {
                        dialog.dismiss()
                    }
                }
            }
            UploadPhotoDialogType -> {
                when (buttonType) {
                    ItemListDialogButton -> {
                        when (data as UploadAvatar) {
                            UploadAvatar.CAMERA -> {
                                if (isAllPermissionGranted(
                                        Manifest.permission.CAMERA,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                                    )
                                ) {
                                    takePicture()
                                } else {
                                    ActivityCompat.requestPermissions(
                                        this, arrayOf(
                                            Manifest.permission.CAMERA,
                                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                                        ),
                                        REQUEST_CODE_TAKE_CAMERA_PERMISSION
                                    )
                                }
                            }
                            UploadAvatar.GALLERY -> {
                                if (isAllPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                                    openGallery()
                                } else {
                                    ActivityCompat.requestPermissions(
                                        this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                                        REQUEST_CODE_IMAGE_CHOOSER_PERMISSION
                                    )
                                    return
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    companion object {
        const val REQUEST_CODE_PICK_IMAGE = 1932
        const val REQUEST_CODE_IMAGE_CHOOSER_PERMISSION = 1933
        const val REQUEST_CODE_TAKE_PICTURE = 1934
        const val REQUEST_CODE_TAKE_CAMERA_PERMISSION = 1935
    }
}