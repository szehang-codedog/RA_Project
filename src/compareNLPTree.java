import java.util.List;

import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Sentence;
import edu.stanford.nlp.trees.Tree;

public class compareNLPTree<T> {
	public compareNLPTree(NLPTreeNode<T> expNLPTree, NLPTreeNode<T> actNLPTree, List<T> rules) {
	}

	/////test/////
	public static void main(String[] args) {
		Document expDoc = new Document("I am Apple.");
		Sentence expSent = expDoc.sentence(0);
		Tree expTree = expSent.parse();
		buildNLPTreeNode a1 = new buildNLPTreeNode();
		List<NLPTreeNode<String>> expNLPTreeList = a1.buildTree(expTree, null);
		NLPTreeNode<String> expNLPTree = expNLPTreeList.get(0);
		
		Document actDoc = new Document("I am Samsung.");
		Sentence actSent = actDoc.sentence(0);
		Tree actTree = actSent.parse();
		buildNLPTreeNode a2 = new buildNLPTreeNode();
		List<NLPTreeNode<String>> actNLPTreeList = a2.buildTree(actTree, null);
		NLPTreeNode<String> actNLPTree = actNLPTreeList.get(0);
		
		
		compareNLPTree<String> compareTest = new compareNLPTree<String>(expNLPTree, actNLPTree, null);
		
		
		System.out.println("------------------------------------");
		System.out.println("expTree");
		expNLPTree.printWholeTree();
		System.out.println("actTree");
		actNLPTree.printWholeTree();
		
		System.out.println("------------------------------------");
		System.out.println(expNLPTreeList.get(7).data);
		System.out.println(actNLPTreeList.get(7).data);
		System.out.println("Root = " + expNLPTreeList.get(7).getRoot().data);
		
		PhraseSimilarity ps = new PhraseSimilarity();
		double two_phrase_sim = ps.phraseSim(expNLPTreeList.get(10).data, actNLPTreeList.get(10).data);
		System.out.println("sim = " + two_phrase_sim);
		if (two_phrase_sim >= 0.6) // to be changed
			System.out.println("match ture");
		else
			System.out.println("match false");
		
		System.out.println("------------------------------------");
		for(NLPTreeNode<String> node :expNLPTree.leafNodeList()) {
			System.out.println(node.data);
		}
		for(NLPTreeNode<String> node :actNLPTree.leafNodeList()) {
			System.out.println(node.data);
		}
	}
}
