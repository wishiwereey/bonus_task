## Java BST vs. Hash Table
This project compares a Binary Search Tree and a Hash Table. Both structures store integer keys.
I used the same keys for both structures.

## Project files
- BST.java contains the binary search tree.
- HashTable.java contains the hash table.
- IntSet.java is their common interface.
- StructureTest.java checks the code.
- Benchmark.java measures execution time.
- results contains the measured results.

## Complexity analysis
BST
- Insertion: best O(1), worst O(n).
- Search: best O(1), worst O(n).
- Deletion: best O(1), worst O(n).

Hash Table
- Insertion: best O(1), worst O(n).
- Search: best O(1), worst O(n).
- Deletion: best O(1), worst O(n).

## Conclusion
Hash Table was faster for search and deletion in these tests. BST insertion was faster for the two smaller random datasets. 
Sorted insertion made the BST much slower. Performance depends on tree shape, hash collisions, and resizing.
