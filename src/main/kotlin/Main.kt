package org.example

import java.io.File

fun main() {
    val input = File("input.txt").readLines()

    val numbersWithPositions = input.first().parseNumbersWithPositions()
    println(numbersWithPositions)
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
