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

    interface ShowFragment {
        fun goToSlideshowFragment(imagesPath: List<Image>?, position: Int)
    }

    private lateinit var recyclerView: RecyclerView
    private val galleryViewModel: GalleryViewModel by lazy { GalleryViewModel(requireContext().contentResolver) }

    private lateinit var mainActivity: MainActivity

    companion object {
        fun getInstance(): GalleryFragment {
            return GalleryFragment()
        }
    }

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

                        mainActivity.goToSlideshowFragment(
                            galleryViewModel.getImages().value,
                            position
                        )
                    }

                    override fun onLongClick(view: View?, position: Int) {}
                })
        )

        galleryViewModel.imagePaths.observe(owner = this) {
            adapter.refreshImages(it)
        }

        return v
    }
}