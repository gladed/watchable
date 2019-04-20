package util

import java.lang.Exception

class Cannot(private val doSomething: String) : Exception("Cannot $doSomething") {
    override fun toString(): String = "Cannot $doSomething"
}
