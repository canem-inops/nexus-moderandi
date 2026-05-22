package dev.canem.nexusmoderandi.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.canem.nexusmoderandi.data.db.AllowedNumberDao
import dev.canem.nexusmoderandi.data.db.AppDatabase
import dev.canem.nexusmoderandi.data.db.RejectedCallDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "nexus_moderandi.db"
        ).build()
    }

    @Provides
    fun provideAllowedNumberDao(db: AppDatabase): AllowedNumberDao = db.allowedNumberDao()

    @Provides
    fun provideRejectedCallDao(db: AppDatabase): RejectedCallDao = db.rejectedCallDao()
}
