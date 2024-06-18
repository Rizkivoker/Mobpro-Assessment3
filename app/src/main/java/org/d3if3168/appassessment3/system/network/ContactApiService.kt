package org.d3if3168.appassessment3.system.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.d3if3168.appassessment3.system.database.model.Contact
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query


private const val BASE_URL = "https://rizkivokers.000webhostapp.com/files/"

// Define Moshi object for JSON conversion
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

// Define Retrofit object
private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()

data class ContactResponse(
    val results: List<Contact>
)

interface ContactAPIService {
    @GET("get_data.php")
    suspend fun getAllContacts(): ContactResponse

    @Multipart
    @POST("add_contact.php")
    suspend fun addContact(
        @Part("email") email: RequestBody,
        @Part("contactName") contactName: RequestBody,
        @Part("contactPhone") contactPhone: RequestBody,
        @Part("contactEmail") contactEmail: RequestBody,
        @Part("contactBirthday") contactBirthday: RequestBody,
        @Part contactImageUrl: MultipartBody.Part?
    ): ContactResponse

    @FormUrlEncoded
    @POST("delete_contact.php")
    suspend fun deleteContact(
        @Field("id") id: String
    ): ContactResponse
}


object ContactAPI {
    val retrofitService: ContactAPIService by lazy {
        retrofit.create(ContactAPIService::class.java)
    }

    fun imgUrl(imageId: String): String {
        return "$BASE_URL$imageId"
    }
}

enum class ContactStatus { LOADING, SUCCESS, FAILED }