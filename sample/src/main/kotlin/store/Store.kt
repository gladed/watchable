package store

import model.Bird

interface Store {
    /** Create and return a new Bird object for use by the specified scope until it completes. */
    suspend fun makeBird(name: String): Bird

    /** Return the Bird with this id if it exists. */
    suspend fun findBird(id: String): Bird?
}
