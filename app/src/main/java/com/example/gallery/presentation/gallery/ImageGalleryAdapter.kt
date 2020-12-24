package com.example.gallery.presentation.gallery

import android.content.Context
import android.view.*
import android.view.GestureDetector.SimpleOnGestureListener
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.gallery.core.data.Image
import com.example.gallery.databinding.ItemPhotoBinding
import java.io.File


class ImageGalleryAdapter :
    RecyclerView.Adapter<ImageGalleryAdapter.ViewHolder>() {

    private var imagesPath: List<Image> = ArrayList()

    fun refreshImages(images: List<Image>) {
        this.imagesPath = images
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(
            ItemPhotoBinding.inflate(LayoutInflater.from(p0.context), p0, false)
        )
    }

    override fun getItemCount(): Int {
        return imagesPath.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(imagesPath[position].uri)
    }

    class ViewHolder internal constructor(itemPhotoBinding: ItemPhotoBinding) :
        RecyclerView.ViewHolder(itemPhotoBinding.root) {
        private var imageView = itemPhotoBinding.ivPhoto

        fun bind(imagePath: String) {
            Glide.with(imageView.context).load(File(imagePath))
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView)
        }
    }

    interface ClickListener {
        fun onClick(view: View?, position: Int)
        fun onLongClick(view: View?, position: Int)
    }

    class RecyclerTouchListener(
        context: Context?,
        recyclerView: RecyclerView,
        private val clickListener: ClickListener?
    ) :
        OnItemTouchListener {
        private val gestureDetector: GestureDetector =
            GestureDetector(context, object : SimpleOnGestureListener() {
                override fun onSingleTapUp(e: MotionEvent): Boolean {
                    return true
                }

                override fun onLongPress(e: MotionEvent) {
                    val child =
                        recyclerView.findChildViewUnder(e.x, e.y)
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(
                            child,
                            recyclerView.getChildAdapterPosition(child)
                        )
                    }
                }
            })

        override fun onInterceptTouchEvent(
            rv: RecyclerView,
            e: MotionEvent
        ): Boolean {
            with(rv) {
                val child = findChildViewUnder(e.x, e.y)
                if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                    clickListener.onClick(child, getChildAdapterPosition(child))
                }
            }

            return false
        }

        override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
        override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}

    }

}