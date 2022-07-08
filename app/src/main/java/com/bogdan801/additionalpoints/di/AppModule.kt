package com.bogdan801.additionalpoints.di

import android.content.Context
import androidx.room.Room
import com.bogdan801.additionalpoints.data.database.Database
import com.bogdan801.additionalpoints.data.repository.RepositoryImpl
import com.bogdan801.additionalpoints.domain.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    /**
     * Method that provides BaseApplication
     */
    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): BaseApplication {
        return app as BaseApplication
    }

    /**
     * Method that provides a database
     */
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext app: Context) =
        Room.databaseBuilder(app, Database::class.java, "database")
            .createFromAsset("database/database.db")
            .build()

    /**
     * Method that provides a data access object
     */
    @Provides
    fun provideDao(db :Database) = db.dbDao

    /**
     * Method that provides a Repository
     */
    @Provides
    @Singleton
    fun provideRepository(db: Database): Repository {
        return RepositoryImpl(db.dbDao)
    }
}