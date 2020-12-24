package com.example.gallery.presentation.gallery

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.gallery.MainActivity
import com.example.gallery.R
import com.example.gallery.core.data.Image
import com.example.gallery.databinding.GalleryFragmentBinding
import com.example.gallery.presentation.common.GalleryViewModelFactory
import com.example.gallery.presentation.common.di.ApplicationModule
import com.example.gallery.presentation.common.di.DaggerApplicationComponent
import javax.inject.Inject


class GalleryFragment : Fragment() {

    private lateinit var mainActivity: MainActivity
    private lateinit var mGalleryViewModel: GalleryViewModel

    private val viewBinding by viewBinding(GalleryFragmentBinding::bind)

    @Inject
    lateinit var galleryViewModelFactory: GalleryViewModelFactory

    override fun onAttach(context: Context) {
        DaggerApplicationComponent.builder().applicationModule(ApplicationModule(requireContext())).build().inject(this)
        super.onAttach(context)
        mainActivity = context as MainActivity
        val aGalleryViewModel: GalleryViewModel by requireActivity().viewModels {
            galleryViewModelFactory
        }

        mGalleryViewModel = aGalleryViewModel

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.gallery_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = ImageGalleryAdapter()
        with(viewBinding.rv) {
            layoutManager = GridLayoutManager(context, 3)
            itemAnimator = DefaultItemAnimator()
            this.adapter = adapter
            addOnItemTouchListener(
                ImageGalleryAdapter.RecyclerTouchListener(
                    context,
                    this,
                    object : ImageGalleryAdapter.ClickListener {
                        override fun onClick(view: View?, position: Int) {
                            mGalleryViewModel.onItemClick(position)
                        }

                        override fun onLongClick(view: View?, position: Int) {}
                    })
            )
        }

        mGalleryViewModel.apply {

            imagePaths.observe(viewLifecycleOwner, { adapter.refreshImages(it) })
            eventProvider.observe(viewLifecycleOwner, {
                when (it) {
                    is GalleryViewModel.Event.ShowDetails ->
                        mainActivity.goToSlideshowFragment(
                            mGalleryViewModel.imagePaths.value!!,
                            it.position
                        )
                }
            })
        }
    }

    interface ShowFragment {
        fun goToSlideshowFragment(imagesPath: List<Image>, position: Int)
    }

    companion object {
        fun getInstance(): GalleryFragment {
            return GalleryFragment()
        }
    }
}
