package com.davidspartan.androidflipcardgame

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import org.junit.Rule
import org.junit.Test

class Test {
    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testFullUserFlow_createsUserAndPlaysGame(){
        val username = generateRandomUsername()

        rule.onNodeWithText("Create New User").performClick()

        rule.onNodeWithText("User Name")
            .performClick()
            .performTextInput(username)

        rule.onNodeWithText("Create User").performClick()

        rule.waitUntil(timeoutMillis = 5000) {
            rule.onAllNodesWithText(username).fetchSemanticsNodes().isNotEmpty()
        }

        rule.onNodeWithText(username).assertExists()

        rule.onNodeWithTag(username).performClick()

        rule.onNodeWithText("Play").performClick()

        val allCards = rule.onAllNodes(hasTestTagStartingWith("card_color_")) // Get all Card UI elements
        // Find the first color that appears at least twice
        val colorMap = mutableMapOf<String, MutableList<SemanticsNodeInteraction>>()

        for (i in 0 until allCards.fetchSemanticsNodes().size) {
            val tag = allCards[i].fetchSemanticsNode().config.getOrNull(SemanticsProperties.TestTag)
            if (tag?.startsWith("card_color_") == true) {
                colorMap.putIfAbsent(tag, mutableListOf())
                colorMap[tag]?.add(allCards[i])
            }
        }

        // Extract and sort pairs (ensuring each color has two elements before adding)
        val sortedPairs = colorMap.values
            .filter { it.size >= 2 } // Only consider colors with at least two cards
            .flatMap { it.take(2) }  // Take only two cards per color
            .chunked(2)              // Group them into pairs [(0,1), (2,3), etc.]

        require(sortedPairs.isNotEmpty()) { "No matching card pairs found! Total cards: ${allCards.fetchSemanticsNodes().size}" }

        // Click on all matching pairs and check score
        var score = 0
        sortedPairs.forEach { pair ->
            pair[0].performClick()
            pair[1].performClick()
            score++
            if (score == sortedPairs.size){
                val scoreText = rule.onAllNodes(hasText("Score: $score", substring = true))
                scoreText.fetchSemanticsNodes().isNotEmpty()
            }else{
                rule.waitUntil(timeoutMillis = 1000) {
                    val scoreText = rule.onAllNodes(hasText("Current score is $score", substring = true))
                    scoreText.fetchSemanticsNodes().isNotEmpty()
                }
            }

        }
        rule.onNodeWithText("Go To Menu").performClick()

        rule.onNodeWithText("Themes").performClick()

        rule.onNodeWithText("Forest").performClick()

        //this is a popup
        rule.onNodeWithText("Dismiss").performClick()

        rule.onNodeWithText("Go To Menu").performClick()

    }

    private fun hasTestTagStartingWith(prefix: String): SemanticsMatcher {
        return SemanticsMatcher(
            description = "Has a test tag that starts with '$prefix'"
        ) { node ->
            val testTag = node.config.getOrNull(SemanticsProperties.TestTag)
            testTag?.startsWith(prefix) == true
        }
    }

    private fun generateRandomUsername(): String {
        val chars = ('a'..'z') + ('0'..'9')
        return (1..16)
            .map { chars.random() }
            .joinToString("")
    }
}