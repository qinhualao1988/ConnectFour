package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// game UI section
@Composable
fun GameScreen(
    viewModel: GameViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // game title
        Text(
            text = "Connect Four",
            fontSize = 30.sp
        )
        // use spacer() to add space between two elements
        Spacer(
            modifier = Modifier.padding(8.dp)
        )
        // let player know whose turn currently
        Text(
            text = viewModel.message,
            fontSize = 20.sp
        )

        Spacer(
            modifier = Modifier.padding(8.dp)
        )
        // create game buttons to let player drop cell to the column
        ColumnButtons(
            gameState = viewModel.gameState, // check game state to determine if the button is clickable
            onColumnClick = viewModel::playColumn // modify the viewmodel that the player selected this column
        )

        Spacer(
            modifier = Modifier.padding(8.dp)
        )

        GameBoard(
            board = viewModel.board
        )

        Spacer(
            modifier = Modifier.padding(12.dp)
        )

        Button(
            onClick = viewModel::resetGame // call resetGame() => function reference
        ) {
            Text("Reset Game")
        }
    }
}

@Composable
private fun ColumnButtons(
    gameState: GameState,
    onColumnClick: (Int) -> Unit // pass in column number
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        // use repeat() function to create 7 buttons
        repeat(7) { column ->
            OutlinedButton(
                modifier = Modifier.weight(1f),
                enabled = gameState == GameState.PLAYING,
                onClick = {
                    onColumnClick(column)
                }
            ) {
                Text(
                    text = "${column + 1}", // button's number start at 1
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
private fun GameBoard(
    board: Array<Array<Piece>>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Blue)
            .padding(4.dp),
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        board.forEach { row -> // means each row in the board(will be 6 in total)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                row.forEach { piece -> // means each piece in every row( will be 7 in total)
                    BoardCell(
                        modifier = Modifier.weight(1f),
                        piece = piece
                    )
                }
            }
        }
    }
}

@Composable
private fun BoardCell(
    piece: Piece,
    modifier: Modifier = Modifier
) {
    val cellColor = when (piece) {
        Piece.EMPTY -> Color.White
        Piece.RED -> Color.Red
        Piece.YELLOW -> Color.Yellow
    }

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(cellColor)
            .border(
                width = 1.dp,
                color = Color.DarkGray,
                shape = CircleShape
            )
    )
}