package br.com.joaogalli.flickrimages.ui.imagesearch

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import br.com.joaogalli.flickrimages.MainNavRoute
import br.com.joaogalli.flickrimages.R
import br.com.joaogalli.flickrimages.domain.model.FlickrImage
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.delay

@Composable
fun ImageSearchScreenComposable(
    viewModel: ImageSearchViewModel,
    onImageSelected: (flickrImage: FlickrImage) -> Unit,
) {

    Column(
        Modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.screen_padding))
    ) {
        val state = viewModel.getStateCompose()

        SearchBar(enableSearch = !state.isLoading) { query ->
            viewModel.searchImages(query)
        }
        Spacer(modifier = Modifier.height(8.dp))

        Box(modifier = Modifier.weight(1f)) {

            when {
                state.isLoading -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = stringResource(R.string.imageSearch_label_loading_please_wait))
                    }
                }

                state.genericErrorOccurred -> {
                    Text(text = stringResource(R.string.imageSearch_something_wrong_happened))
                }

                state.images.isNotEmpty() -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.images) { flickrImage ->
                            OutlinedCard(
                                onClick = {
                                    onImageSelected(flickrImage)
                                }
                            ) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(flickrImage.imageUrl)
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = flickrImage.title,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxWidth(),
                                )
                            }
                        }
                    }
                }

                else -> {
                    EmptySearchResultComposable()
                }
            }
        }
    }
}

@Composable
private fun SearchBar(
    enableSearch: Boolean,
    onSearch: (query: String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val searchValue = remember {
        mutableStateOf("")
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = searchValue.value,
            onValueChange = {
                searchValue.value = it
            },
            modifier = Modifier.weight(1f),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    keyboardController?.hide()
                }
            ),
        )

        LaunchedEffect(searchValue.value) {
            // Debounce
            delay(300)
            onSearch(searchValue.value)
        }

        Spacer(modifier = Modifier.width(4.dp))
        IconButton(enabled = enableSearch,
            onClick = {
                onSearch(searchValue.value)
                keyboardController?.hide()
            }) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = stringResource(R.string.imageSearch_click_to_search_images_on_flickr),
                modifier = Modifier.size(40.dp)
            )
        }
    }
}

@Composable
private fun EmptySearchResultComposable() {
    Text(text = stringResource(R.string.imageSearch_emptyMessage))
}