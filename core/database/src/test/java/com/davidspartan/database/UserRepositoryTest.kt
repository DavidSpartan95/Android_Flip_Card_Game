import com.davidspartan.database.data.UserRepository
import com.davidspartan.database.realm.User
import com.davidspartan.database.realm.Theme
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest // `runTest` is preferred over `runBlockingTest` in newer versions of Kotlin
import org.junit.After
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
        // Add 1 dummy user to the realm
        realm.writeBlocking {
            val name = "John"
            val defaultTheme = Theme().apply {
                this.name = "Default"
                primaryHexColor = "#7b9acc"
                secondaryHexColor = "#FCF6F5"
                textHexColor = "#7b9acc"
                price = 0
            }

            val newUser = User().apply {
                this.name = name
                this.themes.add(defaultTheme)
                this.selectedTheme = defaultTheme
                this.score = 100
            }

            copyToRealm(newUser)
        }
    }
    //this prevent data to persist after each test
    @After
    fun tearDown() {
        realm.writeBlocking {
            deleteAll()
        }
        realm.close()
    }

    @Test
    fun `test add and select users with name David`() = runTest {
        // Given a user name "David"
        val userName = "David"

        // Act: Add a new users
        userRepository.addUser(userName)


        // Query the realm to check if the user was added
        val users = realm.query<User>().find()

        // Assert: Verify the user is in the realm and has the correct name
        assertEquals(2, users.size)
        assertEquals(userName, users[1].name)

        //Select the user with name David
        userRepository.selectUser(users[1].id)
        assertEquals(userName, userRepository.selectedUser.value!!.name)

    }

    @Test
    fun `delete user with name David`() = runTest {

        var users = realm.query<User>().find()

        userRepository.deleteUser(users[0])

        users = realm.query<User>().find()

        assertEquals(0, users.size)

    }

    @Test
    fun `add score to user`() = runTest {

        var users = realm.query<User>().find()

        userRepository.addScore(users[0].id, 3)

        users = realm.query<User>().find()
        //John should have 103 score as he starts with 100
        assertEquals(103, users[0].score)

    }
    @Test
    fun `user has theme with name testTheme`() = runTest {

        val users = realm.query<User>().find().first()
        assertEquals(false, userRepository.userHasThemeWithName(users, "WonderLand"))
        assertEquals(true, userRepository.userHasThemeWithName(users, "Default"))
    }

    @Test
    fun `purchase and select theme`() = runTest {

        var users = realm.query<User>().find()

        val testTheme = Theme().apply {
            this.name = "testTheme"
            primaryHexColor = "#FF0000"
            secondaryHexColor = "#008000"
            textHexColor = "#FF0000"
            price = 10
        }


        userRepository.purchaseTheme(users[0], testTheme)

        users = realm.query<User>().find()

        userRepository.selectTheme(users[0], testTheme)

        users = realm.query<User>().find()

        //User should now have testTheme as selected theme and 90 points as score
        assertEquals(testTheme.name, users[0].selectedTheme.name )
        assertEquals(90, users[0].score)

    }

}
