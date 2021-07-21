package com.nads.epicureapp.splashscreen

import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*

import com.nads.epicureapp.model.apiservice.datamodels.Datas
import com.nads.epicureapp.model.apiservice.datamodels.LoggedUser
import com.nads.epicureapp.model.apiservice.datamodels.ResultModel
import com.nads.epicureapp.model.apiservice.datamodels.SplashData
import com.nads.epicureapp.model.repositories.EpicureRepository
import kotlinx.coroutines.launch

class SplashViewModel @ViewModelInject constructor( private val epicureRepository: EpicureRepository,
                                                    @Assisted private val savedStateHandle: SavedStateHandle
): ViewModel() {


    var epicurelogins = MutableLiveData<LoggedUser>()
    var checkingdata = MutableLiveData<ResultModel>()
    init{
        Log.i("SplashViewModel", "SplashViewModel created!")


    }
              val epicuresplashvid: LiveData<SplashData> = epicureRepository.epicuresplashvid

    fun getloginuser(username:String, password:String) {
        viewModelScope.launch{
            val data = epicureRepository.logier(username, password)
            epicurelogins.value = data
        }
    }
   fun checkversion(versioncode:Int){
       viewModelScope.launch {
            checkingdata.value = epicureRepository.checkingversion(versioncode)
       }


   }
    override fun onCleared() {
        super.onCleared()
        Log.i("GameViewModel", "GameViewModel destroyed!")
    }

}