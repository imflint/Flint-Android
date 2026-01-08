package com.flint.presentation.main.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.flint.presentation.explore.navigation.exploreNavGraph
import com.flint.presentation.home.navigation.homeNavGraph
import com.flint.presentation.profile.navigation.profileNavGraph

@Composable
fun MainNavHost(
    navigator: MainNavigator,
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier
){
    NavHost(
        navController = navigator.navController,
        startDestination = navigator.startDestination,
        modifier = modifier
    ){
        homeNavGraph(paddingValues)
        exploreNavGraph(paddingValues)
        profileNavGraph(paddingValues)
    }
}
