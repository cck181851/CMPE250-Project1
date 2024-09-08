import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

class Node {
    int key;
    Node left, right, parent;
    int height;

    public Node(int key) {
        this.key = key;
        this.left = this.right = this.parent = null;
        this.height = 1; 
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getBalance() {
        return getHeight(left) - getHeight(right);
    }

    private int getHeight(Node node) {
        return node == null ? 0 : node.height;
    }

    public void updateHeight() {
        this.height = 1 + Math.max(getHeight(left), getHeight(right));
    }
}

public class Tree {
    private Node root;

    public Tree(int[] initialList) {
        Set<Integer> uniqueValues = new TreeSet<>();
        for (int value : initialList) {
            uniqueValues.add(value);
        }
        for (int value : uniqueValues) {
            insert(new Node(value));
        }
    }

    public void insert(Node nodeToInsert) {
        if (root == null) {
            root = nodeToInsert;
            return;
        }

        Node current = root;
        Node parent = null;
        while (true) {
            parent = current;
            if (nodeToInsert.getKey() < current.getKey()) {
                current = current.getLeft();
                if (current == null) {
                    parent.setLeft(nodeToInsert);
                    break;
                }
            } else {
                current = current.getRight();
                if (current == null) {
                    parent.setRight(nodeToInsert);
                    break;
                }
            }
        }
        nodeToInsert.setParent(parent);
        rebalance(parent);
    }

    public Node search(int key) {
        Node current = root;
        while (current != null) {
            if (key == current.getKey()) {
                return current;
            } else if (key < current.getKey()) {
                current = current.getLeft();
            } else {
                current = current.getRight();
            }
        }
        return null;
    }

    public boolean delete(int key) {
        Node node = search(key);
        if (node == null) {
            return false;
        }
        return deleteNode(node);
    }

    private boolean deleteNode(Node node) {
        Node parent = node.getParent();
        if (node.getLeft() != null && node.getRight() != null) {
            Node successor = getMinimum(node.getRight());
            node.setKey(successor.getKey());
            deleteNode(successor);
        } else if (node == root) {
            root = (node.getLeft() != null) ? node.getLeft() : node.getRight();
            if (root != null) {
                root.setParent(null);
            }
        } else {
            Node child = (node.getLeft() != null) ? node.getLeft() : node.getRight();
            if (node == node.getParent().getLeft()) {
                node.getParent().setLeft(child);
            } else {
                node.getParent().setRight(child);
            }
            if (child != null) {
                child.setParent(node.getParent());
            }
        }
        Node ancestor = parent;
        while (ancestor != null) {
            rebalance(ancestor);
            ancestor = ancestor.getParent();
        }
        return true;
    }

    private Node getMinimum(Node node) {
        while (node.getLeft() != null) {
            node = node.getLeft();
        }
        return node;
    }

    private void rebalance(Node node) {
        if (node == null) return;
        node.updateHeight();

        int balance = node.getBalance();
        if (balance > 1) {
            if (node.getLeft().getBalance() < 0) {
                rotateLeft(node.getLeft());
            }
            rotateRight(node);
        } else if (balance < -1) {
            if (node.getRight().getBalance() > 0) {
                rotateRight(node.getRight());
            }
            rotateLeft(node);
        }
    }

    private void rotateLeft(Node node) {
        Node newRoot = node.getRight();
        node.setRight(newRoot.getLeft());
        if (newRoot.getLeft() != null) {
            newRoot.getLeft().setParent(node);
        }
        newRoot.setParent(node.getParent());
        if (node.getParent() == null) {
            root = newRoot;
        } else if (node == node.getParent().getLeft()) {
            node.getParent().setLeft(newRoot);
        } else {
            node.getParent().setRight(newRoot);
        }
        newRoot.setLeft(node);
        node.setParent(newRoot);
        node.updateHeight();
        newRoot.updateHeight();
    }

    private void rotateRight(Node node) {
        Node newRoot = node.getLeft();
        node.setLeft(newRoot.getRight());
        if (newRoot.getRight() != null) {
            newRoot.getRight().setParent(node);
        }
        newRoot.setParent(node.getParent());
        if (node.getParent() == null) {
            root = newRoot;
        } else if (node == node.getParent().getRight()) {
            node.getParent().setRight(newRoot);
        } else {
            node.getParent().setLeft(newRoot);
        }
        newRoot.setRight(node);
        node.setParent(newRoot);
        node.updateHeight();
        newRoot.updateHeight();
    }
}
