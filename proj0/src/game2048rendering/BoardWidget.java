package game2048rendering;

import game2048logic.Model;
import ucb.gui2.Pad;

import java.util.ArrayList;
import java.util.HashMap;

import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.FontMetrics;

import static java.lang.Math.max;
import static java.lang.Math.abs;
import static java.lang.Math.round;

/** A widget that displays a 2048 board.
 *  @author P. N. Hilfinger
 */
// 用于显示 2048 棋盘的部件。
class BoardWidget extends Pad {

    /* Parameters controlling sizes, speeds, colors, and fonts. */
    // 控制尺寸、速度、颜色和字体的参数。

    /** Colors of empty squares and grid lines. */
    // 空白格子和网格线的颜色。
    static final Color
        EMPTY_SQUARE_COLOR = new Color(205, 192, 176),
        BAR_COLOR = new Color(184, 173, 158);

    /** Bar width separating tiles and length of tile's side
     *  (pixels). */
    // 分隔方块的边框宽度，以及方块边长（像素）。
    static final int
        TILE_SEP = 15,
        TILE_SIDE = 100,
        TILE_SIDE_SEP = TILE_SEP + TILE_SIDE;

    /** Font used for numbering on tiles with <= 2 digits. */
    // 数值位数不超过 2 位时使用的字体。
    static final Font TILE_FONT2 = new Font("SansSerif", 1, 48);
    /** Font used for numbering on tiles with 3 digits. */
    // 数值为 3 位时使用的字体。
    static final Font TILE_FONT3 = new Font("SansSerif", 1, 40);
    /** Font used for numbering on tiles with 4 digits. */
    // 数值为 4 位时使用的字体。
    static final Font TILE_FONT4 = new Font("SansSerif", 1, 32);

    /** Color for overlay text on board. */
    // 棋盘覆盖层文字的颜色。
    static final Color OVERLAY_COLOR = new Color(200, 0, 0, 64);

    /** Font for overlay text on board. */
    // 棋盘覆盖层文字使用的字体。
    static final Font OVERLAY_FONT = new Font("SansSerif", 1, 64);

    /** Wait between animation steps (in milliseconds). */
    // 动画每一步之间的等待时间（毫秒）。
    static final int TICK = 10;

    /** Amount to move per second (in rows/columns). */
    // 每秒移动的距离（以行/列为单位）。
    static final float MOVE_DELTA = 10.0f;

    /** Fractional increase in size for "bloom effect". */
    // “绽放效果”时尺寸增大的比例。
    static final float BLOOM_FACTOR = 0.1f;

    /** Time over which a tile "blooms" in seconds. */
    // 方块执行“绽放效果”的持续时间（秒）。
    static final float BLOOM_TIME = 0.5f;

    /** Ticks over which a tile "blooms" out or in. */
    // 方块完成一次“绽放”展开和收回所经历的 tick 数。
    static final int BLOOM_TICKS = (int) (20.0 * BLOOM_TIME / TICK);

    /** Mapping from numbers on tiles to their text and background
     *  colors. */
    // 方块数字到其文字颜色和背景颜色的映射。
    static final HashMap<Integer, Color[]> TILE_COLORS = new HashMap<>();

    /** List of tile values and corresponding background and foreground
     *  color values. */
    // 方块数值及其对应的背景色、前景色定义表。
    private static final int[][] TILE_COLOR_MAP = {
        { 2, 0x776e65, 0xeee4da },
        { 4, 0x776e65, 0xede0c8 },
        { 8, 0xf9f6f2, 0xf2b179 },
        { 16, 0xf9f6f2, 0xf59563 },
        { 32, 0xf9f6f2, 0xf67c5f },
        { 64, 0xf9f6f2, 0xf65e3b },
        { 128, 0xf9f6f2, 0xedcf72 },
        { 256, 0xf9f6f2, 0xedcc61 },
        { 512, 0xf9f6f2, 0xedc850 },
        { 1024, 0xf9f6f2, 0xedc53f },
        { 2048, 0xf9f6f2, 0xedc22e },
    };

    static {
        /* { "LABEL", "TEXT COLOR (hex)", "BACKGROUND COLOR (hex)" } */
        // { "标签", "文字颜色（十六进制）", "背景颜色（十六进制）" }
        for (int[] tileData : TILE_COLOR_MAP) {
            TILE_COLORS.put(tileData[0],
                            new Color[] { new Color(tileData[1]),
                                          new Color(tileData[2]) });
        }
    }

    /** A graphical representation of a 2048 board with SIZE rows and
     *  columns. */
    // 创建一个具有 SIZE 行和 SIZE 列的 2048 棋盘图形组件。
    BoardWidget(int size) {
        _size = size;
        _boardSide = size * TILE_SIDE_SEP + TILE_SEP;
        _tiles = new ArrayList<>();
        setPreferredSize(_boardSide, _boardSide);
    }

    /** Render board on G. */
    // 在图形上下文 G 上绘制棋盘。
    @Override
    public synchronized void paintComponent(Graphics2D g) {
        g.setColor(EMPTY_SQUARE_COLOR);
        g.fillRect(0, 0, _boardSide, _boardSide);
        g.setColor(BAR_COLOR);
        for (int k = 0; k <= _boardSide; k += TILE_SIDE_SEP) {
            g.fillRect(0, k, _boardSide, TILE_SEP);
            g.fillRect(k, 0, TILE_SEP, _boardSide);
        }
        for (Tile tile : _tiles) {
            render(g, tile);
        }
        if (_end) {
            g.setFont(OVERLAY_FONT);
            FontMetrics metrics = g.getFontMetrics();
            g.setColor(OVERLAY_COLOR);
            g.drawString("GAME OVER",
                         (_boardSide
                          - metrics.stringWidth("GAME OVER")) / 2,
                         (2 * _boardSide + metrics.getMaxAscent()) / 4);
        }
    }

    /** Render TILE on G. */
    // 在图形上下文 G 上绘制单个 TILE。
    private void render(Graphics2D g, Tile tile) {
        int col0 = tile.x(),
            row0 = tile.y(),
            col1 = tile.next().x(),
            row1 = tile.next().y();
        int dcol = Integer.compare(col1, col0),
            drow = Integer.compare(row1, row0);

        float vcol, vrow;
        if (_distMoved >= max(abs(col0 - col1), abs(row0 - row1))) {
            vcol = col1; vrow = row1;
        } else {
            vcol = col0 + _distMoved * dcol;
            vrow = row0 + _distMoved * drow;
        }

        int ulx = Math.round(vcol * TILE_SIDE_SEP + TILE_SEP),
            uly = Math.round((_size - vrow - 1) * TILE_SIDE_SEP + TILE_SEP);

        if (tile.value() < 100) {
            g.setFont(TILE_FONT2);
        } else if (tile.value() < 1000) {
            g.setFont(TILE_FONT3);
        } else {
            g.setFont(TILE_FONT4);
        }
        FontMetrics metrics = g.getFontMetrics();
        int bloom;
        if (_bloomingTiles != null && _bloomingTiles.contains(tile)) {
            bloom = _bloom;
        } else {
            bloom = 0;
        }
        g.setColor(TILE_COLORS.get(tile.value())[1]);
        g.fillRect(ulx - bloom, uly - bloom, 2 * bloom + TILE_SIDE,
                   2 * bloom + TILE_SIDE);
        g.setColor(TILE_COLORS.get(tile.value())[0]);

        String label = Integer.toString(tile.value());
        g.drawString(label,
                     ulx + (TILE_SIDE - metrics.stringWidth(label)) / 2,
                     uly + (2 * TILE_SIDE + metrics.getMaxAscent()) / 4);

    }

    /** Return the list of all Tiles in MODEL. */
    // 返回 MODEL 中所有 Tile 的列表。
    private ArrayList<Tile> modelTiles(Model model) {
        ArrayList<Tile> result = new ArrayList<>();
        for (int col = 0; col < model.size(); col += 1) {
            for (int row = 0; row < model.size(); row += 1) {
                Tile tile = model.tile(col, row);
                if (tile != null) {
                    result.add(tile);
                }
            }
        }
        return result;
    }

    /** Return the list of all tiles in NEXTTILES that are newly
     *  created or the result of merging of current tiles. */
    // 返回 NEXTTILES 中那些新创建的方块，
    // 或由当前方块合并而成、需要执行绽放效果的方块。
    private ArrayList<Tile> newTiles(ArrayList<Tile> nextTiles) {
        ArrayList<Tile> bloomers = new ArrayList<>(nextTiles);
        for (Tile tile : _tiles) {
            if (tile.next().value() == tile.value()) {
                bloomers.remove(tile.next());
            }
        }
        return bloomers;
    }

    /** Wait for one tick (TICK milliseconds). */
    // 等待一个 tick（TICK 毫秒）。
    private void tick() {
        try {
            wait(TICK);
        } catch (InterruptedException excp) {
            assert false : "Internal error: unexpected interrupt";
        }
    }

    /** Create the blooming effect on tiles in BLOOMINGTILES. */
    // 为 BLOOMINGTILES 中的方块创建绽放动画效果。
    private void doBlooming(ArrayList<Tile> bloomingTiles) {
        _bloomingTiles = bloomingTiles;
        if (bloomingTiles.isEmpty()) {
            return;
        }
        for (int k = 1; k <= BLOOM_TICKS; k += 1) {
            _bloom = round(TILE_SIDE * BLOOM_FACTOR * k / BLOOM_TICKS);
            repaint();
            tick();
        }
        for (int k = BLOOM_TICKS - 1; k >= 0; k -= 1) {
            _bloom = round(TILE_SIDE * BLOOM_FACTOR * k / BLOOM_TICKS);
            repaint();
            tick();
        }
        _bloomingTiles = null;
    }


    /** Move tiles to their new positions and save a new set of tiles from
     *  MODEL, which is assumed to reflect the next state of the tiles after
     *  the completion of all movement. */
    // 将方块移动到它们的新位置，并从 MODEL 中保存一组新的方块。
    // 假定 MODEL 已经反映出所有移动完成后的方块最终状态。
    synchronized void update(Model model) {
        float dist;
        ArrayList<Tile> nextTiles = modelTiles(model);

        dist = 0.0f;
        for (Tile tile : _tiles) {
            dist = Math.max(dist, tile.distToNext());
        }
        _distMoved = 0.0f;
        while (_distMoved < dist) {
            repaint();
            tick();
            _distMoved = Math.min(dist,
                                  _distMoved + TICK * MOVE_DELTA / 1000.0f);
        }


        ArrayList<Tile> bloomers = newTiles(nextTiles);
        _tiles = nextTiles;
        doBlooming(bloomers);
        _end = model.gameOver();
        _distMoved = 0.0f;
        repaint();
    }

    /** A list of Tiles currently being displayed. */
    // 当前正在显示的方块列表。
    private ArrayList<Tile> _tiles;
    /** A list of Tiles currently being displayed with blooming effect. */
    // 当前正在显示且带有绽放效果的方块列表。
    private ArrayList<Tile> _bloomingTiles;

    /** Distance tiles have moved toward their next positions, in units of
     *  rows and columns. */
    // 方块朝其下一个位置移动的距离，以行和列为单位。
    private float _distMoved;
    /** Amount to add to sides of tiles in _bloomingTiles. */
    // 对 _bloomingTiles 中方块四边额外增加的像素量。
    private int _bloom;

    /** Number of rows and of columns. */
    // 行数和列数。
    private final int _size;

    /** Length (in pixels) of the side of the board. */
    // 棋盘边长（像素）。
    private final int _boardSide;

    /** True iff "GAME OVER" message is being displayed. */
    // 当且仅当正在显示 “GAME OVER” 提示时为 true。
    private boolean _end;
}
