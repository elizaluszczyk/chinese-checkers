package com.chinesecheckers.parsers;

import com.chinesecheckers.board.Move;
import com.chinesecheckers.exceptions.InvalidMoveException;
import com.chinesecheckers.interfaces.MoveParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StandardMoveParser implements MoveParser {
    
    private static final Logger logger = LoggerFactory.getLogger(StandardMoveParser.class);
    private static final String MOVE_COMMAND = "MOVE";
    private static final String TO_KEYWORD = "TO";
    private static final String EXPECTED_FORMAT = "MOVE x1,y1 TO x2,y2";
    
    @Override
    public Move parseMove(String message) throws InvalidMoveException {
        logger.debug("Parsing move command: {}", message);
        
        if (message == null) {
            logger.error("Received null move command");
            throw new InvalidMoveException("Move command cannot be null.");
        }
        
        String trimmedMessage = message.trim();
        
        if (!trimmedMessage.startsWith(MOVE_COMMAND)) {
            logger.warn("Invalid move format, doesn't start with '{}': {}", MOVE_COMMAND, trimmedMessage);
            throw new InvalidMoveException("Invalid move format. Move should start with '" + MOVE_COMMAND + "'.");
        }
        
        try {
            String[] parts = trimmedMessage.split("\\s+");
            
            if (parts.length != 4) {
                logger.warn("Invalid move format, incorrect number of parts ({}): {}", parts.length, trimmedMessage);
                throw new InvalidMoveException("Invalid move format. Expected: '" + EXPECTED_FORMAT + "'.");
            }
            
            if (!parts[2].equalsIgnoreCase(TO_KEYWORD)) {
                logger.warn("Invalid move format, missing '{}' keyword: {}", TO_KEYWORD, trimmedMessage);
                throw new InvalidMoveException("Invalid move format. Expected keyword '" + TO_KEYWORD + "' as the third part.");
            }
            
            String[] startPosition = parts[1].split(",");
            String[] endPosition = parts[3].split(",");
            
            if (startPosition.length != 2 || endPosition.length != 2) {
                logger.warn("Invalid coordinates format: start={}, end={}", parts[1], parts[3]);
                throw new InvalidMoveException("Invalid coordinates format. Expected 'x,y' for both positions.");
            }
            
            try {
                int x1 = Integer.parseInt(startPosition[0]);
                int y1 = Integer.parseInt(startPosition[1]);
                int x2 = Integer.parseInt(endPosition[0]);
                int y2 = Integer.parseInt(endPosition[1]);
                
                logger.debug("Successfully parsed move: ({},{}) -> ({},{})", x1, y1, x2, y2);
                
                return new Move(x1, y1, x2, y2);
            } catch (NumberFormatException e) {
                logger.error("Failed to parse coordinate values: {}", e.getMessage());
                throw new InvalidMoveException("Coordinates must be valid integers.");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            logger.error("Array index error while parsing move: {}", e.getMessage());
            throw new InvalidMoveException("Invalid move format. Expected: '" + EXPECTED_FORMAT + "'.");
        }
    }
}
