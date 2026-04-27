# Tutorial 3.1 & 3.2: Kotlin Greeting & Regex Annotation Processors

Course: Mobile Computing

Student(s): Tomás Santos

Date: 2026-04-22

Repository URL: github.com/tomassantos2025/GreetingProcessorProject

---

## 1. Introduction

This assignment explores compile-time annotation processing in Kotlin through the implementation of custom annotations and their respective processors. The main goal is to reduce repetitive boilerplate code by generating functionality automatically during compilation.

Two annotation-based solutions were developed:

* `@Greeting`: generates wrapper methods that print a custom message before executing the original method.
* `@Extract`: generates implementations of abstract methods that extract data from a string using regular expressions.

The project follows the required multi-module architecture: one module defines annotations, another implements processors, and a third demonstrates usage. This structure highlights a declarative programming approach using Kotlin, KAPT, and KotlinPoet.

---

## 2. System Overview

The system is composed of three main modules:

* `annotations`: defines the `@Greeting` and `@Extract` annotations.
* `processor`: implements the annotation processors responsible for code generation.
* `app`: demonstrates the generated code through practical examples.

### Main features:

* Custom annotations applied at function level.
* Compile-time code generation using KAPT.
* Automatic generation of wrapper and extractor classes.
* Demonstration of generated code in a runnable application.

Two main use cases are implemented:

### Greeting Processor

* Annotated methods trigger generation of a wrapper class.
* The wrapper prints a message before delegating execution.

### Regex Processor

* Abstract methods annotated with `@Extract` are automatically implemented.
* Generated code uses regular expressions to extract values from input strings.

---

## 3. Architecture and Design

The project follows a clear separation of concerns:

```text
GreetingProcessorProject/
|-- annotations/
|   |-- Greeting.kt
|   |-- Extract.kt
|-- processor/
|   |-- GreetingProcessor.kt
|   |-- RegexProcessor.kt
|-- app/
|   |-- Main.kt
|   |-- MyClass.kt
|   |-- DataProcessor.kt
|-- build.gradle.kts
```

### Design decisions:

* `AnnotationTarget.FUNCTION` ensures annotations are applied only to methods.
* `AnnotationRetention.SOURCE` is sufficient since processing occurs at compile-time.
* KAPT integrates annotation processing into the Kotlin build system.
* `AutoService` simplifies processor registration.
* KotlinPoet provides a structured approach to code generation.

### Architectural approach:

* **GreetingProcessor** uses composition:

    * Generates a wrapper class (`MyClassWrapper`)
    * Delegates calls to the original instance

* **RegexProcessor** uses inheritance:

    * Generates a subclass (`DataProcessorExtractor`)
    * Implements abstract methods dynamically

This distinction demonstrates two different design strategies in code generation.

---

## 4. Implementation

### Greeting Annotation

```kotlin
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class Greeting(val message: String)
```

### Extract Annotation

```kotlin
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class Extract(val regex: String)
```

---

### Greeting Processor Workflow:

1. Locate methods annotated with `@Greeting`.
2. Group methods by enclosing class.
3. Generate a wrapper class.
4. Replicate method signatures.
5. Print the greeting message.
6. Delegate execution to the original object.

---

### Regex Processor Workflow:

1. Locate abstract methods annotated with `@Extract`.
2. Identify the enclosing abstract class.
3. Generate a subclass (`Extractor`).
4. Implement methods using `Regex`.
5. Return extracted values from the input string.

---

### Example Usage

#### Greeting:

```kotlin
@Greeting("Hello from MyClass!")
fun sayHello() { ... }
```

Generated:

```kotlin
println("Hello from MyClass!")
original.sayHello()
```

---

#### Regex:

```kotlin
@Extract(regex = "Name: (\\w+)")
abstract fun getName(): String?
```

Generated:

```kotlin
val match = Regex("Name: (\\w+)").find(input)
return match?.groupValues?.get(1)
```

---

## 5. Testing and Validation

Validation was performed through:

* Successful build execution:

```bash
.\gradlew.bat build
```

* Inspection of generated files:

```
app/build/generated/source/kaptKotlin/
```

* Manual execution of the application.

### Observed output:

```text
Hello from MyClass!
Executing sayHello method
Welcome to compute!
Computing something...
-----
Name: John
Address: 123 Street
```

This confirms both processors work as expected.

### Known limitations:

* No automated tests (JUnit).
* Greeting wrapper does not return values.
* Limited support for advanced cases (generics, overloads, suspend functions).

---

## 6. Usage Instructions

### Requirements

* JDK 17
* IntelliJ IDEA
* Gradle

### Setup

```powershell
git clone https://github.com/tomassantos2025/GreetingProcessorProject.git
cd GreetingProcessorProject
.\gradlew.bat build
```

### Execution

Run `Main.kt` inside IntelliJ.

---

# Autonomous Software Engineering Sections

## 7. Prompting Strategy

AI was used to refine documentation structure and align it with the actual project implementation.

Prompts evolved from generic templates to context-aware instructions, improving accuracy and relevance.

---

## 8. Autonomous Agent Workflow

1. Inspect repository structure.
2. Analyze modules and source code.
3. Validate generated files.
4. Build project.
5. Execute demo.
6. Generate documentation.

---

## 9. Verification of AI-Generated Artifacts

All statements were verified against:

* Source code
* Generated files
* Build output

---

## 10. Human vs AI Contribution

* Human: implementation and project setup
* AI: documentation, validation, structuring

---

## 11. Ethical and Responsible Use

* Avoided fabricated content
* Verified all claims
* Explicitly documented limitations

---

# Development Process

## 12. Version Control and Commit History

The project follows a structured commit strategy using conventional commits:

* `feat(annotations)` – annotation definitions
* `feat(processor)` – processor implementations
* `test(app)` – validation usage

This ensures traceability and clarity in development progression.

---

## 13. Difficulties and Lessons Learned

Main challenges:

* Correct configuration of KAPT
* Processor registration
* Debugging generated code

Key lessons:

* Compile-time code generation reduces boilerplate
* Multi-module architecture improves maintainability
* KotlinPoet simplifies structured generation

---

## 14. Future Improvements

* Support return values in generated methods
* Add automated tests
* Improve edge-case handling
* Explore KSP as alternative to KAPT

---

## 15. AI Usage Disclosure (Mandatory)

AI tools used:

* ChatGPT (OpenAI)

Used for:

* Documentation
* Code explanation
* Validation support

> Final responsibility remains with the author.
