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

				match = match && compare_sameStructure(exp_child, act_child);
			}
			return match;
		}catch (IndexOutOfBoundsException err){
			System.err.println(err);
			return false;
		}
	}

	public boolean compare_diffStructure(NLPTreeNode<T> currentExp, NLPTreeNode<T> currentAct, NLPTreeNode<T> lastAct){
		// BASE case
		if(currentExp.isLeaf()){
			if (isIgnore(currentExp.data))
				return true;

			while ((currentAct.tokenID <= lastAct.tokenID) && isIgnore(currentAct.data))
				currentAct = currentAct.getRoot().leafNodeList().get(currentAct.tokenID + 1);

			if (currentAct.tokenID > lastAct.tokenID)
				return false;

			currentAct = currentAct.getRoot().leafNodeList().get(currentAct.tokenID + 1);

			System.out.println("Comparing " + currentExp.data + " VS " + currentAct.data + "   ((" + currentExp.data.equals(currentAct.data));
			return currentExp.data.equals(currentAct.data);
		}

		//RECURSIVE Case
		for (NLPTreeNode<T> exp_child : currentExp.children){
			if (!compare_diffStructure(exp_child, currentAct, lastAct))
				return false;
		}

		if(currentAct.tokenID <= lastAct.tokenID){
			/*
			if there are remaining tokens that are not ignored
				return false
			 */
			for (NLPTreeNode<T> token: currentAct.getRoot().leafNodeList()) {
				if (token.tokenID > currentAct.tokenID) {
					if (!isIgnore(token.data)) {
						return false;
					}
				}
			}
		}

		return true;
	}

	public boolean isIgnore(T word){
		if (stop_word.contains(word))
			return true;
		return false;
	}
	
	public String getType(String data) {
		String type = "";
		try {
			Double.parseDouble(data);
			if (data.contains(".")) {
				type = "DOUBLE";
			} else {
				try {
					Integer.parseInt(data); // because value "1F" is considered as double which has no ".", treat is as
											// string
					type = "INT";
				} catch (NumberFormatException e) {
					type = "STR";
				}
			}
		} catch (NumberFormatException e) {
			type = "STR";
		}
		return type;
	}

	public static void main(String[] args) {
		coreNLPOutput NLPTree = new coreNLPOutput();
		newCompare compare = new newCompare();
		List<NLPTreeNode<String>> test_exp = NLPTree.parseSentence("Input the interest rate");
		List<NLPTreeNode<String>> test_act = NLPTree.parseSentence("Input the interest rate");

		System.out.println(compare.compare_sameStructure(test_exp.get(0), test_act.get(0)));
		System.out.println("-----------------------------------------------------");
		System.out.println(compare.compare_diffStructure(test_exp.get(0), test_act.get(0), test_act.get(test_act.size()-1)));
	}

	
	
}
