package game2048logic;
import game2048rendering.Board;
import jh61b.grader.GradedTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import static com.google.common.truth.Truth.assertWithMessage;


/** Tests the emptySpaceExists() method of Model.
 *
 * @author Omar Khan
 */
// 测试 Model 的 emptySpaceExists() 方法。
@Timeout(value = 60, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
public class TestEmptySpace {

    /** Note that this isn't a possible board state. */
    // 注意，这并不是一个实际游戏中可能出现的棋盘状态。
    @Test
    @Tag("empty-space")
    @DisplayName("Fully empty board")
    @GradedTest(number = "2.1")
    public void testCompletelyEmpty() {
        int[][] rawVals = new int[][] {
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
        };
        Model m = new Model(rawVals, 0);
        assertWithMessage("Board is full of empty space\n" + m.getBoard()).that(m.emptySpaceExists()).isTrue();
    }

    /** Tests a board that is completely full except for the top row. */
    // 测试一个除了最上面一行之外都被填满的棋盘。
    @Test
    @Tag("empty-space")
    @DisplayName("Empty top row")
    @GradedTest(number = "2.2")
    public void testEmptyTopRow() {
        int[][] rawVals = new int[][] {
                {0, 0, 0, 0},
                {2, 4, 2, 4},
                {4, 2, 4, 2},
                {2, 4, 2, 4},
        };
        Model m = new Model(rawVals, 0);
        assertWithMessage("Top row is empty\n" + m.getBoard()).that(m.emptySpaceExists()).isTrue();

    }

    /** Tests a board that is completely full except for the bottom row. */
    // 测试一个除了最下面一行之外都被填满的棋盘。
    @Test
    @Tag("empty-space")
    @DisplayName("Empty bottom row")
    @GradedTest(number = "2.3")
    public void testEmptyBottomRow() {
        int[][] rawVals = new int[][] {
                {2, 4, 2, 4},
                {4, 2, 4, 2},
                {2, 4, 2, 4},
                {0, 0, 0, 0},
        };
        Model m = new Model(rawVals, 0);
        assertWithMessage("Bottom row is empty\n" + m.getBoard()).that(m.emptySpaceExists()).isTrue();

    }


    /** Tests a board that is completely full except for the left column. */
    // 测试一个除了最左边一列之外都被填满的棋盘。
    @Test
    @Tag("empty-space")
    @DisplayName("Empty left column")
    @GradedTest(number = "2.4")
    public void testEmptyLeftCol() {
        int[][] rawVals = new int[][] {
                {0, 4, 2, 4},
                {0, 2, 4, 2},
                {0, 4, 2, 4},
                {0, 2, 4, 2},
        };
        Model m = new Model(rawVals, 0);
        assertWithMessage("Left col is empty\n" + m.getBoard()).that(m.emptySpaceExists()).isTrue();
    }

    /** Tests a board that is completely full except for the right column. */
    // 测试一个除了最右边一列之外都被填满的棋盘。
    @Test
    @Tag("empty-space")
    @DisplayName("Empty right column")
    @GradedTest(number = "2.5")
    public void testEmptyRightCol() {
        int[][] rawVals = new int[][] {
                {2, 4, 2, 0},
                {4, 2, 4, 0},
                {2, 4, 2, 0},
                {4, 2, 4, 0},
        };
        Model m = new Model(rawVals, 0);
        assertWithMessage("Right col is empty\n" + m.getBoard()).that(m.emptySpaceExists()).isTrue();
    }

    /** Tests a completely full board except one piece. */
    // 测试一个只有一个空格、其余位置全满的棋盘。
    @Test
    @Tag("empty-space")
    @DisplayName("One empty space")
    @GradedTest(number = "2.6")
    public void testAlmostFullBoard() {
        int[][] rawVals = new int[][] {
                {2, 4, 2, 4},
                {4, 2, 4, 2},
                {2, 0, 2, 4},
                {4, 2, 4, 2},
        };
        Model m = new Model(rawVals, 0);
        assertWithMessage("Board is not full\n" + m.getBoard()).that(m.emptySpaceExists()).isTrue();
    }

    /** Tests a completely full board.
     * The game isn't over since you can merge, but the emptySpaceExists method
     * should only look for empty space (and not adjacent values).
     */
    // 测试一个完全填满的棋盘。
    // 虽然游戏还未结束，因为仍然可以合并，但 emptySpaceExists 方法
    // 只应检查是否存在空格，而不应考虑相邻方块数值。
    @Test
    @Tag("empty-space")
    @DisplayName("Full board with valid merge")
    @GradedTest(number = "2.7")
    public void testFullBoard() {
        int[][] rawVals = new int[][] {
                {2, 2, 2, 2},
                {2, 2, 2, 2},
                {2, 2, 2, 2},
                {2, 2, 2, 2},
        };
        Model m = new Model(rawVals, 0);
        assertWithMessage("Board is full\n" + m.getBoard()).that(m.emptySpaceExists()).isFalse();
    }

    /** Tests a completely full board. */
    // 测试一个完全填满的棋盘。
    @Test
    @Tag("empty-space")
    @DisplayName("Full board")
    @GradedTest(number = "2.8")
    public void testFullBoardNoMerge() {
        int[][] rawVals = new int[][] {
                {2, 4, 2, 4},
                {4, 2, 4, 2},
                {2, 4, 2, 4},
                {4, 2, 4, 2},
        };
        Model m = new Model(rawVals, 0);
        assertWithMessage("Board is full\n" + m.getBoard()).that(m.emptySpaceExists()).isFalse();

    }
}
