1. declare annotations:
```
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class When(val description: String)

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class Then(val description: String)
```

2. Create new module :gherkinprocessor
3. Copy GherkinProcessor.kt to the module
4. Create new file gherkinprocessor/resources/META-INF/services/com.google.devtools.ksp.processing.SymbolProcessorProvider
5. Register the processor by writing into the file com.example.gherkinprocessor.ScenarioProcessorProvider
6. build


GherkinProcessor.kt
```
package com.effatheresoft.gherkinprocessor

import com.effatheresoft.annotations.RunThen
import com.effatheresoft.annotations.RunWhen
import com.effatheresoft.annotations.Scenario
import com.effatheresoft.annotations.Then
import com.effatheresoft.annotations.When
import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.squareup.kotlinpoet.DelicateKotlinPoetApi
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.toAnnotationSpec
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.ksp.writeTo
import org.junit.Test

class ScenarioProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        // Find all functions annotated with @Scenario
        val scenarioFunctions = resolver.getSymbolsWithAnnotation(Scenario::class.qualifiedName!!)
            .filterIsInstance<KSFunctionDeclaration>()

        if (!scenarioFunctions.iterator().hasNext()) {
            return emptyList() // No scenarios found, nothing to do.
        }

        // Group the scenario functions by the class they belong to.
        val scenariosByClass = scenarioFunctions.groupBy { it.parentDeclaration as KSClassDeclaration }

        // Process each class that contains scenarios.
        scenariosByClass.forEach { (testClass, scenarios) ->
            // Use KotlinPoet to generate the file.
            generateTestClass(testClass, scenarios)
        }

        // Multiple processing rounds are not needed
        // Is generating new files, not modifying existing ones, so an empty list is returned.
        return emptyList()
    }

    @OptIn(KspExperimental::class, DelicateKotlinPoetApi::class)
    private fun generateTestClass(originalTestClass: KSClassDeclaration, scenarios: List<KSFunctionDeclaration>) {
        val generatedClassName = "${originalTestClass.simpleName.asString()}_Generated"

        // Create a map of all available steps (@When, @Then) in the class and its parents.
        // The key is the description string, and the value is the function name.
        val stepImplementations = mutableMapOf<String, String>()
        originalTestClass.getAllFunctions().forEach { func ->
            func.getAnnotationsByType(When::class).firstOrNull()?.let {
                stepImplementations[it.description] = func.simpleName.asString()
            }
            func.getAnnotationsByType(Then::class).firstOrNull()?.let {
                stepImplementations[it.description] = func.simpleName.asString()
            }
        }

        // Use KotlinPoet to build the new test class file.
        val fileSpec = FileSpec.builder(
            packageName = originalTestClass.packageName.asString(),
            fileName = generatedClassName
        ).apply {
            // Build the class itself.
            val classSpec = TypeSpec.classBuilder(generatedClassName).apply {
                // Make the generated class inherit from the original test class.
                // This is crucial for inheriting Hilt rules, JUnit rules, and helper methods.
                superclass(originalTestClass.asType(emptyList()).toTypeName())

                // Copy class-level annotations like @HiltAndroidTest and @RunWith
                originalTestClass.annotations.forEach {
                    addAnnotation(it.toAnnotationSpec())
                }

                // For each @Scenario, create a corresponding @Test function.
                scenarios.forEach { scenarioFunc ->
                    val testFunSpec = FunSpec.builder(scenarioFunc.simpleName.asString()).apply {
                        addModifiers(KModifier.OVERRIDE)
                        addAnnotation(Test::class)

                        // Find all @RunWhen and @RunThen steps for the current scenario.
                        val whenSteps = scenarioFunc.getAnnotationsByType(RunWhen::class)
                        val thenSteps = scenarioFunc.getAnnotationsByType(RunThen::class)

                        // Add the function calls to the test body in order.
                        whenSteps.forEach { step ->
                            stepImplementations[step.description]?.let { funcName ->
                                addStatement("%N()", funcName)
                            } ?: logger.error("Step implementation not found for: '${step.description}'", scenarioFunc)
                        }
                        thenSteps.forEach { step ->
                            stepImplementations[step.description]?.let { funcName ->
                                addStatement("%N()", funcName)
                            } ?: logger.error("Step implementation not found for: '${step.description}'", scenarioFunc)
                        }
                    }.build()
                    addFunction(testFunSpec)
                }
            }.build()
            addType(classSpec)
        }.build()

        // Write the generated file to the build directory.
        // The `Dependencies` object tells KSP which source file this generated file depends on,
        // which is essential for correct incremental compilation.
        fileSpec.writeTo(
            codeGenerator = codeGenerator,
            dependencies = Dependencies(aggregating = false, originalTestClass.containingFile!!)
        )
    }
}

// Entry point for the processor
// Need to be registered in gherkinprocessor/resources/META-INF/services/com.google.devtools.ksp.processing.SymbolProcessorProvider
// com.effatheresoft.gherkinprocessor.ScenarioProcessorProvider
class ScenarioProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return ScenarioProcessor(environment.codeGenerator, environment.logger)
    }
}
```