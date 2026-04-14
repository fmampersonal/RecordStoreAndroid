package com.example.buspass.bo

import com.example.buspass.entity.BusPass
import com.example.buspass.utility.CisUtility
import java.math.BigDecimal

object BusPassBO {
    /**
     * Calculate the cost of the bus pass and set it's value in the bus pass object.
     *
     * $1 per day for the first 20 days
     * $0.50 for each day over 20 days
     *
     * $10 premium if to include rural routes
     *
     * Adjustments based on type
     * 3-K12 are free
     * 4-Seniors get a 25% discount on their subtotal
     * 5-Students get a 20% discount on their subtotal
     *
     * @param busPass
     * @return the cost
     * @author BJM
     * @since 20241025
     */
    fun calculateBusPassCost(busPass: BusPass): Double {
        var cost = (1 * busPass.lengthOfPass).toDouble()

        //adjust cost since days over 20 are 0.5$ less per day
        if (busPass.lengthOfPass > 20) {
            cost -= (busPass.lengthOfPass - 20) * 0.5
        }

        if (busPass.validForRuralRoute) {
            cost += 10.0
        }

        when (busPass.passType) {
            3 -> {}
            4 -> cost = 0.0
            5 -> cost *= (1 - 0.2)
            6 -> cost *= (1 - 0.25)
        }
        busPass.cost = BigDecimal(cost)
        return cost
    }

    /**
     * Set default values
     *
     * @param busPass
     * @author BJM
     * @since 20241025
     */
    fun setBusPassDefaults(busPass: BusPass) {
        busPass.lengthOfPass = 31
        busPass.cost = BigDecimal(0)
        busPass.passType = 3
        busPass.startDate = CisUtility.getCurrentDate("yyyy-MM-dd")
    }
}