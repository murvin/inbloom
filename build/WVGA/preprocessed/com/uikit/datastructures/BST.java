package com.uikit.datastructures;

import java.util.Enumeration;

public class BST {

    private BSTNode rootNode;
    private int size;

    public BST() {
        rootNode = new BSTNode();
    }

    public boolean isEmpty() {
        return rootNode == null || rootNode.content == null;
    }

    public void insert(IComparable comparable) {

        if (comparable == null) {
            throw new IllegalArgumentException();
        }

        if (isEmpty()) {
            rootNode.content = comparable;
            size++;
        } else {
            _insert(rootNode, comparable);
        }
    }

    public boolean delete(IComparable comparable) {
        BSTNode node = null;
        if ((node = _search(rootNode, comparable)) != null) {
            if (node.rightNode == null ^ node.leftNode == null) { // One child
                if (node.leftNode == null) {
                    if (node == rootNode) {
                        rootNode = node.rightNode;
                    } else {
                        if (node == node.parentNode.leftNode) {
                            node.parentNode.leftNode = node.rightNode;
                        } else {
                            node.parentNode.rightNode = node.rightNode;
                        }
                        node = null;
                    }
                } else {
                    if (node == rootNode) {
                        rootNode = node.leftNode;
                    } else {
                        if (node == node.parentNode.leftNode) {
                            node.parentNode.leftNode = node.leftNode;
                        } else {
                            node.parentNode.rightNode = node.leftNode;
                        }
                        node = null;
                    }
                }

                size--;
                return true;
            } else {
                if (node.rightNode == null) { // A leaf
                    if (node == rootNode) {
                        rootNode = null;
                    } else {
                        if (node == node.parentNode.leftNode) {
                            node.parentNode.leftNode = null;
                        } else {
                            node.parentNode.rightNode = null;
                        }
                        node = null;
                    }
                    size--;
                    return true;
                } else { // 2 children
                    BSTNode temp = leftmost(node.rightNode);
                    node.content = temp.content;
                    temp = null;
                    size--;
                    return true;
                }
            }

        }
        return false;
    }

    /**
     * Method to shed all leaves and branches of the tree
     */
    public void shed() {
        for (Enumeration enu = flatten().elements(); enu.hasMoreElements();) {
            delete((IComparable) enu.nextElement());
        }
    }

    /**
     * Method to search the tree for a particular element.
     *
     * @param comparable        The <code>IComparable</code> to search for.
     * @return                  true if the element exist in the tree.
     */
    public IComparable search(IComparable comparable) {
        if (isEmpty()) {
            return null;
        }

        if (comparable == null) {
            throw new IllegalArgumentException();
        }

        BSTNode node = _search(rootNode, comparable);

        if (node != null) {
            return node.content;
        }

        return null;
    }

    public LinkedList flatten() {

        LinkedList elements = new LinkedList();
        _traverse(rootNode, elements);

        return elements;
    }

    public int size() {
        return size;
    }

    public IComparable getMinElement() {
        if (isEmpty()) {
            throw new IllegalStateException();
        }

        return leftmost(rootNode).content;
    }

    public IComparable getMaxElement() {
        if (isEmpty()) {
            throw new IllegalStateException();
        }

        return rightmost(rootNode).content;
    }

    private void _traverse(BSTNode node, LinkedList nodes) {
        if (node.leftNode != null) {
            _traverse(node.leftNode, nodes);
        }

        nodes.addElement(node.content);

        if (node.rightNode != null) {
            _traverse(node.rightNode, nodes);
        }
    }

    private BSTNode _search(BSTNode node, IComparable comparable) {

        if (node == null) {
            return null;
        }
        if (node.content.compareTo(comparable) == 0) {
            return node;
        } else if (node.content.compareTo(comparable) < 0) {
            return _search(node.rightNode, comparable);
        } else {
            return _search(node.leftNode, comparable);
        }

    }

    private void _insert(BSTNode node, IComparable comparable) {
        if (node.content.compareTo(comparable) == 0) {
            throw new IllegalStateException();
        } else if (node.content.compareTo(comparable) < 0) {
            if (node.rightNode == null) {
                node.rightNode = new BSTNode(node, null, comparable, null);
                size++;
            } else {
                _insert(node.rightNode, comparable);
            }
        } else {
            if (node.leftNode == null) {
                node.leftNode = new BSTNode(node, null, comparable, null);
                size++;
            } else {
                _insert(node.leftNode, comparable);
            }
        }

    }

    private BSTNode leftmost(BSTNode node) {
        if (node.leftNode == null) {
            return node;
        } else {
            return leftmost(node.leftNode);
        }
    }

    private BSTNode rightmost(BSTNode node) {
        if (node.rightNode == null) {
            return node;
        } else {
            return rightmost(node.rightNode);
        }
    }

    final class BSTNode {

        IComparable content;
        BSTNode rightNode;
        BSTNode leftNode;
        BSTNode parentNode;

        public BSTNode() {
        }

        BSTNode(BSTNode parent, BSTNode left, IComparable content, BSTNode right) {
            this.parentNode = parent;
            this.leftNode = left;
            this.content = content;
            this.rightNode = right;
        }
    }
}
