package com.example.gallery

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.gallery.core.data.Image
import com.example.gallery.presentation.details.SlideshowFragment
import com.example.gallery.presentation.gallery.GalleryFragment
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), GalleryFragment.ShowFragment {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()

        val permissionStatus =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)

        if (PackageManager.PERMISSION_GRANTED == permissionStatus) {
            if (supportFragmentManager.backStackEntryCount == 0) {
                showGalleryFragment()
            }
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE == requestCode) {
            if (grantResults.isNotEmpty()
                && PackageManager.PERMISSION_GRANTED == grantResults[0]
            ) {
                // permission granted
                if (supportFragmentManager.backStackEntryCount == 0) {
                    showGalleryFragment()
                }
            }
        }
    }

    private fun showGalleryFragment() {
        val ft: FragmentTransaction =
            supportFragmentManager.beginTransaction()

        val newFragment = GalleryFragment.getInstance()

        with(ft) {
            add(R.id.general_screen, newFragment as Fragment)
            commit()
        }

    }


    override fun goToSlideshowFragment(imagesPath: List<Image>, position: Int) {
        val ft: FragmentTransaction =
            supportFragmentManager.beginTransaction()
        val newFragment: SlideshowFragment =
            SlideshowFragment.newInstance(imagesPath as ArrayList<Image>, position)

        with(ft) {
            add(R.id.general_screen, newFragment as Fragment)
            addToBackStack(null)
            commit()
        }
    }

    companion object {
        const val REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE = 1
    }
}
