package com.example.galaxygram.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.galaxygram.feature.detail.ApodDetailScreen
import com.example.galaxygram.feature.list.HomeScreen

object Routes {
    const val LIST = "list"
    const val DETAIL = "detail"

    fun detailRoute(
        title: String,
        date: String,
        explanation: String,
        imageUrl: String?,
        hdUrl: String?
    ): String {
        fun enc(s: String?) = Uri.encode(s ?: "")
        return "detail?title=${enc(title)}&date=${enc(date)}&explanation=${enc(explanation)}&imageUrl=${enc(imageUrl)}&hdUrl=${enc(hdUrl)}"
    }
}

@Composable
fun GalaxyNavHost() {
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = Routes.LIST) {
        composable(Routes.LIST) {
            HomeScreen(
                onOpenDetail = { item ->
                    nav.navigate(
                        Routes.detailRoute(
                            title = item.title,
                            date = item.date,
                            explanation = item.explanation,
                            imageUrl = item.imageUrl,
                            hdUrl = item.hdUrl
                        )
                    )
                }
            )
        }
        composable(
            route = "detail?title={title}&date={date}&explanation={explanation}&imageUrl={imageUrl}&hdUrl={hdUrl}",
            arguments = listOf(
                navArgument("title") { type = NavType.StringType; defaultValue = "" },
                navArgument("date") { type = NavType.StringType; defaultValue = "" },
                navArgument("explanation") { type = NavType.StringType; defaultValue = "" },
                navArgument("imageUrl") { type = NavType.StringType; defaultValue = "" },
                navArgument("hdUrl") { type = NavType.StringType; defaultValue = "" },
            )
        ) { backStackEntry ->
            val args = backStackEntry.arguments!!
            ApodDetailScreen(
                title = args.getString("title").orEmpty(),
                date = args.getString("date").orEmpty(),
                explanation = args.getString("explanation").orEmpty(),
                imageUrl = args.getString("imageUrl").orEmpty().ifBlank { null },
                hdUrl = args.getString("hdUrl").orEmpty().ifBlank { null },
                onBack = { nav.popBackStack() }
            )
        }
    }
}
