package engassignment.figure1.com.feedof500px

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import engassignment.figure1.com.feedof500px.models.Model
import engassignment.figure1.com.feedof500px.utilities.AppUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_photo_detail.*
import kotlinx.android.synthetic.main.photo_detail.view.*

/**
 * A fragment representing a single Photo detail screen.
 * This fragment is either contained in a [PhotoListActivity]
 * in two-pane mode (on tablets) or a [PhotoDetailActivity]
 * on handsets.
 */
class PhotoDetailFragment : Fragment() {

    /**
     * The photo this fragment is presenting.
     */
    private lateinit var mPhoto: Model.Photo
    private lateinit var mPhotoDisposable: Disposable
    private lateinit var mImageView: ImageView
    private lateinit var mDescTextView: TextView
    private lateinit var mLocTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(AppUtils.ARG_PHOTO_ID)) {
                // Load the photo specified by the fragment.
                mPhotoDisposable = AppUtils.FIVE_HUNDRED_API_SERVICE.getPhoto(it.getLong(AppUtils.ARG_PHOTO_ID), AppUtils.NET_PARAM_IMAGE_SIZE_PHOTO_VALUE, AppUtils.NET_PARAM_CONSUMER_KEY_VALUE)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                    detail ->  run {
                                    mPhoto = detail.photo
                                    activity?.toolbar_layout?.title = mPhoto.name
                                    // Update the view to show the photo detail.
                                    updateView()
                                }
                            }, { error -> Toast.makeText(activity, error.message, Toast.LENGTH_LONG).show() })
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.photo_detail, container, false)

        mImageView = rootView.photo_detail_image
        mDescTextView = rootView.photo_detail_description
        mLocTextView = rootView.photo_detail_location

        return rootView
    }

    override fun onDestroy() {
        super.onDestroy()
        mPhotoDisposable.dispose()
    }

    private fun updateView() {
        mPhoto.let {
            Glide.with(context!!).load(it.images[0].https_url).into(mImageView)
            mDescTextView.text = it.description
            mLocTextView.text = it.location
        }
    }

}
