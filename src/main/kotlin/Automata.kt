class Automata(
    numOfStates: Int, sizeOfABC: Int,
    private val states: List<List<Int>>, private val startStates: List<Int>,
    private val endStates: List<Int>, private val stringToCheck: MutableList<Int>,
) {

    init {
        check(
            states.all { it.size == 3 } && states.all { state -> (state.first() in 0 until numOfStates) && (state.last() in 0 until numOfStates) }
        ) { throw WrongAutomataException() }

        check(stringToCheck.all { it in 0 until sizeOfABC }) { throw WrongStringAlphabetException() }
    }

    private val adj = List(numOfStates) { q_i ->
        states.filter { state -> state.first() == q_i }.map { elem -> elem[2] to elem[1] }.toMutableList()
    }

    private fun dfs(q: Int, str_ind: Int): Boolean {
        if (str_ind != stringToCheck.size) {
            val currSymbol = stringToCheck[str_ind]
            for (u in adj[q]) {
                if (currSymbol == u.second) {
                    if (dfs(u.first, str_ind + 1)) {
                        return true
                    }
                }
            }
        } else {
            return (q in endStates)
        }
        return false
    }

    fun nfa() = startStates.any { dfs(it, 0) }
}