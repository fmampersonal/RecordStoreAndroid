package ca.hccis.buspass.dao

import androidx.room.*
import ca.hccis.buspass.entity.BusPass
import kotlinx.coroutines.flow.Flow

@Dao
interface BusPassDao {

    // Insert a single BusPass entry
    @Insert
    suspend fun insert(busPass: BusPass)

    // Insert multiple BusPass entries (for bulk inserts)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(busPasses: List<BusPass>)

    // Update an existing BusPass entry
    @Update
    suspend fun update(busPass: BusPass)

    // Delete a single BusPass entry
    @Delete
    suspend fun delete(busPass: BusPass)

    // Get all bus passes as a Flow for UI updates
    @Query("SELECT * FROM bus_pass_table ORDER BY id")
    fun getAllBusPasses(): Flow<List<BusPass>>

    // Get all bus passes as a List for synchronization purposes
    @Query("SELECT * FROM bus_pass_table ORDER BY id")
    suspend fun getAllBusPassesList(): List<BusPass>

    @Query("DELETE FROM bus_pass_table")
    fun deleteAll();
}
