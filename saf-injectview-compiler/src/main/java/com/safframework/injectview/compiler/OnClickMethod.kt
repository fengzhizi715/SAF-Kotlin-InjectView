package com.safframework.injectview.compiler

import com.safframework.injectview.annotations.OnClick
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.Name

/**
 * Created by Tony Shen on 2017/1/24.
 */
class OnClickMethod

@Throws(IllegalArgumentException::class)
constructor(element: Element) {

    private val methodElement: ExecutableElement
    val methodName: Name
    var ids: IntArray? = null

    init {
        if (element.kind != ElementKind.METHOD) {
            throw IllegalArgumentException(
                    String.format("Only methods can be annotated with @%s", OnClick::class.java.simpleName))
        }
        this.methodElement = element as ExecutableElement
        this.ids = methodElement.getAnnotation(OnClick::class.java).id

        if (ids == null) {
            throw IllegalArgumentException(String.format("Must set valid ids for @%s", OnClick::class.java.simpleName))
        } else {
            for (id in ids!!) {
                if (id < 0) {
                    throw IllegalArgumentException(String.format("Must set valid id for @%s", OnClick::class.java.simpleName))
                }
            }
        }

        this.methodName = methodElement.simpleName
        // method params count must equals 0
        val parameters = methodElement.parameters
        if (parameters.size > 0) {
            throw IllegalArgumentException(
                    String.format("The method annotated with @%s must have no parameters", OnClick::class.java.simpleName))
        }
    }
}