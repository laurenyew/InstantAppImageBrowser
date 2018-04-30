package laurenyew.imagebrowser.base.commands

import android.support.annotation.VisibleForTesting

/**
 * @author Lauren Yew on 04/29/2018.
 *
 * Base abstract class to run commands
 *
 * Execute will run the command implementation in a background runnable thread
 * It's then up to the command handle update logic / actual logic
 *
 * Splitting up as base class for use in unit tests
 */
abstract class AsyncJobCommand {
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var job: Thread? = null

    open fun execute() {
        job = Thread(Runnable {
            executeCommandImpl()
        })
        job?.start()
    }

    abstract fun executeCommandImpl()

    open fun cancel() {
        job?.interrupt()
        job = null
    }
}