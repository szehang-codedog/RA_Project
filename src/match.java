import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Sentence;
import edu.stanford.nlp.trees.Tree;

public class match<T> {
	
	public boolean match(NLPTreeNode<T> u_exp, NLPTreeNode<T> u_act) {
		/*
		u_exp = root of current subtree of expTree to be matched
   		u_act = similar for actTree 
		*/
		
		if(u_exp.isLeaf()) {
			System.out.println("Compare | " + u_exp.data + " : " + u_act.data + " |   -> " + compare(u_exp, u_act));//DEBUG
			return compare(u_exp, u_act);
		}
		
		for(NLPTreeNode<T> v_exp : u_exp.children) {
			NLPTreeNode<T> v_act = u_act.children.get(v_exp.childID);//v_act = corresponding child of u_act
			if(!match(v_exp, v_act)) {
				return false;
			}
		}
		return true;	
	}
	
	public boolean match(NLPTreeNode<T> u_exp, NLPTreeNode<T> next_act, NLPTreeNode<T> last_act) {
		/*
		next_act is the index to the first not yet used token
   		last_act is the index of the last token in the scope
		*/
		
		if(u_exp.isLeaf()) {//base case
			if(isIgnore(u_exp)) {
				return true;
			}
			while (next_act.tokenID <= last_act.tokenID && isIgnore(next_act)) { //should use <
				next_act = next_act.nextToken;
			}
			if(next_act.tokenID > last_act.tokenID) {
				return false;
			}
			next_act = next_act.nextToken;
			System.out.println("Compare | " + u_exp.data + " : " + next_act.previousToken.data + " |   -> " + compare(u_exp, next_act.previousToken));//DEBUG
			return compare(u_exp, next_act.previousToken);
		} else {//recursive case
			for(NLPTreeNode<T> v_exp: u_exp.children) {
				if(!match(v_exp, next_act, last_act)) {
					return false;
				}
			}
			if (next_act.tokenID <= last_act.tokenID) {
				for(int i = next_act.tokenID; i < next_act.getRoot().leafNodeList().size() -1; i++) {
					NLPTreeNode<T> token = next_act.getRoot().leafNodeList().get(i);
					if(!isIgnore(token)) {
						return false;
					}
				}
			}
			return true;
		}
	}
	
	public boolean isIgnore(NLPTreeNode<T> a) {
		ArrayList<String> stop_word = new ArrayList<String>(Arrays.asList("the", "of", "between"));
		if (stop_word.contains(a)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean compare(NLPTreeNode<T> a, NLPTreeNode<T> b) {
		if(a.data.equals(b.data)) {
			return true;
		} else {
			return false;
		}
	}
	
	///////////////TESTING////////////////
	public static void main(String[] args) {
		coreNLPOutput NLPTree = new coreNLPOutput();
		match<String> test_match = new match<String>();
		
		/*
		List<NLPTreeNode<String>> test_expList = NLPTree.parseSentence("Input the interest rate");
		List<NLPTreeNode<String>> test_actList = NLPTree.parseSentence("Input the interest rate");
		NLPTreeNode<String> test_exp = test_expList.get(0);
		NLPTreeNode<String> test_act = test_expList.get(0);
		test_exp.initialAssign();
		test_act.initialAssign();
		*/
		
		/////////////////////////////////
		Document expDoc = new Document("I am Apple.");
		Sentence expSent = expDoc.sentence(0);
		Tree expTree = expSent.parse();
		buildNLPTreeNode a1 = new buildNLPTreeNode();
		List<NLPTreeNode<String>> expNLPTreeList = a1.buildTree(expTree, null);
		NLPTreeNode<String> test_exp = expNLPTreeList.get(0);
		test_exp.assignNextToken();
		
		Document actDoc = new Document("I am of Apple.");
		Sentence actSent = actDoc.sentence(0);
		Tree actTree = actSent.parse();
		buildNLPTreeNode a2 = new buildNLPTreeNode();
		List<NLPTreeNode<String>> actNLPTreeList = a2.buildTree(actTree, null);
		NLPTreeNode<String> test_act = actNLPTreeList.get(0);
		test_act.assignNextToken();
		/////////////////////////////////
		
		//System.out.println("result = " + test_match.match(test_exp, test_act));
		//test_exp.printWholeTree();
		
		System.out.println("-----------------------------------------------------");
		
		//System.out.println(test_act.getFirstToken().data);
		//System.out.println(test_act.getFirstToken().nextToken.data);
		
		
		System.out.println("result = " + test_match.match(test_exp, test_act.getFirstToken(), test_act.getLastToken()));
		
	}
	
}
