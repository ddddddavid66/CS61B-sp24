package game2048logic;

import game2048rendering.Board;
import game2048rendering.Side;
import game2048rendering.Tile;

import java.util.Formatter;


/** The state of a game of 2048.
 *  @author P. N. Hilfinger + Josh Hug
 */
// 表示 2048 游戏的状态。
public class Model {
    /** Current contents of the board. */
    // 当前棋盘内容。
    private final Board board;
    /** Current score. */
    // 当前分数。
    private int score;

    /* Coordinate System: column x, row y of the board (where x = 0,
     * y = 0 is the lower-left corner of the board) will correspond
     * to board.tile(x, y).  Be careful!
     */
    // 坐标系统说明：棋盘列为 x、行为 y，其中 (0, 0) 是左下角，
    // 并且该坐标对应到 board.tile(x, y)。使用时要特别注意。

    /** Largest piece value. */
    // 最大方块数值。
    public static final int MAX_PIECE = 2048;

    /** A new 2048 game on a board of size SIZE with no pieces
     *  and score 0. */
    // 创建一个边长为 SIZE 的新 2048 游戏，初始无方块且分数为 0。
    public Model(int size) {
        board = new Board(size);
        score = 0;
    }

    /** A new 2048 game where RAWVALUES contain the values of the tiles
     * (0 if null). VALUES is indexed by (x, y) with (0, 0) corresponding
     * to the bottom-left corner. Used for testing purposes. */
    // 使用 RAWVALUES 创建一个新的 2048 游戏，其中 0 表示空位置；
    // VALUES 按 (x, y) 索引，且 (0, 0) 对应左下角。该构造方法用于测试。
    public Model(int[][] rawValues, int score) {
        board = new Board(rawValues);
        this.score = score;
    }

    /** Return the current Tile at (x, y), where 0 <= x < size(),
     *  0 <= y < size(). Returns null if there is no tile there.
     *  Used for testing. */
    // 返回坐标 (x, y) 处当前的 Tile；如果该位置没有方块则返回 null。
    // 该方法用于测试。
    public Tile tile(int x, int y) {
        return board.tile(x, y);
    }

    /** Return the number of squares on one side of the board. */
    // 返回棋盘单边的格子数。
    public int size() {
        return board.size();
    }

    /** Return the current score. */
    // 返回当前分数。
    public int score() {
        return score;
    }


    /** Clear the board to empty and reset the score. */
    // 清空棋盘并重置分数。
    public void clear() {
        score = 0;
        board.clear();
    }

    /** Add TILE to the board. There must be no Tile currently at the
     *  same position. */
    // 将 TILE 添加到棋盘上；该位置当前不能已经有其他 Tile。
    public void addTile(Tile tile) {
        board.addTile(tile);
    }

    /** Return true iff the game is over (there are no moves, or
     *  there is a tile with value 2048 on the board). */
    // 当且仅当游戏结束时返回 true：
    // 也就是棋盘已无合法移动，或棋盘上存在数值为 2048 的方块。
    public boolean gameOver() {
        return maxTileExists() || !atLeastOneMoveExists();
    }

    /** Returns this Model's board. */
    // 返回当前 Model 对应的棋盘对象。
    public Board getBoard() {
        return board;
    }

    /** Returns true if at least one space on the Board is empty.
     *  Empty spaces are stored as null.
     * */
    // 如果棋盘上至少有一个空格，返回 true。
    // 空格使用 null 表示。
    public boolean emptySpaceExists() {
        // 任务 2，补全这个函数。
        for (int i = 0; i < board.size(); i++) {
            for (int j = 0; j < board.size(); j++) {
                Tile tile = board.tile(i, j);
                if(tile == null){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns true if any tile is equal to the maximum valid value.
     * Maximum valid value is given by this.MAX_PIECE. Note that
     * given a Tile object t, we get its value with t.value().
     */
    // 如果任意方块的值等于允许的最大值，则返回 true。
    // 最大值由 this.MAX_PIECE 给出。注意：给定 Tile 对象 t，
    // 可以通过 t.value() 获取它的数值。
    public boolean maxTileExists() {
        // Task 3. Fill in this function.
        for (int i = 0; i < board.size(); i++) {
            for (int j = 0; j < board.size(); j++) {
                Tile tile = board.tile(i, j);
                if(tile != null &&   tile.value() == MAX_PIECE){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns true if there are any valid moves on the board.
     * There are two ways that there can be valid moves:
     * 1. There is at least one empty space on the board.
     * 2. There are two adjacent tiles with the same value.
     */
    // 如果棋盘上存在任意合法移动，则返回 true。
    // 合法移动有两种情况：
    // 1. 棋盘上至少有一个空格。
    // 2. 存在两个相邻且数值相同的方块。
    public boolean atLeastOneMoveExists() {
        boolean isEmptyExist = emptySpaceExists();
        if(isEmptyExist){
            return true;
        }
        for (int i = 0; i < board.size(); i++) {
            for (int j = 0; j < board.size(); j++) {
                Tile tile = board.tile(i, j);
                Tile left = i == 0 ? null : board.tile(i - 1,j);
                Tile right = i == board.size() - 1 ? null : board.tile(i + 1,j);
                Tile up = j == board.size() - 1  ? null : board.tile(i,j + 1);
                Tile down = j == 0 ? null : board.tile(i,j - 1);
                if(left != null && tile.value() == left.value()){
                    return true;
                }
                if(right != null && tile.value() == right.value()){
                    return true;
                }
                if(up != null && tile.value() == up.value()){
                    return true;
                }
                if(down != null && tile.value() == down.value()){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Moves the tile at position (x, y) as far up as possible.
     *
     * Rules for Tilt:
     * 1. If two Tiles are adjacent in the direction of motion and have
     *    the same value, they are merged into one Tile of twice the original
     *    value and that new value is added to the score instance variable
     * 2. A tile that is the result of a merge will not merge again on that
     *    tilt. So each move, every tile will only ever be part of at most one
     *    merge (perhaps zero).
     * 3. When three adjacent tiles in the direction of motion have the same
     *    value, then the leading two tiles in the direction of motion merge,
     *    and the trailing tile does not.
     */
    // 将位置 (x, y) 的方块尽可能向上移动。
    //
    // 倾斜规则如下：
    // 1. 如果运动方向上两个相邻方块数值相同，它们会合并成一个数值翻倍的新方块，
    //    并把该新数值加入分数。
    // 2. 由合并产生的方块在本次倾斜中不会再次合并，因此每个方块每次移动最多参与一次合并。
    // 3. 如果运动方向上有连续三个相同数值的方块，则更靠前的两个会合并，
    //    最后一个不会合并。
    public void moveTileUpAsFarAsPossible(int x, int y) {
        Tile currTile = board.tile(x, y);
        int myValue = currTile.value();
        int targetY = y;
        // TODO：任务 5、6 和 10，补全这个函数。
        // 任务5 向上移动
        //将位置 (x, y) 的方块尽可能向上移动。
        while(targetY + 1 < board.size() && board.tile(x,targetY + 1) == null){
            targetY++;
        }
        //任务6 合并方块  同一个方块 并且只能合并一次 不能合并多次
            if( targetY + 1 < board.size()   ){
                Tile next = board.tile(x, targetY + 1);
                if(next != null
                        && !(next.wasMerged() )
                        && next.value() == currTile.value() ){
                    board.move(x,targetY + 1,currTile);
                    score += currTile.value() * 2;
                    return;
                }
            }
        if(y != targetY){
            board.move(x,targetY,currTile);
        }
    }

    /** Handles the movements of the tilt in column x of the board
     * by moving every tile in the column as far up as possible.
     * The viewing perspective has already been set,
     * so we are tilting the tiles in this column up.
     * */
    // 处理棋盘第 x 列在一次倾斜中的移动，
    // 将该列中的每个方块都尽可能向上移动。
    // 视角已经设置完成，因此这里默认将这一列向上倾斜。
    public void tiltColumn(int x) {
        for (int i = board.size() - 1; i >= 0; i--) {
            if (board.tile(x, i) != null) {
                moveTileUpAsFarAsPossible(x, i);
            }
        }
        board.resetMerged();
    }
    public void tilt(Side side) {
        // 任务8  向上移动整个列
        //任务9 改变方向
        board.setViewingPerspective(side);
        for (int i = board.size() - 1; i >= 0; i--) {
            tiltColumn(i);
        }
        board.setViewingPerspective(Side.NORTH);
    }

    /** Tilts every column of the board toward SIDE.
     */
    // 将棋盘的每一列都朝 SIDE 指定的方向倾斜。
    public void tiltWrapper(Side side) {
        board.resetMerged();
        tilt(side);
    }


    @Override
    public String toString() {
        Formatter out = new Formatter();
        out.format("%n[%n");
        for (int y = size() - 1; y >= 0; y -= 1) {
            for (int x = 0; x < size(); x += 1) {
                if (tile(x, y) == null) {
                    out.format("|    ");
                } else {
                    out.format("|%4d", tile(x, y).value());
                }
            }
            out.format("|%n");
        }
        String over = gameOver() ? "over" : "not over";
        out.format("] %d (game is %s) %n", score(), over);
        return out.toString();
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof Model m) && this.toString().equals(m.toString());
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}
