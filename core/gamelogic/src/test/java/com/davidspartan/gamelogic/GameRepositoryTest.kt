import com.davidspartan.gamelogic.GameRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class GameRepositoryTest {
    private lateinit var gameRepository: GameRepository

    @Before
    fun setup() {
        Dispatchers.setMain(
            dispatcher = Dispatchers.Unconfined
        ) // Set a test dispatcher
        gameRepository = GameRepository()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // Reset Main dispatcher after test
    }

    @Test
    fun `initial state is correct`() {
        val initialState = gameRepository.gameState.value
        val initialCards = gameRepository.cards.value

        assertFalse(initialState.isGameOver)
        assertEquals(0, initialState.score)
        assertEquals(0, initialState.totalFlips)
        assertEquals(6, initialCards.size) // 3 pairs of cards
        assertTrue(initialCards.all { !it.isFlipped && !it.found })
    }

    @Test
    fun `flip one card updates state`() {
        val card = gameRepository.cards.value.first()

        gameRepository.flipCard(card)
        val updatedCard = gameRepository.cards.value.first { it.id == card.id }

        assertTrue(updatedCard.isFlipped)
        assertEquals(1, gameRepository.gameState.value.totalFlips)
    }

    @Test
    fun `flip two matching cards marks them as found`() = runTest {
        val firstMatch = gameRepository.cards.value.groupBy { it.hexColor }.values.first()
        val card1 = firstMatch[0]
        val card2 = firstMatch[1]

        gameRepository.flipCard(card1)
        gameRepository.flipCard(card2)

        // Simulate delay
        Thread.sleep(800)

        val updatedCards = gameRepository.cards.value.filter { it.id == card1.id || it.id == card2.id }
        assertTrue(updatedCards.all { it.found })
        assertEquals(1, gameRepository.gameState.value.score)
    }

    @Test
    fun `flip two non-matching cards resets them`() = runTest {
        val firstCard = gameRepository.cards.value[0]
        val secondCard = gameRepository.cards.value.find { it.hexColor != firstCard.hexColor }!!

        gameRepository.flipCard(firstCard)
        gameRepository.flipCard(secondCard)

        // Simulate delay
        Thread.sleep(800)

        val updatedCards = gameRepository.cards.value.filter { it.id == firstCard.id || it.id == secondCard.id }
        assertTrue(updatedCards.all { !it.isFlipped })
    }

    @Test
    fun `game over when all pairs are found`() = runTest {
        val cardPairs = gameRepository.cards.value.groupBy { it.hexColor }

        for (pair in cardPairs.values) {
            gameRepository.flipCard(pair[0])
            gameRepository.flipCard(pair[1])
            Thread.sleep(800)  // Simulate delay
        }

        assertTrue(gameRepository.gameState.value.isGameOver)
    }

    @Test
    fun `reset game restores initial state`() = runTest {
        gameRepository.resetGame()

        val newState = gameRepository.gameState.value
        val newCards = gameRepository.cards.value

        assertFalse(newState.isGameOver)
        assertEquals(0, newState.score)
        assertEquals(0, newState.totalFlips)
        assertEquals(6, newCards.size)
        assertTrue(newCards.all { !it.isFlipped && !it.found })
    }
}