package com.rondao.shufflesongs.network.converter

import com.squareup.moshi.Types
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

/**
 * Attempt of [JEnvelopedResultConverter] implementation in Kotlin.
 * However, [EnvelopedResult<*>] object received is actually [EnvelopedResult<Any?>].
 *
 * For some reason, the Star Projection did not went forward.
 */
class EnvelopedResultConverter : Converter.Factory() {
    override fun responseBodyConverter(type: Type, annotations: Array<Annotation>, retrofit: Retrofit): Converter<ResponseBody, *>? {
        val envelopedType = Types.newParameterizedType(EnvelopedResult::class.java, Types.getRawType(type))
        val delegate = retrofit.nextResponseBodyConverter<EnvelopedResult<*>>(this, envelopedType, annotations)

        return Converter { value: ResponseBody ->
            delegate.convert(value)?.results
        }
    }
}

data class EnvelopedResult<T>(
        val resultCount: Double,
        val results: T
)