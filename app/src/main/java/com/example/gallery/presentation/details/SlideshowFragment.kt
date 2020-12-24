package com.example.gallery.presentation.details

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.example.gallery.core.data.Image
import com.example.gallery.R
import com.example.gallery.presentation.common.GalleryViewModelFactory
import androidx.activity.viewModels

class SlideshowFragment : Fragment() {
    private lateinit var viewPager: ViewPager
    private lateinit var myViewPagerAdapter: SlideshowAdapter
    private var selectedPosition = 0

    private lateinit var mSlideshowViewModel: SlideshowViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val aSlideshowViewModel: SlideshowViewModel by requireActivity().viewModels {
            GalleryViewModelFactory(requireContext().contentResolver)
        }

        mSlideshowViewModel = aSlideshowViewModel
    }

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
        mSlideshowViewModel.setup(
            arguments?.getSerializable(IMAGES_KEY) as ArrayList<Image>,
            selectedPosition
        )

        myViewPagerAdapter = SlideshowAdapter(requireActivity(), mSlideshowViewModel.imagePaths.value!!)

        viewPager = v.findViewById(R.id.pager)
        with(viewPager) {
            adapter = myViewPagerAdapter
            addOnPageChangeListener(viewPagerPageChangeListener)
            currentItem = selectedPosition
        }

        /*mSlideshowViewModel.imagePaths.observe(viewLifecycleOwner) {
            myViewPagerAdapter.submitImageList(it)
        }*/

        return v
    }

    //  page change listener
    private var viewPagerPageChangeListener: OnPageChangeListener = object : OnPageChangeListener {
        override fun onPageSelected(position: Int) {

        }

        override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {}
        override fun onPageScrollStateChanged(arg0: Int) {}
    }
    /*
    inner class SlideshowAdapter : PagerAdapter() {
        private lateinit var layoutInflater: LayoutInflater

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            layoutInflater =
                requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view: View =
                layoutInflater.inflate(R.layout.image_fullscreen_preview, container, false)
            val imagePreview: ImageView = view.findViewById(R.id.image_full_screen)
            Glide.with(requireActivity())
                .load(File(mSlideshowViewModel.imagePaths.value!![position].uri))
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imagePreview)

            container.addView(view)
            mSlideshowViewModel.selectedPosition.value = position
            return view
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object` as View
        }

        override fun getCount(): Int {
            return mSlideshowViewModel.imagePaths.value!!.size
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }
    }*/

    companion object {
        const val IMAGES_KEY = "images"
        const val IMAGE_PATH_KEY = "image_path"
        fun newInstance(imagesPath: ArrayList<Image>, position: Int): SlideshowFragment {
            val bundle = Bundle()
            bundle.putSerializable(IMAGES_KEY, imagesPath)
            bundle.putInt(IMAGE_PATH_KEY, position)

            val slideshowDialogFragment = SlideshowFragment()
            slideshowDialogFragment.arguments = bundle
            return slideshowDialogFragment
        }
    }
}