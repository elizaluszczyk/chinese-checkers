package com.example.trylma.parsers;

import com.example.trylma.board.Move;
import com.example.trylma.exceptions.InvalidMoveException;
import com.example.trylma.interfaces.MoveParser;

public class StandardMoveParser implements MoveParser {

    @Override
    public Move parseMove(String message) throws InvalidMoveException {
        if (!message.startsWith("MOVE")) {
            throw new InvalidMoveException("Invalid move format. Move should start with 'MOVE'.");
        }
        try {
            String[] parts = message.split(" ");
            if (parts.length != 4 || !parts[2].equalsIgnoreCase("TO")) {
                throw new InvalidMoveException("Invalid move format. Expected: 'MOVE x1,y1 TO x2,y2'.");
            }

            String[] startPosition = parts[1].split(",");
            String[] endPosition = parts[3].split(",");

            int x1 = Integer.parseInt(startPosition[0]);
            int y1 = Integer.parseInt(startPosition[1]);
            int x2 = Integer.parseInt(endPosition[0]);
            int y2 = Integer.parseInt(endPosition[1]);

            return new Move(x1, y1, x2, y2);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            throw new InvalidMoveException("Invalid move coordinates. Ensure the format is 'x1,y1 TO x2,y2'.");
        }
    }

}
