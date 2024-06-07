package br.com.joaogalli.flickrimages.ui.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

abstract class BaseViewModel<STATE, COMMAND> : ViewModel() {

    private val _uiState = MutableStateFlow<STATE>(this.initialState())

    abstract fun initialState(): STATE

    protected fun newState(state: STATE) {
        _uiState.value = state
    }

    @Composable
    fun getStateCompose() : STATE {
        return _uiState.asStateFlow().collectAsStateWithLifecycle().value
    }

    fun getStateValue() : STATE {
        return _uiState.asStateFlow().value
    }

    protected fun updateState(block: (currentState: STATE) -> STATE) {
        val newState = block(_uiState.value)
        newState(newState)
    }

    private val _commandChannel = Channel<COMMAND>()
    private val commandChannelFlow = _commandChannel.receiveAsFlow()

    protected suspend fun newCommand(command: COMMAND) {
        _commandChannel.send(command)
    }

    suspend fun receiveFromCommandChannel(): COMMAND {
        return _commandChannel.receive()
    }

    fun cancelCommandChannel() {
        _commandChannel.cancel()
    }

    @Composable
    fun ListenCommand(onCommand: (command: COMMAND) -> Unit) {
        val lifecycle = LocalLifecycleOwner.current.lifecycle

        LaunchedEffect(this, lifecycle) {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                commandChannelFlow.collect {
                    onCommand(it)
                }
            }
        }
    }
}