package com.safframework.injectview.annotations

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

/**
 * Created by Tony Shen on 2017/1/24.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
annotation class InjectExtra(val key: String = "")