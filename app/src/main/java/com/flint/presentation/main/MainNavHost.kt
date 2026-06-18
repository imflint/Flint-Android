package com.flint.presentation.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import com.flint.core.designsystem.component.toast.ShowToast
import com.flint.core.designsystem.theme.FlintTheme
import com.flint.presentation.collectioncreate.navigation.collectionCreateNavGraph
import com.flint.presentation.collectiondetail.navigation.KEY_SHOW_DELETE_SUCCESS_TOAST
import com.flint.presentation.collectiondetail.navigation.collectionDetailNavGraph
import com.flint.presentation.collectiondetail.report.navigation.KEY_SHOW_REPORT_SUCCESS_TOAST
import com.flint.presentation.collectiondetail.report.navigation.collectionReportNavGraph
import com.flint.presentation.collectionlist.navigation.collectionListNavGraph
import com.flint.presentation.explore.navigation.exploreNavGraph
import com.flint.presentation.home.navigation.homeNavGraph
import com.flint.presentation.login.navigation.loginNavGraph
import com.flint.presentation.onboarding.navigation.onBoardingNavGraph
import com.flint.presentation.profile.navigation.myProfileNavGraph
import com.flint.presentation.profile.navigation.profileNavGraph
import com.flint.presentation.savedcontent.navigation.savedContentListNavGraph
import com.flint.presentation.setting.editprofile.navigation.editProfileNavGraph
import com.flint.presentation.setting.navigation.settingNavGraph
import com.flint.presentation.setting.withdraw.navigation.withdrawCompleteNavGraph
import com.flint.presentation.setting.withdraw.navigation.withdrawNavGraph
import com.flint.presentation.splash.navigation.splashNavGraph

@Composable
fun MainNavHost(
    navigator: MainNavigator,
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier,
) {
    val currentBackStackEntry by navigator.navController.currentBackStackEntryAsState()
    val showDeleteSuccessToast = currentBackStackEntry
        ?.savedStateHandle
        ?.get<Boolean>(KEY_SHOW_DELETE_SUCCESS_TOAST) ?: false
    val showReportSuccessToast = currentBackStackEntry
        ?.savedStateHandle
        ?.get<Boolean>(KEY_SHOW_REPORT_SUCCESS_TOAST) ?: false

    Box(
        modifier =
            modifier
                .fillMaxSize()
                .background(color = FlintTheme.colors.background),
    ) {
        NavHost(
            navController = navigator.navController,
            startDestination = navigator.startDestination,
        ) {
            splashNavGraph(
                paddingValues = paddingValues,
                navigateToLogin = navigator::navigateToLogin,
                navigateToHome = navigator::navigateToHome,
            )

            loginNavGraph(
                paddingValues = paddingValues,
                navigateToOnBoarding = navigator::navigateToOnBoarding,
                navigateToHome = navigator::navigateToHome,
            )

            onBoardingNavGraph(
                paddingValues = paddingValues,
                navigateToHome = navigator::navigateToHome,
                navController = navigator.navController,
            )

            homeNavGraph(
                paddingValues = paddingValues,
                navigateToCollectionList = navigator::navigateToCollectionList,
                navigateToCollectionDetail = navigator::navigateToCollectionDetail,
                navigateToCollectionCreate = navigator::navigateToCollectionCreate,
                navigateToExplore = { navigator.navigate(MainTab.EXPLORE) },
            )

            collectionListNavGraph(
                paddingValues = paddingValues,
                navigateUp = navigator::navigateUp,
                navigateToCollectionDetail = navigator::navigateToCollectionDetail,
                navigateToCollectionList = navigator::navigateToCollectionList,
            )

            collectionDetailNavGraph(
                paddingValues = paddingValues,
                navigateToCollectionList = navigator::navigateToCollectionList,
                navigateUp = navigator::navigateUp,
                navigateToProfile = navigator::navigateToProfile,
                navigateToCollectionReport = navigator::navigateToCollectionReport,
                navController = navigator.navController,
            )

            collectionReportNavGraph(
                paddingValues = paddingValues,
                navController = navigator.navController,
                navigateUp = navigator::navigateUp,
            )

            collectionCreateNavGraph(
                paddingValues = paddingValues,
                navController = navigator.navController,
            )

            savedContentListNavGraph(
                paddingValues = paddingValues,
            )

            exploreNavGraph(
                paddingValues = paddingValues,
                navigateToCollectionDetail = navigator::navigateToCollectionDetail,
                navigateToCollectionCreate = navigator::navigateToCollectionCreate,
            )

            myProfileNavGraph(
                paddingValues = paddingValues,
                navigateToCollectionList = navigator::navigateToCollectionList,
                navigateToSavedContentList = navigator::navigateToSavedContent,
                navigateToCollectionDetail = navigator::navigateToCollectionDetail,
                navigateToSetting = navigator::navigateToSetting,
            )

            profileNavGraph(
                paddingValues = paddingValues,
                navigateUp = navigator::navigateUp,
                navigateToCollectionList = navigator::navigateToCollectionList,
                navigateToSavedContentList = navigator::navigateToSavedContent,
                navigateToCollectionDetail = navigator::navigateToCollectionDetail,
            )

            settingNavGraph(
                navigateUp = navigator::navigateUp,
                navigateToLogin = navigator::navigateToLogin,
                navigateToEditProfile = navigator::navigateToEditProfile,
                navigateToWithdraw = navigator::navigateToWithdraw,
            )

            editProfileNavGraph(
                navigateUp = navigator::navigateUp,
            )

            withdrawNavGraph(
                navigateUp = navigator::navigateUp,
                navigateToWithdrawComplete = navigator::navigateToWithdrawComplete,
            )

            withdrawCompleteNavGraph(
                navigateToLogin = navigator::navigateToLogin,
            )
        }

        if (showDeleteSuccessToast) {
            ShowToast(
                text = "컬렉션을 삭제했어요",
                imageVector = null,
                paddingValues = paddingValues,
                yOffset = 12.dp,
                hide = {
                    currentBackStackEntry?.savedStateHandle?.set(KEY_SHOW_DELETE_SUCCESS_TOAST, false)
                },
            )
        }

        if (showReportSuccessToast) {
            ShowToast(
                text = "신고가 접수되었어요",
                imageVector = null,
                paddingValues = paddingValues,
                yOffset = 12.dp,
                hide = {
                    currentBackStackEntry?.savedStateHandle?.set(KEY_SHOW_REPORT_SUCCESS_TOAST, false)
                },
            )
        }
    }
}
