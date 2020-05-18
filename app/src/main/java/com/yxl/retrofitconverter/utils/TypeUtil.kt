package com.yxl.retrofitconverter.utils

import java.lang.reflect.*
import java.lang.reflect.Array

object TypeUtil {
    fun getRawType(type: Type): Class<*> {
        checkNotNull(type, "type == null")
        if (type is Class<*>) {
            // Type is a normal class.
            return type
        }
        if (type is ParameterizedType) {

            // I'm not exactly sure why getRawType() returns Type instead of Class. Neal isn't either but
            // suspects some pathological case related to nested classes exists.
            val rawType = type.rawType
            require(rawType is Class<*>)
            return rawType
        }
        if (type is GenericArrayType) {
            val componentType =
                type.genericComponentType
            return Array.newInstance(
                getRawType(componentType),
                0
            ).javaClass
        }
        if (type is TypeVariable<*>) {
            // We could use the variable's bounds, but that won't work if there are multiple. Having a raw
            // type that's more general than necessary is okay.
            return Any::class.java
        }
        if (type is WildcardType) {
            return getRawType(
                type.upperBounds[0]
            )
        }
        throw IllegalArgumentException(
            "Expected a Class, ParameterizedType, or "
                    + "GenericArrayType, but <" + type + "> is of type " + type.javaClass.name
        )
    }

    fun <T> checkNotNull(`object`: T?, message: String?): T {
        if (`object` == null) {
            throw NullPointerException(message)
        }
        return `object`
    }
}