import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.trees.Tree;

public class buildNLPTreeNode {
    public buildNLPTreeNode() {
    }

    int nodeID = 0;
    NLPTreeNode<String> root;
    List<NLPTreeNode<String>> NLPTree = new ArrayList<>();

    public List<NLPTreeNode<String>> buildTree(Tree t, NLPTreeNode<String> pNode) { // pNode Parent Node //cNode Child Node

        if (!t.isEmpty()) {
            if (!t.isLeaf()) {
                if (t.value().compareTo("ROOT") == 0) {
                    this.root = new NLPTreeNode<String>(t.value(), "tag", nodeID++);
                    NLPTree.add(this.root);
                    pNode = this.root;
                    //System.err.println("pass root node" + " ID:" + (nodeID-1));
                    System.err.println(root.data + " " + root.type);

                    for (Tree childNode : t.children()) {
                        buildTree(childNode, this.root);
                    }

                } else {

                    NLPTreeNode<String> cNode = new NLPTreeNode<String>(t.value(), "tag", nodeID++);
                    NLPTree.add(cNode);
                    pNode.children.add(cNode);
                    cNode.parent = pNode;
                    pNode = cNode;
                    //System.err.println("pass other node" + " ID:" + (nodeID-1));
                    System.err.println(cNode.data + " " + cNode.type);
                    for (Tree childNode : t.children()) {
                        buildTree(childNode, pNode);
                    }
                }
            } else if (t.isLeaf()) {
                NLPTreeNode<String> cNode = new NLPTreeNode<String>(t.value(), "data", nodeID++);
                NLPTree.add(cNode);
                pNode.children.add(cNode);
                cNode.parent = pNode;
                //System.err.println("pass leaf node" + " ID:" + (nodeID-1));
                System.err.println(cNode.data + " " + cNode.type);
            }
        }

        return NLPTree;
    }
}
