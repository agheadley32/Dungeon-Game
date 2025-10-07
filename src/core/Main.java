package core;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    private static final int WIDTH = 80;
    private static final int HEIGHT = 40;
    private static final String SAVE_FILE = "save.txt";
    private static final Random RANDOM = new Random();
    private static final List<int[]> SHARDS = new ArrayList<>();
    private static final int NUM_ENEMIES = 1;
    private static World world;
    private static TETile[][] tilesList;
    private static int avatarXCoordinate;
    private static int avatarYCoordinate;
    private static int goalXCoordinate;
    private static int goalYCoordinate;
    private static TETile chosenAvatar = Tileset.WARRIOR;
    private static boolean keySpawned = false;
    private static int keyXCoordinate;
    private static int keyYCoordinate;
    private static List<int[]> enemies = new ArrayList<>();
    private static int doorX, doorY;


    public static void main(String[] args) {
        displayMainMenu();
    }

    private static void displayMainMenu() {
        StdDraw.setCanvasSize(800, 400);
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(new java.awt.Font("Monospaced", java.awt.Font.BOLD, 30));
        StdDraw.text(0.5, 0.8, "CS61B: BYOW");
        StdDraw.text(0.5, 0.6, "(N) New Game");
        StdDraw.text(0.5, 0.45, "(L) Load Game");
        StdDraw.text(0.5, 0.3, "(Q) Quit Game");
        StdDraw.show();

        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char option = StdDraw.nextKeyTyped();
                if (option == 'N' || option == 'n') {
                    startNewGame();
                    break;
                } else if (option == 'L' || option == 'l') {
                    System.out.println("Loading saved game...");
                    loadGame();
                    break;
                } else if (option == 'Q' || option == 'q') {
                    System.exit(0);
                }
            }
        }
    }

    private static void startNewGame() {
        StdDraw.clear(Color.BLACK);
        StdDraw.text(0.5, 0.6, "Enter Seed (End with 'S' or 's'):");
        StdDraw.show();

        StringBuilder seedBuilder = new StringBuilder();

        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                if (c == 'S' || c == 's') {
                    break;
                } else if (Character.isDigit(c)) {
                    seedBuilder.append(c);
                    StdDraw.clear(Color.BLACK);
                    StdDraw.text(0.5, 0.6, "Enter Seed (End with 'S' or 's'):");
                    StdDraw.text(0.5, 0.5, seedBuilder.toString());
                    StdDraw.show();
                }
            }
        }
        long seed = Long.parseLong(seedBuilder.toString());
        world = new World(WIDTH, HEIGHT, seed);
        tilesList = world.generateWorld();


        while (true) {
            int x = RANDOM.nextInt(WIDTH);
            int y = RANDOM.nextInt(HEIGHT);
            if (tilesList[x][y] == Tileset.FLOOR) {
                avatarXCoordinate = x;
                avatarYCoordinate = y;
                break;
            }
        }
        showIntroPages();
        showAvatarSelectionPage();
        placeShards();
        renderWorld();
        gameLoop();

    }

    private static void waitForKeyPress() {
        while (!StdDraw.hasNextKeyTyped()) {
        }
        StdDraw.nextKeyTyped();
    }


    private static void showIntroPages() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(new java.awt.Font("Monospaced", java.awt.Font.BOLD, 45));
        StdDraw.text(0.5, 0.8, "Welcome to Dungeon Escape!");
        StdDraw.setFont(new java.awt.Font("Monospaced", java.awt.Font.BOLD, 20));
        StdDraw.text(0.5, 0.6, "You are trapped in a perilous dungeon.");
        StdDraw.text(0.5, 0.5, "Choose your hero and embark");
        StdDraw.text(0.5, 0.4, "on a quest to find the shards, assemble the key,");
        StdDraw.text(0.5, 0.3, "and unlock the door to freedom.");
        StdDraw.text(0.5, 0.1, "Press any key to continue");
        StdDraw.show();
        waitForKeyPress();


        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(new java.awt.Font("Monospaced", java.awt.Font.BOLD, 20));
        StdDraw.text(0.5, 0.6, "But beware! Enemies will block your path,");
        StdDraw.text(0.5, 0.5, "and the final test is a riddle. Answer it correctly");
        StdDraw.text(0.5, 0.4, "to escape, or be trapped forever.");
        StdDraw.text(0.5, 0.1, "Press any key to continue");
        StdDraw.show();
        waitForKeyPress();


        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(new java.awt.Font("Monospaced", java.awt.Font.BOLD, 20));
        StdDraw.text(0.5, 0.6, "Are you ready to face the challenge?");
        StdDraw.text(0.4, 0.4, "(Y) Yes");
        StdDraw.text(0.6, 0.4, "(N) No");
        StdDraw.show();
    }

    private static void middlePage() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.RED);
        StdDraw.setFont(new java.awt.Font("Courier", java.awt.Font.BOLD, 80));
        StdDraw.text(40, 20, "RUNNNNNNNNN!!!");
        StdDraw.show();
        waitForKeyPress();

    }

    private static void placeShards() {
        int placedShards = 0;
        while (placedShards < 3) {
            int x = RANDOM.nextInt(WIDTH);
            int y = RANDOM.nextInt(HEIGHT);
            if (tilesList[x][y] == Tileset.FLOOR) {
                tilesList[x][y] = Tileset.SHARD;
                SHARDS.add(new int[]{x, y});
                placedShards++;
            }
        }
    }

    private static void placeKey() {
        while (true) {
            int x = RANDOM.nextInt(WIDTH);
            int y = RANDOM.nextInt(HEIGHT);
            if (tilesList[x][y] == Tileset.FLOOR) {
                keyXCoordinate = x;
                keyYCoordinate = y;
                tilesList[x][y] = Tileset.KEY;
                keySpawned = true;
                break;
            }
        }
    }

    private static void showAvatarSelectionPage() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(new java.awt.Font("Monospaced", java.awt.Font.BOLD, 20));
        StdDraw.text(0.5, 0.8, "Choose Your Avatar:");
        StdDraw.setPenColor(Color.RED);
        StdDraw.text(0.3, 0.5, "(1) Warrior");
        StdDraw.setPenColor(Color.MAGENTA);
        StdDraw.text(0.5, 0.5, "(2) Mage");
        StdDraw.setPenColor(Color.GREEN);
        StdDraw.text(0.7, 0.5, "(3) Archer");
        StdDraw.show();

        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char choice = StdDraw.nextKeyTyped();
                if (choice == '1') {
                    chosenAvatar = Tileset.WARRIOR;
                    break;
                } else if (choice == '2') {
                    chosenAvatar = Tileset.MAGE;
                    break;
                } else if (choice == '3') {
                    chosenAvatar = Tileset.ARCHER;
                    break;
                }
            }
        }
    }


    private static void loadGame() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(SAVE_FILE))) {
            long seed = in.readLong();
            avatarXCoordinate = in.readInt();
            avatarYCoordinate = in.readInt();
            goalXCoordinate = in.readInt();
            goalYCoordinate = in.readInt();
            SHARDS.clear();
            SHARDS.addAll((List<int[]>) in.readObject());
            keySpawned = in.readBoolean();
            if (keySpawned) {
                keyXCoordinate = in.readInt();
                keyYCoordinate = in.readInt();
            }
            String avatarDescription = (String) in.readObject();
            if (avatarDescription.equals("Warrior")) {
                chosenAvatar = Tileset.WARRIOR;
            } else if (avatarDescription.equals("Mage")) {
                chosenAvatar = Tileset.MAGE;
            } else if (avatarDescription.equals("Archer")) {
                chosenAvatar = Tileset.ARCHER;
            }
            enemies.clear();
            enemies.addAll((List<int[]>) in.readObject());

            world = new World(WIDTH, HEIGHT, seed);
            tilesList = world.generateWorld();

            tilesList[avatarXCoordinate][avatarYCoordinate] = chosenAvatar;
            for (int[] shard : SHARDS) {
                tilesList[shard[0]][shard[1]] = Tileset.SHARD;
            }
            if (keySpawned) {
                tilesList[keyXCoordinate][keyYCoordinate] = Tileset.KEY;
            }
            for (int[] enemy : enemies) {
                tilesList[enemy[0]][enemy[1]] = Tileset.enemy;
            }

            System.out.println("Game loaded successfully.");
            renderWorld();
            gameLoop();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Failed to load the game: " + e.getMessage());
            displayMainMenu();
        }
    }


    private static void openDoor() {
        while (true) {
            int x = RANDOM.nextInt(WIDTH);
            int y = RANDOM.nextInt(HEIGHT);

            if (tilesList[x][y] == Tileset.WALL
                    && isValidWallForDoor(x, y)) {
                doorX = x;
                doorY = y;
                tilesList[doorX][doorY] = Tileset.UNLOCKED_DOOR;
                System.out.println("Door spawned at: (" + doorX + ", " + doorY + ")");
                break;
            }
        }
    }

    private static boolean isValidWallForDoor(int x, int y) {
        int floorCount = 0;

        if (x > 0 && tilesList[x - 1][y] == Tileset.FLOOR) {
            floorCount++;
        }
        if (x < WIDTH - 1 && tilesList[x + 1][y] == Tileset.FLOOR) {
            floorCount++;
        }
        if (y > 0 && tilesList[x][y - 1] == Tileset.FLOOR) {
            floorCount++;
        }
        if (y < HEIGHT - 1 && tilesList[x][y + 1] == Tileset.FLOOR) {
            floorCount++;
        }

        return floorCount == 1;
    }


    private static void gameLoop() {
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char move = StdDraw.nextKeyTyped();
                if (move == ':') {
                    while (!StdDraw.hasNextKeyTyped()) {
                    }
                    char q = StdDraw.nextKeyTyped();
                    if (q == 'Q' || q == 'q') {
                        saveGame();
                        System.exit(0);
                    }
                } else {
                    moveAvatar(move);
                    checkShardCollection();
                    if (SHARDS.isEmpty() && !keySpawned) {
                        placeKey();
                    }
                    renderWorld();
                }
            }
        }
    }

    private static void checkShardCollection() {
        for (int i = 0; i < SHARDS.size(); i++) {
            int[] shard = SHARDS.get(i);
            if (avatarXCoordinate == shard[0] && avatarYCoordinate == shard[1]) {
                tilesList[shard[0]][shard[1]] = Tileset.FLOOR;
                SHARDS.remove(i);
                i--;
            }
        }
    }


    private static void moveAvatar(char move) {
        int newX = avatarXCoordinate;
        int newY = avatarYCoordinate;

        if (move == 'W' || move == 'w') {
            newY++;
        } else if (move == 'A' || move == 'a') {
            newX--;
        } else if (move == 'S' || move == 's') {
            newY--;
        } else if (move == 'D' || move == 'd') {
            newX++;
        }


        if (newX >= 0 && newX < WIDTH && newY >= 0 && newY < HEIGHT) {
            if (tilesList[newX][newY] == Tileset.WALL) {
                System.out.println("Blocked by wall!");
                return;
            }

            if (tilesList[newX][newY] == Tileset.KEY) {
                System.out.println("Key collected!");
                tilesList[newX][newY] = Tileset.FLOOR;
                middlePage();
                spawnEnemies();
                openDoor();
            }

            if (tilesList[newX][newY] == Tileset.UNLOCKED_DOOR) {
                displayWinMessage();
                displayIntermediateRiddle();
                displayRiddle();
                return;
            }

            tilesList[avatarXCoordinate][avatarYCoordinate] = Tileset.FLOOR;
            avatarXCoordinate = newX;
            avatarYCoordinate = newY;
            tilesList[avatarXCoordinate][avatarYCoordinate] = Tileset.AVATAR;

            moveEnemies();
            moveEnemies();
            checkForGameOver();
        }
    }


    private static void displayIntermediateRiddle() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(new java.awt.Font("Monospaced", java.awt.Font.BOLD, 50));
        StdDraw.text(40, 25, "Now it's time for the riddle.");
        StdDraw.setFont(new java.awt.Font("Monospaced", java.awt.Font.BOLD, 20));
        StdDraw.text(40, 10, "Press any key to continue");
        StdDraw.show();
        waitForKeyPress();
    }

    private static void displayRiddle() {
        int randomRiddle = RANDOM.nextInt(5);
        if (randomRiddle == 0) {
            while (true) {
                StdDraw.clear(Color.BLACK);
                StdDraw.setPenColor(Color.WHITE);
                StdDraw.setFont(new java.awt.Font("Monospaced", java.awt.Font.BOLD, 22));
                StdDraw.text(40, 35, "In the world of Big O, I'm known to grow fast");
                StdDraw.text(40, 33, "at first, but soon I slow. I handle algorithms with ease,");
                StdDraw.setFont(new java.awt.Font("Monospaced", java.awt.Font.BOLD, 30));
                StdDraw.text(40, 28, "What is my name?");
                StdDraw.setFont(new java.awt.Font("Monospaced", java.awt.Font.BOLD, 24));
                StdDraw.text(40, 18, "(1) O(n)");
                StdDraw.text(40, 15, "(2) O(log n)");
                StdDraw.text(40, 12, "(3) O(1)");
                StdDraw.text(40, 9, "(4) O(n!)");
                StdDraw.show();

                if (StdDraw.hasNextKeyTyped()) {
                    char choice = StdDraw.nextKeyTyped();

                    if (choice == '1' || choice == '3' || choice == '4') {
                        StdDraw.clear(Color.BLACK);
                        StdDraw.setPenColor(Color.WHITE);
                        StdDraw.setFont(new java.awt.Font("Monospaced", java.awt.Font.BOLD, 50));
                        StdDraw.text(40, 20, "Wrong. Enter the Maze and Try Again");
                        StdDraw.setFont(new java.awt.Font("Monospaced", java.awt.Font.BOLD, 20));
                        StdDraw.text(40, 5, "Press any key to return to Main Menu!");
                        StdDraw.show();
                        keySpawned = false;
                        enemies.clear();
                        SHARDS.clear();
                        waitForKeyPress();
                        displayMainMenu();
                        break;
                    } else if (choice == '2') {
                        StdDraw.clear(Color.BLACK);
                        StdDraw.setPenColor(Color.WHITE);
                        StdDraw.setFont(new java.awt.Font("Monospaced", java.awt.Font.BOLD, 50));
                        StdDraw.text(40, 20, "Congratulations!! You Won!!");
                        StdDraw.setFont(new java.awt.Font("Monospaced", java.awt.Font.BOLD, 20));
                        StdDraw.text(40, 5, "Press any key to return to Main Menu!");
                        StdDraw.show();
                        keySpawned = false;
                        enemies.clear();
                        SHARDS.clear();
                        waitForKeyPress();
                        displayMainMenu();
                        break;
                    }
                }
            }
        }
        if (randomRiddle == 1) {
            while (true) {
                StdDraw.clear(Color.BLACK);
                StdDraw.setPenColor(Color.WHITE);
                StdDraw.setFont(new java.awt.Font("Monospaced", java.awt.Font.BOLD, 22));
                StdDraw.text(40, 35, "I consist of nodes and links that bind,");
                StdDraw.text(40, 33, "Each node points to the next one in line.");
                StdDraw.text(40, 31, "I don’t need continuous memory space,");
                StdDraw.setFont(new java.awt.Font("Monospaced", java.awt.Font.BOLD, 30));
                StdDraw.text(40, 28, "What am I?");
                StdDraw.setFont(new java.awt.Font("Monospaced", java.awt.Font.BOLD, 24));
                StdDraw.text(40, 18, "(1) Array");
                StdDraw.text(40, 15, "(2) HashMap");
                StdDraw.text(40, 12, "(3) Linked List");
                StdDraw.text(40, 9, "(4) Binary Search Tree");
                StdDraw.show();

                if (StdDraw.hasNextKeyTyped()) {
                    char choice = StdDraw.nextKeyTyped();

                    if (choice == '1' || choice == '2' || choice == '4') {
                        StdDraw.clear(Color.BLACK);
                        StdDraw.setPenColor(Color.WHITE);
                        StdDraw.setFont(new java.awt.Font("Monospaced", java.awt.Font.BOLD, 50));
                        StdDraw.text(40, 20, "Wrong. Enter the Maze and Try Again");
                        StdDraw.setFont(new java.awt.Font("Monospaced", java.awt.Font.BOLD, 20));
                        StdDraw.text(40, 5, "Press any key to return to Main Menu!");
                        StdDraw.show();
                        keySpawned = false;
                        enemies.clear();
                        SHARDS.clear();
                        waitForKeyPress();
                        displayMainMenu();
                        break;
                    } else if (choice == '3') {
                        StdDraw.clear(Color.BLACK);
                        StdDraw.setPenColor(Color.WHITE);
                        StdDraw.setFont(new java.awt.Font("Monospaced", java.awt.Font.BOLD, 50));
                        StdDraw.text(40, 20, "Congratulations!! You Won!!");
                        StdDraw.setFont(new java.awt.Font("Monospaced", java.awt.Font.BOLD, 20));
                        StdDraw.text(40, 5, "Press any key to return to Main Menu!");
                        StdDraw.show();
                        keySpawned = false;
                        enemies.clear();
                        SHARDS.clear();
                        waitForKeyPress();
                        displayMainMenu();
                        break;
                    }
                }
            }
        }
        if (randomRiddle == 2) {
            while (true) {
                StdDraw.clear(Color.BLACK);
                StdDraw.setPenColor(Color.WHITE);
                StdDraw.setFont(new java.awt.Font("Monospaced", java.awt.Font.BOLD, 22));
                StdDraw.text(40, 35, "I sort by dividing again and again,");
                StdDraw.text(40, 33, "Conquer small parts and bring order to them.");
                StdDraw.text(40, 31, "I’m fast and efficient, but I can be beat,");
                StdDraw.text(40, 29, "When the pivot’s position is not so sweet.");
                StdDraw.setFont(new java.awt.Font("Monospaced", java.awt.Font.BOLD, 30));
                StdDraw.text(40, 27, "What sorting algorithm am I?");
                StdDraw.setFont(new java.awt.Font("Monospaced", java.awt.Font.BOLD, 24));
                StdDraw.text(40, 18, "(1) Merge");
                StdDraw.text(40, 15, "(2) QuickSort");
                StdDraw.text(40, 12, "(3) BubbleSort");
                StdDraw.text(40, 9, "(4) HeapSort");
                StdDraw.show();

                if (StdDraw.hasNextKeyTyped()) {
                    char choice = StdDraw.nextKeyTyped();

                    if (choice == '1' || choice == '3' || choice == '4') {
                        StdDraw.clear(Color.BLACK);
                        StdDraw.setPenColor(Color.WHITE);
                        StdDraw.setFont(new java.awt.Font("Monospaced", java.awt.Font.BOLD, 50));
                        StdDraw.text(40, 20, "Wrong. Enter the Maze and Try Again");
                        StdDraw.setFont(new java.awt.Font("Monospaced", java.awt.Font.BOLD, 20));
                        StdDraw.text(40, 5, "Press any key to return to Main Menu!");
                        StdDraw.show();
                        keySpawned = false;
                        enemies.clear();
                        SHARDS.clear();
                        waitForKeyPress();
                        displayMainMenu();
                        break;
                    } else if (choice == '2') {
                        StdDraw.clear(Color.BLACK);
                        StdDraw.setPenColor(Color.WHITE);
                        StdDraw.setFont(new java.awt.Font("Monospaced", java.awt.Font.BOLD, 50));
                        StdDraw.text(40, 20, "Congratulations!! You Won!!");
                        StdDraw.setFont(new java.awt.Font("Monospaced", java.awt.Font.BOLD, 20));
                        StdDraw.text(40, 5, "Press any key to return to Main Menu!");
                        StdDraw.show();
                        keySpawned = false;
                        enemies.clear();
                        SHARDS.clear();
                        waitForKeyPress();
                        displayMainMenu();
                        break;
                    }
                }
            }
        }
        if (randomRiddle == 3) {
            while (true) {
                StdDraw.clear(Color.BLACK);
                StdDraw.setPenColor(Color.WHITE);
                StdDraw.setFont(new java.awt.Font("Monospaced", java.awt.Font.BOLD, 22));
                StdDraw.text(40, 35, "I traverse trees both left and right,");
                StdDraw.text(40, 33, "My visits are ordered, neat, and tight.");
                StdDraw.text(40, 31, "Root, left, then right—that’s my style,");
                StdDraw.setFont(new java.awt.Font("Monospaced", java.awt.Font.BOLD, 30));
                StdDraw.text(40, 28, "What traversal am I, by a mile?");
                StdDraw.setFont(new java.awt.Font("Monospaced", java.awt.Font.BOLD, 24));
                StdDraw.text(40, 18, "(1) Pre-order Traversal");
                StdDraw.text(40, 15, "(2) In-order Traversal");
                StdDraw.text(40, 12, "(3) Post-order Traversal");
                StdDraw.text(40, 9, "(4) Level-order Traversal");
                StdDraw.show();

                if (StdDraw.hasNextKeyTyped()) {
                    char choice = StdDraw.nextKeyTyped();

                    if (choice == '2' || choice == '3' || choice == '4') {
                        StdDraw.clear(Color.BLACK);
                        StdDraw.setPenColor(Color.WHITE);
                        StdDraw.setFont(new java.awt.Font("Monospaced", java.awt.Font.BOLD, 50));
                        StdDraw.text(40, 20, "Wrong. Enter the Maze and Try Again");
                        StdDraw.setFont(new java.awt.Font("Monospaced", java.awt.Font.BOLD, 20));
                        StdDraw.text(40, 5, "Press any key to return to Main Menu!");
                        StdDraw.show();
                        keySpawned = false;
                        enemies.clear();
                        SHARDS.clear();
                        waitForKeyPress();
                        displayMainMenu();
                        break;
                    } else if (choice == '1') {
                        StdDraw.clear(Color.BLACK);
                        StdDraw.setPenColor(Color.WHITE);
                        StdDraw.setFont(new java.awt.Font("Monospaced", java.awt.Font.BOLD, 50));
                        StdDraw.text(40, 20, "Congratulations!! You Won!!");
                        StdDraw.setFont(new java.awt.Font("Monospaced", java.awt.Font.BOLD, 20));
                        StdDraw.text(40, 5, "Press any key to return to Main Menu!");
                        StdDraw.show();
                        keySpawned = false;
                        enemies.clear();
                        SHARDS.clear();
                        waitForKeyPress();
                        displayMainMenu();
                        break;
                    }
                }
            }
        }
        if (randomRiddle == 4) {
            while (true) {
                StdDraw.clear(Color.BLACK);
                StdDraw.setPenColor(Color.WHITE);
                StdDraw.setFont(new java.awt.Font("Monospaced", java.awt.Font.BOLD, 22));
                StdDraw.text(40, 35, "I’m a collection of elements in a row,");
                StdDraw.text(40, 33, "Indexed from zero, that's how I grow.");
                StdDraw.text(40, 31, "Fixed size and fast to access,");
                StdDraw.setFont(new java.awt.Font("Monospaced", java.awt.Font.BOLD, 30));
                StdDraw.text(40, 28, "What am I?");
                StdDraw.setFont(new java.awt.Font("Monospaced", java.awt.Font.BOLD, 24));
                StdDraw.text(40, 18, "(1) HashMap");
                StdDraw.text(40, 15, "(2) Array");
                StdDraw.text(40, 12, "(3) Stack");
                StdDraw.text(40, 9, "(4) Queue");
                StdDraw.show();

                if (StdDraw.hasNextKeyTyped()) {
                    char choice = StdDraw.nextKeyTyped();

                    if (choice == '1' || choice == '3' || choice == '4') {
                        StdDraw.clear(Color.BLACK);
                        StdDraw.setPenColor(Color.WHITE);
                        StdDraw.setFont(new java.awt.Font("Monospaced", java.awt.Font.BOLD, 50));
                        StdDraw.text(40, 20, "Wrong. Enter the Maze and Try Again");
                        StdDraw.setFont(new java.awt.Font("Monospaced", java.awt.Font.BOLD, 20));
                        StdDraw.text(40, 5, "Press any key to return to Main Menu!");
                        StdDraw.show();
                        keySpawned = false;
                        enemies.clear();
                        SHARDS.clear();
                        waitForKeyPress();
                        displayMainMenu();
                        break;
                    } else if (choice == '2') {
                        StdDraw.clear(Color.BLACK);
                        StdDraw.setPenColor(Color.WHITE);
                        StdDraw.setFont(new java.awt.Font("Monospaced", java.awt.Font.BOLD, 50));
                        StdDraw.text(40, 20, "Congratulations!! You Won!!");
                        StdDraw.setFont(new java.awt.Font("Monospaced", java.awt.Font.BOLD, 20));
                        StdDraw.text(40, 5, "Press any key to return to Main Menu!");
                        StdDraw.show();
                        keySpawned = false;
                        enemies.clear();
                        SHARDS.clear();
                        waitForKeyPress();
                        displayMainMenu();
                        break;
                    }
                }
            }
        }
    }

    private static void moveEnemies() {
        for (int[] enemy : enemies) {
            int x = enemy[0];
            int y = enemy[1];
            tilesList[x][y] = Tileset.FLOOR;
            int newX = x;
            int newY = y;

            int direction = RANDOM.nextInt(4);
            if (direction == 0 && avatarYCoordinate > y) {
                newY++;
            } else if (direction == 1 && avatarXCoordinate < x) {
                newX--;
            } else if (direction == 2 && avatarYCoordinate < y) {
                newY--;
            } else if (direction == 3 && avatarXCoordinate > x) {
                newX++;
            }

            if (newX >= 0 && newX < WIDTH && newY >= 0 && newY < HEIGHT &&
                    tilesList[newX][newY] != Tileset.WALL && tilesList[newX][newY] != Tileset.enemy) {
                enemy[0] = newX;
                enemy[1] = newY;
            }

            tilesList[enemy[0]][enemy[1]] = Tileset.enemy;

            if (enemy[0] == avatarXCoordinate && enemy[1] == avatarYCoordinate) {
                System.out.println("Game Lost! An enemy caught you.");
                keySpawned = false;
                enemies.clear();
                SHARDS.clear();
                displayGameOver();
            }
        }
    }


    private static void checkForGameOver() {
        for (int[] enemy : enemies) {
            int distance = Math.abs(enemy[0] - avatarXCoordinate) + Math.abs(enemy[1] - avatarYCoordinate);

            if (distance <= 1) {
                System.out.println("Game Lost! An enemy caught you.");
                displayGameOver();
            }
        }
    }

    private static void displayGameOver() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.RED);
        StdDraw.setFont(new java.awt.Font("Courier", java.awt.Font.BOLD, 60));
        StdDraw.text(40, 20, "You Lost!");
        StdDraw.show();

        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                displayMainMenu();
            }
        }
    }


    private static void spawnEnemies() {
        enemies.clear();
        for (int i = 0; i < NUM_ENEMIES; i++) {
            while (true) {
                int x = RANDOM.nextInt(WIDTH);
                int y = RANDOM.nextInt(HEIGHT);
                if (tilesList[x][y] == Tileset.FLOOR) {
                    enemies.add(new int[]{x, y});
                    tilesList[x][y] = Tileset.enemy;
                    break;
                }
            }
        }
    }

    private static void renderWorld() {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        tilesList[avatarXCoordinate][avatarYCoordinate] = chosenAvatar;
        ter.renderFrame(tilesList);
    }

    private static void displayWinMessage() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(new java.awt.Font("Courier", java.awt.Font.BOLD, 40));

        StdDraw.text(40, 30, "Congratulations! You escaped the maze!");
        StdDraw.text(40, 20, "But you're not quite done yet.");
        StdDraw.text(40, 15, " Time for the riddle.");

        StdDraw.setFont(new java.awt.Font("Monospaced", java.awt.Font.BOLD, 20));

        StdDraw.text(40, 5, "Press any key to see your riddle");
        StdDraw.show();

        waitForKeyPress();

    }


    private static void saveGame() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
            out.writeLong(world.getSeed());
            out.writeInt(avatarXCoordinate);
            out.writeInt(avatarYCoordinate);
            out.writeInt(goalXCoordinate);
            out.writeInt(goalYCoordinate);
            out.writeObject(SHARDS);
            out.writeBoolean(keySpawned);
            if (keySpawned) {
                out.writeInt(keyXCoordinate);
                out.writeInt(keyYCoordinate);
            }
            out.writeObject(chosenAvatar.description());
            out.writeObject(enemies);
            System.out.println("Game saved successfully.");
        } catch (IOException e) {
            System.out.println("Failed to save the game: " + e.getMessage());
        }
    }


}