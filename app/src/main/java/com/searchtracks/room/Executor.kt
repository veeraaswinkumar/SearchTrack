package com.searchtracks.room

import java.util.concurrent.Executors

/*
 *  Use Executor for threading
 */
object Executor {
    fun ioThread(t: Runnable?) {
        val ioExecutor =
            Executors.newSingleThreadExecutor()
        ioExecutor.execute(t)
    }
}