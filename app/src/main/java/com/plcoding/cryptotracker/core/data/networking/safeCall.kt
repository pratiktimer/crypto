package com.plcoding.cryptotracker.core.data.networking

import com.plcoding.cryptotracker.core.domain.util.NetworkError
import com.plcoding.cryptotracker.core.domain.util.Result
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.SerializationException
import java.nio.channels.UnresolvedAddressException
import kotlin.coroutines.coroutineContext

suspend inline fun <reified T>safeCall(
    execute:()-> HttpResponse
):Result<T, NetworkError>{
    val response = try{
     execute()
    }
    catch (e: UnresolvedAddressException){
        return Result.Error(NetworkError.NO_INTER)
    }
    catch (w: SerializationException){
        return Result.Error(NetworkError.SERIALIZATION)
    }
    catch (e: Exception){
        coroutineContext.ensureActive()
        return Result.Error(NetworkError.UNKNOWN)
    }

    return responseToResult(response)
}