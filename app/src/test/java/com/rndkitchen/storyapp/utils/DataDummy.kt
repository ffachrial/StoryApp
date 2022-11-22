package com.rndkitchen.storyapp.utils

import com.rndkitchen.storyapp.data.local.entity.StoriesEntity
import com.rndkitchen.storyapp.data.remote.LoginRequest
import com.rndkitchen.storyapp.data.remote.RegisterBody
import com.rndkitchen.storyapp.data.remote.response.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

object DataDummy {
    fun generateDummyUploadImg(): MultipartBody.Part {
        val storyUpload: File = createTempFile()
        val inputStream: InputStream? =
            javaClass.classLoader?.getResourceAsStream("anak_kucing_munchkin.jpg")
        val outputStream: OutputStream = FileOutputStream(storyUpload)
        val buf = ByteArray(1024)
        var len: Int
        if (inputStream != null) {
            while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
        }
        outputStream.close()
        inputStream?.close()

        val requestImgFile = storyUpload.asRequestBody("image/jpeg".toMediaTypeOrNull())

        return MultipartBody.Part.createFormData(
            "photo",
            storyUpload.name,
            requestImgFile
        )
    }

    fun generateDummyUploadDesc(): RequestBody {
        val imgDesc = "Ini gambar keren"
        return imgDesc.toRequestBody("text/plain".toMediaType())
    }

    fun generateDummyPutStoryResponse(): PutStoryResponse {
        return PutStoryResponse(
            false,
            "Story created successfully"
        )
    }

    fun generateDummyRegisterBody(): RegisterBody {
        return RegisterBody(
            "sapider man",
            "sapi.der@man.net",
            "sapiderman"
        )
    }

    fun generateDummyRegisterResponse(): RegisterResponse {
        return  RegisterResponse(
            false,
            "User created"
        )
    }

    fun generateDummyLoginRequest(): LoginRequest {
        return LoginRequest(
            "karep.mu@lah.net",
            "karepmudewe"
        )
    }

    fun generateDummyLoginResponse(): LoginResponse {
        return LoginResponse(
            false,
            "success",
            LoginResponse.User(
                "user-aedshsrnwyk",
                "name blablabla",
                "token arslhgknboqpring"
            )
        )
    }

    fun generateDummyStoryResponse(): List<StoryResponse> {
        val items: MutableList<StoryResponse> = arrayListOf()
        for (i in 0..100) {
            val story = StoryResponse(
                i.toString(),
                "name + $i",
                "description $i",
                "photoUrl https://rndkitchen.com/$i",
                40.7434,
                74.0080,
                "createdAt $i"
            )
            items.add(story)
        }
        return items
    }

    fun generateDummyStoriesEntity(): List<StoriesEntity> {
        val storiesList = ArrayList<StoriesEntity>()

        for (i in 0..10) {
            val stories = StoriesEntity(
                "id-$i",
                "I Wayan Kemana Aja",
                "Ini adalah keyboard",
                "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/commons/feature-1-kurikulum-global-3.png",
                40.7434,
                74.0080,
                "2011-01-11T11:11:11Z"
            )
            storiesList.add(stories)
        }
        return storiesList
    }
}