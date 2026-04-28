package uestc.b3dman.ftpclient

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import uestc.b3dman.ftpclient.ui.screens.addaccount.AddAccountScreen
import uestc.b3dman.ftpclient.ui.screens.browser.BrowserScreen
import uestc.b3dman.ftpclient.ui.screens.login.LoginScreen


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Preview
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    // TODO: 可能要添加更多的屏幕
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                onNavigateToBrowser = {
                    navController.navigate("browser")
                },
                onNavigateToAddAccount = {
                    navController.navigate("add_account")
                },
                onNavigateToEditAccount = { accountId ->
                    navController.navigate("add_account?accountId=$accountId")
                }
            )
       }
        composable(
            "add_account?accountId={accountId}",
            arguments = listOf(
                navArgument("accountId") {
                    type = NavType.IntType
                    defaultValue = -1 // -1 表示新建账号，其他值表示编辑对应 ID 的账号
                }
            )
        ) { backStackEntry ->
            val accountId = backStackEntry.arguments?.getInt("accountId") ?: -1
            AddAccountScreen(
                accountId = accountId,
                onBack = { navController.popBackStack() }
            )
        }
        composable("browser") {
            BrowserScreen(onExit = {
                navController.popBackStack()
            })
        }
    }
}