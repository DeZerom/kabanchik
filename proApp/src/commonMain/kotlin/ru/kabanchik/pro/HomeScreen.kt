package ru.kabanchik.pro

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
import kabanchik.proapp.generated.resources.Res
import kabanchik.proapp.generated.resources.home_tab_chats
import kabanchik.proapp.generated.resources.home_tab_profile
import org.jetbrains.compose.resources.stringResource
import ru.kabanchik.common.uiKit.icons.KabanchikIcons
import ru.kabanchik.common.uiKit.widgets.CommonBottomBar
import ru.kabanchik.common.uiKit.widgets.CommonBottomBarItem
import ru.kabanchik.pro.component.ProHomeComponent
import ru.kabanchik.pro.feature.chat.api.flow.ProChatFlowScreen
import ru.kabanchik.pro.feature.profile.api.ProProfileScreen

private val Tabs = ProHomeComponent.Tab.entries

@Composable
fun HomeScreen(component: ProHomeComponent) {
    val stack by component.stack.subscribeAsState()
    val activeChild = stack.active.instance

    val showBottomNavigation = when (activeChild) {
        is ProHomeComponent.Child.Chats -> {
            activeChild.component.showBottomNavigation.collectAsState().value
        }
        is ProHomeComponent.Child.Profile -> true
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
                is ProHomeComponent.Child.Chats -> ProChatFlowScreen(component = child.component)
                is ProHomeComponent.Child.Profile -> ProProfileScreen(component = child.component)
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
private fun ProHomeComponent.Tab.toBottomBarItem(): CommonBottomBarItem {
    return when (this) {
        ProHomeComponent.Tab.Chats -> CommonBottomBarItem(
            title = stringResource(Res.string.home_tab_chats),
            icon = KabanchikIcons.Chat20
        )
        ProHomeComponent.Tab.Profile -> CommonBottomBarItem(
            title = stringResource(Res.string.home_tab_profile),
            icon = KabanchikIcons.Profile80
        )
    }
}
