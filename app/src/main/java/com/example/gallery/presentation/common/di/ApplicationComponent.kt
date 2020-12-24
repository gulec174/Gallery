package com.example.gallery.presentation.common.di

import com.example.gallery.presentation.details.SlideshowFragment
import com.example.gallery.presentation.gallery.GalleryFragment
import dagger.Component

@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {
    fun inject(slideshowFragment: SlideshowFragment)
    fun inject(galleryFragment: GalleryFragment)
}