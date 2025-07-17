package com.chinesecheckers.interfaces;

import java.util.ArrayList;

public interface Board {
  ArrayList<ArrayList<Field>> getBoard();

  Field getField(int x, int y);

  ArrayList<ArrayList<Field>> getArmsOfStar();
}
