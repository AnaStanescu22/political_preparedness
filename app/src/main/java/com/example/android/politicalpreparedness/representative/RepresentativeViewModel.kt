package com.example.android.politicalpreparedness.representative

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.model.Representative
import com.example.android.politicalpreparedness.utils.CivicsApiStatus
import kotlinx.coroutines.launch
import timber.log.Timber

class RepresentativeViewModel : ViewModel() {

    val apiService = CivicsApi.retrofitService

    private val _apiStatus: MutableLiveData<CivicsApiStatus> = MutableLiveData()
    val apiStatus: LiveData<CivicsApiStatus>
        get() = _apiStatus

    private val _representatives = MutableLiveData<List<Representative>>()
    val representatives: LiveData<List<Representative>>
        get() = _representatives

    private val _address = MutableLiveData<Address>()
    val address: LiveData<Address>
        get() = _address

    init {
        _address.value = Address("", null, "", "", "")
    }

    fun getRepresentativesList(address: Address?) {
        _apiStatus.value = CivicsApiStatus.LOADING

        viewModelScope.launch {
            _representatives.value = arrayListOf()
            if (address != null) {
                try {
                    _address.value = address
                    val (offices, officials) = apiService.getRepresentatives(_address.value?.toFormattedString()!!)
                    _representatives.value =
                        offices.flatMap { office -> office.getRepresentatives(officials) }
                    _apiStatus.value = CivicsApiStatus.DONE
                } catch (e: Exception) {
                    Timber.e(
                        "Error: %s", e.localizedMessage)
                    _apiStatus.value = CivicsApiStatus.ERROR
                }
            }
        }
    }

    fun getRepresentativesList() {
        Timber.d("address: %s", _address.value)
        getRepresentativesList(_address.value)
    }
}
