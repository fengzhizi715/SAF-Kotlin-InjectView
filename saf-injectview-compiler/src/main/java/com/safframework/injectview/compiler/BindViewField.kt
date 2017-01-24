package com.safframework.injectview.compiler

import com.safframework.injectview.annotations.InjectView
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.Name
import javax.lang.model.element.VariableElement
import javax.lang.model.type.TypeMirror

/**
 * Created by Tony Shen on 2017/1/24.
 */

class BindViewField

@Throws(IllegalArgumentException::class)
constructor(element: Element) {
    private val mFieldElement: VariableElement
    val resId: Int

    init {
        if (element.kind != ElementKind.FIELD) {
            throw IllegalArgumentException(
                    String.format("Only fields can be annotated with @%s", InjectView::class.java.simpleName))
        }

        mFieldElement = element as VariableElement
        val injectView = mFieldElement.getAnnotation(InjectView::class.java)
        resId = injectView.value

        if (resId < 0) {
            throw IllegalArgumentException(
                    String.format("id() in %s for field %s is not valid !", InjectView::class.java.simpleName,
                            mFieldElement.simpleName))
        }
    }

    val fieldName: Name
        get() = mFieldElement.simpleName

    val fieldType: TypeMirror
        get() = mFieldElement.asType()
}