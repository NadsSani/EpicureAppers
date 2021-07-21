package com.nads.epicureapp.ui.homepage.model

import android.text.TextUtils.indexOf
import android.util.Log
import androidx.lifecycle.liveData
import androidx.paging.PagingSource
import com.nads.epicureapp.model.repositories.EpicureRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TopFoodSource(
    val epicureRepository: EpicureRepository
) : PagingSource<Int, DatavalueX>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult.Page<Int, DatavalueX> {
        try {
            // Start refresh at page 1 if undefined.
            val nextPageNumber = params.key ?: 1

            val respons = epicureRepository.getfoods(nextPageNumber)

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
            // Handle errors in this block and return LoadResult.Error if it is an
            // expected error (such as a network failure).
        }
    }
}

