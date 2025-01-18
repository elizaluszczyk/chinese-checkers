package com.example.trylma;

import com.example.trylma.board.DefaultBoardWithPlacedPawns;
import com.example.trylma.client.GameClient;
import com.example.trylma.server.GameServer;

public class App {
    public static void main(String[] args) {
     DefaultBoardWithPlacedPawns board = new DefaultBoardWithPlacedPawns(2);
    board.printBoard();
    board.printActiveFields();
    // board.printStartingPositions();
//       if (args.length == 0) {
//            System.out.println("Please specify 'server' or 'client' as an argument.");
//            return;
//        }
//
//        String mode = args[0].toLowerCase();
//        int port = 58901;
//                if (null == mode) {
//            System.out.println("Invalid argument. Please specify 'server' or 'client'.");
//        } else switch (mode) {
//            case "server" -> {
//                GameServer server = new GameServer(port);
//                server.start();
//            }
//           case "client" -> {
//                String serverAddress = "localhost";
//                GameClient client = new GameClient(serverAddress, port);
//                client.start();
//           }
//            default -> System.out.println("Invalid argument. Please specify 'server' or 'client'.");
//       }
    }
}
