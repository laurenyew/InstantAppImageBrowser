package laurenyew.imagebrowser.base.commands

/**
 * Base abstract class to run commands.
 * Execute will run the command implementation in a background runnable thread
 * It's then up to the command handle update logic / actual logic
 * Splitting up for use in unit tests
 */
abstract class AsyncJobCommand {
    private var job: Thread? = null

    open fun execute() {
        job = Thread(Runnable {
            commandImpl()
        })
        job?.start()
    }

    abstract fun commandImpl()

    open fun cancel() {
        job?.interrupt()
    }
}