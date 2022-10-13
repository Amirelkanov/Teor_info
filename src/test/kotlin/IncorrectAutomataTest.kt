import org.junit.Test
import kotlin.test.assertFailsWith


internal class IncorrectAutomataTest {
    @Test
    fun incorrectStringAlphabetTest() {
        assertFailsWith<WrongStringAlphabetException> {
            Automata(3, 2,
                listOf(
                    listOf(0, 0, 1),
                    listOf(0, 0, 2),
                    listOf(0, 1, 2),
                    listOf(1, 0, 2),
                    listOf(2, 1, 2),
                    listOf(2, 0, 0),
                ),
                listOf(0), listOf(1),
                mutableListOf(3, 3, 3, 3))
        }
    }

    @Test
    fun incorrectAutomataStateLenTest() {
        assertFailsWith<WrongAutomataException> {
            Automata(3, 2,
                listOf(
                    listOf(0, 0, 1),
                    listOf(0, 0, 2),
                    listOf(0, 1),
                    listOf(1, 0, 2),
                    listOf(2, 1, 2),
                    listOf(2, 0, 0),
                ),
                listOf(0), listOf(1),
                mutableListOf(1, 1, 0, 0))
        }
    }

    @Test
    fun incorrectAutomataAlphabetTest() {
        assertFailsWith<WrongAutomataException> {
            Automata(3, 2,
                listOf(
                    listOf(0, 0, 1),
                    listOf(0, 0, 2),
                    listOf(0, 1),
                    listOf(1, 0, 2),
                    listOf(2, 1, 2),
                    listOf(2, 0, 0),
                ),
                listOf(0), listOf(1),
                mutableListOf(1, 1, 0, 0))
        }
    }
}