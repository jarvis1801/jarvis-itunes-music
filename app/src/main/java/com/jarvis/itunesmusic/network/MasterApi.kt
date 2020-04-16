package com.jarvis.itunesmusic.network

import com.jarvis.itunesmusic.network.service.MasterService
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

object MasterApi {
    private var mCompositeDisposable: CompositeDisposable = CompositeDisposable()
    private var apiService: MasterService = MasterService.retrofitService()

    fun create(): MasterApi {
        return this
    }


    fun dispose() {
        mCompositeDisposable.dispose()
    }

    fun getSearchResult(
        term: String,
        media: String,
        entity: String,
        limit: String,
        onComplete: () -> Unit,
        onNext: (t: String) -> Unit,
        onError: (e: Throwable?) -> Unit
    ) {
        apiService.getSearchResult(term, media, entity, limit)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<String> {
                override fun onComplete() {
                    onComplete()
                }

                override fun onSubscribe(d: Disposable) {
                    mCompositeDisposable.add(d)
                }

                override fun onNext(t: String) {
                    onNext(t)
                }

                override fun onError(e: Throwable) {
                    onError(e)
                }
            })
    }
}