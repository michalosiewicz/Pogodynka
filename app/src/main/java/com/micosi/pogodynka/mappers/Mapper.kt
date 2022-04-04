package com.micosi.pogodynka.mappers

interface Mapper<TInput, TOutput> {

    fun map(input: TInput): TOutput
}