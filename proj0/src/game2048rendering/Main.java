package game2048rendering;

import game2048logic.Model;

/** The main class for the 2048 game.
 *  @author P. N. Hilfinger
 */
// 2048 游戏的主类。
public class Main {
    /** Probability of choosing 2 as random tile (as opposed to 4). */
    // 随机生成方块时，生成 2 而不是 4 的概率。
    static final double TILE2_PROBABILITY = 0.9;

    /** Number of squares on the side of a board. */
    // 棋盘单边的格子数。
    static final int BOARD_SIZE = 4;

    /** Random seed. Ignored if 0. */
    // 随机种子；如果为 0 则忽略。
    static final long RANDOM_SEED = 0;

    /** If true, the custom start is used. Otherwise, the board starts off blank. */
    // 如果为 true，则使用自定义开局；否则棋盘从空白状态开始。
    static final boolean USE_CUSTOM_START = false;

    /** Custom starting state of the game. Useful for debugging. */
    // 自定义的游戏初始状态，便于调试。
    static final Model CUSTOM_START = new Model(new int[][]{
            {2, 0, 2, 128},
            {0, 0, 8, 0},
            {8, 64, 0, 128},
            {4, 64, 8, 256},
    }, 0);

    public static void main(String[] args) {
        Model model = USE_CUSTOM_START ? CUSTOM_START : new Model(BOARD_SIZE);

        GUI gui = new GUI("2048 61B", model);
        gui.display(true);

        Game game = new Game(model, gui, TILE2_PROBABILITY, RANDOM_SEED);
        try {
            game.playGame(USE_CUSTOM_START);
            while (game.playing()) {
                game.playGame(false);
            }
        } catch (IllegalStateException excp) {
            System.err.printf("Internal error: %s%n", excp.getMessage());
            System.exit(1);
        }

        System.exit(0);
    }

}
