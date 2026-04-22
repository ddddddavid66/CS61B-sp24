package game2048rendering;

import game2048logic.Model;
import ucb.gui2.TopLevel;
import ucb.gui2.LayoutSpec;

import java.util.concurrent.ArrayBlockingQueue;

import java.awt.event.KeyEvent;


/** The GUI controller for a 2048 board and buttons.
 *  @author P. N. Hilfinger
 */
// 2048 棋盘和按钮的 GUI 控制器。
class GUI extends TopLevel {

    /** A new window with given TITLE providing a view of MODEL. */
    // 创建一个标题为 TITLE 的新窗口，用于显示 MODEL。
    GUI(String title, Model model) {
        super(title, true);
        addMenuButton("Game->New", this::newGame);
        addMenuButton("Game->Quit", this::quit);

        addLabel("", "Score", new LayoutSpec("y", 1));

        _model = model;

        _widget = new BoardWidget(model.size());
        add(_widget,
            new LayoutSpec("y", 0,
                           "height", "REMAINDER",
                           "width", "REMAINDER"));

        _widget.requestFocusInWindow();
        _widget.setKeyHandler("keypress", this::keyPressed);
        setPreferredFocus(_widget);
        setScore(0);
    }

    /** Response to "Quit" button click. */
    // 处理点击 “Quit” 按钮后的响应。
    private void quit(String dummy) {
        _pendingKeys.offer("Quit");
        _widget.requestFocusInWindow();
    }

    /** Response to "New Game" button click. */
    // 处理点击 “New Game” 按钮后的响应。
    private void newGame(String dummy) {
        _pendingKeys.offer("New Game");
        _widget.requestFocusInWindow();
    }

    /** Respond to the user pressing key E by queuing the key on our
     *  queue of pending keys.*/
    // 当用户按下某个键时，将该按键加入待处理按键队列。
    private void keyPressed(String unused, KeyEvent e) {
        _pendingKeys.offer(e.getKeyCode() + "");
    }

    /** Return the next pending event, waiting for it as necessary.
     *  Ordinary key presses are reported as the key codes of the
     *  character pressed.  In addition, menu-button clicks result in
     *  the messages "Quit" or "New Game". */
    // 返回下一个待处理事件，必要时会阻塞等待。
    // 普通按键会以按键编码形式返回；菜单按钮点击会返回 "Quit" 或 "New Game"。
    private String readKey() {
        try {
            return _pendingKeys.take();
        } catch (InterruptedException excp) {
            throw new Error("unexpected interrupt");
        }
    }

    /** Return which direction arrow was pressed. */
    // 返回被按下的方向键所对应的方向字符串。
    String getKey() {
        String command = readKey();
        switch (command) {
            case "↑" -> command = "Up";
            case "→" -> command = "Right";
            case "↓" -> command = "Down";
            case "←" -> command = "Left";
            default -> {}
        }

        return command;
    }

    /** Set the current score being displayed to SCORE. */
    // 将当前显示的分数更新为 SCORE。
    private void setScore(int score) {
        setLabel("Score", String.format("Score: %6d", score));
    }

    /** Plays an animation to update the GUI to the new state of the board. */
    // 播放动画，并将 GUI 更新到棋盘的新状态。
    void update() {
        _widget.update(_model);
        setScore(_model.score());
    }

    /** The board widget. */
    // 棋盘部件。
    private final BoardWidget _widget;

    /** The game model being viewed. */
    // 当前显示的游戏模型。
    private final Model _model;

    /** Queue of pending key presses. */
    // 待处理按键队列。
    private final ArrayBlockingQueue<String> _pendingKeys =
        new ArrayBlockingQueue<>(5);

}
