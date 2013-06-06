package com.uikit.datastructures;

import java.util.Enumeration;

public final class LinkedList {

    private final Node head, tail;
    private int size;

    public LinkedList() {
        head = new Node(null, null, null);
        tail = new Node(head, null, null);
        head.nextNode = tail;
    }

    public LinkedList(Object[] elements) {
        this();
        if (elements == null) {
            throw new IllegalArgumentException();
        }

        for (int i = 0; i < elements.length; i++) {
            Object object = elements[i];
            addElement(object);
        }
    }

    public boolean isEmpty() {
        return head.nextNode == tail;
    }

    public void addElement(Object element) {
        tail.previousNode.nextNode = new Node(tail.previousNode, element, tail);
        tail.previousNode = tail.previousNode.nextNode;
        size++;
    }

    public boolean addElementAfter(Object pred, Object element) {
        Node predecessor = find(head, pred);
        if (predecessor != null) {
            predecessor.nextNode = new Node(predecessor, element, predecessor.nextNode);
            predecessor.nextNode.nextNode.previousNode = predecessor.nextNode;
            size++;
            return true;
        }
        return false;
    }

    public Object[] toArray() {

        Object[] elements = new Object[size];

        Enumeration enu;
        int i;
        for (enu = elements(), i = 0; enu.hasMoreElements(); i++) {
            elements[i] = enu.nextElement();
        }

        return elements;

    }
    Node lastNode;

    private Node find(Node next, Object element) {
        lastNode = null;
        Node temp = next.nextNode;
        if (temp == null || temp == head || temp == tail) {
            return head;
        } else {
            if (temp.content == element) {
                lastNode = temp;
            } else {
                find(temp, element);
            }
        }
        return lastNode;
    }

    public void addElementToFront(Object element) {
        head.nextNode = new Node(head, element, head.nextNode);
        head.nextNode.nextNode.previousNode = head.nextNode;
        size++;
    }

    public Object getFirstElement() {
        if (isEmpty()) {
            throw new ListEmptyException();
        }
        if (head.nextNode != null) {
            return head.nextNode.content;
        } else {
            return null;
        }
    }

    public Object getLastElement() {
        if (isEmpty()) {
            throw new ListEmptyException();
        }

        return tail.previousNode.content;
    }

    public boolean removeElement(Object element) {
        if (isEmpty()) {
            return false;
        }
        Node node = find(head, element);
        if (node == null) {
            return false;
        }

        node.previousNode.nextNode = node.nextNode;
        node.nextNode.previousNode = node.previousNode;
        Node.nulifyNode(node);
        size--;
        return true;
    }

    public final int size() {
        return size;
    }

    public boolean removeFirstElement() {
        if (isEmpty()) {
            return false;
        }

        Node temp = head.nextNode;
        head.nextNode = temp.nextNode;
        Node.nulifyNode(temp);
        size--;
        return true;
    }

    public boolean removeLastElement() {
        if (isEmpty()) {
            return false;
        }

        Node last = tail.previousNode;
        last.previousNode.nextNode = tail;
        Node.nulifyNode(last);
        size--;
        return true;
    }

    public boolean removeAllElements() {
        if (isEmpty()) {
            return false;
        }

        int count = size;
        for (int i = 0; i < count; i++) {
            removeFirstElement();
        }
        return true;
    }

    public Enumeration elements() {
        return new Enumeration() {

            Node temp = head;

            public boolean hasMoreElements() {
                if (temp != null) {
                    temp = temp.nextNode;
                    if (temp == tail || temp == null || temp.content == null) {
                        return false;
                    } else {
                        return true;
                    }
                }
                return false;

            }

            public Object nextElement() {
                return temp.content;
            }
        };
    }

    public Enumeration reverseElements() {
        return new Enumeration() {

            Node temp = tail;

            public boolean hasMoreElements() {
                temp = temp.previousNode;
                return (temp != head && temp.content != null);
            }

            public Object nextElement() {
                return temp.content;
            }
        };
    }

    public String toString() {
        StringBuffer toString = new StringBuffer();
        for (Enumeration enu = elements(); enu.hasMoreElements();) {
            toString.append("\n").append(enu.nextElement().toString());
        }
        return toString.toString();
    }

    public class ListEmptyException extends RuntimeException {
    }

    public int hashCode() {
        return size ^ 5;
    }

    public boolean equals(Object obj) {
        return ((obj instanceof LinkedList) && isElementsEqual((LinkedList) obj));
    }

    private boolean isElementsEqual(LinkedList oList) {
        if (oList.size != this.size) {
            return false;
        }

        Enumeration enu;
        Enumeration oEnu;

        for (enu = this.elements(), oEnu = oList.elements(); enu.hasMoreElements() && oEnu.hasMoreElements();) {
            if (!(enu.nextElement().equals(oEnu.nextElement()))) {
                return false;
            }
        }
        return true;
    }
}
