public final class BST implements IntSet {
    private static final class Node {
        int key;
        Node left, right;
        Node(int key) { this.key = key; }
    }
    private Node root;
    private int size;
    public int size() { return size; }
    public boolean contains(int key) {
        Node n = root;
        while (n != null) {
            if (key == n.key) return true;
            n = key < n.key ? n.left : n.right;
        }
        return false;
    }
    public boolean insert(int key) {
        Node parent = null, n = root;
        while (n != null) {
            if (key == n.key) return false;
            parent = n;
            n = key < n.key ? n.left : n.right;
        }
        Node added = new Node(key);
        if (parent == null) root = added;
        else if (key < parent.key) parent.left = added;
        else parent.right = added;
        size++;
        return true;
    }
    public boolean delete(int key) {
        Node parent = null, n = root;
        while (n != null && n.key != key) {
            parent = n;
            n = key < n.key ? n.left : n.right;
        }
        if (n == null) return false;
        if (n.left != null && n.right != null) {
            Node successorParent = n, successor = n.right;
            while (successor.left != null) {
                successorParent = successor;
                successor = successor.left;
            }
            n.key = successor.key;
            parent = successorParent;
            n = successor;
        }
        Node child = n.left != null ? n.left : n.right;
        if (parent == null) root = child;
        else if (parent.left == n) parent.left = child;
        else parent.right = child;
        size--;
        return true;
    }
}
