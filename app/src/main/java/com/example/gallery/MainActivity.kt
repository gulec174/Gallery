package com.example.gallery

import android.Manifest
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gallery.ImageGalleryAdapter.ClickListener


class MainActivity : AppCompatActivity() {
    private val REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE = 1

    private lateinit var recyclerView: RecyclerView

    private var handler: Handler? = null

    private val MSG_EMAILS_ARE_LOADED = 1

    private lateinit var imagesPath: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val permissionStatus: Int =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)

        if (PackageManager.PERMISSION_GRANTED == permissionStatus) {
            loadImages()
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE
            )
        }

        handler = Handler(Handler.Callback { msg ->
            if (MSG_EMAILS_ARE_LOADED == msg.what) {
                initRecyclerView()
            }
            true
        })

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE == requestCode) {
            if (grantResults.size > 0
                && PackageManager.PERMISSION_GRANTED == grantResults[0]
            ) {
                // permission granted
                loadImages()
            }
        }
    }

    fun getImages(): List<String> {
        val imagesPath = mutableListOf<String>()
        val projection = arrayOf(
            MediaStore.Images.ImageColumns._ID,
            MediaStore.Images.ImageColumns.DATA,
            MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
            MediaStore.Images.ImageColumns.DATE_TAKEN,
            MediaStore.Images.ImageColumns.MIME_TYPE
        )

        val cursor: Cursor? = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC"
        )

        if (cursor != null && cursor.count > 0) {
            cursor.moveToFirst()
            do {
                val path =
                    cursor!!.getString(cursor!!.getColumnIndex(MediaStore.Images.Media.DATA))
                imagesPath.add(path)
            } while (cursor.moveToNext())
            cursor.close()
            return imagesPath
        }
        return emptyList<String>()
    }

    fun loadImages() = Thread(Runnable {
        kotlin.run {
            imagesPath = getImages().toList()
            handler?.sendEmptyMessage(MSG_EMAILS_ARE_LOADED)
        }
    }).start()


    private fun initRecyclerView() {
        recyclerView = findViewById(R.id.rv)
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        recyclerView.adapter = ImageGalleryAdapter(imagesPath, this)
        recyclerView.itemAnimator = DefaultItemAnimator()

        recyclerView.addOnItemTouchListener(
            ImageGalleryAdapter.RecyclerTouchListener(
                applicationContext,
                recyclerView,
                object : ClickListener {
                    override fun onClick(view: View?, position: Int) {
                        val bundle = Bundle()
                        bundle.putString("image_path", imagesPath[position])

                        val ft: FragmentTransaction =
                            supportFragmentManager.beginTransaction()
                        val newFragment: SlideshowDialogFragment? = SlideshowDialogFragment.newInstance()
                        newFragment?.setArguments(bundle)
                        newFragment?.show(ft, "slideshow")
                    }

                    override fun onLongClick(view: View?, position: Int) {}
                })
        )


    }
}