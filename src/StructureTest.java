import java.util.HashSet;
import java.util.Random;
public final class StructureTest {
    private static void check(boolean condition) {
        if (!condition) throw new AssertionError("Incorrect result");
    }
    private static void test(IntSet set) {
        HashSet<Integer> reference = new HashSet<>();
        int[] keys = {5, 3, 8, 2, 4, 6, 9, 7, -1, Integer.MIN_VALUE, Integer.MAX_VALUE};
        for (int key : keys) check(set.insert(key) == reference.add(key));
        check(!set.insert(5));
        // Root with two children, leaf, one-child node, missing key, extremes.
        for (int key : new int[]{5, 2, 6, 42, Integer.MIN_VALUE, Integer.MAX_VALUE})
            check(set.delete(key) == reference.remove(key));
        Random random = new Random(12345);
        for (int i = 0; i < 100000; i++) {
            int key = random.nextInt(2001) - 1000;
            switch (random.nextInt(3)) {
                case 0: check(set.insert(key) == reference.add(key)); break;
                case 1: check(set.delete(key) == reference.remove(key)); break;
                default: check(set.contains(key) == reference.contains(key));
            }
            check(set.size() == reference.size());
        }
        for (int key : reference) check(set.contains(key));
        for (int key : reference) check(set.delete(key));
        check(set.size() == 0);
    }
    public static void main(String[] args) {
        test(new BST());
        test(new HashTable());
        IntSet collisions = new HashTable();
        for (int i = 0; i < 10; i++) check(collisions.insert(i * 16));
        for (int i = 0; i < 10; i++) check(collisions.contains(i * 16));
        for (int i = 0; i < 10; i++) check(collisions.delete(i * 16));
        BST deep = new BST();
        for (int i = 0; i < 10000; i++) deep.insert(i);
        check(deep.contains(9999));
        for (int i = 9999; i >= 0; i--) check(deep.delete(i));
        System.out.println("All correctness tests passed.");
    }
}
