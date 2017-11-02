package de.eckerta.kata

enum class State { DRAW, UNFINISHED, X, O}
enum class Field(val state : State) { X(State.X), O(State.O), EMPTY(State.UNFINISHED) }

fun String.toFields() : List<List<Field>> = this.lines().map { it.trim().map { c -> Field.values().find { it.name == c.toString() } ?: Field.EMPTY } }
fun List<Field>.state() : State = Field.values().filter { field -> this.all { it == field } }.firstOrNull()?.state ?: if (this.contains(Field.EMPTY)) State.UNFINISHED else State.DRAW

class Board(val rows : List<List<Field>>) {
    val columns = (0 until rows.size).map { column -> rows.map { it[column] } }
    val diagonals = listOf(rows.mapIndexed { index, fields -> fields[index] }, rows.mapIndexed { index, fields -> fields[rows.size - index - 1] })
    val state : State = (rows + columns + diagonals).map { it.state() }.maxBy { it.ordinal }!!
}