package ru.otus.contacts.state

import com.motorro.commonstatemachine.CommonStateMachine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.kodein.mock.Mock
import org.kodein.mock.UsesMocks
import org.kodein.mock.tests.TestsWithMocks
import ru.otus.contacts.data.UiGesture
import ru.otus.contacts.data.UiState
import kotlin.test.AfterTest

@ExperimentalCoroutinesApi
@UsesMocks(
    CommonStateMachine::class,
    ContactsFactory::class
)
internal abstract class BaseStateTest : TestsWithMocks() {
    override fun setUpMocks() {
        injectMocks(mocker)
        init()
    }

    @Mock
    lateinit var stateMachine: CommonStateMachine<UiGesture, UiState>

    @Mock
    lateinit var factory: ContactsFactory

    protected lateinit var context: ContactsContext
    protected lateinit var nextState: ContactsState

    private fun init() {
        every { stateMachine.setMachineState(isNotNull()) } returns Unit
        every { stateMachine.setUiState(isNotNull()) } returns Unit

        context = object : ContactsContext {
            override val factory: ContactsFactory get() = this@BaseStateTest.factory
        }
        nextState = object : ContactsState() {
            override fun doStart() = Unit
            override fun doProcess(gesture: UiGesture) = Unit
        }

        Dispatchers.setMain(UnconfinedTestDispatcher())

        doInit()
    }

    protected open fun doInit() {

    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }
}