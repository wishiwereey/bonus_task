public final class HashTable implements IntSet {
    private static final class Entry {
        final int key;
        Entry next;
        Entry(int key, Entry next) { this.key = key; this.next = next; }
    }
    private Entry[] buckets = new Entry[16];
    private int size;
    public int size() { return size; }
    private static int index(int key, int capacity) {
        int hash = key ^ (key >>> 16);
        return hash & (capacity - 1);
    }
    public boolean contains(int key) {
        for (Entry e = buckets[index(key, buckets.length)]; e != null; e = e.next)
            if (e.key == key) return true;
        return false;
    }
    public boolean insert(int key) {
        if (contains(key)) return false;
        if (size + 1 > buckets.length * 0.75) resize();
        int i = index(key, buckets.length);
        buckets[i] = new Entry(key, buckets[i]);
        size++;
        return true;
    }
    private void resize() {
        Entry[] expanded = new Entry[buckets.length * 2];
        for (Entry head : buckets) {
            while (head != null) {
                Entry next = head.next;
                int i = index(head.key, expanded.length);
                head.next = expanded[i];
                expanded[i] = head;
                head = next;
            }
        }
        buckets = expanded;
    }
    public boolean delete(int key) {
        int i = index(key, buckets.length);
        Entry previous = null;
        for (Entry e = buckets[i]; e != null; e = e.next) {
            if (e.key == key) {
                if (previous == null) buckets[i] = e.next;
                else previous.next = e.next;
                size--;
                return true;
            }
            previous = e;
        }
        return false;
    }
}
