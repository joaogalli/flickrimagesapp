package br.com.joaogalli.flickrimages.data.remote

import br.com.joaogalli.flickrimages.data.remote.model.ImageSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrApi {

    @GET("services/feeds/photos_public.gne")
    suspend fun searchImage(
        @Query("format") format: String = "json",
        @Query("nojsoncallback") noJsonCallback: String = "1",
        @Query("tags") query: String,
    ): Response<ImageSearchResponse>

}