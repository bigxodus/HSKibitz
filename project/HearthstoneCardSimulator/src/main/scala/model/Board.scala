package model

import model.gamestate.GameState

case class Board (
    player1: Player,
    player2: Player,
    turn: Turn.Value,
    state: GameState
) {
    def currentPlayer: Player = {
        if (Turn.TURN_PLAYER_1 == turn) {
            player1
        } else {
            player2
        }
    }
}
