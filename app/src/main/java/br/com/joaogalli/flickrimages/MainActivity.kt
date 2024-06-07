package br.com.joaogalli.flickrimages

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.com.joaogalli.flickrimages.domain.model.FlickrImage
import br.com.joaogalli.flickrimages.ui.imagedetails.ImageDetailsScreenComposable
import br.com.joaogalli.flickrimages.ui.imagesearch.ImageSearchScreenComposable
import br.com.joaogalli.flickrimages.ui.imagesearch.ImageSearchViewModel
import br.com.joaogalli.flickrimages.ui.theme.FlickrImagesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlickrImagesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainNavigationComposable()
                }
            }
        }
    }
}

@Composable
private fun MainNavigationComposable() {
    val navController = rememberNavController()

    val flickrImageSelected = remember {
        mutableStateOf<FlickrImage?>(null)
    }

    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = MainNavRoute.ROUTE,
        modifier = Modifier.fillMaxSize()
    ) {
        navigation(
            route = MainNavRoute.ROUTE,
            startDestination = MainNavRoute.IMAGE_SEARCH
        ) {

            composable(MainNavRoute.IMAGE_SEARCH) {
                val imageSearchViewModel: ImageSearchViewModel =
                    hiltViewModel<ImageSearchViewModel>()
                ImageSearchScreenComposable(
                    viewModel = imageSearchViewModel,
                ) {
                    flickrImageSelected.value = it
                    navController.navigate(MainNavRoute.IMAGE_DETAILS)
                }
            }

            composable(MainNavRoute.IMAGE_DETAILS) {
                flickrImageSelected.value?.let {
                    ImageDetailsScreenComposable(it, navController)
                } ?: run {
                    navController.popBackStack()
                    Toast.makeText(
                        context,
                        stringResource(R.string.general_something_went_wrong), Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}

object MainNavRoute {
    const val ROUTE = "main_route"
    const val IMAGE_SEARCH = "main_image_search"
    const val IMAGE_DETAILS = "main_image_details"
}