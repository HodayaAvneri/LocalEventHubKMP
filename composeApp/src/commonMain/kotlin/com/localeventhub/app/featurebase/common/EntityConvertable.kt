package com.localeventhub.app.featurebase.common

interface EntityConvertible<I, O> {
    fun toEntity(): O
    fun fromEntity(model: O): I {
        throw NotImplementedError()
    }
}