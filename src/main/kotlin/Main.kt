package org.example

import java.io.File
import kotlin.math.abs
import kotlin.math.log10

fun main() {
    val input = File("input.txt").readLines()

    val indexedNumberLines = input.map { it.parseNumbersWithIndices() }
    val symbolIndexLines = input.map { it.findSymbolIndices() }

    val sumOfGearRatios: Int = symbolIndexLines.flatMapIndexed { lineIndex, symbolIndices ->
        when (lineIndex) {
            0 -> {
                val adjacentNumberLines = indexedNumberLines.take(2)

                symbolIndices.map {
                    it.adjacentNumbersInSameLine(adjacentNumberLines[0]) +
                            it.adjacentNumbersInNeighbouringLine(adjacentNumberLines[1])
                }.filter { it.size == 2 }.map { it[0] * it[1] }
            }

            symbolIndexLines.lastIndex -> {
                val adjacentNumberLines = indexedNumberLines.takeLast(2)

                symbolIndices.map {
                    it.adjacentNumbersInNeighbouringLine(adjacentNumberLines[0]) +
                            it.adjacentNumbersInSameLine(adjacentNumberLines[1])
                }.filter { it.size == 2 }.map { it[0] * it[1] }
            }

            else -> {
                val adjacentNumberLines = listOf(
                    indexedNumberLines[lineIndex - 1],
                    indexedNumberLines[lineIndex],
                    indexedNumberLines[lineIndex + 1]
                )

                symbolIndices.map {
                    it.adjacentNumbersInNeighbouringLine(adjacentNumberLines[0]) +
                            it.adjacentNumbersInSameLine(adjacentNumberLines[1]) +
                            it.adjacentNumbersInNeighbouringLine(adjacentNumberLines[2])
                }.filter { it.size == 2 }.map { it[0] * it[1] }
            }
        }
    }.sumOf { it }

    println(sumOfGearRatios)
}

val numberRegex = """\d+""".toRegex()

private fun String.parseNumbersWithIndices(): List<IndexedValue<Int>> = numberRegex.findAll(this).map {
    IndexedValue(
        index = it.range.first,
        value = it.value.toInt(),
    )
}.toList()

val symbolRegex = """\*""".toRegex()

fun String.findSymbolIndices(): List<Int> = symbolRegex.findAll(this).map { it.range.first }.toList()

fun Int.adjacentNumbersInSameLine(indexedNumbers: List<IndexedValue<Int>>): List<Int> =
    indexedNumbers.filter { it.index - 1 == this || it.index + it.value.decimalPositions == this }.map { it.value }

fun Int.adjacentNumbersInNeighbouringLine(indexedNumbers: List<IndexedValue<Int>>): List<Int> =
    indexedNumbers.filter { this in it.index - 1..it.index + it.value.decimalPositions }.map { it.value }

val Int.decimalPositions
    get() = when (this) {
        0 -> 1
        else -> log10(abs(toDouble())).toInt() + 1
    }
