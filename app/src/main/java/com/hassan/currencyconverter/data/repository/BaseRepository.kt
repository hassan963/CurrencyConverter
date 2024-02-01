package com.hassan.currencyconverter.data.repository

import com.hassan.currencyconverter.data.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

abstract class BaseRepository {
    suspend fun <T> apiCall(apiToBeCalled: suspend () -> Response<T>): Status<T> {
        return withContext(Dispatchers.IO) {
            try {
                val response: Response<T> = apiToBeCalled()

                if (response.isSuccessful) {
                    Status.Success(response.body()!!)
                } else {
                    Status.Error("Something went wrong")
                }
            } catch (e: HttpException) {
                Status.Error(e.message() ?: "Something went wrong")
            } catch (e: IOException) {
                Status.Error("Please check your internet connection")
            } catch (e: Exception) {
                Status.Error("Something went wrong")
            }
        }
    }
}