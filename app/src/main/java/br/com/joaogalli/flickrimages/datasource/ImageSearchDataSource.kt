package br.com.joaogalli.flickrimages.datasource

import br.com.joaogalli.flickrimages.data.remote.FlickrApi
import br.com.joaogalli.flickrimages.data.remote.model.ImageSearchResponse
import javax.inject.Inject

class ImageSearchDataSource @Inject constructor(
    private val flickrApi: FlickrApi
) {

    suspend fun searchImage(query: String): Result<ImageSearchResponse> {
        val response = flickrApi.searchImage(query = query)

        return if (response.isSuccessful && response.body() != null) {
            Result.success(response.body()!!)
        } else {
            // Generic error passed on
            Result.failure(RuntimeException())
        }
    }

}