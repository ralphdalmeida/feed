package engassignment.figure1.com.feedof500px

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import engassignment.figure1.com.feedof500px.adapters.SimpleItemRecyclerViewAdapter
import engassignment.figure1.com.feedof500px.utilities.AppUtils
import engassignment.figure1.com.feedof500px.utilities.FeedScrollListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_photo_list.*
import kotlinx.android.synthetic.main.photo_list.*

/**
 * An activity representing a list of Photos. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of photos, which when touched,
 * lead to a [PhotoDetailActivity] representing
 * photo details. On tablets, the activity presents the list of photos and
 * photo details side-by-side using two vertical panes.
 */
class PhotoListActivity : AppCompatActivity() {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var mTwoPane: Boolean = false
    private lateinit var mDisposable: Disposable
    private var mIsLastPage: Boolean = false
    private var mIsLoading: Boolean = false
    private var mTotalPages: Int = 0
    private var mCurrentPage: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_list)

        setSupportActionBar(toolbar)
        toolbar.title = title

        if (photo_detail_container != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true
        }

        val mAdapter = SimpleItemRecyclerViewAdapter(supportFragmentManager, mTwoPane)

        val mLinearLayoutManager = LinearLayoutManager(this@PhotoListActivity, LinearLayoutManager.VERTICAL, false)
        photo_list.layoutManager = mLinearLayoutManager
        photo_list.itemAnimator = DefaultItemAnimator()
        photo_list.adapter = mAdapter
        photo_list.addOnScrollListener(object: FeedScrollListener(mLinearLayoutManager) {
            override fun loadMorePhotos() {
                mIsLoading = true
                mCurrentPage += 1
                loadNextFeed(mAdapter)
            }

            override fun totalPagesCount(): Int {
                return mTotalPages
            }

            override fun isLastPage(): Boolean {
                return mIsLastPage
            }

            override fun isLoading(): Boolean {
                return mIsLoading
            }
        })

        loadInitialFeed(progressbar, mAdapter)
    }

    private fun loadInitialFeed(progressBar: ProgressBar, adapter: SimpleItemRecyclerViewAdapter) {
        mDisposable = AppUtils.FIVE_HUNDRED_API_SERVICE.getFeed(AppUtils.NET_PARAM_FEATURE_VALUE, AppUtils.NET_PARAM_IMAGE_SIZE_VALUE, AppUtils.NET_PARAM_INCLUDE_STORE_VALUE, mCurrentPage, AppUtils.NET_PARAM_CONSUMER_KEY_VALUE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                            feed ->  run {
                                mTotalPages = feed.total_pages
                                mCurrentPage = feed.current_page
                                progressBar.visibility = View.GONE
                                adapter.addPhotos(feed.photos)

                                if (mCurrentPage <= mTotalPages) adapter.addLast() else mIsLastPage = true
                            }
                        }, { error -> Toast.makeText(this, error.message, Toast.LENGTH_LONG).show() })

    }

    private fun loadNextFeed(adapter: SimpleItemRecyclerViewAdapter) {
        mDisposable = AppUtils.FIVE_HUNDRED_API_SERVICE.getFeed(AppUtils.NET_PARAM_FEATURE_VALUE, AppUtils.NET_PARAM_IMAGE_SIZE_VALUE, AppUtils.NET_PARAM_INCLUDE_STORE_VALUE, mCurrentPage, AppUtils.NET_PARAM_CONSUMER_KEY_VALUE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    feed ->  run {
                    adapter.removeLast()
                    mIsLoading = false
                    adapter.addPhotos(feed.photos)
                    Toast.makeText(this, getString(R.string.feed_page_number).plus(feed.current_page.toString()), Toast.LENGTH_LONG).show()

                    if (mCurrentPage != mTotalPages) adapter.addLast() else mIsLastPage = true
                }
                }, { error -> Toast.makeText(this, error.message, Toast.LENGTH_LONG).show() })

    }

    override fun onDestroy() {
        super.onDestroy()
        mDisposable.dispose()
    }

}
