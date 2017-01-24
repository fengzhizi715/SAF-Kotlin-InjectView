package com.safframework.injectview

/**
 * Created by Tony Shen on 2017/1/24.
 */

interface ViewBinder {

    fun inject(host: Any, target: Any, finder: Injector.Finder)
}