package xget.dev.jet.remote.util

import android.util.Log
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockkStatic
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext

class LogMockExtension : BeforeEachCallback {
    override fun beforeEach(context: ExtensionContext?) {
        println("clearing mocks...")
        clearAllMocks()

        mockkStatic(Log::class)
        every { Log.v(any(), any(),any()) } returns 0
        every { Log.d(any(), any(),any()) } returns 0
        every { Log.i(any(), any(),any()) } returns 0
        every { Log.e(any(), any(),any()) } returns 0
        every { Log.v(any(), any()) } returns 0
        every { Log.d(any(), any()) } returns 0
        every { Log.i(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
    }
}
