package engassignment.figure1.com.feedof500px.adapters

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import engassignment.figure1.com.feedof500px.PhotoDetailActivity
import engassignment.figure1.com.feedof500px.PhotoDetailFragment
import engassignment.figure1.com.feedof500px.R
import engassignment.figure1.com.feedof500px.models.Model
import engassignment.figure1.com.feedof500px.utilities.AppUtils
import kotlinx.android.synthetic.main.photo_list_content.view.*

/**
 * Created by Ralph on 2018-01-16.
 *
 */
class SimpleItemRecyclerViewAdapter(private val supportFragmentManager: FragmentManager,
                                    private val mTwoPane: Boolean) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val mPhotos: MutableList<Model.Photo> = ArrayList()
    private val mOnClickListener: View.OnClickListener
    private var mStillLoading: Boolean = false

    init {
        mOnClickListener = View.OnClickListener { v ->
            val photo = v.tag as Model.Photo
            if (mTwoPane) {
                val fragment = PhotoDetailFragment().apply {
                    arguments = Bundle().apply {
                        putLong(AppUtils.ARG_PHOTO_ID, photo.id)
                    }
                }
                supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.photo_detail_container, fragment)
                        .commit()
            } else {
                val intent = Intent(v.context, PhotoDetailActivity::class.java).apply {
                    putExtra(AppUtils.ARG_PHOTO_ID, photo.id)
                }
                v.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.photo_list_content, parent, false)
        when(viewType) {
            AppUtils.LOADING_PHOTO -> return FeedViewHolder(view)
            AppUtils.LOADING_PROGRESS -> return ProgressBarViewHolder(view)
        }
        return FeedViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val photo = mPhotos[position]
        when(getItemViewType(position)) {
            AppUtils.LOADING_PHOTO -> {
                holder as FeedViewHolder
                if (!TextUtils.isEmpty(photo.images[0].https_url) && photo.images[0].https_url != "null")
                    Glide.with(holder.mImageView.context).load(photo.images[0].https_url).into(holder.mImageView)
                else {
                    Glide.with(holder.mImageView.context).clear(holder.mImageView)
                    holder.mImageView.setImageResource(android.R.drawable.gallery_thumb)
                }

                if (!TextUtils.isEmpty(photo.name) && photo.name != "null") {
                    holder.mNameText.visibility = View.VISIBLE
                    holder.mNameText.text = photo.name
                } else { holder.mNameText.visibility = View.GONE }

                if (!TextUtils.isEmpty(photo.description) && photo.description != "null") {
                    holder.mDescriptionText.visibility = View.VISIBLE
                    holder.mDescriptionText.text = photo.description
                } else { holder.mDescriptionText.visibility = View.GONE }

                if (!TextUtils.isEmpty(photo.location) && photo.location != "null") {
                    holder.mLocationText.visibility = View.VISIBLE
                    holder.mLocationText.text = photo.location
                } else { holder.mLocationText.visibility = View.GONE }

                if (!TextUtils.isEmpty(photo.user.firstname) && photo.user.firstname != "null") {
                    holder.mCreditText.visibility = View.VISIBLE
                    holder.mCreditText.text = (holder.mCreditText.context.getString(R.string.photo_credit)).plus(photo.user.firstname)
                } else { holder.mCreditText.visibility = View.GONE }

                with(holder.itemView) {
                    tag = photo
                    setOnClickListener(mOnClickListener)
                }
            }
            AppUtils.LOADING_PROGRESS -> { holder as ProgressBarViewHolder
            }
        }

    }

    override fun getItemCount(): Int {
        return mPhotos.size
    }

    override fun getItemViewType(position: Int): Int {
        if ((position == mPhotos.size-1) && mStillLoading) return AppUtils.LOADING_PROGRESS
        return AppUtils.LOADING_PHOTO
    }

    private fun addPhoto(photo: Model.Photo) {
        mPhotos.add(photo)
        notifyItemInserted(mPhotos.size-1)
    }

    fun addPhotos(photos: List<Model.Photo>) {
        photos.forEach { photo -> addPhoto(photo) }
    }

    private fun removePhoto(photo: Model.Photo) {
        val pos: Int = mPhotos.indexOf(photo)
        if (pos > -1) {
            mPhotos.removeAt(pos)
            notifyItemRemoved(pos)
        }
    }

    fun addLast() {
        mStillLoading = true
        addPhoto(Model.Photo(0, "null", "null", "null", "null", ArrayList(), Model.User(0, "null")))
    }

    fun removeLast() {
        mStillLoading = false
        val pos = mPhotos.size - 1
        val photo: Model.Photo = getPhoto(pos)
        photo.let {
            removePhoto(photo)
        }
    }

    private fun getPhoto(position: Int): Model.Photo {
        return mPhotos[position]
    }

    inner class FeedViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
        val mImageView: ImageView = mView.image_view
        val mNameText: TextView = mView.name_text
        val mDescriptionText: TextView = mView.description_text
        val mLocationText: TextView = mView.location_text
        val mCreditText: TextView = mView.credit_text
    }

    inner class ProgressBarViewHolder(mView: View) : RecyclerView.ViewHolder(mView)

}