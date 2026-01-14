package com.effatheresoft.mindlesslyhiragana.ui.home

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.effatheresoft.mindlesslyhiragana.R

@Composable
fun HomeDrawer(
    @StringRes title: Int,
    drawerState: DrawerState,
    onResetButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    text = stringResource(title),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(16.dp)
                )
                HorizontalDivider()
                NavigationDrawerItem(
                    label = { Text(stringResource(R.string.reset_progress)) },
                    selected = false,
                    onClick = onResetButtonClick,
                    icon = { Icon(
                        painter = painterResource(R.drawable.delete_24px),
                        contentDescription = null
                    ) }
                )
            }
        },
        modifier = modifier
    ) {
        content()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar(
    @StringRes title: Int,
    onMenuIconClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = { Text(stringResource(title)) },
        navigationIcon = {
            IconButton(onClick = onMenuIconClick) {
                Icon(
                    painter = painterResource(R.drawable.menu_24px),
                    contentDescription = stringResource(R.string.open_menu)
                )
            }
        },
        modifier = modifier
    )
}

@Composable
fun HomeCategoryItem(
    title: String,
    isLocked: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextButton(
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
    ) {
        Row {
            Text(title)
            Spacer(modifier = Modifier.weight(1f))
            when(isLocked) {
                true -> Icon(
                    painter = painterResource(R.drawable.lock_24px),
                    contentDescription = stringResource(R.string.x_category_locked, title)
                )
                false -> Icon(
                    painter = painterResource(R.drawable.arrow_right_24px),
                    contentDescription = stringResource(R.string.x_category_unlocked, title)
                )

            }
        }
    }
}

@Composable
fun HomeDialog(
    onConfirm: () -> Unit,
    @StringRes onConfirmLabel: Int,
    onDismiss: () -> Unit,
    @StringRes title: Int,
    @StringRes text: Int,
    @DrawableRes icon: Int,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = { Button(onConfirm) { Text(stringResource(onConfirmLabel)) } },
        dismissButton = { Button(onConfirm) { Text(stringResource(R.string.cancel)) } },
        title = { Text(stringResource(title)) },
        text = { Text(stringResource(text)) },
        icon = { Icon(painterResource(icon), null) },
        modifier = modifier
    )
}
