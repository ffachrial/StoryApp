package com.rndkitchen.storyapp.ui.storyadd

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.rndkitchen.storyapp.data.remote.Result
import com.rndkitchen.storyapp.databinding.ActivityStoryAddBinding
import com.rndkitchen.storyapp.ui.ViewModelFactory
import com.rndkitchen.storyapp.ui.main.CameraActivity
import com.rndkitchen.storyapp.util.SessionManager
import com.rndkitchen.storyapp.util.reduceFileImage
import com.rndkitchen.storyapp.util.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class StoryAddActivity : AppCompatActivity() {
    private lateinit var activityStoryAddBinding: ActivityStoryAddBinding
    private val binding get() = activityStoryAddBinding
    private var storyUpload: File? = null

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, StoryAddActivity::class.java)
            context.startActivity(intent)
        }
        const val CAMERA_X_RESULT = 200

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityStoryAddBinding = ActivityStoryAddBinding.inflate(layoutInflater)
        setContentView(activityStoryAddBinding.root)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        title = "Add Story"

        binding.btnOpenCamera.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            launcherIntentCamera.launch(intent)
        }

        binding.btnOpenGallery.setOnClickListener {
            val intent = Intent()
            intent.action = ACTION_GET_CONTENT
            intent.type = "image/*"
            val chooser = Intent.createChooser(intent, "Choose a Picture")
            launcherIntentGallery.launch(chooser)
        }

        binding.buttonAdd.setOnClickListener {
            uploadImage()
        }
    }

    private fun uploadImage() {
        if (storyUpload != null) {
            val file = reduceFileImage(storyUpload as File)
            val imgDesc = binding.edAddDescription.text
            val description = imgDesc.toString().toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultiPart = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )
            val storiesViewModel = obtainViewModel(this@StoryAddActivity)

            val token = SessionManager.getToken(this)

            storiesViewModel.putStory("Bearer $token", imageMultiPart, description).observe(this) { response ->
                when(response) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(this@StoryAddActivity, "Upload Story Success", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(this@StoryAddActivity, "Upload Story Failed " + response.error, Toast.LENGTH_SHORT).show()
                    }
                }
            }

        } else {
            Toast.makeText(this@StoryAddActivity, "Please put your image file.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity) : StoryAddViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[StoryAddViewModel::class.java]
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (!allPermissionsGranted()) {
            Toast.makeText(
                this,
                "Don't have permission",
                Toast.LENGTH_SHORT
            ).show()
            finish()
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){ result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@StoryAddActivity)
            storyUpload = myFile
            binding.imgPreview.setImageURI(selectedImg)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File

            storyUpload = myFile
            val result =  BitmapFactory.decodeFile(myFile.path)

            binding.imgPreview.setImageBitmap(result)
        }
    }
}