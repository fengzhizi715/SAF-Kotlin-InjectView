package com.safframework.injectview

/**
 * Created by Tony Shen on 2017/1/24.
 */

interface ViewBinder<T> {

    fun inject(host: T, target: Any, finder: Injector.Finder)
}