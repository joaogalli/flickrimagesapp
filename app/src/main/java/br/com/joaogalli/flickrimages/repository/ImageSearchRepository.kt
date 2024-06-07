package br.com.joaogalli.flickrimages.repository

import br.com.joaogalli.flickrimages.datasource.ImageSearchDataSource
import br.com.joaogalli.flickrimages.domain.model.FlickrImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import javax.inject.Inject

class ImageSearchRepository @Inject constructor(
    private val imageSearchDataSource: ImageSearchDataSource,
) {

    suspend fun search(query: String): List<FlickrImage> = withContext(Dispatchers.IO) {
        val result = imageSearchDataSource.searchImage(query)

        if (result.isSuccess) {
            val imageSearchResponse = result.getOrThrow()

            imageSearchResponse.items.map {

                val publishedDate = LocalDateTime
                    .parse(it.published, DateTimeFormatter.ISO_ZONED_DATE_TIME)
                    .format(
                        DateTimeFormatter.ofLocalizedDateTime(
                            FormatStyle.LONG,
                            FormatStyle.SHORT
                        )
                    )

                FlickrImage(
                    imageUrl = it.media.m,
                    title = it.title,
                    description = it.description,
                    author = it.author,
                    publishedDate = publishedDate,
                )
            }
        } else {
            throw result.exceptionOrNull() ?: RuntimeException()
        }
    }
}
