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

package io.gladed.watchable

/**
 * Defines special values for watcher timing.
 *
 * When period is >0, changes are collected and delivered no more frequently than that many milliseconds.
 */
object Period {
    /**
     * A watcher with this period run very soon after the change is made. This is the default for all
     * watching operations.
     */
    const val IMMEDIATE = 0L

    /**
     * A watcher that runs before the change is fully applied. If it throws the change will be rolled
     * back and the exception re-thrown at the site of the change.
     */
    const val INLINE = -1L
}
