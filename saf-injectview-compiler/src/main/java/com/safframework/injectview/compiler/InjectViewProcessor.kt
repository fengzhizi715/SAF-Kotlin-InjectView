package com.safframework.injectview.compiler

import com.safframework.injectview.annotations.InjectExtra
import com.safframework.injectview.annotations.InjectView
import com.safframework.injectview.annotations.InjectViews
import com.safframework.injectview.annotations.OnClick
import java.io.IOException
import java.util.*
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements

/**
 * Created by Tony Shen on 2017/1/24.
 */
class InjectViewProcessor : AbstractProcessor() {

    var mFiler: Filer?=null //文件相关的辅助类
    var mElementUtils: Elements?=null //元素相关的辅助类
    var mMessager: Messager?=null //日志相关的辅助类

    @Synchronized override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        mFiler = processingEnv.filer
        mElementUtils = processingEnv.elementUtils
        mMessager = processingEnv.messager
    }

    /**
     * @return 指定使用的 Java 版本。通常返回 SourceVersion.latestSupported()。
     */
    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    /**
     * @return 指定哪些注解应该被注解处理器注册
     */
    override fun getSupportedAnnotationTypes(): Set<String> {
        val types = LinkedHashSet<String>()
        types.add(InjectView::class.java.simpleName)
        types.add(InjectViews::class.java.simpleName)
        types.add(OnClick::class.java.simpleName)
        types.add(InjectExtra::class.java.simpleName)
        return types
    }


    private val mAnnotatedClassMap = HashMap<String, AnnotatedClass>()

    override fun process(annotations: Set<TypeElement>, roundEnv: RoundEnvironment): Boolean {
        mAnnotatedClassMap.clear()

        try {
            processInjectView(roundEnv)
            processInjectViews(roundEnv)
            processInjectExtra(roundEnv)
            processOnClick(roundEnv)
        } catch (e: IllegalArgumentException) {
            Utils.error(mMessager, e.message)
            return true // stop process
        }

        for (annotatedClass in mAnnotatedClassMap.values) {
            try {
                Utils.info(mMessager, "Generating file for %s", annotatedClass.fullClassName)
                annotatedClass.generateFinder().writeTo(mFiler)
            } catch (e: IOException) {
                Utils.error(mMessager, "Generate file failed, reason: %s", e.message!!)
                return true
            }

        }
        return true
    }

    @Throws(IllegalArgumentException::class)
    private fun processInjectView(roundEnv: RoundEnvironment) {

        var annotatedClass: AnnotatedClass? = null
        var field: BindViewField? = null
        val set = roundEnv.getElementsAnnotatedWith(InjectView::class.java) as Set<Element>
        if (set != null && set.size > 0) {
            for (element in set) {
                annotatedClass = getAnnotatedClass(element, "@InjectView")
                if (annotatedClass == null)
                    continue

                field = BindViewField(element)
                annotatedClass.addField(field)
            }
        }
    }

    private fun processInjectViews(roundEnv: RoundEnvironment) {
        var annotatedClass: AnnotatedClass? = null
        var field: BindViewFields? = null
        val set = roundEnv.getElementsAnnotatedWith(InjectViews::class.java) as Set<Element>
        if (set != null && set.size > 0) {
            for (element in set) {
                annotatedClass = getAnnotatedClass(element, "@InjectViews")
                if (annotatedClass == null)
                    continue

                field = BindViewFields(element)
                annotatedClass.addFields(field)
            }
        }
    }

    private fun processInjectExtra(roundEnv: RoundEnvironment) {
        val set = roundEnv.getElementsAnnotatedWith(InjectExtra::class.java) as Set<Element>
        if (set != null && set.size > 0) {
            var annotatedClass: AnnotatedClass? = null
            var field: ExtraField? = null
            for (element in set) {
                annotatedClass = getAnnotatedClass(element, "@InjectExtra")
                if (annotatedClass == null)
                    continue

                field = ExtraField(element)
                annotatedClass.addExtraField(field)
            }
        }
    }

    private fun processOnClick(roundEnv: RoundEnvironment) {
        for (element in roundEnv.getElementsAnnotatedWith(OnClick::class.java)) {
            val annotatedClass = getAnnotatedClass(element, "@OnClick") ?: continue

            val method = OnClickMethod(element)
            annotatedClass.addMethod(method)
        }
    }

    private fun getAnnotatedClass(element: Element, annotationName: String): AnnotatedClass? {
        val classElement = element.enclosingElement as TypeElement

        //检测是否是支持的注解类型，如果不是里面会报错
        if (!Utils.isValidClass(mMessager, classElement, annotationName)) {
            return null
        }

        val fullClassName = classElement.qualifiedName.toString()
        var annotatedClass: AnnotatedClass? = mAnnotatedClassMap[fullClassName]
        if (annotatedClass == null) {
            annotatedClass = AnnotatedClass(classElement, mElementUtils!!)
            mAnnotatedClassMap.put(fullClassName, annotatedClass)
        }
        return annotatedClass
    }
}