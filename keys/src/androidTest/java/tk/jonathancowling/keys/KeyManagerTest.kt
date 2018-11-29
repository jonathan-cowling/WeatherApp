package tk.jonathancowling.keys

import org.hamcrest.CoreMatchers.*
import org.junit.Test

import org.junit.Assert.*

class KeyManagerTest {

    @Test
    fun weatherApi() {
        try {
            assertThat("key is not a non empty string",
                    KeyManager().weatherApi(), allOf(not(nullValue()), not("")))
        } catch (e: UnsatisfiedLinkError) {
            fail("could not link to cpp")
        }
    }
}
