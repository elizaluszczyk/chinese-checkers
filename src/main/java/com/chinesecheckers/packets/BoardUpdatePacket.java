package com.chinesecheckers.packets;

import java.util.ArrayList;

import com.chinesecheckers.interfaces.Field;
import com.chinesecheckers.server.ServerPacket;

public class BoardUpdatePacket implements ServerPacket {
    private static final long serialVersionUID = 1L;
    private final ArrayList<ArrayList<FieldData>> board;

    public BoardUpdatePacket(ArrayList<ArrayList<Field>> board) {
        this.board = new ArrayList<>();
        
        for (ArrayList<Field> row : board) {
            this.board.add(new ArrayList<>());
            for (Field field : row) {
                
                
                String playerId = null;

                if (field.isOccupied()) playerId = field.getPawn().getPlayerId();

                this.board.get(this.board.size() - 1).add(new FieldData(
                    field.getX(),
                    field.getY(),
                    field.isActive(),
                    field.isOccupied(),
                    playerId
                ));
            }
        }
    }

    public ArrayList<ArrayList<FieldData>> getBoard() {
        return board;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (ArrayList<FieldData> row : board) {
            for (FieldData field : row) {
                builder.append(field.isActive() ? field.toString() : " ");
            }
            builder.append("\n");
        }
        builder.append("]");
        return builder.toString();
    }

    @Override
    public PacketType getType() {
        return PacketType.BOARD_UPDATE;
    }
}
