package com.safframework.injectview.annotations

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * Created by Tony Shen on 2017/1/24.
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(RetentionPolicy.CLASS)
annotation class OnClick(val id: IntArray)
