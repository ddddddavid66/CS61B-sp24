package game2048rendering;

import game2048logic.Model;

import java.awt.event.KeyEvent;
import java.util.Random;

import static game2048rendering.Side.*;

/** The input/output and GUI controller for play of a game of 2048.
 *  @author P. N. Hilfinger. */
// 2048 游戏运行过程中的输入输出与 GUI 控制器。
class Game {


    /** Controller for a game represented by MODEL, using GUI as the
     *  source of key inputs. Uses SEED as the random seed. */
    // 为 MODEL 表示的游戏创建控制器，使用 GUI 作为按键输入来源，
    // 并使用 SEED 作为随机种子。
    public Game(Model model, GUI gui, double tile2p, long seed) {
        _model = model;
        _playing = true;
        _gui = gui;
        _probOf2 = tile2p;

        if (seed == 0) {
            _random = new Random();
        } else {
            _random = new Random(seed);
        }
    }

    /** Return true iff we have not received a Quit command. */
    // 当且仅当尚未收到 Quit 命令时返回 true。
    boolean playing() {
        return _playing;
    }

    /** Clear the board and play one game, until receiving a quit or
     *  new-game request.  Update the viewer with each added tile or
     *  change in the board from tilting. */
    // 清空棋盘并运行一局游戏，直到收到退出或新游戏请求。
    // 每次新增方块或棋盘倾斜发生变化时，都更新界面显示。
    void playGame(boolean hotStart) {

        if (!hotStart) {
            _model.clear();
            _model.addTile(getValidNewTile());
        }
        while (_playing) {
            if (!hotStart) {
                if (!_model.gameOver()) {
                    _model.addTile(getValidNewTile());
                    _gui.update();
                }
            }
            if (hotStart) {
                _gui.update();
                hotStart = false;
            }

            boolean moved;
            moved = false;
            while (!moved) {
                String cmnd = _gui.getKey();
                switch (cmnd) {
                    case "Quit":
                        _playing = false;
                        return;
                    case "New Game":
                        return;
                    case KeyEvent.VK_UP + "": case KeyEvent.VK_DOWN + "": case KeyEvent.VK_LEFT + "": case KeyEvent.VK_RIGHT+ "":
                    case "\u2190": case "\u2191": case "\u2192": case "\u2193":
                        if (!_model.gameOver()) {
                            _gui.update();
                            moved = false;
                        }

                        String stateBefore = _model.toString();
                        _model.tiltWrapper(keyToSide(cmnd));
                        String stateAfter = _model.toString();

                        if (!stateBefore.equals(stateAfter)) {
                            _gui.update();
                            moved = true;
                        }

                        break;
                    default:
                        break;
                }

            }
        }
    }

    /** Return the side indicated by KEY ("Up", "Down", "Left",
     *  or "Right"). */
    // 根据 KEY 返回对应的方向 Side（"Up"、"Down"、"Left" 或 "Right"）。
    private Side keyToSide(String key) {
        return switch (key) {
            case KeyEvent.VK_UP + "", "\u2191" -> NORTH;
            case KeyEvent.VK_DOWN + "", "\u2193" -> SOUTH;
            case KeyEvent.VK_LEFT + "", "\u2190" -> WEST;
            case KeyEvent.VK_RIGHT+ "", "\u2192" -> EAST;
            default -> throw new IllegalArgumentException("unknown key designation");
        };
    }

    /** Return a valid tile, using our source's tile input until finding
     *  one that fits on the current board. Assumes there is at least one
     *  empty square on the board. */
    // 返回一个合法的新方块。
    // 会持续生成，直到找到一个能放入当前棋盘空位的方块为止。
    // 假设当前棋盘至少存在一个空格。
    private Tile getValidNewTile() {
        while (true) {
            Tile tile = generateNewTile(_model.size());
            if (_model.tile(tile.x(), tile.y()) == null) {
                return tile;
            }
        }
    }

    /** Return a randomly positioned tile with either value of 2 with
     * probability _probOf2 or a value of 4 with probability 1 - _probOf2 in a
     * board with size SIZE. */
    // 在边长为 SIZE 的棋盘上随机生成一个位置的方块。
    // 该方块以 _probOf2 的概率取值为 2，否则取值为 4。
    private Tile generateNewTile(int size) {
        int c = _random.nextInt(size), r = _random.nextInt(size);
        int v = _random.nextDouble() <= _probOf2 ? 2 : 4;

        return Tile.create(v, c, r);
    }

    /** The playing board. */
    // 游戏棋盘模型。
    private final Model _model;

    /** GUI from which random commands are collected. */
    // 收集用户命令的 GUI。
    private final GUI _gui;

    /** Probability that the next tile is 2, rather than a 4. */
    // 下一个方块生成 2 而不是 4 的概率。
    private final double _probOf2;

    /** Source of random numbers. */
    // 随机数来源。
    private final Random _random;

    /** True while user is still willing to play. */
    // 当用户仍然愿意继续游戏时为 true。
    private boolean _playing;

}
