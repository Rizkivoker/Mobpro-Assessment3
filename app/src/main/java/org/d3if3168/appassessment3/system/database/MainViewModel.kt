package org.d3if3168.appassessment3.system.database

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.d3if3168.appassessment3.system.database.model.Contact
import org.d3if3168.appassessment3.system.network.ContactAPI
import org.d3if3168.appassessment3.system.network.ContactStatus
import java.io.ByteArrayOutputStream

class MainViewModel : ViewModel() {
    private val _contactData = MutableLiveData<List<Contact>>()
    val contactData: LiveData<List<Contact>> get() = _contactData


    var _status = MutableStateFlow(ContactStatus.LOADING)
        private set

    init {
        getAllContacts()
    }

    fun getAllContacts() {
        viewModelScope.launch {
            _status.value = ContactStatus.LOADING
            try {
                val response = ContactAPI.retrofitService.getAllContacts()
                _contactData.value = response.results
                _status.value = ContactStatus.SUCCESS
                Log.d("MainViewModel", "Success fetching contacts: ${response.results}")
            } catch (e: Exception) {
                _status.value = ContactStatus.FAILED
                Log.e("MainViewModel", "Error fetching contacts: ${e.message}")
            }
        }
    }

    fun addContact(email: String, contactName: String, contactPhone: String, contactEmail: String, contactBirthday: String, contactImageUrl: Bitmap?) {
        viewModelScope.launch {
            _status.value = ContactStatus.LOADING
            try {
                val emailPart = email.toRequestBody("text/plain".toMediaTypeOrNull())
                val contactNamePart = contactName.toRequestBody("text/plain".toMediaTypeOrNull())
                val contactPhonePart = contactPhone.toRequestBody("text/plain".toMediaTypeOrNull())
                val contactEmailPart = contactEmail.toRequestBody("text/plain".toMediaTypeOrNull())
                val contactBirthdayPart = contactBirthday.toRequestBody("text/plain".toMediaTypeOrNull())

                val contactImagePart: MultipartBody.Part? = contactImageUrl?.let {
                    val byteArrayOutputStream = ByteArrayOutputStream()
                    it.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
                    val requestBody = byteArrayOutputStream.toByteArray().toRequestBody("image/jpeg".toMediaTypeOrNull())
                    MultipartBody.Part.createFormData("contactImageUrl", "contact.jpg", requestBody)
                }

                val response = ContactAPI.retrofitService.addContact(emailPart, contactNamePart, contactPhonePart, contactEmailPart, contactBirthdayPart, contactImagePart)
                _contactData.value = response.results
                _status.value = ContactStatus.SUCCESS
                getAllContacts()
            } catch (e: Exception) {
                _status.value = ContactStatus.FAILED
                Log.e("MainViewModel", "Error adding contact: ${e.message}")
            }
        }
    }

    fun deleteContact(id: String) {
        viewModelScope.launch {
            _status.value = ContactStatus.LOADING
            try {
                val response = ContactAPI.retrofitService.deleteContact(id)
                _contactData.value = response.results
                _status.value = ContactStatus.SUCCESS
                getAllContacts()
            } catch (e: Exception) {
                _status.value = ContactStatus.FAILED
                Log.e("MainViewModel", "Error deleting contact: ${e.message}")
            }
        }
    }
}
