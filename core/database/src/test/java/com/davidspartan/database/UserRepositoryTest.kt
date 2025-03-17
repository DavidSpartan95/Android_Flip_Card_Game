import com.davidspartan.database.data.UserRepository
import com.davidspartan.database.realm.User
import com.davidspartan.database.realm.Theme
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest // `runTest` is preferred over `runBlockingTest` in newer versions of Kotlin
import org.junit.Before
import org.junit.Test


class UserRepositoryTest {

    private lateinit var userRepository: UserRepository
    private lateinit var realm: Realm

    @Before
    fun setup() {
        // Create an in-memory Realm instance for testing
        val config = RealmConfiguration.Builder(schema = setOf(User::class, Theme::class))
            .inMemory()
            .name("test-realm")
            .build()
        realm = Realm.open(config)

        // Initialize the UserRepository with the in-memory Realm instance
        userRepository = UserRepository(realm)
    }

    @Test
    fun `test add user with name David`() = runTest {
        // Given a user name "David"
        val userName = "David"

        // Act: Add a new user with name "David"
        userRepository.addUser(userName)

        // Query the realm to check if the user was added
        val users = realm.query<User>().find()

        // Assert: Verify the user is in the realm and has the correct name
        assertEquals(1, users.size)
        assertEquals(userName, users[0].name)
    }
}
