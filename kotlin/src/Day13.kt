package aoc22.kotlin

import kotlinx.serialization.json.*
import java.io.File

typealias Packet = List<Any>

fun main() {

    File("input/day13_example")
        .useLines {
            val result = it.filterNot(String::isEmpty)
                .map { parsePacket(it) }
                .zipWithNext()
                .filterIndexed { index, _ -> index % 2 == 1 }
                .mapIndexed { index, pairOfPackets -> if (inCorrectOrder(pairOfPackets)) index else 1 }
                .reduce { a, b -> a * b }

            println("Result is $result")
        }
}

private fun getPacketComparator() = Comparator<Any> { el1, el2 ->
    when {
        el1 is Int && el2 is Int -> el1 - el2
//        el1 is List<*> && el2 is Int ->
//        (el1 == null) -> -1
        else -> 1
    }
}

private fun inCorrectOrder(pairOfPackets: Pair<Packet, Packet>): Boolean =
    getPacketComparator().compare(pairOfPackets.first, pairOfPackets.second) == -1

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
