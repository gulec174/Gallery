package com.example.gallery


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import java.io.File


class SlideshowDialogFragment : DialogFragment() {

    private lateinit var imageView1: ImageView

    companion object {
        fun newInstance(): SlideshowDialogFragment? {
            return SlideshowDialogFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        super.onCreateView(inflater, container, savedInstanceState)
        val v: View =
            inflater.inflate(R.layout.image_fullscreen_preview, container, false)
        imageView1 = v.findViewById(R.id.image_full_screen)
        val path = arguments?.getString("image_path")
        Log.d("!!!", "it is $path")
        Glide.with(context).load(File(path))
            .thumbnail(0.5f)
            .crossFade()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(imageView1)

        return v
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

}