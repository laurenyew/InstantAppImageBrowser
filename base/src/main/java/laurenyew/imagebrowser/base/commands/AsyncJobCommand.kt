package laurenyew.imagebrowser.base.commands

import android.support.annotation.VisibleForTesting

/**
 * Base abstract class to run commands.
 * Execute will run the command implementation in a background runnable thread
 * It's then up to the command handle update logic / actual logic
 * Splitting up for use in unit tests
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