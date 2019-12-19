package com.rondao.shufflesongs.network.converter;

import com.squareup.moshi.Types;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Whatever [Type] this converter receives, it will delegate the conversion forward,
 * but changing the [Type] to [EnvelopedResult<Type>].
 *
 * After receiving the [EnvelopedResult<Type>] object result,
 * it can decapsulate the result and retrieve it.
 */
public class JEnvelopedResultConverter extends Converter.Factory {
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        Type envelopedType = Types.newParameterizedType(EnvelopedResult.class, type);
        Converter<ResponseBody, ?> delegate = retrofit.nextResponseBodyConverter(this, envelopedType, annotations);

        return value -> {
            EnvelopedResult<?> envelope = (EnvelopedResult<?>) delegate.convert(value);
            return envelope.getResults();
        };
    }
}
