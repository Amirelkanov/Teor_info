import org.junit.Test
import kotlin.test.assertEquals

/*
2.txt:

Автомат принимает только строку 101:

(q_0) --[1]--> (q_1) --[0]--> (q_2) --> ((q3)) --[0, 1]--> (DEAD_STATE)

*/

internal class AutomataOnSecondInputDataTest {
    @Test
    fun acceptStringTest1() {
        assertEquals(UI().runStringThroughAutomata("data/2.txt", mutableListOf(1, 0, 1)), true)
    }

    @Test
    fun rejectStringTest1() {
        assertEquals(UI().runStringThroughAutomata("data/2.txt", mutableListOf(1, 1, 1)), false)
    }

    @Test
    fun rejectStringTest2() {
        assertEquals(UI().runStringThroughAutomata("data/2.txt", mutableListOf(1, 0, 1, 1, 1, 1)), false)
    }
}