package aoc22.kotlin

import java.io.File
import java.lang.Integer.parseInt
import java.util.stream.IntStream


fun main() {
    calorieCountPerElf()
        .max()
        .ifPresent { println("Highest elf calorie count is $it") }

    calorieCountPerElf()
        .sorted()
        .limit(3)
        .forEach { println("Top elf calorie count is $it") }
}

private fun calorieCountPerElf(): IntStream = File("input/day01_example")
    .readText()
    .split("\n\n").stream()
    .map { it.split("\n").stream().map { parseInt(it) }.reduce(0, Integer::sum) }
    .mapToInt { it }


