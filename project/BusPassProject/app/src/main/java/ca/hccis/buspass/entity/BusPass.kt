package ca.hccis.buspass.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ca.hccis.buspass.utility.CisUtility
import java.math.BigDecimal

@Entity(tableName = "bus_pass_table")
data class BusPass(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var name: String = "",
    var address: String = "",
    var city: String = "",
    var preferredRoute: String = "",
    var passType: Int = 3, //default to regular
    var validForRuralRoute: Boolean = false,
    var lengthOfPass: Int = 1,
    var colorOfPass: Int = 1,
    var startDate: String = CisUtility.getCurrentDate("yyyy-MM-dd"),
    var cost: BigDecimal = BigDecimal.ZERO
)
