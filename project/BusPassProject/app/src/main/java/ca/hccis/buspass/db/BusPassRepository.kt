package ca.hccis.buspass.db

import ca.hccis.buspass.dao.BusPassDao
import ca.hccis.buspass.entity.BusPass
import ca.hccis.buspass.utility.ApiUtility
import ca.hccis.buspass.utility.CisUtility
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class BusPassRepository(private val busPassDao: BusPassDao) {
    val allBusPasses: Flow<List<BusPass>> = busPassDao.getAllBusPasses()
    var count: Int = 0

    /**
     * Insert a bus pass into the api and local database.
     */
    suspend fun insert(busPass: BusPass) {
        ApiUtility.insert(busPass)
        busPassDao.insert(busPass)
    }

    /**
     * Update a bus pass in the local database.
     */
    suspend fun update(busPass: BusPass) {
        ApiUtility.insert(busPass)
        busPassDao.update(busPass)
    }

    /**
     * Delete a bus pass from the local database.
     */
    suspend fun delete(busPass: BusPass) {
        ApiUtility.delete(busPass)
        busPassDao.delete(busPass)
    }

    /**
     * Fetch bus passes from the API and save them into the local database.
     * This ensures only new entries are added, and existing data is preserved.
     */
    suspend fun fetchAndSaveBusPassesFromApi() {
        withContext(Dispatchers.IO) {
            try {
                // Fetch data from the API using ApiUtility
                val busPassesFromApi = ApiUtility.fetchBusPasses()

//                // Fetch all existing entries from Room
//                val existingBusPasses = busPassDao.getAllBusPassesList()
//
//                // Filter API data to exclude entries already in the database
//                val newBusPasses = busPassesFromApi.filter { apiBusPass ->
//                    existingBusPasses.none { localBusPass -> localBusPass.id == apiBusPass.id }
//                }
//                // Insert only new entries into the database
//                busPassDao.insertAll(newBusPasses)

                //BJM delete all the room db bus passes and replace with those from the api.
                busPassDao.deleteAll()
                busPassDao.insertAll(busPassesFromApi)
            } catch (e: Exception) {
                e.printStackTrace()
                throw e // Re-throw the exception to handle it appropriately in the ViewModel
            }
        }
    }

    /**
     * Fetch bus passes from the API
     */
    suspend fun countBusPasses():Int {

        withContext(Dispatchers.IO) {
            try {
                // Fetch data from the API using ApiUtility
                val busPassesFromApi = ApiUtility.fetchBusPasses()
                count = busPassesFromApi.size
            } catch (e: Exception) {
                e.printStackTrace()
                throw e // Re-throw the exception to handle it appropriately in the ViewModel
            }
        }
        CisUtility.log("BJTEST","In BusPassRepository.countBusPasses:"+count)
        return count
    }

    }
