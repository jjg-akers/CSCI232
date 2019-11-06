/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * Taken from algorithms, 4th edition by Sedgewick and Wayne
 */
//package binarysearchtree;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 *
 * @author k57h721
 */

public class BinarySearchTree<Key extends Comparable<Key>, Value> {
    private Node root;             // root of BST

    private class Node {
        private Key key;           // sorted by key
        private Value val;         // associated data
        private Node left, right;  // left and right subtrees
        private int size;          // number of nodes in subtree

        public Node(Key key, Value val, int size) {
            this.key = key;
            this.val = val;
            this.size = size;
        }
    }


    /**
     * Initializes an empty symbol table.
     */
    public BinarySearchTree() {
    }

    /**
     * Returns true if this symbol table is empty.
     *
     * @return {@code true} if this symbol table is empty; {@code false} otherwise
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Returns the number of key-value pairs in this symbol table.
     *
     * @return the number of key-value pairs in this symbol table
     */
    public int size() {
        return size(root);
    }

    // return number of key-value pairs in BST rooted at x
    private int size(Node x) {
        if (x == null) return 0;
        else return x.size;
    }

    /**
     * Does this symbol table contain the given key?
     *
     * @param key the key
     * @return {@code true} if this symbol table contains {@code key} and
     * {@code false} otherwise
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public boolean contains(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to contains() is null");
        return get(key) != null;
    }

    /**
     * Returns the value associated with the given key.
     *
     * @param key the key
     * @return the value associated with the given key if the key is in the symbol table
     * and {@code null} if the key is not in the symbol table
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public Value get(Key key) {
        return get(root, key);
    }

    private Value get(Node x, Key key) {
        if (key == null) throw new IllegalArgumentException("calls get() with a null key");
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if (cmp < 0) return get(x.left, key);
        else if (cmp > 0) return get(x.right, key);
        else return x.val;
    }

    /**
     * Inserts the specified key-value pair into the symbol table, overwriting the old
     * value with the new value if the symbol table already contains the specified key.
     * Deletes the specified key (and its associated value) from this symbol table
     * if the specified value is {@code null}.
     *
     * @param key the key
     * @param val the value
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public void put(Key key, Value val) {
        if (key == null) throw new IllegalArgumentException("calls put() with a null key");
        if (val == null) {
            delete(key);
            return;
        }
        root = put(root, key, val);
        assert check();
    }

    private Node put(Node x, Key key, Value val) {
        if (x == null) return new Node(key, val, 1);
        int cmp = key.compareTo(x.key);
        if (cmp < 0) x.left = put(x.left, key, val);
        else if (cmp > 0) x.right = put(x.right, key, val);
        else x.val = val;
        x.size = 1 + size(x.left) + size(x.right);
        return x;
    }


    /**
     * Removes the smallest key and associated value from the symbol table.
     *
     * @throws NoSuchElementException if the symbol table is empty
     */
    public void deleteMin() {
        if (isEmpty()) throw new NoSuchElementException("Symbol table underflow");
        root = deleteMin(root);
        assert check();
    }

    private Node deleteMin(Node x) {
        if (x.left == null) return x.right;
        x.left = deleteMin(x.left);
        x.size = size(x.left) + size(x.right) + 1;
        return x;
    }

    /**
     * Removes the largest key and associated value from the symbol table.
     *
     * @throws NoSuchElementException if the symbol table is empty
     */
    public void deleteMax() {
        if (isEmpty()) throw new NoSuchElementException("Symbol table underflow");
        root = deleteMax(root);
        assert check();
    }

    private Node deleteMax(Node x) {
        if (x.right == null) return x.left;
        x.right = deleteMax(x.right);
        x.size = size(x.left) + size(x.right) + 1;
        return x;
    }

    /**
     * Removes the specified key and its associated value from this symbol table
     * (if the key is in this symbol table).
     *
     * @param key the key
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public void delete(Key key) {
        if (key == null) throw new IllegalArgumentException("calls delete() with a null key");
        root = delete(root, key);
        assert check();
    }

    private Node delete(Node x, Key key) {
        if (x == null) return null;

        int cmp = key.compareTo(x.key);
        if (cmp < 0) x.left = delete(x.left, key);
        else if (cmp > 0) x.right = delete(x.right, key);
        else {
            if (x.right == null) return x.left;
            if (x.left == null) return x.right;
            Node t = x;
            x = min(t.right);
            x.right = deleteMin(t.right);
            x.left = t.left;
        }
        x.size = size(x.left) + size(x.right) + 1;
        return x;
    }


    /**
     * Returns the smallest key in the symbol table.
     *
     * @return the smallest key in the symbol table
     * @throws NoSuchElementException if the symbol table is empty
     */
    public Key min() {
        if (isEmpty()) throw new NoSuchElementException("calls min() with empty symbol table");
        return min(root).key;
    }

    private Node min(Node x) {
        if (x.left == null) return x;
        else return min(x.left);
    }

    /**
     * Returns the largest key in the symbol table.
     *
     * @return the largest key in the symbol table
     * @throws NoSuchElementException if the symbol table is empty
     */
    public Key max() {
        if (isEmpty()) throw new NoSuchElementException("calls max() with empty symbol table");
        return max(root).key;
    }

    private Node max(Node x) {
        if (x.right == null) return x;
        else return max(x.right);
    }

    /**
     * Returns the largest key in the symbol table less than or equal to {@code key}.
     *
     * @param key the key
     * @return the largest key in the symbol table less than or equal to {@code key}
     * @throws NoSuchElementException   if there is no such key
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public Key floor(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to floor() is null");
        if (isEmpty()) throw new NoSuchElementException("calls floor() with empty symbol table");
        Node x = floor(root, key);
        if (x == null) return null;
        else return x.key;
    }

    private Node floor(Node x, Key key) {
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if (cmp == 0) return x;
        if (cmp < 0) return floor(x.left, key);
        Node t = floor(x.right, key);
        if (t != null) return t;
        else return x;
    }

    public Key floor2(Key key) {
        return floor2(root, key, null);
    }

    private Key floor2(Node x, Key key, Key best) {
        if (x == null) return best;
        int cmp = key.compareTo(x.key);
        if (cmp < 0) return floor2(x.left, key, best);
        else if (cmp > 0) return floor2(x.right, key, x.key);
        else return x.key;
    }

    /**
     * Returns the smallest key in the symbol table greater than or equal to {@code key}.
     *
     * @param key the key
     * @return the smallest key in the symbol table greater than or equal to {@code key}
     * @throws NoSuchElementException   if there is no such key
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public Key ceiling(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to ceiling() is null");
        if (isEmpty()) throw new NoSuchElementException("calls ceiling() with empty symbol table");
        Node x = ceiling(root, key);
        if (x == null) return null;
        else return x.key;
    }

    private Node ceiling(Node x, Key key) {
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if (cmp == 0) return x;
        if (cmp < 0) {
            Node t = ceiling(x.left, key);
            if (t != null) return t;
            else return x;
        }
        return ceiling(x.right, key);
    }

    /**
     * Return the key in the symbol table whose rank is {@code k}.
     * This is the (k+1)st smallest key in the symbol table.
     *
     * @param k the order statistic
     * @return the key in the symbol table of rank {@code k}
     * @throws IllegalArgumentException unless {@code k} is between 0 and
     *                                  <em>n</em>–1
     */
    public Key select(int k) {
        if (k < 0 || k >= size()) {
            throw new IllegalArgumentException("argument to select() is invalid: " + k);
        }
        Node x = select(root, k);
        return x.key;
    }

    // Return key of rank k. 
    private Node select(Node x, int k) {
        if (x == null) return null;
        int t = size(x.left);
        if (t > k) return select(x.left, k);
        else if (t < k) return select(x.right, k - t - 1);
        else return x;
    }

    /**
     * Return the number of keys in the symbol table strictly less than {@code key}.
     *
     * @param key the key
     * @return the number of keys in the symbol table strictly less than {@code key}
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public int rank(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to rank() is null");
        return rank(key, root);
    }

    // Number of keys in the subtree less than key.
    private int rank(Key key, Node x) {
        if (x == null) return 0;
        int cmp = key.compareTo(x.key);
        if (cmp < 0) return rank(key, x.left);
        else if (cmp > 0) return 1 + size(x.left) + rank(key, x.right);
        else return size(x.left);
    }

    /**
     * Returns all keys in the symbol table as an {@code Iterable}.
     * To iterate over all of the keys in the symbol table named {@code st},
     * use the foreach notation: {@code for (Key key : st.keys())}.
     *
     * @return all keys in the symbol table
     */
    public Iterable<Key> keys() {
        if (isEmpty()) return new Queue<Key>();
        return keys(min(), max());
    }

    /**
     * Returns all keys in the symbol table in the given range,
     * as an {@code Iterable}.
     *
     * @param lo minimum endpoint
     * @param hi maximum endpoint
     * @return all keys in the symbol table between {@code lo}
     * (inclusive) and {@code hi} (inclusive)
     * @throws IllegalArgumentException if either {@code lo} or {@code hi}
     *                                  is {@code null}
     */
    public Iterable<Key> keys(Key lo, Key hi) {
        if (lo == null) throw new IllegalArgumentException("first argument to keys() is null");
        if (hi == null) throw new IllegalArgumentException("second argument to keys() is null");

        Queue<Key> queue = new Queue<Key>();
        keys(root, queue, lo, hi);
        return queue;
    }

    private void keys(Node x, Queue<Key> queue, Key lo, Key hi) {
        if (x == null) return;
        int cmplo = lo.compareTo(x.key);
        int cmphi = hi.compareTo(x.key);
        if (cmplo < 0) keys(x.left, queue, lo, hi);
        if (cmplo <= 0 && cmphi >= 0) queue.enqueue(x.key);
        if (cmphi > 0) keys(x.right, queue, lo, hi);
    }

    /**
     * Returns the number of keys in the symbol table in the given range.
     *
     * @param lo minimum endpoint
     * @param hi maximum endpoint
     * @return the number of keys in the symbol table between {@code lo}
     * (inclusive) and {@code hi} (inclusive)
     * @throws IllegalArgumentException if either {@code lo} or {@code hi}
     *                                  is {@code null}
     */
    public int size(Key lo, Key hi) {
        if (lo == null) throw new IllegalArgumentException("first argument to size() is null");
        if (hi == null) throw new IllegalArgumentException("second argument to size() is null");

        if (lo.compareTo(hi) > 0) return 0;
        if (contains(hi)) return rank(hi) - rank(lo) + 1;
        else return rank(hi) - rank(lo);
    }

    /**
     * Returns the height of the BST (for debugging).
     *
     * @return the height of the BST (a 1-node tree has height 0)
     */
    public int height() {
        return height(root);
    }

    private int height(Node x) {
        if (x == null) return -1;
        return 1 + Math.max(height(x.left), height(x.right));
    }

    /**
     * Returns the keys in the BST in level order (for debugging).
     *
     * @return the keys in the BST in level order traversal
     */
    public Iterable<Key> levelOrder() {
        Queue<Key> keys = new Queue<Key>();
        Queue<Node> queue = new Queue<Node>();
        queue.enqueue(root);
        while (!queue.isEmpty()) {
            Node x = queue.dequeue();
            if (x == null) continue;
            keys.enqueue(x.key);
            queue.enqueue(x.left);
            queue.enqueue(x.right);
        }
        return keys;
    }

    /*************************************************************************
     *  Check integrity of BST data structure.
     ***************************************************************************/
    private boolean check() {
        if (!isBST()) System.out.println("Not in symmetric order");
        if (!isSizeConsistent()) System.out.println("Subtree counts not consistent");
        if (!isRankConsistent()) System.out.println("Ranks not consistent");
        return isBST() && isSizeConsistent() && isRankConsistent();
    }

    // does this binary tree satisfy symmetric order?
    // Note: this test also ensures that data structure is a binary tree since order is strict
    private boolean isBST() {
        return isBST(root, null, null);
    }

    // is the tree rooted at x a BST with all keys strictly between min and max
    // (if min or max is null, treat as empty constraint)
    // Credit: Bob Dondero's elegant solution
    private boolean isBST(Node x, Key min, Key max) {
        if (x == null) return true;
        if (min != null && x.key.compareTo(min) <= 0) return false;
        if (max != null && x.key.compareTo(max) >= 0) return false;
        return isBST(x.left, min, x.key) && isBST(x.right, x.key, max);
    }

    // are the size fields correct?
    private boolean isSizeConsistent() {
        return isSizeConsistent(root);
    }

    private boolean isSizeConsistent(Node x) {
        if (x == null) return true;
        if (x.size != size(x.left) + size(x.right) + 1) return false;
        return isSizeConsistent(x.left) && isSizeConsistent(x.right);
    }

    // check that ranks are consistent
    private boolean isRankConsistent() {
        for (int i = 0; i < size(); i++)
            if (i != rank(select(i))) return false;
        for (Key key : keys())
            if (key.compareTo(select(rank(key))) != 0) return false;
        return true;
    }

    //************* TRAVERSAL METHODS ***************

    // Breadth First Traversal

    public void breadthFirstTraversal() {
        //create a queue
        Queue<Node> BFTQueue = new Queue();

        // add the root of BST as first element of Queue
        // check if BST is empty
        if (this.root == null) {
            System.out.println("BST is empty");
            return;
        } else {
            BFTQueue.enqueue(this.root);
        }

        //int BFTcounter = 0;

        // add, dequeue, and print nodes at each level
        while (!BFTQueue.isEmpty()) {
            //deque next node, add its children to the queue and then print it
            Node currentNode = BFTQueue.dequeue();

            if (currentNode.left != null) {
                BFTQueue.enqueue(currentNode.left);
            }
            if (currentNode.right != null) {
                BFTQueue.enqueue(currentNode.right);
            }

//            else {
//                System.out.println("all nodes printed");
//                return;
//            }

            System.out.println("" + currentNode.val + currentNode.key);
            //System.out.println(currentNode.val + currentNode.key);
            //BFTcounter++;
        }
        //System.out.println("all nodes printed");
        //System.out.println("total nodes printed" + BFTcounter);

    }

    // depthfirst traversal
    // use recuseive method to print keys in the left subtree, root, right subtree
    // inorder: left, root, right

    public void DFTrecurse(Node currentnode) {

        if (currentnode == null) {
            return;

        } else {
            // resurse down to last left node
            DFTrecurse(currentnode.left);
            System.out.println("" + currentnode.val + currentnode.key);

            //recurse down right side of tree
            DFTrecurse(currentnode.right);
        }
    }

    public void depthFirstTraversal() {
        // find left most node
        // add it to queue
        // add parent
        // add right child
        if (this.root == null) {
            System.out.println("BST is empty");
            return;
        } else {
            DFTrecurse(this.root);
        }
        //System.out.println("done with Depth FT ");


    }


    // ************ MAIN *************

    public static void main(String[] args) throws FileNotFoundException {

        String row = "";
        String rowStore = "";
        ArrayList<String> data = new ArrayList<>();
        ArrayList<String> temp = new ArrayList<>();
        ArrayList<ArrayList<String>> container = new ArrayList<ArrayList<String>>();
        ArrayList<CourseInfo> courses = new ArrayList<>();

        // Use a bufferedReader to read in the CSV
        // Then use scanner to parse the lines

        Scanner lineScanner = null;

        BufferedReader csvReader = new BufferedReader(new FileReader("classes.csv"));

        int count = 0;

        try {

            //read in all lines of data
            while ((row = csvReader.readLine()) != null) {
                //System.out.println(row);
                rowStore += row;

                // if the row is not a new line
                // add the row to the current string

                String key;
                String[] value = new String[4];

                data.add(row);

            }

            // close the csv
            csvReader.close();

        } catch (Exception e) {
            System.out.println("file not found");
        }

        //System.out.println("rowstor: " + rowStore);
        String next;

        lineScanner = new Scanner(rowStore);
        lineScanner.useDelimiter(",");

        while (lineScanner.hasNext()) {
            next = lineScanner.next();
            //System.out.println(next);
            if (next.length() != 0) {
                temp.add(next);
                if (next.length() > 4 && next.substring(0, 4).equalsIgnoreCase("CSCI")) {
                    //System.out.println(next);

                    // count up the numver of classes
                    count++;
                    container.add(new ArrayList<String>());

                }
            }
        }

        // setup container to track which course have already been added
        ArrayList<String> courseID = new ArrayList<>();
        int j = -1;

        for (int i = 0; i < temp.size(); i++) {
            if (temp.get(i).length() > 4 && temp.get(i).substring(0, 4).equalsIgnoreCase("CSCI")) {
                j++;
                // add the course number to a seperate list to keep track of sections
                //courseNumber.add

            }
            container.get(j).add(temp.get(i));
            //System.out.println(container.get(j).get(i));
        }

        // loop through the data and create new courseInfo objects
        for (int i = 0; i < container.size(); i++) {
            //System.out.println("container: " + i);
            // add course ID to an array to track number of sections
            if (!courseID.contains(container.get(i).get(0).substring(0, 8) + container.get(i).get(1))) {       // if this is a new course ID

                courseID.add(container.get(i).get(0).substring(0, 8) + container.get(i).get(1));

                // create a new course info object
                CourseInfo currentCourse = new CourseInfo(container.get(i).get(0).substring(0, 8));
                //System.out.println("course ID: " + currentCourse.ID);

                // set courseID with constructor
                // set sections to 1
                currentCourse.numberSections = 1;


                // now loop through the data and set all the courseInfo fileds
                for (int k = 1; k < container.get(i).size(); k++) {
                    //System.out.println("Value: " + container.get(i).get(k));
                    switch (k) {
                        case 1:
                            currentCourse.title = container.get(i).get(k);
                            //System.out.println("course title: " + currentCourse.title);
                            break;
                        case 3:
                            currentCourse.numberOfSeats = Integer.parseInt(container.get(i).get(k));
                            //System.out.println("seats: " + currentCourse.numberOfSeats);

                            break;
                        case 10:
                            currentCourse.key = container.get(i).get(k);
                            //System.out.println("course key: " + currentCourse.key);

                            break;
                        case 11:
                            currentCourse.key += "-" + container.get(i).get(k);
                            //System.out.println("course key: " + currentCourse.key);

                            break;
                    }

                }
                // The course info object is build, now add to courses list
                courses.add(currentCourse);

            } else {      // If the courseID already exists, update the sections and seats
                for (int k = 0; k < courses.size(); k++) {
                    // find the right courseInfo object

                    if (courses.get(k).ID.equalsIgnoreCase(container.get(i).get(0).substring(0, 8))) {
                        // update the fields
                        courses.get(k).numberSections += 1;
                        courses.get(k).numberOfSeats += Integer.parseInt(container.get(i).get(3));
                    }

                }
            }

        }


        // Now add add them to the BST
        BinarySearchTree myTree = new BinarySearchTree();

        for (int i = 0; i < courses.size(); i++) {
            String key = courses.get(i).key;
            String value = courses.get(i).ID + "\t\t" + courses.get(i).numberSections + "\t\t" + courses.get(i).numberOfSeats +
                    "\t\t";

            // add the corrent amount of table so the information will print correctly.
            if (courses.get(i).title.length() > 31) {
                value = value + courses.get(i).title + "\t";
            } else if (courses.get(i).title.length() > 29) {
                value = value + courses.get(i).title + "\t\t";
            } else if (courses.get(i).title.length() > 28) {
                value = value + courses.get(i).title + "\t\t";
            } else if (courses.get(i).title.length() > 24) {
                value = value + courses.get(i).title + "\t\t\t";
            } else if (courses.get(i).title.length() > 19) {
                value = value + courses.get(i).title + "\t\t\t\t";
            } else if (courses.get(i).title.length() > 18) {
                value = value + courses.get(i).title + "\t\t\t\t\t";
            } else if (courses.get(i).title.length() > 15) {
                value = value + courses.get(i).title + "\t\t\t\t\t";
            } else {
                value = value + courses.get(i).title + "\t\t\t\t\t\t\t";
            }
            // System.out.println("key: " + key);
            //System.out.println("value: " + value);
            //System.out.println(myTree.size());
            myTree.put(key, value);
            //System.out.println(myTree.size());

        }

        //System.out.println("Number of courses: " + courses.size());
        //System.out.println("tree size: " + myTree.size());


        // Print table for Breadth
        System.out.println("Breadth First: \n");
        System.out.println("ID\t\t\t\tSect\tSeats\tTitle\t\t\t\t\t\t\t\tWhen");
        myTree.breadthFirstTraversal();

        //print table for depth
        System.out.println("\nDepth First: \n");
        System.out.println("ID\t\t\t\tSect\tSeats\tTitle\t\t\t\t\t\t\t\tWhen");
        myTree.depthFirstTraversal();


    }


}


// CSV Alterations

    // removed 481 and 494-003 because they had TBA for time and place

    // removed the , in the title of CSCI291.
