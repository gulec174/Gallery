package com.example.gallery.presentation.details

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.gallery.core.data.Image
import com.example.gallery.databinding.ImageFullscreenPreviewBinding
import java.io.File

class SlideshowAdapter(private val activity: Context, private val images: List<Image>) : PagerAdapter() {
    private lateinit var layoutInflater: LayoutInflater

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view: View
        val imagePreview: ImageView
        layoutInflater =
            activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val imageFullscreenPreviewBinding = ImageFullscreenPreviewBinding.inflate(
            LayoutInflater.from(
                container.context
            ), container, false)
        with(imageFullscreenPreviewBinding) {
            view = root
            imagePreview = imageFullScreen
        }

        Glide.with(activity)
            .load(File(images[position].uri))
            .thumbnail(0.5f)
            .crossFade()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(imagePreview)

        container.addView(view)
        return view
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as View
    }

    override fun getCount(): Int {
        return images.size
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}