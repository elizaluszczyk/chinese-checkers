package com.example.trylma.interfaces;

import com.example.trylma.board.Pawn;

    public interface Field {
        boolean isOccupied();

        void setOccupied(boolean occupied);
    }
