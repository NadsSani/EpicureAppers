package com.nads.epicureapp.di

import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import com.nads.epicureapp.model.apiservice.Epicureservices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.IOException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object Epicureservicemodule {

    @Singleton
    @Provides
    fun providesOkHttpClient(): OkHttpClient {

        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(Interceptor.invoke { chain: Interceptor.Chain ->
                val orginal: Request = chain.request()
                val request = orginal.newBuilder().header("key", "GHYT 85KJ 74YU RTYU").method(
                    orginal.method,
                    orginal.body
                )
                    .build()

                chain.proceed(request)

            }).build()


        return client
    }
    @Singleton
    @Provides
    fun providesRetrofitClient(): Retrofit {
        val gson = GsonBuilder()
            .registerTypeAdapter(Int::class.javaPrimitiveType, IntTypeAdapter())
            .registerTypeAdapter(Int::class.java, IntTypeAdapter()).create()
        return Retrofit.Builder()
            .baseUrl("https://www.epicureapp.xyz/EpicureApp/epicure/public/index.php/")
            .client(providesOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    @Singleton
    @Provides
    fun provideapiservice(retrofit: Retrofit):Epicureservices{
        var service: Epicureservices = retrofit.create(Epicureservices::class.java)
        return service
    }


    class IntTypeAdapter : TypeAdapter<Number?>() {
        @Throws(IOException::class)
        override fun write(out: JsonWriter, value: Number?) {
            out.value(value)
        }

        @Throws(IOException::class)
        override fun read(`in`: JsonReader): Number? {
            if (`in`.peek() == JsonToken.NULL) {
                `in`.nextNull()
                return null
            }
            return try {
                val result = `in`.nextString()
                if ("" == result) {
                    null
                } else result.toInt()
            } catch (e: NumberFormatException) {
                throw JsonSyntaxException(e)
            }
        }
    }



    }