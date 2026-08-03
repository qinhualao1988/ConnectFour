package com.example.myapplication

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class GameViewModel: ViewModel() {

    private val game = ConnectFourGame()

    var board by mutableStateOf(copyBoard())
        private set

    var currentPlayer by mutableStateOf(game.currentPlayer)
        private set

    var gameState by mutableStateOf(game.gameState)
        private set

    var message by mutableStateOf(
        "${game.currentPlayer.name}'s turn： ${game.currentPlayer.pieceColor}"
    )
        private set

    fun playColumn(column: Int) {
        val success = game.playTurn(column)

        if(!success) {
            message = "this column is full"
            return
        }

        updateUiState()
    }

    fun resetGame() {
        game.gameReset()
        updateUiState()
    }

    /*
     * Array<Array<Piece>> => matrix with Piece in element.
     * this function return a copy of the current game board
     * .map{ row -> row.copyOf() } => copy each row to create a new board
     * this allows 'Compose' to know that the board has changed and recompose the UI
     * toTypedArray() => convert 'list' back to 'array' since .map() return list type
     */
    private fun copyBoard(): Array<Array<Piece>> {
        return game.board.getBoard().map{ row -> row.copyOf() }.toTypedArray()
    }

    private fun updateUiState() {
        board = copyBoard()
        currentPlayer = game.currentPlayer
        gameState = game.gameState

        message = when(game.gameState) {
            GameState.PLAYING -> "${game.currentPlayer.name}'s turn: ${game.currentPlayer.pieceColor}"
            GameState.RED_WIN -> "Player 1 wins the game"
            GameState.YELLOW_WIN -> "Player 2 wins the game"
            GameState.DRAW -> "Game Draw"
        }
    }

}