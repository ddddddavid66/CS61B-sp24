package game2048logic;
import game2048rendering.Board;
import jh61b.grader.GradedTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import static com.google.common.truth.Truth.assertWithMessage;

/** Tests the atLeastOneMoveExists() method of Model.
 *
 * You shouldn't expect to pass these tests until you're passing all the tests
 * in TestEmptySpace.
 *
 * @author Omar Khan
 */
// 测试 Model 的 atLeastOneMoveExists() 方法。
// 在通过 TestEmptySpace 中的所有测试之前，不应预期通过这些测试。
@Timeout(value = 60, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
public class TestAtLeastOneMoveExists {

    /** Tests a board with some empty space.
     *
     *  Note that this isn't a comprehensive test for empty space. For that,
     * see the TestEmptySpace class.
     */
    // 测试一个存在部分空格的棋盘。
    // 注意，这不是对空格情况的完整测试；更完整的测试见 TestEmptySpace。
    @Test
    @Tag("at-least-one-move-exists")
    @DisplayName("Empty Space Exists")
    @GradedTest(number = "1.1")
    public void testEmptySpace() {
        int[][] rawVals = new int[][] {
                {0, 0, 4, 0},
                {0, 0, 0, 0},
                {0, 2, 0, 0},
                {0, 0, 0, 0},
        };

        Model m = new Model(rawVals, 0);
        assertWithMessage("A tilt in any direction will change the board "
                        + "(there is empty space on the board)\n" + m.getBoard()).that(m.atLeastOneMoveExists()).isTrue();
    }

    /** Tests a board where a tilt in any direction would cause a change. */
    // 测试一个向任意方向倾斜都会发生变化的棋盘。
    @Test
    @Tag("at-least-one-move-exists")
    @DisplayName("Valid Tilt Exists")
    @GradedTest(number = "1.2")
    public void testAnyDir() {
        int[][] rawVals = new int[][] {
                {2, 4, 2, 2},
                {4, 2, 4, 2},
                {2, 4, 2, 4},
                {4, 2, 4, 2},
        };

        Model m = new Model(rawVals, 0);
        assertWithMessage("A tilt in any direction will change the board\n"
                + m.getBoard()).that(m.atLeastOneMoveExists()).isTrue();
    }

    /** Tests a board where a tilt left or right would cause a change. */
    // 测试一个向左或向右倾斜会发生变化的棋盘。
    @Test
    @Tag("at-least-one-move-exists")
    @DisplayName("Valid Left/Right Tilt")
    @GradedTest(number = "1.3")
    public void testLeftOrRight() {
        int[][] rawVals = new int[][] {
                {2, 4, 2, 4},
                {4, 8, 4, 2},
                {2, 2, 2, 4},
                {4, 8, 4, 2},
        };

        Model m = new Model(rawVals, 0);
        assertWithMessage("A tilt left or right will change the board\n" + m.getBoard()).that(m.atLeastOneMoveExists()).isTrue();
    }

    /** Tests a board where a tilt up or down would cause a change. */
    // 测试一个向上或向下倾斜会发生变化的棋盘。
    @Test
    @Tag("at-least-one-move-exists")
    @DisplayName("Valid Up/Down Tilt")
    @GradedTest(number = "1.4")
    public void testUpOrDown() {
        int[][] rawVals = new int[][] {
                {2, 4, 2, 4},
                {4, 8, 4, 2},
                {2, 16, 4, 8},
                {4, 8, 4, 2},
        };

        Model m = new Model(rawVals, 0);
        assertWithMessage("A tilt up or down will change the board\n" + m.getBoard()).that(m.atLeastOneMoveExists()).isTrue();
    }

    /** Tests a board where some move exists (max tile is on the board).
     *
     * While having the max tile on the board does mean the game is over, it
     * should not be handled in this method.
     */
    // 测试一个仍然存在可移动操作的棋盘，即使棋盘上已经出现最大方块。
    // 虽然最大方块出现在棋盘上意味着游戏结束，但这不应由此方法负责判断。
    @Test
    @Tag("at-least-one-move-exists")
    @DisplayName("Valid Tilt with Max Tile")
    @GradedTest(number = "1.5")
    public void testMoveExistsMaxPiece() {
        int[][] rawVals = new int[][] {
                {2, 4, 2, 4},
                {4, 2, 4, 2},
                {2, 2, 2, 4},
                {4, 2, 4, 2048},
        };

        Model m = new Model(rawVals, 0);
        assertWithMessage("A tilt in any direction will change the board\n"
                + m.getBoard()).that(m.atLeastOneMoveExists()).isTrue();
    }

    /** Tests a board where no move exists. */
    // 测试一个不存在任何合法移动的棋盘。
    @Test
    @Tag("at-least-one-move-exists")
    @DisplayName("No Valid Move")
    @GradedTest(number = "1.6")
    public void testNoMoveExists1() {
        int[][] rawVals = new int[][] {
                {2, 4, 2, 4},
                {4, 2, 4, 2},
                {2, 4, 2, 4},
                {4, 2, 4, 2},
        };

        Model m = new Model(rawVals, 0);
        assertWithMessage("No move exists\n" + m.getBoard()).that(m.atLeastOneMoveExists()).isFalse();
    }

    /** Tests a board where no move exists. */
    // 测试一个不存在任何合法移动的棋盘。
    @Test
    @Tag("at-least-one-move-exists")
    @DisplayName("No Valid Move")
    @GradedTest(number = "1.7")
    public void testNoMoveExists2() {
        int[][] rawVals = new int[][] {
                {2, 1024, 2, 4},
                {4, 2, 4, 2},
                {2, 8, 16, 4},
                {512, 2, 4, 2},
        };

        Model m = new Model(rawVals, 0);
        assertWithMessage("No move exists\n" + m.getBoard()).that(m.atLeastOneMoveExists()).isFalse();
    }

    /** Tests a board where no move exists. */
    // 测试一个不存在任何合法移动的棋盘。
    @Test
    @Tag("at-least-one-move-exists")
    @DisplayName("No Valid Move")
    @GradedTest(number = "1.8")
    public void testNoMoveExists3() {
        int[][] rawVals = new int[][] {
                {8, 4, 2, 32},
                {32, 2, 4, 2},
                {2, 8, 2, 4},
                {4, 64, 4, 64},
        };

        Model m = new Model(rawVals, 0);
        assertWithMessage("No move exists\n" + m.getBoard()).that(m.atLeastOneMoveExists()).isFalse();
    }

    /** Tests a board where no move exists. */
    // 测试一个不存在任何合法移动的棋盘。
    @Test
    @Tag("at-least-one-move-exists")
    @DisplayName("No Valid Move")
    @GradedTest(number = "1.9")
    public void testNoMoveExists4() {
        int[][] rawVals = new int[][] {
                {2, 4, 2, 32},
                {32, 2, 4, 2},
                {2, 128, 2, 4},
                {4, 2, 4, 2},
        };

        Model m = new Model(rawVals, 0);
        assertWithMessage("No move exists\n" + m.getBoard()).that(m.atLeastOneMoveExists()).isFalse();
    }

    /** Tests a board where no move exists. */
    // 测试一个不存在任何合法移动的棋盘。
    @Test
    @Tag("at-least-one-move-exists")
    @DisplayName("No Valid Move")
    @GradedTest(number = "1.10")
    public void testNoMoveExists5() {
        int[][] rawVals = new int[][] {
                {8, 16, 2, 32},
                {32, 2, 64, 2},
                {2, 256, 128, 256},
                {1024, 8, 4, 2},
        };

        Model m = new Model(rawVals, 0);
        assertWithMessage("No move exists\n" + m.getBoard()).that(m.atLeastOneMoveExists()).isFalse();

    }
}
