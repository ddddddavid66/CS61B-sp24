package game2048rendering;

/** Symbolic names for the four sides of a board.
 *  @author P. N. Hilfinger */
// 棋盘四个方向的符号名称。
public enum Side {
    /** The parameters (COL0, ROW0, DCOL, and DROW) for each of the
     *  symbolic directions, D, below are to be interpreted as follows:
     *     The board's standard orientation has the top of the board
     *     as NORTH, and rows and columns (see Model) are numbered
     *     from its lower-left corner. Consider the board oriented
     *     so that side D of the board is farthest from you. Then
     *        * (COL0*s, ROW0*s) are the standard coordinates of the
     *          lower-left corner of the reoriented board (where s is the
     *          board size), and
     *        * If (x, y) are the standard coordinates of a certain
     *          square on the reoriented board, then (x+DCOL, y+DROW)
     *          are the standard coordinates of the squares immediately
     *          above it on the reoriented board.
     *  The idea behind going to this trouble is that by using the
     *  x() and y() methods below to translate from reoriented to
     *  standard coordinates, one can arrange to use exactly the same code
     *  to compute the result of tilting the board in any particular
     *  direction. */
    // 下面每个方向对应的参数 (COL0, ROW0, DCOL, DROW) 需要按如下方式理解：
    // 棋盘的标准朝向以上方为 NORTH，行列编号从左下角开始。
    // 设想将棋盘旋转到使方向 D 远离观察者，此时：
    // 1. (COL0 * s, ROW0 * s) 是重新定向后棋盘左下角在标准坐标中的位置，其中 s 是棋盘边长；
    // 2. 如果 (x, y) 是重新定向后棋盘上某个格子的标准坐标，
    //    那么 (x + DCOL, y + DROW) 就是其正上方格子的标准坐标。
    // 这样设计的目的是借助下面的 x() 和 y() 方法，把重新定向后的坐标映射回标准坐标，
    // 从而使用同一套代码处理棋盘朝任意方向倾斜的结果。

    NORTH(0, 0, 0, 1),
    EAST(0, 1, 1, 0),
    SOUTH(1, 1, 0, -1),
    WEST(1, 0, -1, 0);

    /** The side that is in the direction (DCOL, DROW) from any square
     *  of the board.  Here, "direction (DCOL, DROW) means that to
     *  move one space in the direction of this Side increases the row
     *  by DROW and the colunn by DCOL.  (COL0, ROW0) are the row and
     *  column of the lower-left square when sitting at the board facing
     *  towards this Side. */
    // 表示从棋盘任意格子出发，沿着 (DCOL, DROW) 方向所对应的一侧。
    // 这里的“方向 (DCOL, DROW)”指向该 Side 方向移动一格时，
    // 行增加 DROW，列增加 DCOL。
    // (COL0, ROW0) 表示当面向该 Side 观察棋盘时，左下角格子的行列参数。
    Side(int col0, int row0, int dcol, int drow) {
        this._row0 = row0;
        this._col0 = col0;
        this._drow = drow;
        this._dcol = dcol;
    }

    /** Return the standard x-coordinate for square (x, y) on a board
     *  of size SIZE oriented with this Side on top. */
    // 返回当该 Side 处于棋盘上方时，边长为 SIZE 的棋盘上格子 (x, y)
    // 对应的标准 x 坐标。
    int x(int x, int y, int size) {
        return _col0 * (size - 1) + x * _drow + y * _dcol;
    }

    /** Return the standard y-coordinate for square (x, y) on a board
     *  of size SIZE oriented with this Side on top. */
    // 返回当该 Side 处于棋盘上方时，边长为 SIZE 的棋盘上格子 (x, y)
    // 对应的标准 y 坐标。
    int y(int x, int y, int size) {
        return _row0 * (size - 1) - x * _dcol + y * _drow;
    }

    /** Parameters describing this Side, as documented in the comment at the
     *  start of this class. */
    // 描述该 Side 的参数，含义见本类开头注释。
    private final int _row0, _col0, _drow, _dcol;
}
