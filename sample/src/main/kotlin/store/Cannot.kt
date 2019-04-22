package store

import java.lang.Exception

/** Something cannot be done. */
open class Cannot(doThis: String) : Exception(doThis)
