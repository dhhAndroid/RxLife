package com.dhh.demo.rxlife2

import com.dhh.rxlife2.DisposeScope
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

/**
 * Created by dhh on 2019/12/21.
 *
 * @author dhh
 */
interface CustomDisposeScope : DisposeScope {
    // add custom extension methods
    fun <T> Observable<T>.commit(onNext: (T) -> Unit): Disposable {
        return bindOnDestroy().observeOn(AndroidSchedulers.mainThread()).subscribe(onNext, {})
    }
}