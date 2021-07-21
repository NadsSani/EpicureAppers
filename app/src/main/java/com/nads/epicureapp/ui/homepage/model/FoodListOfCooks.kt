package com.nads.epicureapp.ui.homepage.model

import androidx.paging.PagingSource
import com.nads.epicureapp.model.repositories.EpicureRepository

class FoodListOfCooks(
    val epicureRepository: EpicureRepository,val username:String?
) : PagingSource<Int, DatavalueXX>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult.Page<Int, DatavalueXX>
    {
        try {
            // Start refresh at page 1 if undefined.
            val nextPageNumber = params.key ?: 1

            val respons = epicureRepository.getcooksfoodlist(username,nextPageNumber)

            val responseData = mutableListOf<DatavalueXX>()
            val data = respons.datavalues
            responseData.addAll(data)
            return LoadResult.Page(
                data = responseData,
                prevKey = null, // Only paging forward.
                nextKey = nextPageNumber.plus(1)
            )
        } catch (e: Exception) {
            return LoadResult.Page(
                data = emptyList(),
                prevKey = null, // Only paging forward.
                nextKey = null
            )
            // Handle errors in this block and return LoadResult.Error if it is an
            // expected error (such as a network failure).
        }
    }









}