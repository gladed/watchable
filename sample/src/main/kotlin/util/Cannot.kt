package util

class Cannot(doSomething: String) : Exception()

fun cannot(doSomething: String): Nothing = throw Cannot(doSomething)