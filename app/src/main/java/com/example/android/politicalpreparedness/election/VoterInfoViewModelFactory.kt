package com.example.android.politicalpreparedness.election

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.CivicsApiService

class VoterInfoViewModelFactory(private val localDataSource: ElectionDao, private val remoteDataSource: CivicsApiService) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VoterInfoViewModel::class.java)) {
            return VoterInfoViewModel(localDataSource, remoteDataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}