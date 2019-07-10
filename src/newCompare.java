import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
	u_exp = currentExp
	u_act = currentAct

	v_exp = exp_child
	v_act = act_child
 */
public class newCompare<T> {
	ArrayList<String> stop_word = new ArrayList<String>(Arrays.asList("the", "of", "between"));

	public boolean compare_sameStructure(NLPTreeNode<T> currentExp, NLPTreeNode<T> currentAct) {
		try {
			// BASE case
			if (currentExp.isLeaf()) {
				System.out.println("Comparing " + currentExp.data + " VS " + currentAct.data + "   ((" + currentExp.data.equals(currentAct.data)); // DEBUG
				return (currentExp.data.equals(currentAct.data));
			}

			// RECURSIVE case
			boolean match = true;
			for (int i = 0; i < currentExp.children.size(); i++) {
				NLPTreeNode<T> exp_child = currentExp.children.get(i);
				NLPTreeNode<T> act_child = currentAct.children.get(i);
				//System.out.println("exp_child = " + exp_child.data + " | act_child = " + act_child.data );//DEBUG
				
				match = match && compare_sameStructure(exp_child, act_child);
			}
			return match;
		} catch (IndexOutOfBoundsException err) {
			System.err.println(err);
			return false;
		}
	}

	public boolean compare_diffStructure(NLPTreeNode<T> currentExp, NLPTreeNode<T> nextAct, NLPTreeNode<T> lastAct) {
		// nextAct = the first not used token
		// lastAct = the last token of the scope

		// BASE case
		System.out.println("currentExp = " + currentExp.data + " | nextAct = " + nextAct.data + " | lastAct = " + lastAct.data);//DEBUG
		if (currentExp.isLeaf()) {
			if (isIgnore(currentExp.data)) {
				System.out.println("\"" + currentExp.data + "\"" + " in EXPECTED is ignored");
				return true;
			}

			while ((nextAct.tokenID <= lastAct.tokenID) && isIgnore(nextAct.data)) {
				System.out.println("\"" + nextAct.data + "\"" + " in ACTUAL is ignored");
				nextAct = nextAct.getRoot().leafNodeList().get(nextAct.tokenID + 1);
				System.out.println("\"" + nextAct.data + "\"" + " is the next NON-IGNORED token");
			}

			if (nextAct.tokenID > lastAct.tokenID) {
				System.out.println("Margin out of bound");//DEBUG
				return false;
			}

			nextAct = nextAct.getRoot().leafNodeList().get(nextAct.tokenID + 1); //if no this line, it can pass the first comparison

			System.out.println("Comparing " + currentExp.data + " VS " + nextAct.getRoot().leafNodeList().get(nextAct.tokenID - 1).data + "   ((" + currentExp.data.equals(nextAct.getRoot().leafNodeList().get(nextAct.tokenID - 1).data));//DEBUG
			return currentExp.data.equals(nextAct.getRoot().leafNodeList().get(nextAct.tokenID - 1).data);// return compare(currentExp, actSeq[next_act-1])

		} else { // RECURSIVE Case

			for (NLPTreeNode<T> exp_child : currentExp.children) {
				if (!compare_diffStructure(exp_child, nextAct, lastAct))
					return false;
			}

			if (nextAct.tokenID <= lastAct.tokenID) {
				/*
				for (NLPTreeNode<T> token : nextAct.getRoot().leafNodeList()) {
					if (token.tokenID > nextAct.tokenID) {
						if (!isIgnore(token.data)) {
							return false;
						}
					}
				}
				 */
			}
			return true;
		}
	}

	public boolean isIgnore(T word) {
		if (stop_word.contains(word))
			return true;
		return false;
	}

	/*
	 * public String getType(String data) { String type = ""; try {
	 * Double.parseDouble(data); if (data.contains(".")) { type = "DOUBLE"; } else {
	 * try { Integer.parseInt(data); // because value "1F" is considered as double
	 * which has no ".", treat is as // string type = "INT"; } catch
	 * (NumberFormatException e) { type = "STR"; } } } catch (NumberFormatException
	 * e) { type = "STR"; } return type; }
	 */

	public static void main(String[] args) {
		coreNLPOutput NLPTree = new coreNLPOutput();
		newCompare compare = new newCompare();
		List<NLPTreeNode<String>> test_exp = NLPTree.parseSentence("Input the interest rate");
		List<NLPTreeNode<String>> test_act = NLPTree.parseSentence("Input the interest rate");

		System.out.println(compare.compare_sameStructure(test_exp.get(0), test_act.get(0)));
		System.out.println("-----------------------------------------------------");
		
		NLPTreeNode<String> a = test_act.get(0).getRoot().leafNodeList().get(0);
		NLPTreeNode<String> b = test_act.get(0).getRoot().leafNodeList().get(test_act.get(0).getRoot().leafNodeList().size()-1);
		
		System.out.println(
				compare.compare_diffStructure(test_exp.get(0), a, b));
	}

}
