package ru.otus.contacts.utils

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SuspendLazyTest {
    @Test
    fun lazyCreatesValue() = runTest {
        var wasCalled = false
        val lazy = SuspendLazy {
            wasCalled = true
            "String"
        }

        assertFalse { wasCalled }
        assertEquals("String", lazy())
        assertTrue { wasCalled }
    }

    @Test
    fun callsFactoryOnce() = runTest {
        var wasCalled = 0
        val lazy = SuspendLazy {
            ++wasCalled
            "String"
        }

        assertEquals(0, wasCalled)
        assertEquals("String", lazy())
        assertEquals("String", lazy())
        assertEquals(1, wasCalled)
    }
}