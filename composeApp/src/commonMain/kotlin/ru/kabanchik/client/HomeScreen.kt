package ru.kabanchik.client

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import kabanchik.composeapp.generated.resources.Res
import kabanchik.composeapp.generated.resources.home_tab_chats
import kabanchik.composeapp.generated.resources.home_tab_profile
import org.jetbrains.compose.resources.stringResource
import ru.kabanchik.client.component.HomeComponent
import ru.kabanchik.client.feature.profile.api.ClientProfileScreen
import ru.kabanchik.common.uiKit.icons.KabanchikIcons
import ru.kabanchik.common.uiKit.widgets.CommonBottomBar
import ru.kabanchik.common.uiKit.widgets.CommonBottomBarItem
import ru.kabanchik.feature.client.chatDetails.api.flow.ClientChatFlowScreen

private val Tabs = HomeComponent.Tab.entries

@Composable
fun HomeScreen(component: HomeComponent) {
    val stack by component.stack.subscribeAsState()
    val activeChild = stack.active.instance

    val showBottomNavigation = when (activeChild) {
        is HomeComponent.Child.Chats -> {
            activeChild.component.showBottomNavigation.collectAsState().value
        }
        is HomeComponent.Child.Profile -> true
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Children(
            stack = stack,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            when (val child = it.instance) {
                is HomeComponent.Child.Chats -> ClientChatFlowScreen(component = child.component)
                is HomeComponent.Child.Profile -> ClientProfileScreen(component = child.component)
            }
        }

        AnimatedVisibility(
            visible = showBottomNavigation,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            CommonBottomBar(
                items = Tabs.map { it.toBottomBarItem() },
                selectedIndex = Tabs.indexOf(activeChild.tab),
                onSelect = { index -> component.onTabSelected(Tabs[index]) }
            )
        }
    }
}

@Composable
private fun HomeComponent.Tab.toBottomBarItem(): CommonBottomBarItem {
    return when (this) {
        HomeComponent.Tab.Chats -> CommonBottomBarItem(
            title = stringResource(Res.string.home_tab_chats),
            icon = KabanchikIcons.Chat20
        )
        HomeComponent.Tab.Profile -> CommonBottomBarItem(
            title = stringResource(Res.string.home_tab_profile),
            icon = KabanchikIcons.Profile80
        )
    }
}
