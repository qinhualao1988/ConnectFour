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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Brush
import androidx.compose.foundation.clickable
// game UI section
@Composable
fun GameScreen(
    viewModel: GameViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // game title
        GameStatusHeader(
            message = viewModel.message
        )
        // create game buttons to let player drop cell to the column


        GameBoard(
            board = viewModel.board,
            gameState = viewModel.gameState,
            onColumnClick = viewModel::playColumn
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
private fun GameStatusHeader(
    message: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(16.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E293B)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Connect Four",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Text(
                text = message,
                fontSize = 20.sp,
                color = Color.White
            )
        }
    }
}


@Composable
private fun GameBoard(
    board: Array<Array<Piece>>,
    gameState: GameState,
    onColumnClick: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(20.dp)
            )
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF2563EB),
                        Color(0xFF1D4ED8)
                    )
                ),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        for (column in 0 until 7) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable(
                        enabled = gameState == GameState.PLAYING
                    ) {
                        onColumnClick(column)
                    },
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                for (row in 0 until 6) {
                    BoardCell(
                        modifier = Modifier.fillMaxWidth(),
                        piece = board[row][column]
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
        Piece.EMPTY -> Color(0xFF0F172A)
        Piece.RED -> Color(0xFFEF4444)
        Piece.YELLOW -> Color(0xFFEAB308)
    }

    val borderColor = if (piece == Piece.EMPTY) {
        Color(0xFF334155)
    } else {
        Color.Transparent
    }

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(cellColor)
            .border(
                width = 2.dp,
                color = borderColor,
                shape = CircleShape
            )
    )
}