package com.example.calculatorpretbolt

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class BoltAccessibilityService : AccessibilityService() {

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        val rootNode = rootInActiveWindow ?: return

        val texts = mutableListOf<String>()
        collectAllText(rootNode, texts)

        val price = parsePrice(texts)
        val distance = parseDistance(texts)

        if (price != null && distance != null && distance > 0.0) {
            val rate = price / distance
            OverlayService.instance?.updateValue(rate, price, distance)
        }
    }

    private fun collectAllText(node: AccessibilityNodeInfo, list: MutableList<String>) {
        node.text?.let { if (it.isNotBlank()) list.add(it.toString()) }
        for (i in 0 until node.childCount) {
            node.getChild(i)?.let { collectAllText(it, list) }
        }
    }

    // Identifică formate de genul: "25,50 lei", "25.50 RON", "30 lei"
    private fun parsePrice(texts: List<String>): Double? {
        val regex = Regex("""(\d+[.,]?\d*)\s*(?:lei|ron)""", RegexOption.IGNORE_CASE)
        for (text in texts) {
            val match = regex.find(text)
            if (match != null) {
                return match.groupValues[1].replace(",", ".").toDoubleOrNull()
            }
        }
        return null
    }

    // Identifică formate de genul: "4.2 km", "4,2 km", "800 m"
    private fun parseDistance(texts: List<String>): Double? {
        val regexKm = Regex("""(\d+[.,]?\d*)\s*km""", RegexOption.IGNORE_CASE)
        val regexM = Regex("""(\d+)\s*m\b""", RegexOption.IGNORE_CASE)

        for (text in texts) {
            val matchKm = regexKm.find(text)
            if (matchKm != null) {
                return matchKm.groupValues[1].replace(",", ".").toDoubleOrNull()
            }
            val matchM = regexM.find(text)
            if (matchM != null) {
                val meters = matchM.groupValues[1].toDoubleOrNull() ?: continue
                return meters / 1000.0 // Transformare în km
            }
        }
        return null
    }

    override fun onInterrupt() {}
}