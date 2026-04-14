package com.example.buspass.entity

import java.math.BigDecimal

data class BusPass (
    var id: Int = 0,
    var name: String = "",
    var address: String = "",
    var city: String = "",
    var preferredRoute: String = "",
    var passType: Int = 1,
    var validForRuralRoute: Boolean = false,
    var lengthOfPass: Int = 1,
    var startDate: String = "",
    var cost: BigDecimal = BigDecimal.ZERO
)
