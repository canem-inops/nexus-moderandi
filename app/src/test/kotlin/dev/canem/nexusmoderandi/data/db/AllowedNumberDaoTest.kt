package dev.canem.nexusmoderandi.data.db

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import dev.canem.nexusmoderandi.data.entity.AllowedNumber
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class AllowedNumberDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var dao: AllowedNumberDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.allowedNumberDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun `insert and retrieve allowed number`() = runTest {
        dao.insert(AllowedNumber(phoneNumber = "+15551234567"))

        val all = dao.getAll().first()
        assertEquals(1, all.size)
        assertEquals("+15551234567", all[0].phoneNumber)
    }

    @Test
    fun `exists returns true for inserted number`() = runTest {
        dao.insert(AllowedNumber(phoneNumber = "+15551234567"))
        assertTrue(dao.exists("+15551234567"))
    }

    @Test
    fun `exists returns false for missing number`() = runTest {
        assertFalse(dao.exists("+15559999999"))
    }

    @Test
    fun `delete removes the number`() = runTest {
        dao.insert(AllowedNumber(phoneNumber = "+15551234567"))
        dao.delete("+15551234567")

        assertFalse(dao.exists("+15551234567"))
        assertEquals(0, dao.getAll().first().size)
    }

    @Test
    fun `duplicate insert is ignored`() = runTest {
        dao.insert(AllowedNumber(phoneNumber = "+15551234567"))
        dao.insert(AllowedNumber(phoneNumber = "+15551234567"))

        assertEquals(1, dao.getAll().first().size)
    }

    @Test
    fun `getAllSync returns all entries`() = runTest {
        dao.insert(AllowedNumber(phoneNumber = "+15551111111"))
        dao.insert(AllowedNumber(phoneNumber = "+15552222222"))

        val all = dao.getAllSync()
        assertEquals(2, all.size)
    }

    @Test
    fun `getAll returns entries ordered by addedAt descending`() = runTest {
        dao.insert(AllowedNumber(phoneNumber = "+15551111111", addedAt = 100))
        dao.insert(AllowedNumber(phoneNumber = "+15552222222", addedAt = 200))

        val all = dao.getAll().first()
        assertEquals("+15552222222", all[0].phoneNumber)
        assertEquals("+15551111111", all[1].phoneNumber)
    }
}
