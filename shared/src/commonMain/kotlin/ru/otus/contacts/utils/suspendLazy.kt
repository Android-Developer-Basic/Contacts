package ru.otus.contacts.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

@OptIn(DelicateCoroutinesApi::class)
class SuspendLazy<T : Any>(scope: CoroutineScope = GlobalScope, block: suspend () -> T) {
    private val value = scope.async(Dispatchers.Unconfined, start = CoroutineStart.LAZY) {
        withContext(NonCancellable) {
            block()
        }
    }

    suspend operator fun invoke(): T = value.await()
}