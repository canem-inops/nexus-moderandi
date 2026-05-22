package dev.canem.nexusmoderandi.data.db

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import dev.canem.nexusmoderandi.data.entity.RejectedCall
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class RejectedCallDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var dao: RejectedCallDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.rejectedCallDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun `insert and retrieve rejected call`() = runTest {
        dao.insert(RejectedCall(phoneNumber = "+15559999999", timestamp = 1000))

        val all = dao.getAll().first()
        assertEquals(1, all.size)
        assertEquals("+15559999999", all[0].phoneNumber)
    }

    @Test
    fun `multiple rejected calls from same number are all stored`() = runTest {
        dao.insert(RejectedCall(phoneNumber = "+15559999999", timestamp = 1000))
        dao.insert(RejectedCall(phoneNumber = "+15559999999", timestamp = 2000))

        val all = dao.getAll().first()
        assertEquals(2, all.size)
    }

    @Test
    fun `getAll returns entries ordered by timestamp descending`() = runTest {
        dao.insert(RejectedCall(phoneNumber = "+15551111111", timestamp = 100))
        dao.insert(RejectedCall(phoneNumber = "+15552222222", timestamp = 200))

        val all = dao.getAll().first()
        assertEquals("+15552222222", all[0].phoneNumber)
        assertEquals("+15551111111", all[1].phoneNumber)
    }

    @Test
    fun `countSince returns only entries after the given timestamp`() = runTest {
        dao.insert(RejectedCall(phoneNumber = "+15551111111", timestamp = 100))
        dao.insert(RejectedCall(phoneNumber = "+15552222222", timestamp = 200))
        dao.insert(RejectedCall(phoneNumber = "+15553333333", timestamp = 300))

        assertEquals(2, dao.countSince(200))
        assertEquals(1, dao.countSince(300))
        assertEquals(0, dao.countSince(400))
        assertEquals(3, dao.countSince(1))
    }

    @Test
    fun `clearAll removes all entries`() = runTest {
        dao.insert(RejectedCall(phoneNumber = "+15551111111"))
        dao.insert(RejectedCall(phoneNumber = "+15552222222"))
        dao.clearAll()

        assertEquals(0, dao.getAll().first().size)
    }
}
