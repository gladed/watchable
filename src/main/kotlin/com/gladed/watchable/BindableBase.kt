/*
 * (c) Copyright 2019 Glade Diviney.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gladed.watchable

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import java.lang.IllegalStateException

/**
 * A utility implementation of [Bindable] which uses [onChange] to effect change.
 */
class BindableBase<T, C : Change<T>>(
    private val owner: Watchable<T, C>,
    private val onChange: (C) -> Unit
) : Bindable<T, C>, CoroutineScope by owner {
    /** Binding, if any. */
    private var binding: Job? = null

    private var isOnChange: Boolean = false

    /** Source for this binding. */
    override var boundTo: Watchable<T, C>? = null
        private set(value) { field = value }

    /** Throw if this is not a good time to change the owner object. */
    fun checkChange() {
        if (isBound() && !isOnChange) {
            throw IllegalStateException("A bound object may not be modified.")
        }
    }

    override fun bind(other: Watchable<T, C>) {
        if (binding != null) throw IllegalStateException("Object already bound")

        // Chase up the parent stack
        var parent = other as? Bindable<*, *>
        while (parent != null) {
            if (parent == owner) throw IllegalStateException("Circular binding not permitted")
            parent = parent.boundTo as? Bindable<*, *>
        }

        boundTo = other

        // Perform the binding
        binding = watch(other) {
            isOnChange = true
            onChange(it)
            isOnChange = false
        }
    }

    override fun unbind() {
        binding?.also {
            it.cancel()
            binding = null
            boundTo = null
        }
    }
}
