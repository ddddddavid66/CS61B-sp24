package game2048logic;

import game2048rendering.Side;

import static com.google.common.truth.Truth.assertWithMessage;

public class TestUtils {

    /**
     * Checks that performing a tilt in the specified direction on the before
     * Model results in the after Model
     */
    // 检查对 before Model 按指定方向执行一次倾斜后，
    // 结果是否与 after Model 一致。
    public static void checkTilt(Model before, Model after, Side direction) {
        String prevBoard = before.toString();
        before.tiltWrapper(direction);
        String errMsg = String.format("Board incorrect. Before tilting towards"
                        + " %s, your board looked like:%s%nAfter the call to"
                        + " tilt, we expected:%s%nBut your board looks like:%s.",
                direction, prevBoard, after, before);
        assertWithMessage(errMsg).that(before).isEqualTo(after);
    }
}
