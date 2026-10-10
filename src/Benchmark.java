import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;
public class Benchmark {
    static final int WARMUPS = 5;
    static final int RUNS = 15;
    static volatile int result;
    static int[] keys(int n, long seed) {
        int[] keys = new int[n];
        for (int i = 0; i < n; i++) keys[i] = i;
        Random random = new Random(seed);
        for (int i = n - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int temp = keys[i];
            keys[i] = keys[j];
            keys[j] = temp;
        }
        return keys;
    }
    static long[] measure(IntSet set, int[] insert, int[] search, int[] delete) {
        long[] times = new long[4];
        int count = 0;
        long start = System.nanoTime();
        for (int key : insert) if (set.insert(key)) count++;
        times[0] = System.nanoTime() - start;
        start = System.nanoTime();
        for (int key : search) if (set.contains(key)) count++;
        times[1] = System.nanoTime() - start;
        int found = 0;
        start = System.nanoTime();
        for (int key : search) if (set.contains(key + search.length)) found++;
        times[2] = System.nanoTime() - start;
        start = System.nanoTime();
        for (int key : delete) if (set.delete(key)) count++;
        times[3] = System.nanoTime() - start;
        if (count != insert.length * 3 || found != 0 || set.size() != 0)
            throw new AssertionError("Incorrect result");
        result = count;
        return times;
    }
    public static void main(String[] args) throws Exception {
        Files.createDirectories(Path.of("results"));
        try (PrintWriter raw = new PrintWriter("results/raw.csv");
             PrintWriter summary = new PrintWriter("results/summary.csv");
             PrintWriter environment = new PrintWriter("results/environment.txt")) {
            raw.println("scenario,n,run,structure,insert_ns,search_hit_ns,search_miss_ns,delete_ns");
            summary.println("scenario,n,structure,insert_ms,search_hit_ms,search_miss_ms,delete_ms");
            environment.println("Java: " + System.getProperty("java.version"));
            environment.println("OS: " + System.getProperty("os.name"));
            environment.println("Warmups: " + WARMUPS + ", runs: " + RUNS);
            environment.println("Times are medians for complete batches.");
            for (String scenario : new String[]{"random", "sorted"}) {
                for (int n : new int[]{100, 1000, 10000}) {
                    long[][][] samples = new long[2][4][RUNS];
                    for (int run = -WARMUPS; run < RUNS; run++) {
                        long seed = 12345L + n + run;
                        int[] insert = keys(n, seed);
                        if (scenario.equals("sorted")) Arrays.sort(insert);
                        int[] search = keys(n, seed + 100);
                        int[] delete = keys(n, seed + 200);
                        for (int order = 0; order < 2; order++) {
                            int type = Math.floorMod(run + order, 2);
                            IntSet set = type == 0 ? new BST() : new HashTable();
                            long[] times = measure(set, insert, search, delete);
                            if (run < 0) continue;
                            String name = type == 0 ? "BST" : "HashTable";
                            raw.printf("%s,%d,%d,%s,%d,%d,%d,%d%n",
                                    scenario, n, run, name, times[0], times[1], times[2], times[3]);
                            for (int operation = 0; operation < 4; operation++)
                                samples[type][operation][run] = times[operation];
                        }
                    }
                    for (int type = 0; type < 2; type++) {
                        summary.printf("%s,%d,%s", scenario, n, type == 0 ? "BST" : "HashTable");
                        for (int operation = 0; operation < 4; operation++) {
                            Arrays.sort(samples[type][operation]);
                            double median = samples[type][operation][RUNS / 2] / 1000000.0;
                            summary.printf(Locale.US, ",%.6f", median);
                        }
                        summary.println();
                    }
                    System.out.println("Completed " + scenario + ": " + n);
                }
            }
        }
    }
}
