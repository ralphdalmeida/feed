package engassignment.figure1.com.feedof500px.services

import engassignment.figure1.com.feedof500px.models.Model
import engassignment.figure1.com.feedof500px.utilities.AppUtils
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


/**
 * Created by Ralph on 2018-01-16.
 *
 */
interface FiveHundredApiService {

    @GET(AppUtils.NET_GET_FEED_LINK)
    fun getFeed(@Query(AppUtils.NET_PARAM_FEATURE) feature: String, @Query(AppUtils.NET_PARAM_IMAGE_SIZE) image_size: Int,
                @Query(AppUtils.NET_PARAM_INCLUDE_STORE) include_store: String, @Query(AppUtils.NET_PARAM_PAGE) page: Int,
                @Query(AppUtils.NET_PARAM_CONSUMER_KEY) consumer_key: String): Observable<Model.Feed>

    @GET(AppUtils.NET_GET_PHOTO_LINK)
    fun getPhoto(@Path("id") id: Long, @Query(AppUtils.NET_PARAM_IMAGE_SIZE) image_size: Int,
                 @Query(AppUtils.NET_PARAM_CONSUMER_KEY) consumer_key: String): Observable<Model.FeedDetail>

    companion object {
        fun create(): FiveHundredApiService {
            val retrofit = Retrofit.Builder().addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(AppUtils.NET_BASE_URL)
                    .build()
            return retrofit.create(FiveHundredApiService::class.java)
        }
    }
}