package com.safframework.injectview.compiler

import com.squareup.javapoet.*
import java.util.*
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements

/**
 * Created by Tony Shen on 2017/1/24.
 */
class AnnotatedClass(var mClassElement: TypeElement, var mElementUtils: Elements) {
    var mFields: MutableList<BindViewField>
    var mFieldss: MutableList<BindViewFields>
    var mExtraFields: MutableList<ExtraField>
    var mMethods: MutableList<OnClickMethod>

    init {
        this.mFields = ArrayList<BindViewField>()
        this.mFieldss = ArrayList<BindViewFields>()
        this.mExtraFields = ArrayList<ExtraField>()
        this.mMethods = ArrayList<OnClickMethod>()
    }

    val fullClassName: String
        get() = mClassElement.qualifiedName.toString()

    fun addField(field: BindViewField) {
        mFields.add(field)
    }

    fun addFields(field: BindViewFields) {
        mFieldss.add(field)
    }

    fun addExtraField(extraField: ExtraField) {
        mExtraFields.add(extraField)
    }

    fun addMethod(method: OnClickMethod) {
        mMethods.add(method)
    }

    fun generateFinder(): JavaFile {

        // method inject(final T host, Object source, FINDER finder)
        val injectMethodBuilder = MethodSpec.methodBuilder("inject")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override::class.java)
                .addParameter(TypeName.get(mClassElement.asType()), "host", Modifier.FINAL)
                .addParameter(TypeName.OBJECT, "source")
                .addParameter(TypeUtils.FINDER, "finder")

        for (field in mFields) {
            // find view
            injectMethodBuilder.addStatement("host.\$N = (\$T)(finder.findById(source, \$L))", field.fieldName,
                    ClassName.get(field.fieldType), field.resId)
        }

        var fieldTypeName: String? = null
        for (field in mFieldss) {
            // find views
            fieldTypeName = field.fieldType.toString()

            if (fieldTypeName.startsWith("java.util.List")) {
                val ids = field.resIds

                injectMethodBuilder.addStatement("host.\$N = new \$T<>()", field.fieldName, TypeUtils.ARRAY_LIST)

                val first = fieldTypeName.indexOf("<")
                if (first == -1) continue

                val last = fieldTypeName.lastIndexOf(">")
                if (last == -1) continue

                val clazz = fieldTypeName.substring(first + 1, last)
                val dot = clazz.lastIndexOf(".")
                if (dot == -1) continue

                val packageName = clazz.substring(0, dot)
                val simpleName = clazz.substring(dot + 1)
                val className = ClassName.get(packageName, simpleName)

                ids?.map {
                    injectMethodBuilder.addStatement("host.\$N.add((\$T)(finder.findById(source, \$L)))", field.fieldName, className, it)
                }
            } else if (fieldTypeName.endsWith("[]")) {
                val ids = field.resIds
                val length = ids?.size
                val first = fieldTypeName.indexOf("[")
                if (first == -1) continue

                val clazz = fieldTypeName.substring(0, first)

                val dot = clazz.lastIndexOf(".")
                if (dot == -1) continue

                val packageName = clazz.substring(0, dot)
                val simpleName = clazz.substring(dot + 1)
                val className = ClassName.get(packageName, simpleName)
                injectMethodBuilder.addStatement("host.\$N = new \$T[\$L]", field.fieldName, className, length)

                var i = 0;
                for (id in ids!!) {
                    injectMethodBuilder.addStatement("host.\$N[\$L] = (\$T)(finder.findById(source, \$L))", field.fieldName, i, className, id)
                    i++
                }
            }
        }

        for (field in mExtraFields) {
            // find extra
            injectMethodBuilder.addStatement("host.\$N = (\$T)(finder.getExtra(source, \$L, \$L))", field.fieldName,
                    ClassName.get(field.fieldType), "\"" + field.key + "\"", "\"" + field.fieldName + "\"")
        }

        if (mMethods.size > 0) {
            injectMethodBuilder.addStatement("\$T listener", TypeUtils.ANDROID_ON_CLICK_LISTENER)

            mMethods.map {
                // declare OnClickListener anonymous class
                val listener = TypeSpec.anonymousClassBuilder("")
                        .addSuperinterface(TypeUtils.ANDROID_ON_CLICK_LISTENER)
                        .addMethod(MethodSpec.methodBuilder("onClick")
                                .addAnnotation(Override::class.java)
                                .addModifiers(Modifier.PUBLIC)
                                .returns(TypeName.VOID)
                                .addParameter(TypeUtils.ANDROID_VIEW, "view")
                                .addStatement("host.\$N()", it.methodName)
                                .build())
                        .build()
                injectMethodBuilder.addStatement("listener = \$L ", listener)
                for (id in it.ids!!) {
                    // set listeners
                    injectMethodBuilder.addStatement("finder.findById(source, \$L).setOnClickListener(listener)", id)
                }
            }
        }

        // generate whole class
        val finderClass = TypeSpec.classBuilder(mClassElement.simpleName.toString() + "\$\$ViewBinder")
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ParameterizedTypeName.get(TypeUtils.VIEW_BINDER, TypeName.get(mClassElement.asType())))
                .addMethod(injectMethodBuilder.build())
                .build()

        val packageName = mElementUtils.getPackageOf(mClassElement).qualifiedName.toString()

        return JavaFile.builder(packageName, finderClass).build()
    }
}