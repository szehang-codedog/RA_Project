import java.util.List;

import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Sentence;
import edu.stanford.nlp.trees.Tree;

public class compareNLPTree<T> {
	public boolean compareTree_sameStructure(NLPTreeNode<T> expTreeNode, NLPTreeNode<T> actTreeNode) {
		try {
			if (expTreeNode.children.size() == 0 || actTreeNode.children.size() == 0) {
				System.out.println("Comparing " + expTreeNode.data + " VS " + actTreeNode.data);
				return (expTreeNode.data.equals(actTreeNode.data));
			}
			boolean match = true;
			for (int i = 0; i < expTreeNode.children.size(); i++) {
				NLPTreeNode exp_child = expTreeNode.children.get(i);
				NLPTreeNode act_child = actTreeNode.children.get(i);

				match = match && compareTree_sameStructure(exp_child, act_child);

			}
			return match;
		}catch (IndexOutOfBoundsException err){
			System.err.println(err);
			return false;
		}
	}
	
	/////test/////
	public static void main(String[] args) {
		/*
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
		
		
		compareNLPTree<String> compareTest = new compareNLPTree<String>(expNLPTree, actNLPTree);
		
		
		System.out.println("------------------------------------");
		System.out.println("expTree");
		expNLPTree.printWholeTree();
		System.out.println("actTree");
		actNLPTree.printWholeTree();
		
		System.out.println("------------------------------------");
		System.out.println(expNLPTreeList.get(7).data);
		System.out.println(actNLPTreeList.get(7).data);
		
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

		 */
		coreNLPOutput NLPTree = new coreNLPOutput();
		compareNLPTree compare = new compareNLPTree();
		List<NLPTreeNode<String>> test_exp = NLPTree.parseSentence("Input the interest rate");
		List<NLPTreeNode<String>> test_act = NLPTree.parseSentence("Input the interest rate");

		System.out.println(compare.compareTree_sameStructure(test_exp.get(0), test_act.get(0)));
	}
}
