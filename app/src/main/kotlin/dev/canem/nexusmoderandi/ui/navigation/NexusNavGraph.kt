package dev.canem.nexusmoderandi.ui.navigation

import android.app.role.RoleManager
import android.content.Context
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CallMissed
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.canem.nexusmoderandi.ui.allowlist.AllowListScreen
import dev.canem.nexusmoderandi.ui.rejectedcalls.RejectedCallsScreen
import dev.canem.nexusmoderandi.ui.setup.SetupScreen

@Composable
fun NexusNavGraph() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val roleManager = context.getSystemService(Context.ROLE_SERVICE) as RoleManager
    val hasRole = roleManager.isRoleHeld(RoleManager.ROLE_CALL_SCREENING)

    val startDestination = if (hasRole) "allowlist" else "setup"
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (hasRole && currentRoute != "setup") {
                NavigationBar {
                    NavigationBarItem(
                        selected = currentRoute == "allowlist",
                        onClick = {
                            navController.navigate("allowlist") {
                                popUpTo("allowlist") { inclusive = true }
                            }
                        },
                        icon = { Icon(Icons.Default.Shield, contentDescription = null) },
                        label = { Text("Allow List") }
                    )
                    NavigationBarItem(
                        selected = currentRoute == "rejected",
                        onClick = {
                            navController.navigate("rejected") {
                                popUpTo("allowlist")
                            }
                        },
                        icon = { Icon(Icons.AutoMirrored.Filled.CallMissed, contentDescription = null) },
                        label = { Text("Rejected") }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("setup") {
                SetupScreen(
                    onRoleGranted = {
                        navController.navigate("allowlist") {
                            popUpTo("setup") { inclusive = true }
                        }
                    }
                )
            }
            composable("allowlist") {
                AllowListScreen()
            }
            composable("rejected") {
                RejectedCallsScreen()
            }
        }
    }
}
