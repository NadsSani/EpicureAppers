package com.nads.epicureapp.model.apiservice
/*
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception




class Epicureclient {
    //val gson:GsonBuilder= GsonBuilder().registerTypeAdapter()
    val client:OkHttpClient = OkHttpClient.Builder()
    .addInterceptor(Interceptor.invoke { chain: Interceptor.Chain ->
        val request:Request = chain.request()
            request.newBuilder().header("key","GHYT 85KJ 74YU RTYU").method(request.method,request.body)
                .build()

        val response: Response = chain.proceed(request)
        return@invoke response
    }).build()


    var retrofit = Retrofit.Builder()
        .baseUrl("http://epicureapp.starwingsindia.com/EpicureApp/epicure/public/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    var service: Epicureservices = retrofit.create(Epicureservices::class.java)



}
*/