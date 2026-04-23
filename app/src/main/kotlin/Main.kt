package com.example.app

fun main() {

    // ----------- PARTE 1 (Greeting) -----------
    val myClass = MyClass()
    val wrapped = MyClassWrapper(myClass)

    wrapped.sayHello()
    wrapped.compute()

    println("-----")

    // ----------- PARTE 2 (Regex) -----------
    val input = "Name: John Address: 123 Street"

    val extractor = DataProcessorExtractor(input)

    println("Name: ${extractor.getName()}")
    println("Address: ${extractor.getAddress()}")
}
