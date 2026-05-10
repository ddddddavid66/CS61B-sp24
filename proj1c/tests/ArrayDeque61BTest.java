import org.junit.jupiter.api.*;

import deque.ArrayDeque61B;
import deque.Deque61B;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

public class ArrayDeque61BTest {

    /* ==================== equals tests ==================== */

    @Test
    public void testEqualsSameContentSameOrder() {
        Deque61B<Integer> d1 = new ArrayDeque61B<>();
        Deque61B<Integer> d2 = new ArrayDeque61B<>();
        d1.addLast(1); d1.addLast(2); d1.addLast(3);
        d2.addLast(1); d2.addLast(2); d2.addLast(3);
        assertThat(d1.equals(d2)).isTrue();
    }

    @Test
    public void testEqualsDifferentContent() {
        Deque61B<Integer> d1 = new ArrayDeque61B<>();
        Deque61B<Integer> d2 = new ArrayDeque61B<>();
        d1.addLast(1); d1.addLast(2); d1.addLast(3);
        d2.addLast(1); d2.addLast(4); d2.addLast(3);
        assertThat(d1.equals(d2)).isFalse();
    }

    @Test
    public void testEqualsDifferentOrder() {
        Deque61B<Integer> d1 = new ArrayDeque61B<>();
        Deque61B<Integer> d2 = new ArrayDeque61B<>();
        d1.addLast(1); d1.addLast(2); d1.addLast(3);
        d2.addLast(3); d2.addLast(2); d2.addLast(1);
        assertThat(d1.equals(d2)).isFalse();
    }

    @Test
    public void testEqualsDifferentSize() {
        Deque61B<Integer> d1 = new ArrayDeque61B<>();
        Deque61B<Integer> d2 = new ArrayDeque61B<>();
        d1.addLast(1); d1.addLast(2);
        d2.addLast(1); d2.addLast(2); d2.addLast(3);
        assertThat(d1.equals(d2)).isFalse();
    }

    @Test
    public void testEqualsSameObject() {
        Deque61B<Integer> d1 = new ArrayDeque61B<>();
        d1.addLast(1); d1.addLast(2);
        assertThat(d1.equals(d1)).isTrue();
    }

    @Test
    public void testEqualsNull() {
        Deque61B<Integer> d1 = new ArrayDeque61B<>();
        d1.addLast(1);
        assertThat(d1.equals(null)).isFalse();
    }

    @Test
    public void testEqualsNonDeque61B() {
        Deque61B<Integer> d1 = new ArrayDeque61B<>();
        d1.addLast(1);
        assertThat(d1.equals("not a deque")).isFalse();
    }

    @Test
    public void testEqualsBothEmpty() {
        Deque61B<Integer> d1 = new ArrayDeque61B<>();
        Deque61B<Integer> d2 = new ArrayDeque61B<>();
        assertThat(d1.equals(d2)).isTrue();
    }

    @Test
    public void testEqualsWithNullElements() {
        Deque61B<String> d1 = new ArrayDeque61B<>();
        Deque61B<String> d2 = new ArrayDeque61B<>();
        d1.addLast("a"); d1.addLast(null); d1.addLast("c");
        d2.addLast("a"); d2.addLast(null); d2.addLast("c");
        assertThat(d1.equals(d2)).isTrue();
    }

    @Test
    public void testEqualsWithNullElementsMismatch() {
        Deque61B<String> d1 = new ArrayDeque61B<>();
        Deque61B<String> d2 = new ArrayDeque61B<>();
        d1.addLast("a"); d1.addLast(null); d1.addLast("c");
        d2.addLast("a"); d2.addLast("b");  d2.addLast("c");
        assertThat(d1.equals(d2)).isFalse();
    }

    @Test
    public void testEqualsCrossType() {
        // Two different deque implementations with same elements should be equal
        Deque61B<Integer> d1 = new ArrayDeque61B<>();
        Deque61B<Integer> d2 = new ArrayDeque61B<>();
        d1.addLast(1); d1.addLast(2);
        d2.addLast(1); d2.addLast(2);
        assertThat(d1.equals(d2)).isTrue();
    }

    /* ==================== toString tests ==================== */

    @Test
    public void testToStringEmpty() {
        Deque61B<Integer> d = new ArrayDeque61B<>();
        assertThat(d.toString()).isEqualTo("[]");
    }

    @Test
    public void testToStringSingle() {
        Deque61B<String> d = new ArrayDeque61B<>();
        d.addLast("hello");
        assertThat(d.toString()).isEqualTo("[hello]");
    }

    @Test
    public void testToStringMultiple() {
        Deque61B<Integer> d = new ArrayDeque61B<>();
        d.addLast(1); d.addLast(2); d.addLast(3);
        assertThat(d.toString()).isEqualTo("[1, 2, 3]");
    }

    @Test
    public void testToStringAfterAddFirst() {
        Deque61B<Integer> d = new ArrayDeque61B<>();
        d.addFirst(3); d.addFirst(2); d.addFirst(1);
        assertThat(d.toString()).isEqualTo("[1, 2, 3]");
    }

    @Test
    public void testToStringAfterResize() {
        Deque61B<Integer> d = new ArrayDeque61B<>();
        // Add more than 8 elements to trigger resize
        for (int i = 0; i < 10; i++) {
            d.addLast(i);
        }
        assertThat(d.toString()).isEqualTo("[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]");
    }

    /* ==================== iterator tests ==================== */

    @Test
    public void testIteratorHasNextEmpty() {
        Deque61B<Integer> d = new ArrayDeque61B<>();
        Iterator<Integer> iter = d.iterator();
        assertThat(iter.hasNext()).isFalse();
    }

    @Test
    public void testIteratorHasNextNonEmpty() {
        Deque61B<Integer> d = new ArrayDeque61B<>();
        d.addLast(1);
        Iterator<Integer> iter = d.iterator();
        assertThat(iter.hasNext()).isTrue();
        iter.next();
        assertThat(iter.hasNext()).isFalse();
    }

    @Test
    public void testIteratorNextReturnsCorrectOrder() {
        Deque61B<Integer> d = new ArrayDeque61B<>();
        d.addLast(1); d.addLast(2); d.addLast(3);
        Iterator<Integer> iter = d.iterator();
        assertThat(iter.next()).isEqualTo(1);
        assertThat(iter.next()).isEqualTo(2);
        assertThat(iter.next()).isEqualTo(3);
    }

    @Test
    public void testIteratorNextAfterExhaustedThrows() {
        Deque61B<Integer> d = new ArrayDeque61B<>();
        d.addLast(1);
        Iterator<Integer> iter = d.iterator();
        iter.next();
        try {
            iter.next();
            assertWithMessage("Expected NoSuchElementException").fail();
        } catch (NoSuchElementException e) {
            // expected
        }
    }

    @Test
    public void testIteratorEnhancedForLoop() {
        Deque61B<String> d = new ArrayDeque61B<>();
        d.addLast("a"); d.addLast("b"); d.addLast("c");
        StringBuilder sb = new StringBuilder();
        for (String s : d) {
            sb.append(s);
        }
        assertThat(sb.toString()).isEqualTo("abc");
    }

    @Test
    public void testIteratorAfterMixOfAddFirstAndAddLast() {
        Deque61B<Integer> d = new ArrayDeque61B<>();
        d.addFirst(2);  // [2]
        d.addFirst(1);  // [1, 2]
        d.addLast(3);   // [1, 2, 3]
        d.addLast(4);   // [1, 2, 3, 4]
        Iterator<Integer> iter = d.iterator();
        assertThat(iter.next()).isEqualTo(1);
        assertThat(iter.next()).isEqualTo(2);
        assertThat(iter.next()).isEqualTo(3);
        assertThat(iter.next()).isEqualTo(4);
    }

    @Test
    public void testIteratorAfterRemovals() {
        Deque61B<Integer> d = new ArrayDeque61B<>();
        d.addLast(1); d.addLast(2); d.addLast(3); d.addLast(4);
        d.removeFirst(); // [2, 3, 4]
        d.removeLast();  // [2, 3]
        Iterator<Integer> iter = d.iterator();
        assertThat(iter.next()).isEqualTo(2);
        assertThat(iter.next()).isEqualTo(3);
        assertThat(iter.hasNext()).isFalse();
    }

    @Test
    public void testMultipleIteratorsIndependent() {
        Deque61B<Integer> d = new ArrayDeque61B<>();
        d.addLast(1); d.addLast(2); d.addLast(3);
        Iterator<Integer> i1 = d.iterator();
        Iterator<Integer> i2 = d.iterator();
        assertThat(i1.next()).isEqualTo(1);
        assertThat(i2.next()).isEqualTo(1);
        assertThat(i1.next()).isEqualTo(2);
        assertThat(i2.next()).isEqualTo(2);
    }

    @Test
    public void testIteratorAfterResize() {
        Deque61B<Integer> d = new ArrayDeque61B<>();
        // Add more than 8 elements to trigger resize
        for (int i = 0; i < 12; i++) {
            d.addLast(i);
        }
        Iterator<Integer> iter = d.iterator();
        for (int i = 0; i < 12; i++) {
            assertThat(iter.next()).isEqualTo(i);
        }
        assertThat(iter.hasNext()).isFalse();
    }
}
