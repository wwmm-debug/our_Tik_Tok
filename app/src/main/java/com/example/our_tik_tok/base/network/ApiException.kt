package com.example.our_tik_tok.base.network


class ApiException(
  val errorCode: Int,
  val errorMsg: String
) : RuntimeException("errorCode = $errorCode   errorMsg = $errorMsg")