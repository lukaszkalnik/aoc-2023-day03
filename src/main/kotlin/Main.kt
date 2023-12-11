package org.example

import java.io.File

fun main() {
    val input = File("input.txt").readLines()

    val indexedNumbers = input.map { it.parseNumbersWithPositions() }
    val symbolIndices = input[1].findSymbolIndices()
    println(symbolIndices)

//    indexedNumbers.filterIndexed { lineIndex, indexedNumber ->
//        when {
//            lineIndex == 0 -> {
//                val inputLines = input.take(2)
//
//            }
//            lineIndex == indexedNumbers.lastIndex -> {
//                val inputLines = input.takeLast(2)
//            }
//            else -> {
//                val inputLines = listOf(input[lineIndex - 1], input[lineIndex], input[lineIndex + 1])
//            }
//        }
//    }
}

private fun String.parseNumbersWithPositions(): List<IndexedValue<Int>> {
    val numbersWithPositions = mutableListOf<IndexedValue<Int>>()
    val iterator = iterator().withIndex()

    while (iterator.hasNext()) {
        var numberString = ""
        var index = 0
        val indexedChar = iterator.next()

        if (indexedChar.value.isDigit()) {
            numberString += indexedChar.value
            index = indexedChar.index

            while (iterator.hasNext()) {
                val nextIndexedChar = iterator.next()
                if (nextIndexedChar.value.isDigit()) {
                    numberString += nextIndexedChar.value
                } else {
                    numbersWithPositions.add(
                        IndexedValue(
                            value = numberString.toInt(),
                            index = index,
                        )
                    )
                    break
                }
            }
        }
    }

    return numbersWithPositions
}

fun String.findSymbolIndices(): List<Int> = mapIndexedNotNull { index, char ->
    if ("""[^\d.]""".toRegex().matches(char.toString())) index else null
}

fun IndexedValue<Int>.checkIfSymbolInAnotherLine(inputLine: String): Boolean = true
