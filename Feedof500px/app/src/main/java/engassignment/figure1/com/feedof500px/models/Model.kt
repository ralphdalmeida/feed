package engassignment.figure1.com.feedof500px.models

/**
 * Created by Ralph on 2018-01-16.
 *
 */
object Model {
    data class Feed(val current_page: Int, val total_pages: Int, val photos: List<Photo>)
    data class FeedDetail(val photo: Photo)
    data class Photo(val id: Long, val name: String, val description: String, val location: String, val taken_at: String, val images: List<Images>, val user: User)
    data class Images(val https_url: String)
    data class User(val id: Long, val firstname: String)
}