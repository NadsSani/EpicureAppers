package com.nads.epicureapp.ui.homepage.model

import androidx.paging.PagingSource
import com.nads.epicureapp.model.repositories.EpicureRepository

class SerchCooksDataSource(
val epicureRepository: EpicureRepository,val username :String?
) : PagingSource<Int, DatavalueXXX>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DatavalueXXX> {
        try {
            // Start refresh at page 1 if undefined.
            val nextPageNumber = params.key ?: 1

            val respons = epicureRepository.serchcooks(username,nextPageNumber)

            val responseData = mutableListOf<DatavalueXXX>()
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