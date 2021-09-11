package com.example.myapplication.data

import com.example.myapplication.data.models.PushNotification
import com.example.myapplication.data.util.Constants.CONTENT_TYPE
import com.example.myapplication.data.util.Constants.SERVER_KEY
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationApi {

    @Headers("Authorization: key=$SERVER_KEY","Content-Type:$CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postNotification(
        @Body pushNotification: PushNotification
    ): Response<ResponseBody>
}