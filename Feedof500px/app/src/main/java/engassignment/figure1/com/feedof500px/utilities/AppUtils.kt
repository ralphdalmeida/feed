package engassignment.figure1.com.feedof500px.utilities

import engassignment.figure1.com.feedof500px.services.FiveHundredApiService

/**
 * Created by Ralph on 2018-01-16.
 *
 */
class AppUtils {

    companion object {
        val FIVE_HUNDRED_API_SERVICE by lazy {
            FiveHundredApiService.create()
        }
        const val NET_GET_FEED_LINK: String = "photos"
        const val NET_GET_PHOTO_LINK: String = "photos/{id}"
        const val NET_PARAM_FEATURE: String = "feature"
        const val NET_PARAM_IMAGE_SIZE: String = "image_size"
        const val NET_PARAM_PAGE: String = "page"
        const val NET_PARAM_INCLUDE_STORE: String = "include_store"
        const val NET_PARAM_CONSUMER_KEY: String = "consumer_key"
        const val NET_BASE_URL: String = "https://api.500px.com/v1/"
        const val NET_PARAM_FEATURE_VALUE: String = "fresh_today"
        const val NET_PARAM_IMAGE_SIZE_VALUE: Int = 2
        const val NET_PARAM_IMAGE_SIZE_PHOTO_VALUE: Int = 440
        const val NET_PARAM_INCLUDE_STORE_VALUE: String = "store_download"
        const val NET_PARAM_CONSUMER_KEY_VALUE: String = "AJHjroGYgXAl3OabA1SjMw180lakiWUOIyJBIodC"
        const val ARG_PHOTO_ID = "photo_id"
        const val LOADING_PHOTO: Int = 0
        const val LOADING_PROGRESS: Int = 1
    }

}