package ui;

import chess.ChessGame;
import chess.ChessPosition;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.internal.LinkedTreeMap;
import websocket.ServerFacadeWebsocket;
import websocket.commands.UserGameCommand;

import java.util.*;

import static ui.DisplayLogic.*;

public class PostLoginUIHelperClasses {
    public int findIDObserver() throws Exception {
        String listofGames = serverFacade.listGame(authToken);
        Map gameDataInfoArray = new Gson().fromJson(listofGames, Map.class);
        ArrayList<LinkedTreeMap> gamesInGameList = (ArrayList<LinkedTreeMap>) gameDataInfoArray.get("games");
        System.out.println("Here are the games!");
        int playGameInputs = 0;
        findIDPlayHelperHelper(gamesInGameList);
        System.out.println("Please input the game number you would like to observe.");
        LinkedTreeMap correctGame = null;

        playGameInputs = new PreloginUI().getInputInt();
        boolean validInput = false;
        correctGame = helperFuncForCodeQuality(validInput, playGameInputs, correctGame,gamesInGameList);
        Object correctGameID = correctGame.get("gameID");
        double newCorrectGameID = (double)correctGameID;
        String observeMap = new Gson().toJson(correctGame.get("game"));
        JsonParser parser = new JsonParser();
        JsonElement jsonItem = parser.parseString(observeMap);
        JsonObject object = jsonItem.getAsJsonObject();
        ChessGame game = new Gson().fromJson(object, ChessGame.class);
        DisplayLogic.game = game;
        UserGameCommand joining = new UserGameCommand(UserGameCommand.CommandType.CONNECT,
                authToken, (int) newCorrectGameID);
        ServerFacadeWebsocket.session.getBasicRemote().sendText(new Gson().toJson(joining));
        return playGameInputs;
    }
    public void findIDPlayHelperHelper(ArrayList<LinkedTreeMap> gamesInGameList ) {
        for (int i = 0; i < gamesInGameList.size(); i++) {
            LinkedTreeMap hopeGame = gamesInGameList.get(i);
            Object shouldbeName = hopeGame.get("gameName");
            String newShouldBeName = (String) shouldbeName;
            System.out.println("Game number " + (i + 1) + " " + "Game name" + newShouldBeName);
            if (hopeGame.get("whiteUsername") != null) {
                Object whiteUsername = hopeGame.get("whiteUsername");
                String newWhiteUsername = (String) whiteUsername;
                System.out.println("Game number " + (i + 1) + " White player " + newWhiteUsername);
            } else {
                System.out.println((i + 1) + " " + " There is no white player yet in this game. " +
                        "You can be registered as the white player in this game.");
            }
            if (hopeGame.get("blackUsername") != null) {
                Object blackUsername = hopeGame.get("blackUsername");
                String newBlackUsername = (String) blackUsername;
                System.out.println("Game number " + (i + 1) + " Black player " + newBlackUsername);
            } else {
                System.out.println((i + 1) + " " + " There is no black player yet in this game. " +
                        "You can be registered as the black player in this game.");
            }


        }
    }

        public LinkedTreeMap helperFuncForCodeQuality(boolean validInput, int playGameInputs,
                                                      LinkedTreeMap correctGame, ArrayList<LinkedTreeMap> gamesInGameList){
            while(!validInput) {
                if (playGameInputs >= 1 && playGameInputs <= gamesInGameList.size()) {
                    validInput = true;
                    correctGame = gamesInGameList.get(playGameInputs - 1);
                } else {
                    System.out.println("Please input a valid integer");
                    int retry = new PreloginUI().getInputInt();
                    if(retry >= 1 && retry <= gamesInGameList.size()){
                        validInput = true;
                        correctGame = gamesInGameList.get(retry - 1);
                    }
                }
            }
            return correctGame;
        }
        public String handleHighlightAndDrawBoard(String resultOfChoice) {
            if (Objects.equals(resultOfChoice, "highlight board")) {
                System.out.println("Please input a position to highlight.");
                DisplayLogicPlayGame.MakeMoveType startPos = null;
                startPos = gameUI.playHelper(startPos, "startHighlight");
                gameUI.highlightMoves(startPos);
            } else if (Objects.equals(resultOfChoice, "draw board")) {
                gameUI.drawBoard();
            }
            return resultOfChoice;
        }

    public DisplayLogicPlayGame.MakeMoveType getInputIntStartForHighlighting() throws InputMismatchException, IndexOutOfBoundsException {
        Map<String, Integer> translatorCol = new HashMap<>();
        translatorCol.put("a", 1);
        translatorCol.put("b", 2);
        translatorCol.put("c", 3);
        translatorCol.put("d", 4);
        translatorCol.put("e", 5);
        translatorCol.put("f", 6);
        translatorCol.put("g", 7);
        translatorCol.put("h", 8);
        try{
            System.out.println("Please input a column and a row such as a 1 or d 6 (start pos)");
            String[] startingArray = new String[2];
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            startingArray = input.split(" ");
            int nums = 0;
            try{if (startingArray.length == 2){
                nums = Integer.parseInt(startingArray[1]);
            }else{
                System.out.println("Wrong num of args");
                return null;
            }} catch (NumberFormatException e) {
                System.out.println("please input an int");
                return null;
            }
            if (Objects.equals(startingArray[0], "quit") ||nums == 0){
                return new DisplayLogicPlayGame.MakeMoveType("quit", 0);
            }

            int translatedPosCol = translatorCol.get(startingArray[0]);
            ChessPosition positionGeneral = new ChessPosition(nums,translatedPosCol);
            while(game.getBoard().getPiece(positionGeneral) == null){
                System.out.println("Please make sure that you grabbed a piece on the board.");
                return null;
            }

            try{while(translatorCol.get(startingArray[0]) == null){
                System.out.println("Please input a coordinate with a piece.");
                return null;
            }


            } catch (Exception e) {
                return null;
            }
            if (startingArray.length == 2){
                return new DisplayLogicPlayGame.MakeMoveType(startingArray[0],nums);
            }
            //System.out.println("Please input the right number of vals");
        }catch (Exception e){
            System.out.println("Please input a valid integer");
            return null;
        }
        return null;
    }
    }
