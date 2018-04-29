package laurenyew.imagebrowser.base.commands

import com.nhaarman.mockito_kotlin.spy
import com.nhaarman.mockito_kotlin.verify
import laurenyew.imagebrowser.base.BuildConfig
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLooper

/**
 * Verify AsyncJobCommand logic
 */
@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class AsyncJobCommandTest {
    private lateinit var command: AsyncJobCommand

    @Before
    fun setup() {
        command = spy(MyAsyncJobCommand())
    }

    @Test
    fun `given command is executed, commandImpl should be run and the job thread should be started`() {
        /** Exercise **/
        command.execute()

        /** Verify **/
        val jobThread = command.job
        assertNotNull(jobThread)
        assertTrue(jobThread!!.isAlive)

        /** Exercise **/
        //Run the thread
        ShadowLooper.runUiThreadTasksIncludingDelayedTasks()

        /** Verify **/
        verify(command).executeCommandImpl()
    }

    @Test
    fun `given command is cancelled, the job thread should be interrupted`() {
        /** Arrange **/
        command.execute()

        /** Exercise **/
        command.cancel()

        /** Verify **/
        val jobThread = command.job
        assertNull(jobThread)
    }

    //region Helper Classes
    open class MyAsyncJobCommand : AsyncJobCommand() {
        override fun executeCommandImpl() {
        }
    }
    //endregion
}