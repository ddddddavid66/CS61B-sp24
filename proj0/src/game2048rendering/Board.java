package game2048rendering;

import java.util.Arrays;
import java.util.Formatter;

/**
 * @author hug
 */
// 棋盘数据结构。
public class Board {
    /** Current contents of the board. */
    // 当前棋盘上的瓦片内容，每个元素是一个 Tile 对象或 null
    private final Tile[][] _values;

    /** Side that the board currently views as north. */
    // 当前棋盘的观察方向，将哪个方向视为北
    private Side _viewPerspective;

    public Board(int size) {
        _values = new Tile[size][size];
        _viewPerspective = Side.NORTH;
    }

    /** Shifts the view of the board such that the board behaves as if side S is north. */
    // 设置视角，使得棋盘行为表现为将指定方向 S 当作北
    public void setViewingPerspective(Side s) {
        _viewPerspective = s;
    }

    /** Create a board where RAWVALUES hold the values of the tiles on the board
     * (0 is null) with a current score of SCORE and the viewing perspective set to north. */
    // 根据二维数组 rawValues 创建棋盘，其中值为 0 表示空格子，非 0 创建对应 Tile 对象
    // 默认视角为北
    public Board(int[][] rawValues) {
        int size = rawValues.length;
        _values = new Tile[size][size];
        _viewPerspective = Side.NORTH;
        for (int x = 0; x < size; x += 1) {
            for (int y = 0; y < size; y += 1) {
                int value = rawValues[size - 1 - y][x]; // 翻转 y 轴，使底部为 0 行
                Tile tile;
                if (value == 0) {
                    tile = null;
                } else {
                    tile = Tile.create(value, x, y);
                }
                _values[x][y] = tile;
            }
        }
    }

    /** Returns the size of the board. */
    // 返回棋盘的边长
    public int size() {
        return _values.length;
    }

    /** Return the current Tile at (x, y), when sitting with the board
     *  oriented so that SIDE is at the top (farthest) from you. */
    // 返回在指定视角 SIDE 下 (x, y) 的 Tile 对象
    // 根据当前视角动态映射坐标
    private Tile vtile(int x, int y, Side side) {
        return _values[side.x(x, y, size())][side.y(x, y, size())];
    }

    /** Return the current Tile at (x, y), where 0 <= x < size(),
     *  0 <= y < size(). Returns null if there is no tile there. */
    // 返回当前视角下 (x, y) 的 Tile 对象，如果该位置为空返回 null
    // 其中 x 和 y 必须满足 0 <= x < size() 且 0 <= y < size()。
    public Tile tile(int x, int y) {
        return vtile(x, y, _viewPerspective);
    }

    /** Clear the board to empty and reset the score. */
    // 清空棋盘，将所有格子设为 null
    public void clear() {
        for (Tile[] column : _values) {
            Arrays.fill(column, null);
        }
    }

    /** Adds the tile T to the board */
    // 将 Tile t 添加到棋盘对应位置
    public void addTile(Tile t) {
        _values[t.x()][t.y()] = t;
    }


    /** Places the Tile TILE at column x, y y where x and y are
     * treated as coordinates with respect to the current viewPerspective.
     *
     * (0, 0) is bottom-left corner.
     *
     * If the move is a merge, sets the tile's merged status to true.
     * */
    // 将 Tile tile 放置到棋盘 (x, y) 位置，坐标基于当前视角
    // 如果发生合并，则设置 Tile 的 merged 状态为 true
    public void move(int x, int y, Tile tile) {
        int px = _viewPerspective.x(x, y, size());
        int py = _viewPerspective.y(x, y, size());

        Tile tile1 = vtile(x, y, _viewPerspective);
        _values[tile.x()][tile.y()] = null; // 移除 tile 原来的位置

        // Move or merge the tile. It is important to call setNext
        // on the old tile(s) so they can be animated into position
        // 移动或合并 Tile，需要调用 setNext 保持动画效果
        // 对旧 Tile 调用 setNext，这样它们才能被正确地动画到目标位置
        Tile next;
        if (tile1 == null) {
            next = Tile.create(tile.value(), px, py);
        } else {
            if (tile.value() != tile1.value()) {
                throw new IllegalArgumentException("Tried to merge two unequal tiles: " + tile + " and " + tile1);
            }
            next = Tile.create(2 * tile.value(), px, py);
            tile1.setNext(next); // 设置旧 Tile 的下一位置，用于动画显示
        }
        tile.setMerged(tile1 != null); // 如果有合并，标记 tile 已合并
        next.setMerged(tile.wasMerged()); // 更新 next Tile 的 merged 状态
        tile.setNext(next); // 更新 tile 的 next
        _values[px][py] = next; // 更新棋盘
    }

    /** Resets all the merged booleans to false for every tile on the board. */
    // 将棋盘上所有 Tile 的 merged 状态重置为 false
    public void resetMerged() {
        for (int x = 0; x < size(); x += 1) {
            for (int y = 0; y < size(); y += 1) {
                if (_values[x][y] != null){
                    _values[x][y].setMerged(false);
                }
            }
        }
    }

    /** Returns the board as a string, used for debugging. */
    // 将棋盘转换为字符串，用于调试打印
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
        return out.toString();
    }
}
