package bomb;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Password {
    public static void main(String[] args) {
        Random r = new Random(1337);
        Set<Integer> numbers = new HashSet<>();

        while (numbers.size() < 100000) {
            numbers.add(r.nextInt());
        }

        int i = 0;
        for (int n : numbers) {
            if (i == 1337) {
                System.out.println(n);
                break;
            }
            i++;
        }
    }
}

