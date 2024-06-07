package br.com.joaogalli.flickrimages.repository

import br.com.joaogalli.flickrimages.data.remote.model.ImageSearchResponse
import br.com.joaogalli.flickrimages.data.remote.model.Item
import br.com.joaogalli.flickrimages.data.remote.model.Media
import br.com.joaogalli.flickrimages.datasource.ImageSearchDataSource
import br.com.joaogalli.flickrimages.domain.model.FlickrImage
import br.com.joaogalli.flickrimages.testing.MainDispatcherRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertFailsWith

class ImageSearchRepositoryTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK(relaxed = true)
    lateinit var imageSearchDataSource: ImageSearchDataSource

    private lateinit var imageSearchRepository: ImageSearchRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        imageSearchRepository = ImageSearchRepository(imageSearchDataSource)
    }

    @Test
    fun `search with success`() = runTest {
        val query = "query string"
        val returnList = listOf(
            FlickrImage(
                "imageUrl",
                "title",
                "description",
                "author",
                "publishedDate",
            )
        )

        // Arrange
        coEvery {
            imageSearchDataSource.searchImage(query)
        } returns Result.success(
            ImageSearchResponse(
                title = "",
                description = "",
                modified = "",
                generator = "",
                items = listOf(
                    Item(
                        title = "title",
                        description = "description",
                        media = Media(m = "imageUrl"),
                        link = "",
                        dateTaken = "",
                        published = "2024-05-12T20:04:10-08:00",
                        author = "author",
                        authorId = "",
                        tags = ""
                    ),
                )
            )
        )

        // Act
        val flickrImagesList = imageSearchRepository.search(query)

        // Assert
        Assert.assertEquals(1, flickrImagesList.size)
        val flickrImage1 = flickrImagesList[0]
        Assert.assertEquals("title", flickrImage1.title)
        Assert.assertEquals("description", flickrImage1.description)
        Assert.assertEquals("author", flickrImage1.author)
        Assert.assertEquals("imageUrl", flickrImage1.imageUrl)
        Assert.assertEquals("May 12, 2024, 8:04 PM", flickrImage1.publishedDate)
    }

    @Test
    fun `search with exception`() = runTest {
        val query = "query string"

        // Arrange
        coEvery {
            imageSearchDataSource.searchImage(query)
        } returns Result.failure(RuntimeException())

        // Act
        // Assert
        assertFailsWith<RuntimeException> {
            imageSearchRepository.search(query)
        }
    }
}