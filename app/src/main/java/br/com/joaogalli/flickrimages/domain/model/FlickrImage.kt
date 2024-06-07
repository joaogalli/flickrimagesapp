package br.com.joaogalli.flickrimages.domain.model

data class FlickrImage(
    val imageUrl: String,
    val title: String,
    val description: String,
    val author: String,
    val publishedDate: String,
) {
    fun getNavigationDetailsUrl(): String {
        return "$imageUrl/$title/$description/$author/$publishedDate"
    }
}
