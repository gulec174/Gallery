package com.example.gallery.core.data.repository

import android.content.ContentResolver
import android.provider.MediaStore
import com.example.gallery.core.data.Image
import java.util.ArrayList

class Repository {

    fun getImages(contentResolver: ContentResolver): List<Image> {
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
            with(cursor) {
                moveToFirst()
                do {
                    val path =
                        getString(getColumnIndex("_data"))
                    imagesPath.add(Image(path))
                } while (moveToNext())
                close()
                return imagesPath
            }

        }
        return ArrayList()
    }


}