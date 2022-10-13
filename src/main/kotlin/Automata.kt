import java.io.File

class Automata(
    private val numOfStates: Int, private val sizeOfABC: Int,
    private val states: List<List<Int>>, private val startStates: List<Int>,
    private val endStates: List<Int>, private val stringToCheck: MutableList<Int> = emptyList<Int>().toMutableList(),
) {
    init {
        check(
            states.all { it.size == 3 } && states.all { state -> (state.first() in 0 until numOfStates) && (state.last() in 0 until numOfStates) }
        ) { throw WrongAutomataException() }

        check(stringToCheck.all { it in 0 until sizeOfABC }) { throw WrongStringAlphabetException() }
    }

    private val adj = MutableList(numOfStates) { q_i ->
        states.filter { state -> state.first() == q_i }.map { elem -> elem[2] to elem[1] }.toMutableList()
    }

    private val used = Array(adj.size) { false }

    init {
        unreachableStatesDfs(startStates.first())
        adj.mapIndexed { i, _ -> if (!used[i]) adj.removeAt(i) }
    }

    private fun transitionFun(q: Int, a: Int) = adj[q].first { it.second == a }.first

    private fun minimizeStates(): MutableList<MutableSet<Int>> {
        val R = mutableListOf(((0 until numOfStates) - endStates.toSet()).toMutableSet(), (endStates.toMutableSet()))

        var prevSize = -1
        while (R.size != prevSize) {
            var hasChangedR = false

            for (r_i in R) {
                for (a in 0 until sizeOfABC) {
                    if (r_i.map { q -> transitionFun(q, a) }.toMutableSet() !in R) {
                        prevSize = R.size
                        R.remove(r_i)

                        r_i.groupBy { q ->
                            R.indexOfFirst { class_i -> transitionFun(q, a) in class_i }
                        }.values.map { subclass -> R.add(subclass.toMutableSet()) }

                        hasChangedR = true
                        break
                    }
                }
                if (hasChangedR) break
            }
        }

        return R
    }

    private fun getMinimizedDFA(R: MutableList<MutableSet<Int>>): Triple<Int, MutableList<Int>, MutableList<MutableList<Pair<Int, Int>>>> {
        val minimizedNumOfStates = R.size

        val minimizedEndStates = mutableListOf<Int>()
        R.forEach { class_i -> if (class_i.first() in endStates) minimizedEndStates.add(R.indexOf(class_i)) }

        val minimizedAdj = MutableList(minimizedNumOfStates) { i ->
            R[i].flatMap { q ->
                adj[q].map { pair -> R.indexOfFirst { class_i -> pair.first in class_i } to pair.second }
            }.toSet().toMutableList()
        }

        return Triple(minimizedNumOfStates, minimizedEndStates, minimizedAdj)
    }

    fun saveMinimizedDFAInFileFormat(fileName: String) { // На самом деле, ее можно было сделать более общей, ну да пусть
        val (minimizedNumOfStates, minimizedEndStates, minimizedAdj) = getMinimizedDFA(minimizeStates())
        val strBuilder = StringBuilder()
        strBuilder.append("$minimizedNumOfStates\n$sizeOfABC\n0\n${minimizedEndStates.joinToString(" ")}\n")
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