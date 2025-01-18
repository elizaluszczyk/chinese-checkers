package com.example.trylma;

import com.example.trylma.board.DefaultBoardWithPlacedPawns;
import com.example.trylma.interfaces.Field;
import com.example.trylma.interfaces.Player;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BoardController {

    private Map<String, CircleField> fieldsMap = new HashMap<>();
    private Pane pane;
    private ArrayList<ArrayList<CircleField>> circleFields = new ArrayList<>();
    private DefaultBoardWithPlacedPawns board = new DefaultBoardWithPlacedPawns(2);
    private final ArrayList<Player> listOfPlayers = board.getListOfPlayers();
//    private final ArrayList<Player> listOfPlayers = ArrayList<>();
    private ArrayList<ArrayList<Circle>> armsOfStarCircles = new ArrayList<>();
    private final int hexagon = board.getHexagon();

    public BoardController() {

    }

    public void placePawnsCircles(Pane pane) {
        for(Player player : listOfPlayers) {
            ArrayList<Field> startingPositions = player.getStartingPositions();
            for(Field field : startingPositions) {
                CircleField circleField = fieldsMap.get(generateKey(field.getX(), field.getY()));
                CirclePawn pawn = new CirclePawn(circleField.getCenterX(), circleField.getCenterY(), 15);
                circleField.setPawn(pawn);
                pawn.setFill(Color.BLUE);
                pawn.setPawn(field.getPawn());
                pane.getChildren().add(pawn);
            }
        }
    }
    public void boardSetup(Pane pane){
        createTriangleBoard(pane, 50);
        assignFields();
        mappingAllFields();
        placePawnsCircles(pane);
    }

    public void assignFields() {
        for (int i = 0; i < circleFields.size(); i++) {
            for (int j = 0; j < circleFields.get(i).size();j++) {
                CircleField field = circleFields.get(i).get(j);
                field.setField(board.getField(j,i));
            }
        }
    }

    public void createTriangleBoard(Pane pane, double triangleSide) {
        double width = pane.getWidth();
        double height = pane.getHeight();
        double triangleHeight = triangleSide*Math.sqrt(3) /2;

        int i = 1;
        double startingPointX = width/2;
        double startingPointY1 = (height/20 + 4*triangleHeight);
        for (int y = (int) height/20; y <  startingPointY1 - triangleHeight ; y+= (int) triangleHeight) {
            ArrayList<CircleField> row = new ArrayList<>();
            int multiply = 0;
            for (int x = 0;x < i ; x++) {
                CircleField circle = new CircleField(startingPointX + triangleSide*multiply, y, 20);
                pane.getChildren().add(circle);
                row.add(circle);
                multiply++;
            }
            i++;
            startingPointX-= triangleSide/2;
            circleFields.add(row);
        }
        double startingPointY2 = startingPointY1 + 4 * triangleHeight;
        double startingPointX2 = width/2-6*triangleSide;
        int j = 13;
        for (int y = (int) startingPointY1; y < startingPointY2 - triangleHeight; y+= (int) triangleHeight){
            ArrayList<CircleField> row = new ArrayList<>();
            int multiply = 0;
            for (int x = 0; x<j; x++){
                CircleField circle = new CircleField(startingPointX2 + triangleSide*multiply, y, 20);
                pane.getChildren().add(circle);
                row.add(circle);
                multiply++;
            }
            j--;
            startingPointX2+= triangleSide/2;
            circleFields.add(row);
        }
        double startingPointY3 = startingPointY2 + 5 * triangleHeight;
        double startingPointX3 = width/2-4*triangleSide;
        int k = 9;
        for (int y = (int) startingPointY2; y < startingPointY3 - triangleHeight; y+= (int) triangleHeight){
            ArrayList<CircleField> row = new ArrayList<>();
            int multiply = 0;
            for (int x = 0;x < k ; x++) {
                CircleField circle = new CircleField(startingPointX3 + triangleSide*multiply, y, 20);
                pane.getChildren().add(circle);
                row.add(circle);
                multiply++;
            }
            k++;
            startingPointX3-= triangleSide/2;
            circleFields.add(row);
        }
        double startingPointX4 = width/2 - 1.5*triangleSide;
        int l = 4;
        for (int y = (int) startingPointY3; y < startingPointY3 + 3 *triangleHeight; y+= (int) triangleHeight){
            ArrayList<CircleField> row = new ArrayList<>();
            int multiply = 0;
            for (int x = 0; x<l; x++){
                CircleField circle = new CircleField(startingPointX4 + triangleSide*multiply, y, 20);
                pane.getChildren().add(circle);
                row.add(circle);
                multiply++;
            }
            l--;
            startingPointX4+= triangleSide/2;
            circleFields.add(row);
        }

    }
    private String generateKey(double x, double y) {
        return x + "," + y;
    }
    private void mappingAllFields() {
        System.out.println("Rozmiar circleFields: " + circleFields.size());
        System.out.println("Rozmiar board.getActiveFields(): " + board.getActiveFields().size());

        for (int i = 0; i < circleFields.size(); i ++) {
            for (int j = 0; j < circleFields.get(i).size(); j++) {
                CircleField field = circleFields.get(i).get(j);
                String fieldKey = generateKey(board.getActiveFields().get(i).get(j).getX(), board.getActiveFields().get(i).get(j).getY());
                fieldsMap.put(String.valueOf(fieldKey), field);
            }
        }
        System.out.println("Total fields in map: " + fieldsMap.size());
        System.out.println("Total circles in GUI: " + circleFields.size());
    }
//
}
