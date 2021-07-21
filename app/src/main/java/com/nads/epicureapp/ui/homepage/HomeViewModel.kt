package com.nads.epicureapp.ui.homepage

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Base64
import android.util.Log
import androidx.core.graphics.drawable.toBitmap
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.bumptech.glide.Glide
import com.nads.epicureapp.R
import com.nads.epicureapp.model.apiservice.datamodels.AddRecipeModel
import com.nads.epicureapp.model.apiservice.datamodels.LoggedUser
import com.nads.epicureapp.model.apiservice.datamodels.ResultModel
import com.nads.epicureapp.model.repositories.EpicureRepository
import com.nads.epicureapp.ui.homepage.model.*
import com.nads.epicureapp.ui.homepage.profiledir.models.ResultWFoodId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class HomeViewModel @ViewModelInject constructor(private val epicureRepository: EpicureRepository,
                                                  @Assisted private val savedStateHandle: SavedStateHandle
): ViewModel() {
    lateinit var epiccorouselusername:String
    var epicFoodDetails = MutableLiveData<EpicFoodDetails>()
    var epicsubmitrating = MutableLiveData<ResultModel>()
    var epicsubmitlike = MutableLiveData<ResultModel>()
    var epicliketruefalse = MutableLiveData<Boolean>()
    var epicuretopcooks = MutableLiveData<TopCooks>()
    var epicureserchcooks = MutableLiveData<Boolean>()
    var epicureserchfoods = MutableLiveData<Boolean>()
    var epicuredelrecipe = MutableLiveData<ResultWFoodId>()
    var epicureeditrecipe = MutableLiveData<ResultWFoodId>()
    var epicuregetdataupdatingprofile = MutableLiveData<ProfilUpdate>()
    var epicuresubmitreciperesult = MutableLiveData<AddRecipeModel?>()
    var epicureupdateprofile = MutableLiveData<ResultModel>()
    val flower = Pager(PagingConfig(pageSize = 2, enablePlaceholders = false,prefetchDistance = 20))
    { TopVegFoodSource(epicureRepository) }.flow.cachedIn(viewModelScope)
    val flower2 = Pager(PagingConfig(pageSize = 2, enablePlaceholders = false,prefetchDistance = 20))
    { TopFoodSource(epicureRepository) }.flow.cachedIn(viewModelScope)

    fun submitrating(rating: Float,username:String,foodid: String){
        viewModelScope.launch {
           epicsubmitrating.value = epicureRepository.submitratings(rating,  username, foodid)
        }
    }

    fun submitlike(like:Int,username:String,foodid: String){
        viewModelScope.launch {
            epicsubmitlike.value = epicureRepository.submitlike(like,username, foodid)
        }
    }


    fun getmyrecipeforlist(username: String): Flow<PagingData<DatavalueX>> {
        return Pager(
            config = PagingConfig(
                pageSize = 2,
                enablePlaceholders = false,
                prefetchDistance = 20
            ),
            pagingSourceFactory = { YourRecipeSource(epicureRepository, username) }
        ).flow.cachedIn(viewModelScope)
    }

    init{
        Log.i("HomeModelCreated", "HomeViewModel created!")
    }

    fun getcooksSearchResult(query: String?): Flow<PagingData<DatavalueXXX>> {
        return Pager(
            config = PagingConfig(
                pageSize = 2,
                enablePlaceholders = false,
                prefetchDistance = 20
            ),
            pagingSourceFactory = { SerchCooksDataSource(epicureRepository, query) }
        ).flow.cachedIn(viewModelScope)
    }
    fun getcooksfoodlist():Flow<PagingData<DatavalueXX>>{
        return Pager(
            config = PagingConfig(
                pageSize = 5,
                enablePlaceholders = false,
                prefetchDistance = 20
            ),
            pagingSourceFactory = { FoodListOfCooks(epicureRepository, epiccorouselusername) }
        ).flow.cachedIn(viewModelScope)

    }
    fun delrecipe(category: String?,username: String?,foodid:String?){
       viewModelScope.launch {
      epicuredelrecipe.value = epicureRepository.deleterecipe(category, username, foodid)

       }


   }

    fun getfoodsSearchResult(query: String?): Flow<PagingData<DatavalueXX>> {
        return Pager(
            config = PagingConfig(
                pageSize = 4,
                enablePlaceholders = false,
                prefetchDistance = 20
            ),
            pagingSourceFactory = { SerchFoodsDataSource(epicureRepository, query) }
        ).flow.cachedIn(viewModelScope)
    }
    fun gotoserchfoods(){
         viewModelScope.launch{
             epicureserchfoods.value = true
         }
     }



    fun getdataforupdatingprofile(username: String?){

        viewModelScope.launch{
            epicuregetdataupdatingprofile.value = epicureRepository.getdataforupdatingprofile(username)
        }

    }
    fun updatingprofile(emailid:String
                        ,username:String
                        ,name:String?,
                        phone:String?,
                        locations:String?,aboutyou:String?
                        ,profilepic:Drawable?,facebookid:String?
                        ,instaid:String?){
viewModelScope.launch {
    val bitmap = profilepic?.toBitmap()
    val bytearrystream = ByteArrayOutputStream()
    bitmap?.compress(Bitmap.CompressFormat.PNG, 100, bytearrystream)
    val bb = bytearrystream.toByteArray()
    val proimage = Base64.encodeToString(bb, 0)
    epicureupdateprofile.value = epicureRepository.updatingprofile(emailid,username, name, phone,locations,
        aboutyou, proimage, facebookid, instaid)}

    }

    fun submitvaluesforrecipes(username:String,foodtitle:String,category:String,image:Drawable,description:String){

         viewModelScope.launch {
             val bitmap = image.toBitmap()
             val bytearrystream = ByteArrayOutputStream()
             bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytearrystream)
             val bb = bytearrystream.toByteArray()
             val proimage = Base64.encodeToString(bb, 0)
             Log.e("ErrorIMage",proimage)
                epicuresubmitreciperesult.value = epicureRepository.addrecipesrep(
                    username,foodtitle,category,proimage,description)
         }
     }

    fun gotoserchcooks(){
        viewModelScope.launch{
            epicureserchcooks.value = true
        }
    }
    fun topcooks(){
        viewModelScope.launch{
            epicuretopcooks.value = epicureRepository.topcooks()
        }
    }

    fun getfooddetails(username:String,foodid:String,currentusername:String){
        viewModelScope.launch{
        epicFoodDetails.value = epicureRepository.getfooddetails(username,foodid,currentusername)
        }
    }

    fun updaterecipe(username: String,foodtitle: String
                     ,category: String,image: Drawable,
                     ingredients: String,
                     description: String,
                     foodid: String,newcategory: String
                     ,likes: String,rating:String){
        viewModelScope.launch {
            val bitmap = image.toBitmap()
            val bytearrystream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytearrystream)
            val bb = bytearrystream.toByteArray()
            val proimage = Base64.encodeToString(bb, 0)
            epicureeditrecipe.value = epicureRepository.updaterecipe(username,foodtitle,
            category, proimage,ingredients,
            description, foodid, newcategory, likes, rating)}
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("HomeViewModel", "HomeViewModel destroyed!")
    }
}
