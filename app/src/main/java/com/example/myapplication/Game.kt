package com.example.myapplication

// enum class to store type of slots
enum class Piece {
    EMPTY, RED, YELLOW
}

enum class GameState {
    PLAYING, RED_WIN, YELLOW_WIN, DRAW
}

// player class for store player information
class Player(val name: String, val pieceColor: Piece) {
    val color: Piece get() = pieceColor
    fun playerInfo() {
        println("player information: \nname: $name \npieceColor: $pieceColor \n")
    }
}

// board class
class Board(val rows: Int = 6, val columns: Int = 7) {

    private val grid = Array(rows) {Array(columns) { Piece.EMPTY } } // 2-d array, initial value: EMPTY

    // this function is for compose/ViewModel
    fun getBoard(): Array<Array<Piece>> = grid

    /* drop the piece to the board
     * core idea: loop the 2d array from the bottom to top
     * check if the cell is empty. if empty -- drop, else, keep looping
     * player can't choose which row they can drop: only can choose which column they can drop
     */
    fun dropPiece(column: Int, piece: Piece): Boolean {

        // input validation
        if (column !in 0 until columns) {
            return false
        }

        // loop board from the bottom to the top and check if the cell is available to drop
        for (row in rows - 1 downTo 0) {
            if (grid[row][column] == Piece.EMPTY) {
                grid[row][column] = piece
                return true
            }
        }

        return false // selected column is full
    }

    // check selected column is full
    fun isColumnFull(column: Int): Boolean {
        return column in 0 until columns && grid[0][column] != Piece.EMPTY
    }

    // check the board is full
    fun isBoardFull(): Boolean {
        for (column in 0 until columns) {
            if (!isColumnFull(column)) {
                return false
            }
        }
        return true
    }

    // get the value of cell in board
    fun getCell(row: Int, col: Int): Piece {

        //check if it is in valid row and column
        require(row in 0 until rows) {"invalid row: $row"}
        require(col in 0 until columns) {"invalid column: $col"}

        // return result
        return grid[row][col]
    }

    // clear the board
    fun clearBoard() {
        for (row in 0 until rows) {
            for (col in 0 until columns) {
                grid[row][col] = Piece.EMPTY
            }
        }
    }

    // check if a player win the game
    fun checkWin(piece: Piece): Boolean {
        //input validate: empty cells can not win
        if (piece == Piece.EMPTY) {
            return false
        }

        // 1. check horizontal
        for (row in 0 until rows) {
            for (col in 0 .. columns - 4) {
                if (
                    grid[row][col] == piece &&
                    grid[row][col + 1] == piece &&
                    grid[row][col + 2] == piece &&
                    grid[row][col + 3] == piece
                ) {
                    return true
                }
            }
        }

        // 2. check vertical
        for (row in 0 .. rows - 4) {
            for (col in 0 until columns) {
                if (
                    grid[row][col] == piece &&
                    grid[row + 1][col] == piece &&
                    grid[row + 2][col] == piece &&
                    grid[row + 3][col] == piece
                ) {
                    return true
                }
            }
        }

        // 3. check diagonal: top-left to bottom-right
        for (row in 0 .. rows - 4) {
            for (col in 0 .. columns - 4) {
                if (
                    grid[row][col] == piece &&
                    grid[row + 1][col + 1] == piece &&
                    grid[row + 2][col + 2] == piece &&
                    grid[row + 3][col + 3] == piece
                ) {
                    return true
                }
            }
        }

        // 4. check diagonal:  top-right to bottom-left
        for (row in 0 .. rows - 4) {
            for (col in 3 until columns) {
                if (
                    grid[row][col] == piece &&
                    grid[row + 1][col - 1] == piece &&
                    grid[row + 2][col - 2] == piece &&
                    grid[row + 3][col - 3] == piece
                ) {
                    return true
                }
            }
        }
        // no four cells connect
        return false
    }


} // end board class

class ConnectFourGame {
    val board = Board()
    private val player1 = Player("player1", Piece.RED)
    private val player2 = Player("player2", Piece.YELLOW)
    var currentPlayer: Player = player1 // must be mutable(read and write) in order to swap player
        private set
    var gameState: GameState = GameState.PLAYING
        private set

    private fun switchPlayer() {
        currentPlayer =
            if (currentPlayer == player1) {
                player2
            }else {
                player1
            }
    }

    /*
     * work flow:
     * 1. check game is playing
     * 2. drop piece
     * 3. check piece successfully dropped
     * 4. check this player is won or not
     * 5. check board is full
     * 6. switch player
     */
    fun playTurn(column: Int): Boolean {

        // 1. check game is playing
        if (gameState != GameState.PLAYING) {
            return false
        }
        // 2. drop piece by current player
        val successfulDropPiece = board.dropPiece(column, currentPlayer.pieceColor)

        // 3. check if piece successfully dropped -- if not successfully dropped, do it again
        if (!successfulDropPiece) {
            return false
        }

        // 4. check current player win or not
        if (board.checkWin(currentPlayer.pieceColor)) {
            gameState =
                if (currentPlayer.pieceColor == Piece.RED) {
                    GameState.RED_WIN
                }else {
                    GameState.YELLOW_WIN
                }

            return true
        }

        // 5. check board is full
        if (board.isBoardFull()) {
            gameState = GameState.DRAW

            return true
        }

        //switch player
        switchPlayer()

        return true // keep going
    }

    // game reset
    fun gameReset() {
        currentPlayer = player1
        board.clearBoard()
        gameState = GameState.PLAYING
    }
}