package com.example.our_tik_tok.base.network

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import kotlin.jvm.Throws

open class ApiWrapper<T>(
    @SerializedName("data")
    val data: T,
) : Serializable, ApiStatus()

open class ApiStatus(
    @SerializedName("code")
    val code: Int = 200,
    @SerializedName("msg")
    val msg: String = ""
) : Serializable {

    fun isSuccess(): Boolean {
        return code == 200
    }

    @Throws(ApiException::class)
    fun throwApiExceptionIfFail() {
        if (!isSuccess()) throw ApiException(code, msg)
    }
}

