package uestc.b3dman.ftpclient.data.repository

import uestc.b3dman.ftpclient.data.model.FtpAccount
import uestc.b3dman.ftpclient.data.model.FtpFileItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import uestc.b3dman.ftpclient.data.local.FtpAccountDao
import uestc.b3dman.ftpclient.data.remote.FtpManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FtpRepository @Inject constructor(
    private val accountDao: FtpAccountDao,
    private val ftpManager: FtpManager
) {
    // --- 本地数据库操作 ---
    val savedAccounts: Flow<List<FtpAccount>> = accountDao.getAllAccounts()

    suspend fun saveAccount(account: FtpAccount) {
        accountDao.insertAccount(account)
    }

    suspend fun updateLastLoginTime(account: FtpAccount) {
        val updatedAccount = account.copy(lastLoginTime = System.currentTimeMillis())
        accountDao.updateAccount(updatedAccount)
    }

    suspend fun deleteAccount(account: FtpAccount) {
        accountDao.deleteAccount(account)
    }

    suspend fun updateAccount(account: FtpAccount) {
        accountDao.updateAccount(account)
    }

    suspend fun getAccountById(id: Int): FtpAccount? {
        return accountDao.getAccountById(id)
    }

    // --- 模拟远程 FTP 操作 ---
    suspend fun login(account: FtpAccount): Result<Boolean> {
        return if (ftpManager.connect(account)) Result.success(true) else Result.failure(Exception("Login failed"))
    }

    suspend fun getFiles(path: String): List<FtpFileItem> {
        return ftpManager.listFiles(path)
    }

    suspend fun logout() {
        ftpManager.disconnect()
    }
}