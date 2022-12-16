package aoc22.kotlin

import kotlinx.serialization.json.*
import java.io.File

typealias Packet = List<Any>

fun main() =
    File("input/day13_example")
        .useLines {
            val result = it.filterNot(String::isEmpty)
                .map { parsePacket(it) }
                .zipWithNext()
                .filterIndexed { index, _ -> index % 2 == 0 }
//                .map { pairOfPackets -> inCorrectOrder(pairOfPackets) }
                .mapIndexed { index, pairOfPackets -> if (inCorrectOrder(pairOfPackets)) index else 1 }
//                .forEach { println(it) }
                .reduce { a, b -> a * b }

            println("Result is $result")

            if(result != 13)
                throw RuntimeException("Result should be 13 but was $result. Did you fix the comparePackets method?")
        }


private fun comparePackets(el1: Any, el2: Any): Int =
    when {
        el1 is Int && el2 is Int -> el1 - el2
        el1 is List<*> && el2 is Int -> comparePackets(el1, listOf(el2))
        el1 is Int && el2 is List<*> -> comparePackets(listOf(el1), el2)
        el1 is List<*> && el2 is List<*> -> when {
            el1.isEmpty() && el2.isEmpty() -> 0
            el1.isEmpty() -> -1
            el2.isEmpty() -> 1
            else -> {
                val comparePackets = comparePackets(el1[0]!!, el2[0]!!)
                if (comparePackets == 0)
                    comparePackets(
                        el1.drop(1),
                        el2.drop(1)
                    ) else comparePackets
            }
        }

        else -> throw Exception()
    }

private fun inCorrectOrder(pairOfPackets: Pair<Packet, Packet>): Boolean =
    comparePackets(pairOfPackets.first, pairOfPackets.second) == -1

private fun parsePacket(it: String): Packet =
    mapToPacketList(Json.parseToJsonElement("""{"v":$it}""").jsonObject.get("v")!! as JsonArray);

fun mapToPacketList(packetString: JsonArray): Packet =
    packetString
        .map {
            when (it) {
                is JsonArray -> mapToPacketList(it)
                is JsonPrimitive -> it.int
                else -> throw Exception("Cannot parse $it")
            }
        }
