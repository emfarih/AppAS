package ml.farih.appas.data

import javax.inject.Inject

/**
 * @author by M on 10/11/19
 */
class Repository @Inject constructor(private val apiService: ApiService) {
    suspend fun getData(): List<ApiResponse> {
        return apiService.getAccount()
    }
}