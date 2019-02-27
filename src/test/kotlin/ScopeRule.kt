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

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class ScopeRule(dispatcher: CoroutineDispatcher) : TestRule, CoroutineScope {

    override val coroutineContext = dispatcher + Job() + CoroutineExceptionHandler { _, cause -> log(cause.toString())}

    override fun apply(base: Statement, description: Description) = object : Statement() {
        override fun evaluate() {
            try {
                base.evaluate()
            } finally {
                coroutineContext[Job]!!.cancel()
            }
        }
    }
}
