package com.example.connectfour

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout


enum class DiscColor{ EMPTY, RED, YELLOW }

@Composable
fun GameScreen(
    mode: GameMode = GameMode.ONE_PLAYER,
    onHomeMenu: () -> Unit = {}

) {
    // 6 x 7 grid array for board
    val boardState = remember { mutableStateListOf(*Array(6) { Array(7) { DiscColor.EMPTY } })}
    var playerTurn by remember
    var winner

    // Drop game piece
    fun dropPiece(col: Int) {
        if(winner != null) return

        // Search through rows bottom-up
        for() {
            if(boardState)
        }



    }

    fun resetBoard() {
        DiscColor.Empty
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangment = Arrangment.SpaceBetween
    ) {

    }
}
@Composable
fun GameStatusHeader(
    currentTurn: DiscColor,
    winner: DiscColor?,
    mode: GameMode

) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow()
        colors = ""
        shape = ""
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = ""
            horizontalArrangement = Arrangement.Center
        ) { if(winner != null) {
            Text(

            )
        } else {
            Text(

            )
            Box(

            )
            Text(

            )
        }

        }
    }

}

@Composable
fun ConnectFourBoard(
    boardState: List<>,
    onColumnSelected: (Int) -> Unit

) {
    Box(

    ) {
        Row(

        ) {
            for(

            )
                for(

                )
        }
    }

}

@Composable
fun GameControlPanel(
    onReset: () -> Unit,
    onHomeMenu: () -> Unit

) {
    Row(

    ) {
        OutlinedButton(
            onClick = onHomeMenu,
            modifier = Modifier.weight(1f).height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ""
        ) {
            Text("Main Menu")
        }

        Button(
            onClick = onReset,
            modifier = Modifier.weight(1f).height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB))
        ) {
            Text("Reset Game")
        }
    }


}
