package com.safframework.injectview.compiler

import com.safframework.injectview.annotations.InjectExtra
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.Name
import javax.lang.model.element.VariableElement
import javax.lang.model.type.TypeMirror

/**
 * Created by Tony Shen on 2017/1/24.
 */

class ExtraField

@Throws(IllegalArgumentException::class)
constructor(element: Element) {

    private val mFieldElement: VariableElement
    val key: String?

    init {
        if (element.kind != ElementKind.FIELD) {
            throw IllegalArgumentException(
                    String.format("Only fields can be annotated with @%s", InjectExtra::class.java.simpleName))
        }

        mFieldElement = element as VariableElement
        val injectExtra = mFieldElement.getAnnotation(InjectExtra::class.java)
        key = injectExtra.key

        if (key == null || key === "") {
            throw IllegalArgumentException(
                    String.format("key() in %s for field %s is not valid !", InjectExtra::class.java.simpleName,
                            mFieldElement.simpleName))
        }
    }

    val fieldName: Name
        get() = mFieldElement.simpleName

    val fieldType: TypeMirror
        get() = mFieldElement.asType()
}
