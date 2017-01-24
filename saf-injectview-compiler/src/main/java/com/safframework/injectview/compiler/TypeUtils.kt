package com.safframework.injectview.compiler

import com.squareup.javapoet.ClassName

/**
 * Created by Tony Shen on 2017/1/24.
 */
class TypeUtils {

    companion object {
        val ANDROID_VIEW = ClassName.get("android.view", "View")
        val ANDROID_ON_CLICK_LISTENER = ClassName.get("android.view", "View", "OnClickListener")
        val VIEW_BINDER = ClassName.get("cn.salesuite.injectview", "ViewBinder")
        val FINDER = ClassName.get("cn.salesuite.injectview.Injector", "Finder")
        val ARRAY_LIST = ClassName.get("java.util", "ArrayList")
    }
}