package com.davidspartan.androidflipcardgame.model

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class debouncedClickHandler(
    private val coroutineScope: CoroutineScope,
    private val delayMillis: Long = 1000 // Adjust the delay as needed
) {
    private var job: Job? = null

    fun onClick(block: () -> Unit) {
        job?.cancel() // Cancel the previous job if it exists
        job = coroutineScope.launch {
            delay(delayMillis) // Wait for the specified delay
            block() // Execute the block after the delay
        }
    }
}