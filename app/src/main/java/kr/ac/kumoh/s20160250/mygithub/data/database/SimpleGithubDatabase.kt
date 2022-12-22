package kr.ac.kumoh.s20160250.mygithub.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import kr.ac.kumoh.s20160250.mygithub.data.dao.SearchHistoryDao
import kr.ac.kumoh.s20160250.mygithub.data.entity.GithubRepoEntity

@Database(entities = [GithubRepoEntity::class], version = 1)
abstract class SimpleGithubDatabase: RoomDatabase() {

    abstract fun searchHistoryDao(): SearchHistoryDao
}