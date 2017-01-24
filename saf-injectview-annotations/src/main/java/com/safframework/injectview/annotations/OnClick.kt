package com.safframework.injectview.annotations

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

/**
 * Created by Tony Shen on 2017/1/24.
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
annotation class OnClick(val id: IntArray)
