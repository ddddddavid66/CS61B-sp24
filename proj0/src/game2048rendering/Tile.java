package game2048rendering;

/** Represents the image of a numbered tile on a 2048 board.
 *  @author P. N. Hilfinger.
 */
// 表示 2048 棋盘上一个带数字方块的图像状态。
public class Tile {

    /** A new tile with VALUE as its value at (x, y).  This
     *  constructor is private, so all tiles are created by the
     *  factory method create. */
    // 创建一个位于 (x, y)、数值为 VALUE 的新方块。
    // 该构造器是私有的，因此所有方块都必须通过工厂方法 create 创建。
    private Tile(int value, int x, int y) {
        this._value = value;
        this._x = x;
        this._y = y;
        this._next = null;
        this._merged  = false;
    }

    /** Return whether this tile was already merged. */
    // 返回该方块是否已经参与过合并。
    public boolean wasMerged() {
        return _merged;
    }

    void setMerged(boolean merged) {
        this._merged = merged;
    }

    /** Return my current y-coordinate. */
    // 返回当前的 y 坐标。
    int y() {
        return _y;
    }

    /** Return my current x-coordinate. */
    // 返回当前的 x 坐标。
    int x() {
        return _x;
    }

    /** Return the value supplied to my constructor. */
    // 返回构造该方块时传入的数值。
    public int value() {
        return _value;
    }

    /** Return my next state.  Before I am moved or merged, I am my
     *  own successor. */
    // 返回我的下一个状态。
    // 在移动或合并之前，我自己的后继状态就是我自己。
    Tile next() {
        return _next == null ? this : _next;
    }

    /** Set my next state when I am moved or merged. */
    // 当我发生移动或合并时，设置我的下一个状态。
    void setNext(Tile otherTile) {
        _next = otherTile;
    }

    /** Return a new tile at (x, y) with value VALUE. */
    // 返回一个位于 (x, y)、数值为 VALUE 的新方块。
    public static Tile create(int value, int x, int y) {
        return new Tile(value, x, y);
    }

    /** Return the distance in rows or columns between me and my successor
     *  tile (0 if I have no successor). */
    // 返回我与后继方块之间在行或列方向上的距离；
    // 如果没有后继方块，则返回 0。
    int distToNext() {
        if (_next == null) {
            return 0;
        } else {
            return Math.max(Math.abs(_y - _next.y()),
                            Math.abs(_x - _next.x()));
        }
    }

    @Override
    public String toString() {
        return String.format("Tile %d at position (%d, %d)", value(), x(), y());
    }

    /** My value. */
    // 我的数值。
    private final int _value;

    /** My last position on the board. */
    // 我在棋盘上的上一个位置。
    private final int _x;
    private final int _y;

    /** Whether I have merged. */
    // 我是否已经发生过合并。
    private boolean _merged;

    /** Successor tile: one I am moved to or merged with. */
    // 后继方块：我移动到的方块，或我与之合并后的方块。
    private Tile _next;
}
