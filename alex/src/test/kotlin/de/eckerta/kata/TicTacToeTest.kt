package de.eckerta.kata

import org.amshove.kluent.`should equal`
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.jetbrains.spek.data_driven.data
import org.jetbrains.spek.data_driven.on

class ParserTest : Spek({
    val expected = listOf(
            listOf(Field.EMPTY, Field.EMPTY, Field.EMPTY),
            listOf(Field.X, Field.O, Field.EMPTY),
            listOf(Field.EMPTY, Field.EMPTY, Field.X))

    given ( "a board description with leading or trailing whitespace on a line" ) {
        // invalid character M and extra whitespace in 2nd line should be ignored
        val board = "...\nXO.  \n  ..X"

        on("parsing") {
            val parsed = board.toFields()

            it ("these white space characters should be ignored") {
                parsed `should equal` expected
            }
        }
    }

    given ( "a board description without white space characters" ) {
        // invalid character M and extra whitespace in 2nd line should be ignored
        val board = "..M\nXO.\n@.X"

        on("parsing") {
            val parsed = board.toFields()

            it ("everything except X and O should be treated as empty field") {
                parsed `should equal` expected
            }
        }
    }
})

class LineTest : Spek({
    val cases = arrayOf(
            data("...", State.UNFINISHED),
            data("..X", State.UNFINISHED),
            data("..O", State.UNFINISHED),
            data(".X.", State.UNFINISHED),
            data(".XX", State.UNFINISHED),
            data(".XO", State.UNFINISHED),
            data(".O.", State.UNFINISHED),
            data(".OX", State.UNFINISHED),
            data(".OO", State.UNFINISHED),
            data("X..", State.UNFINISHED),
            data("X.X", State.UNFINISHED),
            data("X.O", State.UNFINISHED),
            data("XX.", State.UNFINISHED),
            data("XXX", State.X),
            data("XXO", State.DRAW),
            data("XO.", State.UNFINISHED),
            data("XOX", State.DRAW),
            data("XOO", State.DRAW),
            data("O..", State.UNFINISHED),
            data("O.X", State.UNFINISHED),
            data("O.O", State.UNFINISHED),
            data("OX.", State.UNFINISHED),
            data("OXX", State.DRAW),
            data("OXO", State.DRAW),
            data("OO.", State.UNFINISHED),
            data("OOX", State.DRAW),
            data("OOO", State.O)
    )

    on("getting the de.eckerta.kata.state of %s", with = *cases) { lineSpec, state ->
        val line = lineSpec.toFields()[0]
        it("should equal $state") {
            line.state() `should equal` state
        }
    }
})

class BoardTest : Spek ({
    given ("a board") {
        val boardSpec = """
                ..X
                XO.
                .XO
            """.trimIndent()

        val board = Board(boardSpec.toFields())

        it("should match the columns") {
            val columnSpec = """
                .X.
                .OX
                X.O
                """.trimIndent()

            board.columns `should equal` columnSpec.toFields()
        }

        it ("should match the diagonals") {
            val diagonalsSpec = """
                .OO
                XO.
                """.trimIndent()

            board.diagonals `should equal` diagonalsSpec.toFields()
        }
    }

    given ("a board with horizontal winning position for O") {
        val boardSpec = """
            XO.
            XOX
            .O.
            """.trimIndent()

        it ("should have de.eckerta.kata.state X") {
            Board(boardSpec.toFields()).state `should equal` State.O
        }
    }

    given ("a full board with vertical winning position for X") {
        val boardSpec = """
            OOX
            XOX
            OXX
            """.trimIndent()

        it ("should have de.eckerta.kata.state O") {
            Board(boardSpec.toFields()).state `should equal` State.X
        }
    }

    given ("a board with diagonal winning position for X") {
        val boardSpec = """
            XOO
            OXX
            OXX
            """.trimIndent()

        it ("should have de.eckerta.kata.state O") {
            Board(boardSpec.toFields()).state `should equal` State.X
        }
    }

    given ("a board with diagonal winning position for O") {
        val boardSpec = """
            XOO
            XOX
            OXX
            """.trimIndent()

        it ("should have de.eckerta.kata.state O") {
            Board(boardSpec.toFields()).state `should equal` State.O
        }
    }

    given ("a full board with no position") {
        val boardSpec = """
            OXX
            XOO
            OXX
            """.trimIndent()

        it ("should have de.eckerta.kata.state O") {
            Board(boardSpec.toFields()).state `should equal` State.DRAW
        }
    }

    given ("a board (not full) with no winning position") {
        val boardSpec = """
            OXX
            XO.
            OXX
            """.trimIndent()

        it ("should have de.eckerta.kata.state O") {
            Board(boardSpec.toFields()).state `should equal` State.UNFINISHED
        }
    }
})