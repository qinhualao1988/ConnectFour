package com.example.connectfour

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


// 
enum class ChipColor{ EMPTY, RED, YELLOW }
enum class GameMode{ONE_PLAYER, TWO_PLAYER}

@Composable
fun GameScreen(
    mode: GameMode = GameMode.ONE_PLAYER,
    onHomeMenu: () -> Unit = {}

) {
    // 6 x 7 grid array for board to track chips
    val boardState = remember {mutableStateListOf(*Array(6) { Array(7) { ChipColor.EMPTY } })}
    var playerTurn by remember {mutableStateOf(ChipColor.RED)}
    var winner by remember {mutableStateOf<ChipColor?>(null)}

    // Drop game piece
    fun dropChip(col: Int) {
        if(winner != null) return

        // Search through rows bottom-up
        for(r in 5 downTo 0) {
            if(boardState[r][col] == ChipColor.EMPTY) {
                // update grid state
                val updatedRow = boardState[r].copyOf()
                updatedRow[col] = playerTurn
                boardState[r] = updatedRow

                //Next Turn
                playerTurn = if(playerTurn == ChipColor.RED) ChipColor.YELLOW else ChipColor.RED
                break
            }
        }
    }
    // Reset Game
    fun resetBoard() {
        for(r in 0..5) {
            boardState[r] = Array(7) { ChipColor.EMPTY }
        }
        playerTurn = ChipColor.RED
        winner = null
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // First Component Game Status Header
        GameStatusHeader(
            currentTurn = playerTurn,
            winner = winner,
            mode = mode
        )

        // 2nd component Connect 4 board
        ConnectFourBoard(
            boardState = boardState,
            onColumnSelected = {col -> dropChip(col)}
        )

        //3rd Component
        GameControlPanel(
            onReset = {resetBoard()},
            onHomeMenu = onHomeMenu
        )
    }
}
// Status Header shows current player turn
@Composable
fun GameStatusHeader(
    currentTurn: ChipColor,
    winner: ChipColor?,
    mode: GameMode

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
            if(winner != null) {
                Text(
                    text = "${if (winner == ChipColor.RED) "RED" else "YELLOW"} WINS!",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = if(winner == ChipColor.RED) Color(0xFFEF4444) else Color(0xFFEAB308)
                )
            } else {
                Text(
                    text = "Current Turn: ",
                    fontSize = 18.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(
                            if(currentTurn == ChipColor.RED) Color(0xFFEF4444) else Color(0xFFEAB308)
                        )
                )
                Text(
                    text = if(currentTurn == ChipColor.RED) " RED" else " YELLOW",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = if(currentTurn == ChipColor.RED) Color(0xFFEF4444) else Color(0xFFEAB308)
                )
            }
        }
    }
}
// 2nd Component Connect Four Board Grid
@Composable
fun ConnectFourBoard(
    boardState: List<Array<ChipColor>>,
    onColumnSelected: (Int) -> Unit
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
            for (col in 0 until 7) {
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onColumnSelected(col) }
                        .padding(vertical = 4.dp, horizontal = 2.dp)
                ) {
                    for (row in 0 until 6) {
                        val chip = boardState[row][col]
                        val chipColor = when (chip) {
                            ChipColor.EMPTY -> Color(0xFF0F172A)
                            ChipColor.RED -> Color(0xFFEF4444)
                            ChipColor.YELLOW -> Color(0xFFEAB308)
                        }
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(chipColor)
                                .border(
                                    width = 2.dp,
                                    color = if (chip == ChipColor.EMPTY) Color(0xFF1E293B) else Color.Transparent,
                                    shape = CircleShape
                                )
                        )
                    }
                }
            }
        }
    }
}

//3rd Component: Home and Reset Buttons
@Composable
fun GameControlPanel(
    onReset: () -> Unit,
    onHomeMenu: () -> Unit

) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedButton(
            onClick = onHomeMenu,
            modifier = Modifier.weight(1f).height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
        ) {
            Text("Main Menu", fontSize = 16.sp)
        }

        Button(
            onClick = onReset,
            modifier = Modifier.weight(1f).height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB))
        ) {
            Text("Reset Game", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}
