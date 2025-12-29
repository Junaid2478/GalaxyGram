package com.example.galaxygram.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.galaxygram.domain.model.Apod
import com.example.galaxygram.feature.detail.ApodDetailScreen
import com.example.galaxygram.feature.list.HomeScreen

object Routes {
    const val LIST = "list"
    const val DETAIL = "detail"

    fun detailRoute(apod: Apod): String {
        fun enc(s: String?) = Uri.encode(s ?: "")
        return "detail?title=${enc(apod.title)}" +
                "&date=${enc(apod.date)}" +
                "&explanation=${enc(apod.explanation)}" +
                "&imageUrl=${enc(apod.imageUrl)}" +
                "&hdUrl=${enc(apod.hdUrl)}" +
                "&isVideo=${apod.isVideo}" +
                "&videoUrl=${enc(apod.videoUrl)}"
    }
}

@Composable
fun GalaxyNavHost() {
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = Routes.LIST) {

        composable(Routes.LIST) {
            HomeScreen(
                onOpenDetail = { apod ->
                    nav.navigate(Routes.detailRoute(apod))
                }
            )
        }

        composable(
            route = "detail?title={title}&date={date}&explanation={explanation}&imageUrl={imageUrl}&hdUrl={hdUrl}&isVideo={isVideo}&videoUrl={videoUrl}",
            arguments = listOf(
                navArgument("title") { type = NavType.StringType; defaultValue = "" },
                navArgument("date") { type = NavType.StringType; defaultValue = "" },
                navArgument("explanation") { type = NavType.StringType; defaultValue = "" },
                navArgument("imageUrl") { type = NavType.StringType; defaultValue = "" },
                navArgument("hdUrl") { type = NavType.StringType; defaultValue = "" },
                navArgument("isVideo") { type = NavType.BoolType; defaultValue = false },
                navArgument("videoUrl") { type = NavType.StringType; defaultValue = "" },
            )
        ) { backStackEntry ->
            val args = backStackEntry.arguments!!
            ApodDetailScreen(
                title = args.getString("title").orEmpty(),
                date = args.getString("date").orEmpty(),
                explanation = args.getString("explanation").orEmpty(),
                imageUrl = args.getString("imageUrl").orEmpty().ifBlank { null },
                hdUrl = args.getString("hdUrl").orEmpty().ifBlank { null },
                isVideo = args.getBoolean("isVideo"),
                videoUrl = args.getString("videoUrl").orEmpty().ifBlank { null },
                onBack = { nav.popBackStack() }
            )
        }
    }
}
