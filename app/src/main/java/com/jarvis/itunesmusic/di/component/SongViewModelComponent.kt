package com.jarvis.itunesmusic.di.component

import com.jarvis.itunesmusic.viewmodel.SongViewModel
import dagger.Component

@Component
interface SongViewModelComponent {
    fun inject(activity: SongViewModel)
}