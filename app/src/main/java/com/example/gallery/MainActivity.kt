package com.example.gallery

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gallery.ImageGalleryAdapter.ClickListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.ArrayList


class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var imagesPath: ArrayList<Image>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


    override fun onStart() {
        super.onStart()

        val permissionStatus =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)

        if (PackageManager.PERMISSION_GRANTED == permissionStatus) {
            loadImages()
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                Storage.REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE
            )
        }

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (Storage.REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE == requestCode) {
            if (grantResults.isNotEmpty()
                && PackageManager.PERMISSION_GRANTED == grantResults[0]
            ) {
                // permission granted
                loadImages()
            }
        }
    }


    private fun getImages(): ArrayList<Image> {
        val imagesPath = ArrayList<Image>()
        val projection = arrayOf(
            MediaStore.Images.ImageColumns._ID,
            "_data",
            "bucket_display_name",
            "datetaken",
            MediaStore.Images.ImageColumns.MIME_TYPE
        )

        val cursor = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            "datetaken DESC"
        )

        if (cursor != null && cursor.count > 0) {
            cursor.moveToFirst()
            do {
                val path =
                    cursor.getString(cursor.getColumnIndex("_data"))
                imagesPath.add(Image(path))
            } while (cursor.moveToNext())
            cursor.close()
            return imagesPath
        }
        return ArrayList()
    }


    private fun loadImages() = CoroutineScope(Dispatchers.IO).launch {
        imagesPath = getImages()
        CoroutineScope(Dispatchers.Main).launch {
            initRecyclerView()
        }
    }


    private fun initRecyclerView() {
        recyclerView = findViewById(R.id.rv)
        with(recyclerView) {
            layoutManager = GridLayoutManager(context, 3)
            adapter = ImageGalleryAdapter(imagesPath)
            itemAnimator = DefaultItemAnimator()
        }

        recyclerView.addOnItemTouchListener(
            ImageGalleryAdapter.RecyclerTouchListener(
                applicationContext,
                recyclerView,
                object : ClickListener {
                    override fun onClick(view: View?, position: Int) {
                        val ft: FragmentTransaction =
                            supportFragmentManager.beginTransaction()
                        val newFragment: SlideshowDialogFragment? =
                            SlideshowDialogFragment.newInstance(imagesPath, position)

                        with(ft) {
                            add(R.id.general_screen, newFragment as Fragment)
                            addToBackStack(null)
                            commit()
                        }
                    }

                    override fun onLongClick(view: View?, position: Int) {}
                })
        )
    }
}
