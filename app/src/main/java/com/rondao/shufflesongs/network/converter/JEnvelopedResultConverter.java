package com.rondao.shufflesongs.network.converter;

import android.util.Log;

import com.squareup.moshi.Types;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;

import kotlin.reflect.jvm.internal.impl.types.WrappedType;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

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
