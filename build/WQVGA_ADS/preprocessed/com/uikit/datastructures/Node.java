package com.uikit.datastructures;

final class Node {

    Object content;
    Node nextNode;
    Node previousNode;

    Node(Node previous, Object content, Node next) {
        this.content = content;
        this.nextNode = next;
        this.previousNode = previous;
    }

    static void nulifyNode(Node node) {
        node.previousNode = null;
        node.nextNode = null;
        node = null;
    }
}
