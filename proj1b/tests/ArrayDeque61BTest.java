import jh61b.utils.Reflection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

public class ArrayDeque61BTest {

     @Test
     @DisplayName("ArrayDeque61B has no fields besides backing array and primitives")
     void noNonTrivialFields() {
         List<Field> badFields = Reflection.getFields(ArrayDeque61B.class)
                 .filter(f -> !(f.getType().isPrimitive() || f.getType().equals(Object[].class) || f.isSynthetic()))
                 .toList();

         assertWithMessage("Found fields that are not array or primitives").that(badFields).isEmpty();
     }

    @Test
    @DisplayName("size and isEmpty basic test")
    public void sizeAndEmptyTest() {
        Deque61B<String> dq = new ArrayDeque61B<>();

        assertThat(dq.isEmpty()).isTrue();
        assertThat(dq.size()).isEqualTo(0);

        dq.addFirst("A");
        assertThat(dq.isEmpty()).isFalse();
        assertThat(dq.size()).isEqualTo(1);

        dq.addLast("B");
        assertThat(dq.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("circular buffer: wrap around when adding")
    public void testCircularWrapAround() {
        Deque61B<Integer> dq = new ArrayDeque61B<>();
        // 初始容量8，nextFirst=3, nextLast=5
        // 向前添加使 nextFirst 绕回数组末尾
        for (int i = 0; i < 4; i++) {
            dq.addFirst(i);  // 添加 0,1,2,3 → 顺序: 3,2,1,0
        }
        // 向后添加使 nextLast 绕回数组开头
        for (int i = 4; i < 7; i++) {
            dq.addLast(i);  // 添加 4,5,6
        }
        // 期望顺序: [3,2,1,0,4,5,6]
        assertThat(dq.toList()).containsExactly(3, 2, 1, 0, 4, 5, 6).inOrder();
        assertThat(dq.size()).isEqualTo(7);
    }
        // ========== 基础功能测试 ==========

        @Test
        @DisplayName("basic addFirst and addLast")
        public void addBasicTest() {
            Deque61B<Integer> dq = new ArrayDeque61B<>();
            dq.addFirst(2);
            dq.addFirst(1);
            dq.addLast(3);
            assertThat(dq.toList()).containsExactly(1, 2, 3).inOrder();
        }



        // ========== 极端情况：空队列操作 ==========

        @Test
        @DisplayName("EXTREME: operations on empty deque return null")
        public void emptyDequeOperations() {
            Deque61B<Integer> dq = new ArrayDeque61B<>();

            // remove 空队列应返回 null
            assertThat(dq.removeFirst()).isNull();
            assertThat(dq.removeLast()).isNull();

            // get 任意索引应返回 null
            assertThat(dq.get(0)).isNull();
            assertThat(dq.get(-1)).isNull();
            assertThat(dq.get(100)).isNull();
            assertThat(dq.getRecursive(0)).isNull();

            // toList 应返回空列表
            assertThat(dq.toList()).isEmpty();

            // 状态不变
            assertThat(dq.isEmpty()).isTrue();
            assertThat(dq.size()).isEqualTo(0);
        }


        // ========== 极端情况：单元素操作 ==========

        @Test
        @DisplayName("EXTREME: single element deque operations")
        public void singleElementOperations() {
            Deque61B<String> dq = new ArrayDeque61B<>();
            dq.addFirst("ONLY");

            // 验证状态
            assertThat(dq.size()).isEqualTo(1);
            assertThat(dq.isEmpty()).isFalse();
            assertThat(dq.toList()).containsExactly("ONLY");

            // get 有效/无效索引
            assertThat(dq.get(0)).isEqualTo("ONLY");
            assertThat(dq.getRecursive(0)).isEqualTo("ONLY");
            assertThat(dq.get(1)).isNull();
            assertThat(dq.get(-1)).isNull();

            // removeFirst 后变空
            assertThat(dq.removeFirst()).isEqualTo("ONLY");
            assertThat(dq.isEmpty()).isTrue();
            assertThat(dq.size()).isEqualTo(0);
            assertThat(dq.toList()).isEmpty();

            // 重新添加验证复用
            dq.addLast("NEW");
            assertThat(dq.toList()).containsExactly("NEW");
        }


        // ========== 极端情况：边界索引 ==========

        @Test
        @DisplayName("EXTREME: get with boundary indices")
        public void getBoundaryIndices() {
            Deque61B<Integer> dq = new ArrayDeque61B<>();
            for (int i = 0; i < 5; i++) {
                dq.addLast(i * 10);  // [0, 10, 20, 30, 40]
            }

            // 有效边界
            assertThat(dq.get(0)).isEqualTo(0);           // 第一个
            assertThat(dq.get(4)).isEqualTo(40);          // 最后一个
            assertThat(dq.getRecursive(0)).isEqualTo(0);
            assertThat(dq.getRecursive(4)).isEqualTo(40);

            // 无效边界
            assertThat(dq.get(-1)).isNull();              // 负索引
            assertThat(dq.get(5)).isNull();               // index == size
            assertThat(dq.get(100)).isNull();             // 远超出
            assertThat(dq.getRecursive(-1)).isNull();
            assertThat(dq.getRecursive(5)).isNull();
        }


        // ========== 循环数组特性：指针环绕 ==========

        @Test
        @DisplayName("EXTREME: circular buffer wrap-around at array boundaries")
        public void circularWrapAround() {
            Deque61B<Integer> dq = new ArrayDeque61B<>();
            // 初始容量8, nextFirst=3, nextLast=4
            // 向前添加使 nextFirst 绕回数组末尾
            for (int i = 0; i < 4; i++) {
                dq.addFirst(i);  // 添加 0,1,2,3 → 逻辑: [3,2,1,0]
            }
            // 向后添加使 nextLast 绕回数组开头
            for (int i = 4; i < 7; i++) {
                dq.addLast(i);  // 添加 4,5,6 → 逻辑: [3,2,1,0,4,5,6]
            }

            assertThat(dq.size()).isEqualTo(7);
            assertThat(dq.toList()).containsExactly(3, 2, 1, 0, 4, 5, 6).inOrder();

            // 验证 get 在环绕后仍正确
            assertThat(dq.get(0)).isEqualTo(3);  // 逻辑第0个在物理[3]
            assertThat(dq.get(3)).isEqualTo(0);  // 逻辑第3个在物理[4]
            assertThat(dq.get(6)).isEqualTo(6);  // 逻辑第6个在物理[6]
        }

        @Test
        @DisplayName("EXTREME: full deque triggers resize and preserves order")
        public void resizeOnFull() {
            Deque61B<Integer> dq = new ArrayDeque61B<>();
            // 初始容量8，添加8个元素
            for (int i = 0; i < 8; i++) {
                dq.addLast(i);
            }
            assertThat(dq.size()).isEqualTo(8);

            // 添加第9个触发扩容（因子=2 → 新容量16）
            dq.addLast(8);
            assertThat(dq.size()).isEqualTo(9);
            assertThat(dq.toList()).containsExactly(0,1,2,3,4,5,6,7,8).inOrder();

            // 扩容后仍能正常添加/删除
            dq.addFirst(-1);
            assertThat(dq.toList()).containsExactly(-1,0,1,2,3,4,5,6,7,8).inOrder();
            assertThat(dq.removeFirst()).isEqualTo(-1);
            assertThat(dq.removeLast()).isEqualTo(8);
            assertThat(dq.toList()).containsExactly(0,1,2,3,4,5,6,7).inOrder();
        }

        @Test
        @DisplayName("EXTREME: multiple resizes with alternating adds")
        public void multipleResizesStress() {
            Deque61B<Integer> dq = new ArrayDeque61B<>();
            // 逐步添加触发多次扩容: 8→16→32→64
            for (int i = 0; i < 100; i++) {
                if (i % 2 == 0) {
                    dq.addFirst(i);
                } else {
                    dq.addLast(i);
                }
            }
            assertThat(dq.size()).isEqualTo(100);

            // 验证顺序: 偶数倒序在前，奇数正序在后
            List<Integer> expected = new java.util.ArrayList<>();
            for (int i = 98; i >= 0; i -= 2) expected.add(i);  // 98,96,...,0
            for (int i = 1; i < 100; i += 2) expected.add(i);   // 1,3,...,99
            assertThat(dq.toList()).containsExactlyElementsIn(expected).inOrder();
        }


        // ========== remove 操作测试 ==========

        @Test
        @DisplayName("removeFirst and removeLast maintain correct order")
        public void removeMaintainsOrder() {
            Deque61B<String> dq = new ArrayDeque61B<>();
            dq.addLast("A");
            dq.addLast("B");
            dq.addLast("C");
            dq.addFirst("X");
            dq.addFirst("Y");
            // 逻辑: [Y, X, A, B, C]

            assertThat(dq.removeFirst()).isEqualTo("Y");
            assertThat(dq.toList()).containsExactly("X", "A", "B", "C").inOrder();

            assertThat(dq.removeLast()).isEqualTo("C");
            assertThat(dq.toList()).containsExactly("X", "A", "B").inOrder();

            assertThat(dq.removeFirst()).isEqualTo("X");
            assertThat(dq.removeLast()).isEqualTo("B");
            assertThat(dq.removeFirst()).isEqualTo("A");
            assertThat(dq.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("EXTREME: remove until empty then reuse")
        public void removeUntilEmptyThenReuse() {
            Deque61B<Integer> dq = new ArrayDeque61B<>();
            dq.addFirst(1);
            dq.addFirst(2);
            dq.addFirst(3);

            assertThat(dq.removeFirst()).isEqualTo(3);
            assertThat(dq.removeFirst()).isEqualTo(2);
            assertThat(dq.removeFirst()).isEqualTo(1);
            assertThat(dq.isEmpty()).isTrue();

            // 空队列后重新使用，验证指针重置正确
            dq.addLast(10);
            dq.addLast(20);
            assertThat(dq.toList()).containsExactly(10, 20).inOrder();
            assertThat(dq.removeLast()).isEqualTo(20);
            assertThat(dq.removeLast()).isEqualTo(10);
            assertThat(dq.removeLast()).isNull();  // 空时返回 null
        }


        // ========== getRecursive 专项测试 ==========

        @Test
        @DisplayName("getRecursive matches iterative get")
        public void getRecursiveMatchesIterative() {
            Deque61B<String> dq = new ArrayDeque61B<>();
            String[] items = {"A", "B", "C", "D", "E"};
            for (String s : items) dq.addLast(s);

            for (int i = 0; i < items.length; i++) {
                assertThat(dq.getRecursive(i)).isEqualTo(dq.get(i));
                assertThat(dq.getRecursive(i)).isEqualTo(items[i]);
            }

            // 边界: 无效索引
            assertThat(dq.getRecursive(-1)).isEqualTo(dq.get(-1));
            assertThat(dq.getRecursive(5)).isEqualTo(dq.get(5));
            assertThat(dq.getRecursive(100)).isEqualTo(dq.get(100));
        }

        @Test
        @DisplayName("EXTREME: getRecursive on circular layout")
        public void getRecursiveCircularLayout() {
            Deque61B<Integer> dq = new ArrayDeque61B<>();
            // 制造环绕布局
            dq.addFirst(3);  // [3]=3, nf→2
            dq.addFirst(2);  // [2]=2, nf→1
            dq.addLast(4);   // [4]=4, nl→5
            dq.addLast(5);   // [5]=5, nl→6
            // 逻辑: [2,3,4,5], 物理: [ ][ ][2][3][4][5][ ][ ]

            assertThat(dq.getRecursive(0)).isEqualTo(2);
            assertThat(dq.getRecursive(1)).isEqualTo(3);
            assertThat(dq.getRecursive(2)).isEqualTo(4);
            assertThat(dq.getRecursive(3)).isEqualTo(5);
            assertThat(dq.getRecursive(4)).isNull();
        }


        // ========== 混合操作与一致性测试 ==========

        @Test
        @DisplayName("mixed operations maintain consistency")
        public void mixedOperationsConsistency() {
            Deque61B<Integer> dq = new ArrayDeque61B<>();
            java.util.Deque<Integer> reference = new java.util.ArrayDeque<>();

            // 随机混合操作序列
            int[] ops = {1, 2, 1, 3, 2, 4, 1, 5, 3, 2};  // 1=addFirst, 2=addLast, 3=removeFirst, 4=removeLast, 5=get
            int val = 0;
            for (int op : ops) {
                switch (op) {
                    case 1:  // addFirst
                        dq.addFirst(val);
                        reference.addFirst(val);
                        val++;
                        break;
                    case 2:  // addLast
                        dq.addLast(val);
                        reference.addLast(val);
                        val++;
                        break;
                    case 3:  // removeFirst
                        assertThat(dq.removeFirst()).isEqualTo(reference.pollFirst());
                        break;
                    case 4:  // removeLast
                        assertThat(dq.removeLast()).isEqualTo(reference.pollLast());
                        break;
                    case 5:  // get (only if non-empty)
                        if (!dq.isEmpty()) {
                            int idx = val % dq.size();
                            assertThat(dq.get(idx)).isEqualTo(reference.toArray(new Integer[0])[idx]);
                        }
                        break;
                }
                // 验证状态同步
                assertThat(dq.size()).isEqualTo(reference.size());
                assertThat(dq.isEmpty()).isEqualTo(reference.isEmpty());
                if (!dq.isEmpty()) {
                    assertThat(dq.toList()).containsExactlyElementsIn(reference).inOrder();
                }
            }
        }

        @Test
        @DisplayName("EXTREME: toList returns independent copy")
        public void toListReturnsIndependentCopy() {
            Deque61B<Integer> dq = new ArrayDeque61B<>();
            dq.addLast(1);
            dq.addLast(2);

            List<Integer> list1 = dq.toList();
            list1.add(999);  // 修改返回的列表

            List<Integer> list2 = dq.toList();
            assertThat(list2).doesNotContain(999);  // 原deque不应受影响
            assertThat(list2).containsExactly(1, 2).inOrder();
            assertThat(dq.size()).isEqualTo(2);  // size 不变
        }


        // ========== 泛型与 null 值测试 ==========

        @Test
        @DisplayName("EXTREME: deque allows null elements (if implementation permits)")
        public void nullElementHandling() {
            Deque61B<String> dq = new ArrayDeque61B<>();

            // 注意: 根据接口文档 "Assumes x is never null"，但实现应能处理
            // 如果实现不支持 null，此测试可能失败，可根据需求调整
            dq.addLast(null);
            dq.addFirst("A");
            dq.addLast("B");

            assertThat(dq.size()).isEqualTo(3);
            assertThat(dq.get(0)).isEqualTo("A");
            assertThat(dq.get(1)).isNull();  // 中间元素是 null
            assertThat(dq.get(2)).isEqualTo("B");
            assertThat(dq.toList()).containsExactly("A", null, "B").inOrder();

            assertThat(dq.removeFirst()).isEqualTo("A");
            assertThat(dq.removeFirst()).isNull();  // 移除 null 元素
            assertThat(dq.removeFirst()).isEqualTo("B");
        }

}
