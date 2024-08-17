package ru.otus.contacts.state

import com.motorro.commonstatemachine.CommonStateMachine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.jetbrains.compose.resources.StringResource
import org.kodein.mock.Mock
import org.kodein.mock.UsesMocks
import org.kodein.mock.tests.TestsWithMocks
import ru.otus.contacts.ResourceWrapper
import ru.otus.contacts.data.UiGesture
import ru.otus.contacts.data.UiState
import ru.otus.contacts.database.ContactsDb
import ru.otus.contacts.database.ContactsDbProvider
import kotlin.test.AfterTest

@ExperimentalCoroutinesApi
@UsesMocks(
    CommonStateMachine::class,
    ContactsFactory::class,
    ContactsDb::class,
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

    @Mock
    lateinit var db: ContactsDb

    protected lateinit var context: ContactsContext
    protected lateinit var nextState: ContactsState
    protected lateinit var dbProvider: ContactsDbProvider

    private fun init() {
        every { stateMachine.setMachineState(isNotNull()) } returns Unit
        every { stateMachine.setUiState(isNotNull()) } returns Unit

        context = object : ContactsContext {
            override val factory: ContactsFactory get() = this@BaseStateTest.factory
            override val resourceWrapper: ResourceWrapper = object : ResourceWrapper {
                override suspend fun getString(resource: StringResource, vararg args: Any): String {
                    return STRING_RESOURCE
                }
            }
        }
        nextState = object : ContactsState() {
            override fun doStart() = Unit
            override fun doProcess(gesture: UiGesture) = Unit
        }
        dbProvider = object : ContactsDbProvider {
            override suspend fun getDb(): ContactsDb = db
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

    companion object {
        const val STRING_RESOURCE = "some string"
    }
}