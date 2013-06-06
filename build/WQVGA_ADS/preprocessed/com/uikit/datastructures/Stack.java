package com.uikit.datastructures;

import java.util.EmptyStackException;
import java.util.Enumeration;

public final class Stack {

    private final Node head, tail;
    private int size;

    public Stack() {
        head = new Node(null, null, null);
        tail = new Node(head, null, null);
        head.nextNode = tail;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public Object peek() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        return head.nextNode.content;
    }

    public Object pop() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }

        Node temp = head.nextNode;
        head.nextNode = temp.nextNode;
        temp.nextNode.previousNode = head;

        Object o = temp.content;
        Node.nulifyNode(temp);
        size--;
        return o;
    }

    public void push(Object item) {
        head.nextNode.previousNode = new Node(head, item, head.nextNode);
        head.nextNode = head.nextNode.previousNode;
        size++;
    }

    public int size() {
        return size;
    }

    public String toString() {
        StringBuffer nodes = new StringBuffer();
        for (Enumeration enu = new Enumeration() {

            Node temp = head;

            public boolean hasMoreElements() {
                return temp.nextNode != tail;
            }

            public Object nextElement() {
                temp = temp.nextNode;
                return temp.content;
            }
        }; enu.hasMoreElements();) {
            nodes.append("\n").append(enu.nextElement().toString());
        }

        return nodes.toString();
    }
}
