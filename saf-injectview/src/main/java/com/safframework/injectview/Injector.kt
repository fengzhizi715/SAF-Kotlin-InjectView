package com.safframework.injectview

import android.app.Activity
import android.app.Dialog
import android.support.v4.app.Fragment
import android.view.View
import java.lang.reflect.Field

/**
 * Created by Tony Shen on 2017/1/24.
 */

object Injector {

    enum class Finder {

        DIALOG {
            override fun findById(source: Any, id: Int): View {
                return (source as Dialog).findViewById(id)
            }
        },
        ACTIVITY {
            override fun findById(source: Any, id: Int): View {
                return (source as Activity).findViewById(id)
            }

            override fun getExtra(source: Any, key: String, fieldName: String): Any? {

                val intent = (source as Activity).intent

                if (intent != null) {
                    val extras = intent.extras

                    var value: Any? = extras?.get(key)

                    var field: Field? = null
                    try {
                        field = source.javaClass.getDeclaredField(fieldName)
                    } catch (e: NoSuchFieldException) {
                        e.printStackTrace()
                    }

                    if (field == null) return null;

                    if (value == null) {
                        when {
                            field.type.name == Int::class.java.name || field.type.name == "int" -> value = 0
                            field.type.name == Boolean::class.java.name || field.type.name == "boolean" -> value = false
                            field.type.name == java.lang.String::class.java.name -> value = ""
                            field.type.name == Long::class.java.name || field.type.name == "long" -> value = 0L
                            field.type.name == Double::class.java.name || field.type.name == "double" -> value = 0.0
                        }
                    }

                    if (value != null) {
                        try {
                            field.isAccessible = true
                            field.set(source, value)
                            return field.get(source)
                        } catch (e: IllegalAccessException) {
                            e.printStackTrace()
                        }

                    }
                }

                return null
            }
        },
        FRAGMENT {
            override fun findById(source: Any, id: Int): View {
                return (source as View).findViewById(id)
            }
        },
        VIEW {
            override fun findById(source: Any, id: Int): View {
                return (source as View).findViewById(id)
            }
        },
        VIEW_HOLDER {
            override fun findById(source: Any, id: Int): View {
                return (source as View).findViewById(id)
            }
        };

        abstract fun findById(source: Any, id: Int): View

        open fun getExtra(source: Any, key: String, fieldName: String): Any? {
            return null
        }
    }

    /**
     * 在Activity中使用注解
     * @param activity
     */
    @JvmStatic fun injectInto(activity: Activity) {
        inject(activity, activity, Finder.ACTIVITY)
    }

    /**
     * 在fragment中使用注解
     * @param fragment
     * *
     * @param v
     * *
     * @return
     */
    @JvmStatic fun injectInto(fragment: Fragment, v: View) {
        inject(fragment, v, Finder.FRAGMENT)
    }

    /**
     * 在adapter中使用注解
     * @param obj
     * *
     * @param v
     * *
     * @return
     */
    @JvmStatic fun injectInto(obj: Object, v: View) {
        inject(obj, v, Finder.VIEW_HOLDER)
    }

    private fun inject(host: Any, source: Any,finder: Finder) {
        val className = host.javaClass.name
        try {
            val finderClass = Class.forName(className+"\$\$ViewBinder")

            val viewBinder = finderClass.newInstance() as ViewBinder<Any>
            viewBinder.inject(host, source, finder)
        } catch (e: Exception) {
            // throw new RuntimeException("Unable to inject for " + className, e);
            println("Unable to inject for " + className)
        }

    }
}
