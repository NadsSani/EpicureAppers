package com.nads.epicureapp.ui.homepage.model

import androidx.paging.PagingSource
import com.nads.epicureapp.model.repositories.EpicureRepository

class YourRecipeSource(
val epicureRepository: EpicureRepository,val username:String
) : PagingSource<Int, DatavalueX>() {


    override fun invalidate() {
        super.invalidate()
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DatavalueX> {
        try {
            // Start refresh at page 1 if undefined.
            val nextPageNumber = params.key ?: 1

            val respons = epicureRepository.getmyrecipe(username,nextPageNumber)

            val responseData = mutableListOf<DatavalueX>()
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
        }
    }


}