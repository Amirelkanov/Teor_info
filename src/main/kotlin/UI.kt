import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader

class UI {

    private fun parseAutomataFromFile(fileName: String): List<List<List<Int>>> {
        val input = FileReader(fileName).readLines().map { stateLine -> stateLine.split(' ').map { it.toInt() } }
        return listOf(input.take(4), input.takeLast(input.size - 4))
    }

    fun runStringThroughAutomata(fileName: String, stringToCheck: MutableList<Int>): Boolean {
        val (initValues, states) = parseAutomataFromFile(fileName)
        val (numOfStates, sizeOfABC, startStates, endStates) = initValues

        return Automata(numOfStates.first(), sizeOfABC.first(), states, startStates, endStates, stringToCheck).nfa()
    }

    fun readAutomataInput() {
        print("Enter fiction automata filename: ")
        val filename = readLine()
        check(!filename.isNullOrEmpty() && File(filename).isFile && File(filename).exists()) {
            throw FileNotFoundException("File not found.")
        }

        print("Enter string needs to be checked (write the chars of the string separated by a space): ")
        val stringToCheck =
            (readLine()?.split(' ')?.map { it.toInt() } ?: throw WrongStringFormat()) as MutableList<Int>
        println("String '${stringToCheck.joinToString(" ")}' has been " +
                if (runStringThroughAutomata(filename, stringToCheck)) "accepted!" else "rejected.")
    }
}
