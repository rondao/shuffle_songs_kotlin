package com.rondao.shufflesongs.network.converter

data class EnvelopedResult<T>(
        val resultCount: Double,
        val results: T
)