package br.com.joaogalli.flickrimages.data.remote.model

import com.squareup.moshi.Json

data class ImageSearchResponse(
    val title: String,
    val description: String,
    val modified: String,
    val generator: String,
    val items: List<Item>,
)

data class Item(
    val title: String,
    val link: String,
    val media: Media,
    @field:Json(name = "date_taken")
    val dateTaken: String,
    val description: String,
    val published: String,
    val author: String,
    @field:Json(name = "author_id")
    val authorId: String,
    val tags: String,
)

data class Media(
    val m: String,
)
