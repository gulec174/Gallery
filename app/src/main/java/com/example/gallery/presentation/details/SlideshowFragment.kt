package com.example.gallery.presentation.details

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.gallery.R
import com.example.gallery.core.data.Image
import com.example.gallery.databinding.FragmentImageSliderBinding
import com.example.gallery.presentation.common.GalleryViewModelFactory
import com.example.gallery.presentation.common.di.ApplicationModule
import com.example.gallery.presentation.common.di.DaggerApplicationComponent
import javax.inject.Inject

class SlideshowFragment : Fragment() {
    private lateinit var myViewPagerAdapter: SlideshowAdapter
    private var selectedPosition = 0

    @Inject
    lateinit var galleryViewModelFactory: GalleryViewModelFactory

    private lateinit var mSlideshowViewModel: SlideshowViewModel

    private val viewBinding by viewBinding(FragmentImageSliderBinding::bind)

    override fun onAttach(context: Context) {
        DaggerApplicationComponent.builder().applicationModule(ApplicationModule(requireContext())).build().inject(this)
        super.onAttach(context)
        val aSlideshowViewModel: SlideshowViewModel by requireActivity().viewModels {
            galleryViewModelFactory
        }

        mSlideshowViewModel = aSlideshowViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return inflater.inflate(R.layout.fragment_image_slider, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        selectedPosition = arguments?.getInt(IMAGE_PATH_KEY) as Int
        @Suppress("UNCHECKED_CAST")
        mSlideshowViewModel.setup(
            arguments?.getSerializable(IMAGES_KEY) as ArrayList<Image>,
            selectedPosition
        )

        myViewPagerAdapter =
            SlideshowAdapter(requireActivity(), mSlideshowViewModel.imagePaths.value!!)

        with(viewBinding.pager) {
            adapter = myViewPagerAdapter
            addOnPageChangeListener(viewPagerPageChangeListener)
            currentItem = selectedPosition
        }
    }

    //  page change listener
    private var viewPagerPageChangeListener: OnPageChangeListener = object : OnPageChangeListener {
        override fun onPageSelected(position: Int) {

        }

        override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {}
        override fun onPageScrollStateChanged(arg0: Int) {}
    }

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