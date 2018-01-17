package engassignment.figure1.com.feedof500px.utilities

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

/**
 * Created by Ralph on 2018-01-16.
 *
 */
abstract class FeedScrollListener(private val layoutManager: LinearLayoutManager) : RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val mInitialVisiblePhotoCount: Int = layoutManager.findFirstVisibleItemPosition()

        if (!isLoading() && !isLastPage()) {
            if ((layoutManager.childCount + mInitialVisiblePhotoCount) >= layoutManager.itemCount && mInitialVisiblePhotoCount >= 0) {
                loadMorePhotos()
            }
        }
    }

    protected abstract fun loadMorePhotos()

    abstract fun totalPagesCount(): Int

    abstract fun isLastPage(): Boolean

    abstract fun isLoading(): Boolean
}