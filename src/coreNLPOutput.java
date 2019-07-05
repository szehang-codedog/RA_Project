//https://stanfordnlp.github.io/CoreNLP/simple.html
import java.util.Arrays;
import java.util.List;

import edu.stanford.nlp.simple.*;
import edu.stanford.nlp.trees.Tree;

public class coreNLPOutput {

	public List<NLPTreeNode<String>> parseSentence(String sentence) {
		Document doc = new Document(sentence);
		Sentence sent = doc.sentence(0);
		Tree t = sent.parse();
		buildNLPTreeNode a = new buildNLPTreeNode();
		List<NLPTreeNode<String>> NLPTree = a.buildTree(t, null);

		return NLPTree; //return whole NLP Tree for further implementation.

	}

	public String findSubtreeByPath(List<NLPTreeNode<String>> NLPTree, int[] path){
		/*
		* This method will find out all leafNodes of the targeted node indicated in path[].
		* And finally return a String for further implement.
		 */
		try{
			String output = "";
			NLPTreeNode target = NLPTree.get(0);
			for (int i = 0; i < path.length; i++) {
				List<NLPTreeNode<String>> childrenList = target.children;
				target = childrenList.get(path[i]-1);
				//System.out.println(target.data);
			}
			List<NLPTreeNode<String>> leafNodes = target.leafNodeList();
			for (int i = 0; i < leafNodes.size(); i++) {
				output = output + leafNodes.get(i).data+ " ";
			}
			//System.out.println(output);
			return output.trim();
		}catch(IndexOutOfBoundsException err){
			System.err.println("Index out of boundary. Please check whether the path is able to browse the tree with no error");
			return null;
		}
	}

	public void printTree(List<NLPTreeNode<String>> NLPTree) {
		System.out.println("Tree Size = " + NLPTree.size());
		for(int i = 0; i<NLPTree.size(); i++) {
			System.out.println(NLPTree.get(i).data);
		}
	}
	
    public static void main(String[] args) {
        coreNLPOutput NLPTree = new coreNLPOutput();
		List<NLPTreeNode<String>> test = NLPTree.parseSentence("Input the interest rate");

    }
}