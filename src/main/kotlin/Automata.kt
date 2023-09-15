import java.io.File

class Automata(
    private var numOfStates: Int, private val sizeOfABC: Int,
    private val states: List<List<Int>>, private val startStates: List<Int>,
    private val endStates: List<Int>, private val stringToCheck: MutableList<Int> = emptyList<Int>().toMutableList(),
) {
    init {
        check(
            states.all { it.size == 3 } && states.all { state -> (state.first() in 0 until numOfStates) && (state.last() in 0 until numOfStates) }
        ) { throw WrongAutomataException() }

        check(stringToCheck.all { it in 0 until sizeOfABC }) { throw WrongStringAlphabetException() }
    }

    private var adj = MutableList(numOfStates) { q_i ->
        states.filter { state -> state.first() == q_i }.map { elem -> elem[2] to elem[1] }.toMutableList()
    }

    private val used = Array(numOfStates) { false }

    init {
        unreachableStatesDfs(startStates.first())
        adj = adj.filterIndexed { i, _ -> used[i] }.toMutableList()
        numOfStates = adj.size
    }

    private fun transitionFun(q: Int, a: Int) = adj[q].firstOrNull { it.second == a }?.first

    private fun minimizeStates(): MutableList<MutableSet<Int>> {
        val R = mutableListOf(((0 until numOfStates) - endStates.toSet()).toMutableSet(), (endStates.toMutableSet()))

        var prevSize = -1
        while (R.size != prevSize) {
            for (r_i in R) {
                R.remove(r_i)
                r_i.groupBy { q ->
                    (0 until sizeOfABC).map { a ->
                        (transitionFun(q, a) == null) to R.indexOfFirst { class_i ->
                            (transitionFun(q, a) in class_i && transitionFun(q, a) != null)
                        }
                    }
                }.values.map { subclass -> R.add(subclass.toMutableSet()) }
                prevSize = R.size
                break
            }
        }
        return R
    }

    private fun getMinimizedDFA(R: MutableList<MutableSet<Int>>): Triple<Int, Pair<Int, MutableList<Int>>, MutableList<MutableList<Pair<Int, Int>>>> {
        val minimizedNumOfStates = R.size

        val minimizedStartState = R.indexOfFirst { class_i -> class_i.any { it == startStates.first() } }
        val minimizedEndStates = mutableListOf<Int>()
        R.forEach { class_i -> if (class_i.first() in endStates) minimizedEndStates.add(R.indexOf(class_i)) }

        val minimizedAdj = MutableList(minimizedNumOfStates) { i ->
            R[i].flatMap { q ->
                adj[q].map { pair -> R.indexOfFirst { class_i -> pair.first in class_i } to pair.second }
            }.toSet().toMutableList()
        }

        return Triple(minimizedNumOfStates, minimizedStartState to minimizedEndStates, minimizedAdj)
    }

    fun saveMinimizedDFAInFileFormat(fileName: String) { // На самом деле, ее можно было сделать более общей, ну да пусть
        val (minimizedNumOfStates, minimizedStartAndEndStates, minimizedAdj) = getMinimizedDFA(minimizeStates())
        val strBuilder = StringBuilder()
        strBuilder.append("$minimizedNumOfStates\n$sizeOfABC\n${minimizedStartAndEndStates.first}\n${
            minimizedStartAndEndStates.second.joinToString(" ")
        }\n")
        minimizedAdj.mapIndexed { i, neighbors ->
            neighbors.map { pair -> strBuilder.append("$i ${pair.second} ${pair.first}\n") }
        }
        File(fileName).writeText(strBuilder.toString())
    }

    private fun unreachableStatesDfs(q: Int) {
        used[q] = true
        for (u in adj[q]) {
            if (!used[u.first]) {
                unreachableStatesDfs(u.first)
            }
        }
    }

    private fun automataRunDfs(q: Int, str_ind: Int): Boolean {
        if (str_ind != stringToCheck.size) {
            val currSymbol = stringToCheck[str_ind]
            for (u in adj[q]) {
                if (currSymbol == u.second) {
                    if (automataRunDfs(u.first, str_ind + 1)) {
                        return true
                    }
                }
            }
        } else {
            return (q in endStates)
        }
        return false
    }

    fun nfa() = startStates.any { automataRunDfs(it, 0) }
}