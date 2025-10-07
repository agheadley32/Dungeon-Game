package tileengine;

import java.awt.Color;

/**
 * Contains constant tile objects, to avoid having to remake the same tiles in different parts of
 * the code.
 *
 * You are free to (and encouraged to) create and add your own tiles to this file. This file will
 * be turned in with the rest of your code.
 *
 * Ex:
 *      world[x][y] = Tileset.FLOOR;
 *
 * The style checker may crash when you try to style check this file due to use of unicode
 * characters. This is OK.
 */

public class Tileset {
    public static final TETile AVATAR = new TETile('@', Color.white, Color.black, "you", 0);
    public static final TETile WALL = new TETile(' ', Color.black, Color.black, "wall",
            "/wall.png", 1);
    public static final TETile FLOOR = new TETile('·', new Color(128, 192, 128), Color.black,
            "floor","/floor.png" , 2);
    public static final TETile NOTHING = new TETile(' ', new Color(62, 43, 43),
            new Color(62, 43, 43), "nothing", "/background.png", 3);
    public static final TETile GRASS = new TETile('"', Color.green, Color.black, "grass", 4);
    public static final TETile WATER = new TETile('≈', Color.blue, Color.black, "water", 5);
    public static final TETile FLOWER = new TETile('❀', Color.magenta, Color.pink, "flower", 6);
    public static final TETile LOCKED_DOOR = new TETile('█', Color.orange, Color.black,
            "locked door", 7);
    public static final TETile UNLOCKED_DOOR = new TETile(' ', Color.black, Color.black,
            "unlocked door", "/doorOpened.png", 8);
    public static final TETile SAND = new TETile('▒', Color.yellow, Color.black, "sand", 9);
    public static final TETile MOUNTAIN = new TETile('▲', Color.gray, Color.black,
            "mountain", 10);
    public static final TETile TREE = new TETile('♠', Color.green, Color.black, "tree", 11);

    public static final TETile CELL = new TETile('█', Color.white, Color.black, "cell", 12);

    public static final TETile WARRIOR = new TETile(' ', Color.BLACK, Color.BLACK, "Warrior",
            "/Warrior.png", 13);
    public static final TETile MAGE = new TETile(' ', Color.BLACK, Color.BLACK, "Mage",
            "/Mage.png", 14);
    public static final TETile ARCHER = new TETile(' ', Color.BLACK, Color.BLACK, "Archer",
            "/archer.png", 15);

    public static final TETile KEY = new TETile(' ', Color.BLACK, Color.BLACK, "Key",
            "/key.png", 16);
    public static final TETile SHARD = new TETile(' ', Color.BLACK, Color.BLACK, "Shard",
            "/bluegem.png", 17);

    public static final TETile enemy = new TETile(' ', Color.BLACK, Color.BLACK, "enemy",
            "/enemy.png", 18);



}


