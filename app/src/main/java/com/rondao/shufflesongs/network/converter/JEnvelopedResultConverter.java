package com.rondao.shufflesongs.network.converter;

import androidx.annotation.NonNull;

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
 * it can de-envelope the result and retrieve it.
 */
public class JEnvelopedResultConverter extends Converter.Factory {
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(@NonNull Type type, @NonNull Annotation[] annotations, Retrofit retrofit) {
        Type envelopedType = Types.newParameterizedType(EnvelopedResult.class, type);
        Converter<ResponseBody, ?> delegate = retrofit.nextResponseBodyConverter(this, envelopedType, annotations);

        return value -> {
            EnvelopedResult<?> envelope = (EnvelopedResult<?>) delegate.convert(value);
            assert envelope != null;
            return envelope.getResults();
        };
    }
}
