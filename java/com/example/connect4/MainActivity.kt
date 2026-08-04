package com.example.connect4

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import android.view.animation.OvershootInterpolator

class MainActivity : AppCompatActivity() {

    private val ROWS = 6
    private val COLS = 7
    private val board = Array(ROWS) { IntArray(COLS) }
    private var p1Turn = true
    private var gameOver = false

    private val cells = Array(ROWS) { arrayOfNulls<Button>(COLS) }
    private lateinit var statusText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        statusText = findViewById(R.id.tvStatus)
        val grid = findViewById<GridLayout>(R.id.gridLayout)


        //Creating the std game board: 6r x 7c
        for (r in 0 until ROWS) {
            for (c in 0 until COLS) {
                cells[r][c] = Button(this).apply {
                    layoutParams = GridLayout.LayoutParams().apply {
                        width = 100; height = 100; setMargins(4, 4, 4, 4)
                    }
                    background = getDrawable(R.drawable.game_piece)
                    setOnClickListener { if (!gameOver) drop(c) }
                }
                grid.addView(cells[r][c])
            }
        }
        grid.setBackgroundColor("#2b68a0".toColorInt())
    }

    private fun drop(col: Int) {
        for (r in ROWS - 1 downTo 0) {
            //If blank, fill with player's color
            if (board[r][col] == 0) {
                val player = if (p1Turn) 1 else 2
                board[r][col] = player
                (cells[r][col]?.background as? GradientDrawable)?.setColor(
                    if (player == 1) Color.RED else Color.YELLOW
                )
                val targetButton = cells[r][col] ?: return
                animateDrop(targetButton, r)

                //Game end checker: Either a player wins or draw occurs
                if (checkWin(r, col, player)) {
                    statusText.text = "Player $player Wins!"
                    statusText.setTextColor(if (player == 1) Color.RED else Color.YELLOW)
                    gameOver = true
                } else if (isBoardFull()) {
                    statusText.text = "It's a Draw!"
                    gameOver = true
                } else {
                    p1Turn = !p1Turn
                    statusText.text = if (p1Turn) "Player 1's Turn" else "Player 2's Turn"

                    val turnColor = if (p1Turn) {
                        "#ff0000".toColorInt() //P1's Color
                    } else {
                        "#FFD700".toColorInt() //P2's Color
                    }
                    statusText.setTextColor(turnColor)
                }
                return
            }
        }
    }

    //Checks the win con with the help of fun count
    private fun checkWin(r: Int, c: Int, p: Int): Boolean {
                         //Horiz            //Vert            //Diag \         //Diag /
        val dirs = listOf(intArrayOf(0, 1), intArrayOf(1, 0), intArrayOf(1, 1), intArrayOf(1, -1))
        return dirs.any { (dr, dc) ->
            1 + count(r, c, dr, dc, p) + count(r, c, -dr, -dc, p) >= 4
        }
    }

    //Use delta's(d) to nav board,
    //dr: +1, -1, 0 -> down, up, stay on row
    //dc: +1, -1, 0 -> right, left, same column
    private fun count(r: Int, c: Int, dr: Int, dc: Int, p: Int): Int {
        var count = 0
        //nr and nc (new) checks to see the player's connect piece
        var nr = r + dr; var nc = c + dc
        while (nr in 0 until ROWS && nc in 0 until COLS && board[nr][nc] == p) {
            count++; nr += dr; nc += dc
        }
        return count
    }

    //I wonder what this fun does
    private fun isBoardFull(): Boolean {
        for (c in 0 until COLS) {
            if (board[0][c] == 0) return false
        }
        return true
    }
}

private fun animateDrop(button: Button, targetRow: Int) {
    //Each button/piece is 150px tall + 4px top margin + 4px bottom margin = 158px total per row
    //Same size as the pieces being placed
    val rowHeight = 158f

    //Calculate distance to travel from above the board
    val distanceToTravel = -((targetRow + 1) * rowHeight)

    //The button is there but isn't (invisible)
    button.translationY = distanceToTravel
    button.alpha = 0f

    //Fade in and animate falling down
    button.animate()
        .translationY(0f)
        .alpha(1f)
        .setDuration(400)
        .setInterpolator(OvershootInterpolator(0.5f)) //Adds that lil bounce
        .start()
}