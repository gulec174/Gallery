package com.example.gallery.presentation.details

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.gallery.core.data.Image
import com.example.gallery.R
import java.io.File


class SlideshowFragment : Fragment() {
    private lateinit var viewPager: ViewPager
    private lateinit var myViewPagerAdapter: GalleryAdapter
    private var selectedPosition = 0

    private val slideshowViewModel by lazy { SlideshowViewModel() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val v: View =
            inflater.inflate(R.layout.fragment_image_slider, container, false)

        requireActivity()

        selectedPosition = arguments?.getInt(IMAGE_PATH_KEY) as Int
        @Suppress("UNCHECKED_CAST")
        slideshowViewModel.imagePaths = arguments?.getSerializable(IMAGES_KEY) as ArrayList<Image>


        myViewPagerAdapter = GalleryAdapter()
        viewPager = v.findViewById(R.id.pager)
        with(viewPager) {
            adapter = myViewPagerAdapter
            addOnPageChangeListener(viewPagerPageChangeListener)
            currentItem = selectedPosition
        }

        return v
    }

    //  page change listener
    private var viewPagerPageChangeListener: OnPageChangeListener = object : OnPageChangeListener {
        override fun onPageSelected(position: Int) {

        }

        override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {}
        override fun onPageScrollStateChanged(arg0: Int) {}
    }

    inner class GalleryAdapter : PagerAdapter() {
        private lateinit var layoutInflater: LayoutInflater

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            layoutInflater =
                requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view: View =
                layoutInflater.inflate(R.layout.image_fullscreen_preview, container, false)
            val imagePreview: ImageView = view.findViewById(R.id.image_full_screen)
            Glide.with(requireActivity()).load(File(slideshowViewModel.getImage(position).uri))
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
            return slideshowViewModel.imagePaths.size
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }
    }

    companion object {
        const val IMAGES_KEY = "images"
        const val IMAGE_PATH_KEY = "image_path"

        fun newInstance(imagesPath: List<Image>, position: Int): SlideshowFragment {
            val bundle = Bundle()
            bundle.putSerializable(IMAGES_KEY, imagesPath)
            bundle.putInt(IMAGE_PATH_KEY, position)

            val slideshowDialogFragment = SlideshowFragment()
            slideshowDialogFragment.arguments = bundle
            return slideshowDialogFragment
        }
    }

}