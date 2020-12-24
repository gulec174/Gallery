package com.example.gallery.presentation.gallery

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gallery.MainActivity
import com.example.gallery.R
import com.example.gallery.core.data.Image
import com.example.gallery.presentation.common.GalleryViewModelFactory


class GalleryFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView

    private lateinit var mainActivity: MainActivity
    private lateinit var mGalleryViewModel: GalleryViewModel


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
        val aGalleryViewModel: GalleryViewModel by requireActivity().viewModels {
            GalleryViewModelFactory(
                requireContext().contentResolver
            )
        }

        mGalleryViewModel = aGalleryViewModel

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
                        mGalleryViewModel.onItemClick(position)
                    }

                    override fun onLongClick(view: View?, position: Int) {}
                })
        )

        mGalleryViewModel.imagePaths.observe(viewLifecycleOwner) {
            adapter.refreshImages(it)
        }

        mGalleryViewModel.eventProvider.observe(viewLifecycleOwner) {
            when (it) {
                is GalleryViewModel.Event.ShowDetails ->
                    mainActivity.goToSlideshowFragment(
                        mGalleryViewModel.imagePaths.value!!,
                        it.position
                    )
            }
        }

        return v
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
