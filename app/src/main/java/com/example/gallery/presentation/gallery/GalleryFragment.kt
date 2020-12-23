package com.example.gallery.presentation.gallery

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gallery.MainActivity
import com.example.gallery.R
import com.example.gallery.core.data.Image

class GalleryFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView

    private val galleryViewModel: GalleryViewModel by lazy { GalleryViewModel(requireContext().contentResolver) }
    private lateinit var mainActivity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v: View =
            inflater.inflate(R.layout.gallery_fragment, container, false)

        recyclerView = v.findViewById(R.id.rv)
        val adapter = ImageGalleryAdapter()
        with(recyclerView) {
            layoutManager = GridLayoutManager(context, 3)
            itemAnimator = DefaultItemAnimator()
            this.adapter = adapter
        }

        recyclerView.addOnItemTouchListener(
            ImageGalleryAdapter.RecyclerTouchListener(
                context,
                recyclerView,
                object : ImageGalleryAdapter.ClickListener {
                    override fun onClick(view: View?, position: Int) {
                        galleryViewModel.onItemClick(position)
                    }

                    override fun onLongClick(view: View?, position: Int) {}
                })
        )

        galleryViewModel.imagePaths.value
        galleryViewModel.imagePaths.observe(owner = this) {
            adapter.refreshImages(it)
        }
        
        galleryViewModel.eventProvider.observe(viewLifecycleOwner) {
            when(it) {
                is GalleryViewModel.Event.ShowDetails -> {
                    mainActivity.goToSlideshowFragment(
                        galleryViewModel.getImages().value,
                        position
                    )
                }
            }
        }

        return v
    }

    interface ShowFragment {
        fun goToSlideshowFragment(imagesPath: List<Image>?, position: Int)
    }

    companion object {
        fun getInstance(): GalleryFragment {
            return GalleryFragment()
        }
    }
}
