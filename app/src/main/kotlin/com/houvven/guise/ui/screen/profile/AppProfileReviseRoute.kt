package com.houvven.guise.ui.screen.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.houvven.guise.hook.store.ModuleStore
import com.houvven.guise.ui.screen.profile.components.rememberProfileReviseState
import com.houvven.guise.util.app.AppScanner
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import org.koin.compose.koinInject

@Destination<RootGraph>
@Composable
fun AppProfileReviseRoute(
    packageName: String,
    appScanner: AppScanner = koinInject(),
    moduleStore: ModuleStore.Hooker = koinInject()
) {
    val app = appScanner.getAppAsUser(packageName) ?: return
    // todo: add a app not found status page to handler error

    val reviseState = rememberProfileReviseState(
        profiles = moduleStore.get(packageName).copy(packageName = packageName)
    )
    val coordinator = rememberAppProfileReviseCoordinator(
        reviseState = reviseState, moduleStore = moduleStore, app = app
    )

    // State observing and declarations
    val uiState by coordinator.screenStateFlow.collectAsStateWithLifecycle(AppProfileReviseState())

    // UI Actions
    val actions = rememberAppProfileReviseActions(coordinator)

    // UI Rendering
    AppProfileReviseScreen(
        state = uiState,
        actions = actions,
        app = app,
        reviseState = reviseState
    )
}


@Composable
fun rememberAppProfileReviseActions(coordinator: AppProfileReviseCoordinator): AppProfileReviseActions {
    return remember(coordinator) {
        AppProfileReviseActions(
            onSave = coordinator::onSave,
            onClearAll = coordinator::onClearAll,
            onRestart = coordinator::onRestart,
            onStop = coordinator::onStop,
            onClearData = coordinator::onClearData,
            onCopyToClipboard = coordinator::onCopyToClipboard
        )
    }
}