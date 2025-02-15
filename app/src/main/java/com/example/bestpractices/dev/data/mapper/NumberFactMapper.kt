package com.example.bestpractices.dev.data.mapper

import com.example.bestpractices.dev.data.model.NumberFactResponse
import com.example.bestpractices.dev.domain.model.NumberFact

class NumberFactMapper {
    fun mapFromResponse(response: NumberFactResponse): NumberFact {
        return NumberFact(
            text = response.text,
            number = response.number,
            found = response.found,
            type = response.type
        )
    }
}