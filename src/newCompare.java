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
	ArrayList<String> stop_word = new ArrayList<String>(Arrays.asList("the", "of", "between", "a"));
	NLPTreeNode<T> next_Actual_pbRef;
	int DEBUG = 1;
	boolean all_expToken_used = false;
	boolean all_actToken_used = false;
	
	public boolean compare_sameStructure(NLPTreeNode<T> currentExp, NLPTreeNode<T> currentAct) {
		try {
			// BASE case
			if (currentExp.isLeaf()) {
				System.out.println("Comparing " + currentExp.data + " VS " + currentAct.data + "   ((" + currentExp.data.equals(currentAct.data)); // DEBUG
				return (currentExp.data.equals(currentAct.data));
			}

			// RECURSIVE case
			boolean match = true;
			for (NLPTreeNode<T> exp_child : currentExp.children) {
				NLPTreeNode<T> act_child = currentAct.getRoot().leafNodeList().get(exp_child.tokenID);
				//System.out.println("exp_child = " + exp_child.data + " | act_child = " + act_child.data ); //DEBUG
				
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
		if (DEBUG == 1)
			System.out.println("		currentExp = " + currentExp.data + " | nextAct = " + nextAct.data + " | lastAct = " + lastAct.data);//DEBUG
		if (currentExp.isLeaf()) {
			if (isIgnore(currentExp.data)) {
				System.out.println("\"" + currentExp.data + "\"" + " in EXPECTED is ignored");
				return true;
			}

			while ((nextAct.tokenID <= lastAct.tokenID) && isIgnore(nextAct.data)) {
				System.out.print("\"" + nextAct.data + "\"" + " in ACTUAL is ignored, ");
				if (nextAct.tokenID + 1 > nextAct.getRoot().leafNodeList().size() - 1) {
					System.out.println("###no more actToken");//DEBUG
					return false;
				}
				nextAct = nextAct.getRoot().leafNodeList().get(nextAct.tokenID + 1);
				System.out.println("\"" + nextAct.data + "\"" + " is the next NON-IGNORED token");
			}
			
			if (nextAct.tokenID > lastAct.tokenID) {
				System.out.println("Margin out of bound");//DEBUG
				return false;
			}
			
			if( (nextAct.tokenID + 1) <= lastAct.tokenID)
				next_Actual_pbRef = nextAct.getRoot().leafNodeList().get(nextAct.tokenID + 1); //if no this line, it can pass the first comparison

			
			System.out.println("Comparing " + currentExp.data + " VS " + nextAct.data + "   ((" + currentExp.data.equals(nextAct.data));//DEBUG
			
			if(currentExp.data.equals(nextAct.data) && currentExp.tokenID == currentExp.getRoot().leafNodeList().size() - 1) {
				all_expToken_used = true;
				//System.out.println("***" + nextAct.data + currentExp.data + " | " + currentExp.tokenID + " " +(currentExp.getRoot().leafNodeList().size() - 1));				
				next_Actual_pbRef = nextAct;
				System.out.println("---" + next_Actual_pbRef.data);
			}

			if(!all_expToken_used && nextAct.nextToken==null) {
				for ( NLPTreeNode<T> token: currentExp.getRoot().leafNodeList()) {
					if(token.tokenID > currentExp.tokenID && !isIgnore(token.data)) {
						return false;
					}
				}
			}
			
			
			/*
			if(nextAct.data.equals(nextAct.data) && nextAct.tokenID == nextAct.getRoot().leafNodeList().size() - 1) {
				all_actToken_used = true;
			}
			*/
			/////
			
			return currentExp.data.equals(nextAct.data);// return compare(currentExp, actSeq[next_act-1])

		} else { // RECURSIVE Case
			for (NLPTreeNode<T> exp_child : currentExp.children) {
				if (!compare_diffStructure(exp_child, next_Actual_pbRef, lastAct))			
					return false;
			}


			///*
			//System.out.println("should not show");//DEBUG
			System.out.println("	all_expToken_used = " + all_expToken_used);//DEBUG
			//System.out.println("all_actToken_used= = =" + all_actToken_used);//DEBUG
			
			//System.out.println(next_Actual_pbRef.data + " " + lastAct.data);
			
			if (next_Actual_pbRef.tokenID < lastAct.tokenID && all_expToken_used) {				
				for (NLPTreeNode<T> token : next_Actual_pbRef.getRoot().leafNodeList()) {
					if (token.tokenID > next_Actual_pbRef.tokenID) {
						System.out.println(token.data);
						if (!isIgnore(token.data)) {
							System.out.print("unuse act_token is/are not ignore: ");//DEBUG
							while(token != null) {
								System.out.print(token.data + " ");
								token = token.nextToken;
							}
							System.out.println();
							return false;
						}
					}
				}
				
				
				//for (int i = nextAct.tokenID; i <= lastAct.tokenID; i++) { // for left-most available token to last token do
				//	if (!isIgnore(nextAct.getRoot().leafNodeList().get(i).data))
				//		return false;
				//}
				 
			}
			//*/
			return true;
		}
	}

	public boolean compare_diffStructure_ss(NLPTreeNode<T> currentExp, NLPTreeNode<T> nextAct, NLPTreeNode<T> lastAct) {
		// nextAct = the first not used token
		// lastAct = the last token of the scope

		// BASE case
		if (DEBUG == 1)
			System.out.println("		currentExp = " + currentExp.data + " | nextAct = " + nextAct.data + " | lastAct = " + lastAct.data);//DEBUG
		if (currentExp.isLeaf()) {
			if (isIgnore(currentExp.data)) {
				System.out.println("\"" + currentExp.data + "\"" + " in EXPECTED is ignored");
				return true;
			}

			while ((nextAct.tokenID <= lastAct.tokenID) && isIgnore(nextAct.data)) {
				System.out.print("\"" + nextAct.data + "\"" + " in ACTUAL is ignored, ");
				if (nextAct.tokenID + 1 > nextAct.getRoot().leafNodeList().size() - 1) {
					System.out.println("###no more actToken");//DEBUG
					return false;
				}
				nextAct = nextAct.getRoot().leafNodeList().get(nextAct.tokenID + 1);
				System.out.println("\"" + nextAct.data + "\"" + " is the next NON-IGNORED token");
			}
			
			if (nextAct.tokenID > lastAct.tokenID) {
				System.out.println("Margin out of bound");//DEBUG
				return false;
			}
			
			if( (nextAct.tokenID + 1) <= lastAct.tokenID)
				next_Actual_pbRef = nextAct.getRoot().leafNodeList().get(nextAct.tokenID + 1); //if no this line, it can pass the first comparison

			
			System.out.println("Comparing " + currentExp.data + " VS " + nextAct.data + "   ((" + currentExp.data.equals(nextAct.data));//DEBUG
			
			if(currentExp.data.equals(nextAct.data) && currentExp.tokenID == currentExp.getRoot().leafNodeList().size() - 1) {
				all_expToken_used = true;
				//System.out.println("***" + nextAct.data + currentExp.data + " | " + currentExp.tokenID + " " +(currentExp.getRoot().leafNodeList().size() - 1));				
				next_Actual_pbRef = nextAct;
				System.out.println("---" + next_Actual_pbRef.data);
			}

			if(!all_expToken_used && nextAct.nextToken==null) {
				for ( NLPTreeNode<T> token: currentExp.getRoot().leafNodeList()) {
					if(token.tokenID > currentExp.tokenID && !isIgnore(token.data)) {
						return false;
					}
				}
			}
			
			
			/*
			if(nextAct.data.equals(nextAct.data) && nextAct.tokenID == nextAct.getRoot().leafNodeList().size() - 1) {
				all_actToken_used = true;
			}
			*/
			/////
			
			return currentExp.data.equals(nextAct.data);// return compare(currentExp, actSeq[next_act-1])

		} else { // RECURSIVE Case
			PhraseSimilarity sim = new PhraseSimilarity();
			
			//handling SS(alpha)
			NLPTreeNode<T> w = nextAct.getwByLeafMostLeaf();
			System.out.println("wData : " + w.data);//DEBUG
			NLPTreeNode<T> wPrime = w;
			boolean found = false;
			
			while(!found) {
				if(wPrime == null) { //*********getwByLeafMostLeaf() will not return NULL, waiting for fix
					return false;
				}
				if(sim.phraseSim(currentExp.subtreeToString(), wPrime.subtreeToString()) >= alpha) {
					found = true;
				} else {
					if (wPrime.isLeaf()) {
						wPrime = null;
					} else {
						wPrime = wPrime.children.get(0);
					}
				}
			}
			lastAct = wPrime.getRightMostLeaf();
			
			//code below are  the same as case 2 above //not test yet//copy only
			for (NLPTreeNode<T> exp_child : currentExp.children) {
				if (!compare_diffStructure(exp_child, next_Actual_pbRef, lastAct))			
					return false;
			}


			///*
			//System.out.println("should not show");//DEBUG
			System.out.println("	all_expToken_used = " + all_expToken_used);//DEBUG
			//System.out.println("all_actToken_used= = =" + all_actToken_used);//DEBUG
			
			//System.out.println(next_Actual_pbRef.data + " " + lastAct.data);
			
			if (next_Actual_pbRef.tokenID < lastAct.tokenID && all_expToken_used) {				
				for (NLPTreeNode<T> token : next_Actual_pbRef.getRoot().leafNodeList()) {
					if (token.tokenID > next_Actual_pbRef.tokenID) {
						System.out.println(token.data);
						if (!isIgnore(token.data)) {
							System.out.print("unuse act_token is/are not ignore: ");//DEBUG
							while(token != null) {
								System.out.print(token.data + " ");
								token = token.nextToken;
							}
							System.out.println();
							return false;
						}
					}
				}
				
				
				//for (int i = nextAct.tokenID; i <= lastAct.tokenID; i++) { // for left-most available token to last token do
				//	if (!isIgnore(nextAct.getRoot().leafNodeList().get(i).data))
				//		return false;
				//}
				 
			}
			//*/
			////////////////////////////WORK STOP HERE LAST TIME////////////////////////
			
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
		List<NLPTreeNode<String>> test_exp = NLPTree.parseSentence("Input the interest rate of");
		List<NLPTreeNode<String>> test_act = NLPTree.parseSentence("Input the interest rate");

		//Case One: Same structure
		System.out.println(compare.compare_sameStructure(test_exp.get(0), test_act.get(0)));
		System.out.println("-----------------------------------------------------");

		//Case Two: Different structure, no SS
		NLPTreeNode<String> a = test_act.get(0).getRoot().leafNodeList().get(0);
		NLPTreeNode<String> b = test_act.get(0).getRoot().leafNodeList().get(test_act.get(0).getRoot().leafNodeList().size()-1);
		compare.next_Actual_pbRef = a;
		System.out.println(compare.compare_diffStructure(test_exp.get(0), compare.next_Actual_pbRef, b));
	
		//Case Three: Different structure with SS(alpha)
		
	}

}
