package com.example.myapplication

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.stylusHoverIcon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// game UI section
@Composable
fun GameScreen(
    viewModel: GameViewModel,
    onHomeMenu: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        //Header
        GameStatusHeader(
            message = viewModel.message,
            currentPlayer = viewModel.currentPlayer
        )

        // Board Grid and Drop Animation
        ConnectFourBoard(
            board = viewModel.board,
            gameState = viewModel.gameState,
            onColumnClick = viewModel::playColumn
        )

        // Bottom Controls
        GameControlPanel(
            onReset = viewModel::resetGame,
            onHomeMenu = onHomeMenu
        )
    }
}

@Composable
private fun GameStatusHeader(
    message: String,
    currentPlayer: Player?
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = message,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            if (currentPlayer != null) {
                Spacer(modifier = Modifier.padding(4.dp))
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .clip(CircleShape)
                        .background(
                            if (currentPlayer.pieceColor == Piece.RED) Color(0xFFEF4444) else Color(
                                0xFFEAB308
                            )
                        )
                )
            }
        }
    }
}

@Composable
private fun ConnectFourBoard(
    board: Array<Array<Piece>>,
    gameState: GameState,
    onColumnClick: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .shadow(12.dp, RoundedCornerShape(20.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF2563EB), Color(0xFF1D4ED8))
                ),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(12.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 7 Columns
            for (col in 0 until 7) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable(enabled = gameState == GameState.PLAYING) {
                            onColumnClick(col)
                        }
                        .padding(vertical = 4.dp)
                ) {
                    // 6 Rows
                    for (row in 0 until 6) {
                        AnimatedBoardCell(
                            piece = board[row][col]
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AnimatedBoardCell(
    piece: Piece
) {
    val chipColor = when (piece) {
        Piece.EMPTY -> Color(0xFF0F172A)
        Piece.RED -> Color(0xFFEF4444)
        Piece.YELLOW -> Color(0xFFEAB308)
    }

    // Disc drop animation for y offset
    val offsetY = remember { Animatable(-300f) }

    LaunchedEffect(piece) {
        if (piece != Piece.EMPTY) {
            offsetY.snapTo(-300f) // Starts above slot
            offsetY.animateTo(
                targetValue = 0f,
                animationSpec = tween(
                    durationMillis = 350,
                    easing = FastOutSlowInEasing
                )
            )
        } else {
            offsetY.snapTo(0f)
        }
    }

    Box(
        modifier = Modifier
            .padding(vertical = 3.dp)
            .aspectRatio(1f)
            .offset { IntOffset(0, offsetY.value.toInt()) }
            .clip(CircleShape)
            .background(chipColor)
            .border(
                width = 2.dp,
                color = if (piece == Piece.EMPTY) Color(0xFF1E293B) else Color.Transparent,
                shape = CircleShape
            )
    )
}

@Composable
private fun GameControlPanel(
    onReset: () -> Unit,
    onHomeMenu: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedButton(
            onClick = onHomeMenu,
            modifier = Modifier
                .weight(1f)
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
        ) {
            Text("Main Menu", fontSize = 16.sp)
        }

        Button(
            onClick = onReset,
            modifier = Modifier
                .weight(1f)
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB))
        ) {
            Text("Reset Game", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}



    /*
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
               */
