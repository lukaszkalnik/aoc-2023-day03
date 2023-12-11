package org.example

import java.io.File
import kotlin.math.abs
import kotlin.math.log10

fun main() {
    val input = File("input.txt").readLines()

    val indexedNumberLines = input.map { it.parseNumbersWithIndices() }
    val symbolIndices = input.map { it.findSymbolIndices() }

    val sumOfSerialNumbers = indexedNumberLines.flatMapIndexed { lineIndex, indexedNumbers ->
        when (lineIndex) {
            0 -> {
                val adjacentSymbolLines = symbolIndices.take(2)

                indexedNumbers.filter {
                    it.isAdjacentSymbolInSameLine(adjacentSymbolLines[0]) ||
                            it.isAdjacentSymbolInNeigbouringLine(adjacentSymbolLines[1])
                }
            }

            indexedNumberLines.lastIndex -> {
                val adjacentSymbolLines = symbolIndices.takeLast(2)

                indexedNumbers.filter {
                    it.isAdjacentSymbolInNeigbouringLine(adjacentSymbolLines[0]) ||
                            it.isAdjacentSymbolInSameLine(adjacentSymbolLines[1])
                }
            }

            else -> {
                val adjacentSymbolLines =
                    listOf(symbolIndices[lineIndex - 1], symbolIndices[lineIndex], symbolIndices[lineIndex + 1])

                indexedNumbers.filter {
                    it.isAdjacentSymbolInNeigbouringLine(adjacentSymbolLines[0]) ||
                            it.isAdjacentSymbolInSameLine(adjacentSymbolLines[1]) ||
                            it.isAdjacentSymbolInNeigbouringLine(adjacentSymbolLines[2])
                }
            }
        }
    }.sumOf { it.value }

    println(sumOfSerialNumbers)
}

val numberRegex = """\d+""".toRegex()

private fun String.parseNumbersWithIndices(): List<IndexedValue<Int>> = numberRegex.findAll(this).map {
    IndexedValue(
        index = it.range.first,
        value = it.value.toInt(),
    )
}.toList()

val symbolRegex = """[^\d.]""".toRegex()

fun String.findSymbolIndices(): List<Int> = symbolRegex.findAll(this).map { it.range.first }.toList()

fun IndexedValue<Int>.isAdjacentSymbolInSameLine(symbolIndices: List<Int>): Boolean =
    symbolIndices.find { it == index - 1 || it == index + value.decimalPositions } != null

fun IndexedValue<Int>.isAdjacentSymbolInNeigbouringLine(symbolIndices: List<Int>): Boolean =
    symbolIndices.find { it in index - 1..index + value.decimalPositions } != null

val Int.decimalPositions
    get() = when (this) {
        0 -> 1
        else -> log10(abs(toDouble())).toInt() + 1
    }
