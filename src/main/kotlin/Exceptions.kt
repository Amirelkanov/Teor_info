class WrongAutomataException : Exception("Incorrect automaton format.")

class WrongStringFormat : Exception("Incorrect string format.")

class WrongStringAlphabetException : Exception("The string uses characters not from the alphabet of the automaton.")
class WrongCommandException: Exception("Wrong command. Try again")