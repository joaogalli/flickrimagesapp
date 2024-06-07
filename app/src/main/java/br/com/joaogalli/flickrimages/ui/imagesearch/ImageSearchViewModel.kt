package br.com.joaogalli.flickrimages.ui.imagesearch

import androidx.lifecycle.viewModelScope
import br.com.joaogalli.flickrimages.domain.model.FlickrImage
import br.com.joaogalli.flickrimages.repository.ImageSearchRepository
import br.com.joaogalli.flickrimages.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageSearchViewModel @Inject constructor(
    private val imageSearchRepository: ImageSearchRepository
) :
    BaseViewModel<ImageSearchState, ImageSearchCommand>() {

    override fun initialState() = ImageSearchState()

    fun searchImages(query: String) = viewModelScope.launch {
        newState(ImageSearchState(isLoading = true))

        try {
            if (query.isBlank()) {
                newState(ImageSearchState())
            } else {
                val list = imageSearchRepository.search(query)

                newState(
                    ImageSearchState(images = list)
                )
            }
        } catch (t: Throwable) {
            newState(ImageSearchState(genericErrorOccurred = true))
        }
    }

}

data class ImageSearchState(
    val isLoading: Boolean = false,
    val genericErrorOccurred: Boolean = false,
    val images: List<FlickrImage> = emptyList()
)

sealed interface ImageSearchCommand
