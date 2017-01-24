package com.safframework.injectview.compiler

import com.safframework.injectview.annotations.InjectViews
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.Name
import javax.lang.model.element.VariableElement
import javax.lang.model.type.TypeMirror

/**
 * Created by Tony Shen on 2017/1/24.
 */

class BindViewFields

@Throws(IllegalArgumentException::class)
constructor(element: Element) {

    private val mFieldElement: VariableElement
    val resIds: IntArray?

    init {
        if (element.kind != ElementKind.FIELD) {
            throw IllegalArgumentException(
                    String.format("Only fields can be annotated with @%s", InjectViews::class.java.simpleName))
        }

        mFieldElement = element as VariableElement
        val injectViews = mFieldElement.getAnnotation(InjectViews::class.java)
        resIds = injectViews.ids

        if (resIds == null || resIds.size == 0) {
            throw IllegalArgumentException(
                    String.format("ids() in %s for field %s is not valid !", InjectViews::class.java.simpleName,
                            mFieldElement.simpleName))
        }
    }

    val fieldName: Name
        get() = mFieldElement.simpleName

    val fieldType: TypeMirror
        get() = mFieldElement.asType()
}