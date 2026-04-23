package processor

import annotations.Extract
import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.*
import java.io.File
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_17)
@SupportedAnnotationTypes("annotations.Extract")
class RegexProcessor : AbstractProcessor() {

    override fun process(
        annotations: MutableSet<out TypeElement>,
        roundEnv: RoundEnvironment
    ): Boolean {

        val processedClasses = mutableSetOf<TypeElement>()

        for (element in roundEnv.getElementsAnnotatedWith(Extract::class.java)) {

            if (element is ExecutableElement) {
                val classElement = element.enclosingElement as TypeElement

                if (!processedClasses.contains(classElement)) {
                    generateExtractor(classElement)
                    processedClasses.add(classElement)
                }
            }
        }

        return true
    }

    private fun generateExtractor(classElement: TypeElement) {

        val packageName =
            processingEnv.elementUtils.getPackageOf(classElement).toString()

        val className = classElement.simpleName.toString()
        val newClassName = "${className}Extractor"

        val constructor = FunSpec.constructorBuilder()
            .addParameter("input", String::class)
            .build()

        val classBuilder = TypeSpec.classBuilder(newClassName)
            .primaryConstructor(constructor)
            .superclass(ClassName(packageName, className))
            .addSuperclassConstructorParameter("input")

        val methods = classElement.enclosedElements
            .filterIsInstance<ExecutableElement>()

        for (method in methods) {

            val annotation = method.getAnnotation(Extract::class.java) ?: continue

            val methodName = method.simpleName.toString()
            val regex = annotation.regex

            val funBuilder = FunSpec.builder(methodName)
                .addModifiers(KModifier.OVERRIDE)
                .returns(String::class.asTypeName().copy(nullable = true))
                .addStatement("val match = Regex(%S).find(input)", regex)
                .addStatement("return match?.groupValues?.get(1)")

            classBuilder.addFunction(funBuilder.build())
        }

        val file = FileSpec.builder(packageName, newClassName)
            .addType(classBuilder.build())
            .build()

        try {
            val kaptDir = processingEnv.options["kapt.kotlin.generated"]

            if (kaptDir != null) {
                file.writeTo(File(kaptDir))
            } else {
                processingEnv.messager.printMessage(
                    Diagnostic.Kind.ERROR,
                    "No kapt dir"
                )
            }
        } catch (e: Exception) {
            processingEnv.messager.printMessage(
                Diagnostic.Kind.ERROR,
                "Error: ${e.message}"
            )
        }
    }
}