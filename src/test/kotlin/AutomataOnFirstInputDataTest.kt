import org.junit.Test
import kotlin.test.assertEquals


/*
1.txt:

Автомат взят из домашнего примера
* */

internal class AutomataOnFirstInputDataTest {
    @Test
    fun acceptStringTest1() {
        assertEquals(UI().runStringThroughAutomata("data/1.txt", mutableListOf(1, 1, 0, 0)), true)
    }

    @Test
    fun acceptStringTest2() {
        assertEquals(UI().runStringThroughAutomata("data/1.txt", mutableListOf(0)), true)
    }

    @Test
    fun acceptStringTest3() {
        assertEquals(UI().runStringThroughAutomata("data/1.txt", mutableListOf(0, 0, 0)), true)
    }

    @Test
    fun rejectStringTest1() {
        assertEquals(UI().runStringThroughAutomata("data/1.txt", mutableListOf(0, 1, 1, 1, 1, 1)), false)
    }

    @Test
    fun rejectStringTest2() {
        assertEquals(UI().runStringThroughAutomata("data/1.txt", mutableListOf(0, 0)), false)
    }
}