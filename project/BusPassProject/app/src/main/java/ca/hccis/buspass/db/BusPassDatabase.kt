package ca.hccis.buspass.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ca.hccis.buspass.entity.BusPass
import ca.hccis.buspass.entity.Converters
import ca.hccis.buspass.dao.BusPassDao

@Database(entities = [BusPass::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class) // Register the Converters class
abstract class BusPassDatabase : RoomDatabase() {
    abstract val busPassDao: BusPassDao

    companion object {
        @Volatile
        private var INSTANCE: BusPassDatabase? = null

        fun getInstance(context: Context): BusPassDatabase {
            synchronized(this) {
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    BusPassDatabase::class.java,
                    "bus_pass_database"
                ).build().also { INSTANCE = it }
            }
        }
    }
}

