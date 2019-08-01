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
	NLPTreeNode<T> last_Actual_pbRef;

	int DEBUG = 1;
	boolean all_expToken_used = false;
	boolean all_actToken_used = false;

	public boolean compare_sameStructure(NLPTreeNode<T> currentExp, NLPTreeNode<T> currentAct) {
		try {
			// BASE case
			if (currentExp.isLeaf()) {
				System.out.println("Comparing " + currentExp.data + " VS " + currentAct.data + "   (("
						+ currentExp.data.equals(currentAct.data)); // DEBUG
				return (currentExp.data.equals(currentAct.data));
			}

			// RECURSIVE case
			boolean match = true;
			for (NLPTreeNode<T> exp_child : currentExp.children) {
				NLPTreeNode<T> act_child = currentAct.getRoot().leafNodeList().get(exp_child.tokenID);
				// System.out.println("exp_child = " + exp_child.data + " | act_child = " +
				// act_child.data ); //DEBUG

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
		next_Actual_pbRef = nextAct;

		// BASE case

		if (DEBUG == 1)
			System.out.println("		currentExp = " + currentExp.data + " | nextAct = " + nextAct.data
					+ " | lastAct = " + lastAct.data);// DEBUG
		if (currentExp.isLeaf()) {
			if (isIgnore(currentExp.data)) {
				System.out.println("\"" + currentExp.data + "\"" + " in EXPECTED is ignored");
				if (currentExp.nextToken == null)
					return false;
				return true;
			}

			while ((nextAct.tokenID <= lastAct.tokenID) && isIgnore(nextAct.data)) {
				System.out.print("\"" + nextAct.data + "\"" + " in ACTUAL is ignored, ");
				if (nextAct.tokenID + 1 > nextAct.getRoot().leafNodeList().size() - 1) {
					System.out.println("###no more actToken");// DEBUG
					return false;
				}
				nextAct = nextAct.getRoot().leafNodeList().get(nextAct.tokenID + 1);
				System.out.println("\"" + nextAct.data + "\"" + " is the next NON-IGNORED token");
			}

			if (nextAct.tokenID > lastAct.tokenID) {
				System.out.println("Margin out of bound");// DEBUG
				return false;
			}

			if ((nextAct.tokenID + 1) <= lastAct.tokenID)
				next_Actual_pbRef = nextAct.getRoot().leafNodeList().get(nextAct.tokenID + 1); // if no this line, it
																								// can pass the first
																								// comparison

			System.out.println("Comparing " + currentExp.data + " VS " + nextAct.data + "   (("
					+ currentExp.data.equals(nextAct.data));// DEBUG

			if (currentExp.data.equals(nextAct.data)
					&& currentExp.tokenID == currentExp.getRoot().leafNodeList().size() - 1) {
				all_expToken_used = true;
				// System.out.println("***" + nextAct.data + currentExp.data + " | " +
				// currentExp.tokenID + " " +(currentExp.getRoot().leafNodeList().size() - 1));
				next_Actual_pbRef = nextAct;
				System.out.println("---" + next_Actual_pbRef.data);
			}

			if (!all_expToken_used && nextAct.nextToken == null) {
				for (NLPTreeNode<T> token : currentExp.getRoot().leafNodeList()) {
					if (token.tokenID > currentExp.tokenID && !isIgnore(token.data)) {
						return false;
					}
				}
			}

			/*
			 * if(nextAct.data.equals(nextAct.data) && nextAct.tokenID ==
			 * nextAct.getRoot().leafNodeList().size() - 1) { all_actToken_used = true; }
			 */
			/////

			return currentExp.data.equals(nextAct.data);// return compare(currentExp, actSeq[next_act-1])

		} else { // RECURSIVE Case
			for (NLPTreeNode<T> exp_child : currentExp.children) {
				if (!compare_diffStructure(exp_child, next_Actual_pbRef, lastAct))
					return false;
			}

			/// *
			// System.out.println("should not show");//DEBUG
			System.out.println("	all_expToken_used = " + all_expToken_used);// DEBUG
			// System.out.println("all_actToken_used= = =" + all_actToken_used);//DEBUG

			// System.out.println(next_Actual_pbRef.data + " " + lastAct.data);

			if (next_Actual_pbRef.tokenID < lastAct.tokenID && all_expToken_used) {
				for (NLPTreeNode<T> token : next_Actual_pbRef.getRoot().leafNodeList()) {
					if (token.tokenID > next_Actual_pbRef.tokenID) {
						System.out.println(token.data);
						if (!isIgnore(token.data)) {
							System.out.print("unuse act_token is/are not ignore: ");// DEBUG
							while (token != null) {
								System.out.print(token.data + " ");
								token = token.nextToken;
							}
							System.out.println();
							return false;
						}
					}
				}

				// for (int i = nextAct.tokenID; i <= lastAct.tokenID; i++) { // for left-most
				// available token to last token do
				// if (!isIgnore(nextAct.getRoot().leafNodeList().get(i).data))
				// return false;
				// }

			}
			// */
			return true;
		}
	}
 
	public boolean compare_diffStructure_ss(NLPTreeNode<T> currentExp, NLPTreeNode<T> nextAct, NLPTreeNode<T> lastAct,
			List<String> sim_rules, List<String> order_rules) {
		// nextAct = the first not used token
		// lastAct = the last token of the scope
		next_Actual_pbRef = nextAct;
		last_Actual_pbRef = lastAct;

		// BASE case
		if (DEBUG == 1)
			System.out.println("		currentExp = " + currentExp.data + " | nextAct = " + nextAct.data
					+ " | lastAct = " + lastAct.data);// DEBUG
		if (currentExp.isLeaf()) {
			if (isIgnore(currentExp.data)) {
				System.out.println("\"" + currentExp.data + "\"" + " in EXPECTED is ignored");
				if (currentExp.nextToken == null)
					return false;
				return true;
			}

			while ((nextAct.tokenID <= lastAct.tokenID) && isIgnore(nextAct.data)) {
				System.out.print("\"" + nextAct.data + "\"" + " in ACTUAL is ignored, ");
				if (nextAct.tokenID + 1 > nextAct.getRoot().leafNodeList().size() - 1) {
					System.out.println("###no more actToken");// DEBUG
					return false;
				}
				nextAct = nextAct.getRoot().leafNodeList().get(nextAct.tokenID + 1);
				System.out.println("\"" + nextAct.data + "\"" + " is the next NON-IGNORED token");
			}

			if (nextAct.tokenID > lastAct.tokenID) {
				System.out.println("Margin out of bound");// DEBUG
				return false;
			}

			if ((nextAct.tokenID + 1) <= lastAct.tokenID)
				next_Actual_pbRef = nextAct.getRoot().leafNodeList().get(nextAct.tokenID + 1); // if no this line, it
																								// can pass the first
																								// comparison

			System.out.println("Comparing " + currentExp.data + " VS " + nextAct.data + "   (("
					+ currentExp.data.equals(nextAct.data));// DEBUG

			if (currentExp.data.equals(nextAct.data)
					&& currentExp.tokenID == currentExp.getRoot().leafNodeList().size() - 1) {
				all_expToken_used = true;
				// System.out.println("***" + nextAct.data + currentExp.data + " | " +
				// currentExp.tokenID + " " +(currentExp.getRoot().leafNodeList().size() - 1));
				next_Actual_pbRef = nextAct;
				System.out.println("---" + next_Actual_pbRef.data);
			}

			if (!all_expToken_used && nextAct.nextToken == null) {
				for (NLPTreeNode<T> token : currentExp.getRoot().leafNodeList()) {
					if (token.tokenID > currentExp.tokenID && !isIgnore(token.data)) {
						return false;
					}
				}
			}

			/*
			 * if(nextAct.data.equals(nextAct.data) && nextAct.tokenID ==
			 * nextAct.getRoot().leafNodeList().size() - 1) { all_actToken_used = true; }
			 */
			/////

			return currentExp.data.equals(nextAct.data);// return compare(currentExp, actSeq[next_act-1])

		} else { // RECURSIVE Case
			PhraseSimilarity sim = new PhraseSimilarity();

			///// testing
			// check sim_rules
			/*
			 * for (String rule : sim_rules) { String path = rule.split(":")[0]; double
			 * alpha = Double.parseDouble(rule.split(":")[1]);
			 * 
			 * NLPTreeNode<T> exp = currentExp.getRoot(); for(String step : path.split(","))
			 * { try { exp = exp.children.get(Integer.parseInt(step)); } catch(Exception
			 * err) { System.out.println("^^^^^the related node is not exist");//DEBUG }
			 * 
			 * } System.out.println("$$$$$$$$$$$the target expNodeData:" + exp.data +
			 * " nodeID:" + exp.nodeID);//DEBUG
			 * System.out.println("$$$$$$$$$$$the current expNodeData:" + currentExp.data +
			 * " nodeID:" + currentExp.nodeID);//DEBUG if (exp.nodeID == currentExp.nodeID)
			 * { //handling SS(alpha) System.out.println("************handling ss");
			 * NLPTreeNode<T> w = nextAct.getwByLeftMostLeaf();
			 * System.out.println("wData : " + w.data);//DEBUG NLPTreeNode<T> w = w; boolean
			 * found = false;
			 * 
			 * while(!found) { if(w == null) { //*********getwByLeftMostLeaf() will not
			 * return NULL, waiting for fix return false; }
			 * if(sim.phraseSim(currentExp.subtreeToString(), w.subtreeToString()) >= alpha)
			 * { System.out.println("%%%%%%%%%%%%%" + currentExp.subtreeToString());
			 * System.out.println("%%%%%%%%%%%%%" + w.subtreeToString());
			 * System.out.println("%%%%%%%%%%%%%sim:" +
			 * sim.phraseSim(currentExp.subtreeToString(), w.subtreeToString()) + " alpha:"
			 * + alpha); found = true; } else { if (w.isLeaf()) { w = null; } else { w =
			 * w.children.get(0); } } } lastAct = w.getRightMostLeaf(); } }
			 */
			/////

			// handling SS(alpha)
			for (String rule : sim_rules) {
				if (currentExp.nodeID == Integer.parseInt(rule.split(":")[0])) {
					System.out.println("Node " + currentExp.data + " need to handle SS...");
					NLPTreeNode<T> w = nextAct.getwByLeftMostLeaf();
					System.out.println("wData : " + w.data + ", " + w.subtreeToString() + ", " + w.nodeID);// DEBUG
					boolean found = false;

					while (!found) {
						if (w == null) { // *********getwByLeftMostLeaf() will not return NULL, waiting for fix
							return false;
						}

						double phraseSim = sim.phraseSim(currentExp.subtreeToString(), w.subtreeToString());

						System.out.println("  UMBC( " + currentExp.subtreeToString() + " , " + w.subtreeToString()
								+ " ) ==> " + phraseSim);

						if (phraseSim >= Double.parseDouble(rule.split(":")[1])) {
							System.out.println("     Score is high enough, will be continued");
							found = true;
						} else {
							if (w.isLeaf()) {
								System.out.println("     Reached leaf but still not enough score");
								w = null;
							} else {
								w = w.children.get(0);
							}
						}
					}
					last_Actual_pbRef = w.getRightMostLeaf();
				}
			}

			// code below are the same as case 2 above //not test yet//copy only
			for (NLPTreeNode<T> exp_child : currentExp.children) {
				if (!compare_diffStructure_ss(exp_child, next_Actual_pbRef, last_Actual_pbRef, sim_rules, order_rules))// not
																														// sure
																														// for
																														// this
																														// line,
																														// should
																														// it
																														// call
																														// different
																														// structure
																														// or
																														// different
																														// structure
																														// with
																														// ss
					return false;
			}

			/// *
			// System.out.println("should not show");//DEBUG
			System.out.println("	all_expToken_used = " + all_expToken_used);// DEBUG
			// System.out.println("all_actToken_used= = =" + all_actToken_used);//DEBUG

			// System.out.println(next_Actual_pbRef.data + " " + lastAct.data);

			if (next_Actual_pbRef.tokenID < last_Actual_pbRef.tokenID && all_expToken_used) {
				for (NLPTreeNode<T> token : next_Actual_pbRef.getRoot().leafNodeList()) {
					if (token.tokenID > next_Actual_pbRef.tokenID) {
						System.out.println(token.data);
						if (!isIgnore(token.data)) {
							System.out.print("unuse act_token is/are not ignore: ");// DEBUG
							while (token != null) {
								System.out.print(token.data + " ");
								token = token.nextToken;
							}
							System.out.println();
							return false;
						}
					}
				}

				// for (int i = nextAct.tokenID; i <= lastAct.tokenID; i++) { // for left-most
				// available token to last token do
				// if (!isIgnore(nextAct.getRoot().leafNodeList().get(i).data))
				// return false;
				// }

			}
			// */
			//////////////////////////// WORK STOP HERE LAST TIME////////////////////////

			return true;
		}
	}

	/// testing//permute
	public boolean compare_diffStructure_ss_p(NLPTreeNode<T> currentExp, NLPTreeNode<T> nextAct, NLPTreeNode<T> lastAct,
			List<String> sim_rules, List<String> order_rules) {
		// nextAct = the first not used token
		// lastAct = the last token of the scope
		next_Actual_pbRef = nextAct;
		last_Actual_pbRef = lastAct;

		// BASE case
		if (DEBUG == 1)
			System.out.println("		currentExp = " + currentExp.data + " | nextAct = " + nextAct.data + " | lastAct = " + lastAct.data);// DEBUG
		if (currentExp.isLeaf()) {
			if (isIgnore(currentExp.data)) {
				/*testing*//*skip token by ignore*/currentExp.usedToken[currentExp.tokenID] = true;
				System.out.println("\"" + currentExp.data + "\"" + " in EXPECTED is ignored");
				if (currentExp.nextToken == null) {
					/*DEBUGING_FALSEPLACE*/System.out.print("DDDDDDDDDD false 01");
					return false;
				}
				
				return true;
			}

			while ((nextAct.tokenID <= lastAct.tokenID) && isIgnore(nextAct.data)) {
				System.out.print("\"" + nextAct.data + "\"" + " in ACTUAL is ignored, ");
				if (nextAct.tokenID + 1 > nextAct.getRoot().leafNodeList().size() - 1) {
					System.out.println("###no more actToken");// DEBUG
					return false;
				}
				nextAct = nextAct.getRoot().leafNodeList().get(nextAct.tokenID + 1);
				System.out.println("\"" + nextAct.data + "\"" + " is the next NON-IGNORED token");
			}

			if (nextAct.tokenID > lastAct.tokenID) {
				System.out.println("Margin out of bound");// DEBUG
				return false;
			}

			if ((nextAct.tokenID + 1) <= lastAct.tokenID)
				next_Actual_pbRef = nextAct.getRoot().leafNodeList().get(nextAct.tokenID + 1);
			

			System.out.println("Comparing " + currentExp.data + " VS " + nextAct.data + "   (("	+ currentExp.data.equals(nextAct.data));// DEBUG

			if (currentExp.data.equals(nextAct.data)) {
				/*tseting1/8*/currentExp.usedToken[currentExp.tokenID] = true;
			}
			
			if (currentExp.data.equals(nextAct.data) && currentExp.isAllTokenUsed()) {
				all_expToken_used = true;
				// System.out.println("***" + nextAct.data + currentExp.data + " | " +
				// currentExp.tokenID + " " +(currentExp.getRoot().leafNodeList().size() - 1));
				next_Actual_pbRef = nextAct;
				System.out.println("---" + next_Actual_pbRef.data);
			}

			//find next expToken if there is no next actToken
			if (!all_expToken_used && nextAct.nextToken == null) {
				for (NLPTreeNode<T> token : currentExp.getRoot().leafNodeList()) {
					if (token.tokenID > currentExp.tokenID && !isIgnore(token.data)) {
						/*DEBUGING_FALSEPLACE*/System.out.print("DDDDDDDDDD false 02");
						return false;
					}
				}
			}

			/*
			 * if(nextAct.data.equals(nextAct.data) && nextAct.tokenID ==
			 * nextAct.getRoot().leafNodeList().size() - 1) { all_actToken_used = true; }
			 */
			/////
			
			/*testing*/currentExp.usedToken[currentExp.tokenID] = true;
			return currentExp.data.equals(nextAct.data);// return compare(currentExp, actSeq[next_act-1])

		} else { // RECURSIVE Case
			PhraseSimilarity sim = new PhraseSimilarity();

			///// testing
			// check sim_rules
			/*
			 * for (String rule : sim_rules) { String path = rule.split(":")[0]; double
			 * alpha = Double.parseDouble(rule.split(":")[1]);
			 * 
			 * NLPTreeNode<T> exp = currentExp.getRoot(); for(String step : path.split(","))
			 * { try { exp = exp.children.get(Integer.parseInt(step)); } catch(Exception
			 * err) { System.out.println("^^^^^the related node is not exist");//DEBUG }
			 * 
			 * } System.out.println("$$$$$$$$$$$the target expNodeData:" + exp.data +
			 * " nodeID:" + exp.nodeID);//DEBUG
			 * System.out.println("$$$$$$$$$$$the current expNodeData:" + currentExp.data +
			 * " nodeID:" + currentExp.nodeID);//DEBUG if (exp.nodeID == currentExp.nodeID)
			 * { //handling SS(alpha) System.out.println("************handling ss");
			 * NLPTreeNode<T> w = nextAct.getwByLeftMostLeaf();
			 * System.out.println("wData : " + w.data);//DEBUG NLPTreeNode<T> w = w; boolean
			 * found = false;
			 * 
			 * while(!found) { if(w == null) { //*********getwByLeftMostLeaf() will not
			 * return NULL, waiting for fix return false; }
			 * if(sim.phraseSim(currentExp.subtreeToString(), w.subtreeToString()) >= alpha)
			 * { System.out.println("%%%%%%%%%%%%%" + currentExp.subtreeToString());
			 * System.out.println("%%%%%%%%%%%%%" + w.subtreeToString());
			 * System.out.println("%%%%%%%%%%%%%sim:" +
			 * sim.phraseSim(currentExp.subtreeToString(), w.subtreeToString()) + " alpha:"
			 * + alpha); found = true; } else { if (w.isLeaf()) { w = null; } else { w =
			 * w.children.get(0); } } } lastAct = w.getRightMostLeaf(); } }
			 */
			/////

			// handling SS(alpha)
			for (String rule : sim_rules) {
				if (currentExp.nodeID == Integer.parseInt(rule.split(":")[0])) {
					System.out.println("Node " + currentExp.data + " need to handle SS...");
					NLPTreeNode<T> w = nextAct.getwByLeftMostLeaf();
					System.out.println("wData : " + w.data + ", " + w.subtreeToString() + ", " + w.nodeID);// DEBUG
					boolean found = false;

					while (!found) {
						if (w == null) { // *********getwByLeftMostLeaf() will not return NULL, waiting for fix
							return false;
						}

						double phraseSim = sim.phraseSim(currentExp.subtreeToString(), w.subtreeToString());

						System.out.println("  UMBC( " + currentExp.subtreeToString() + " , " + w.subtreeToString() + " ) ==> " + phraseSim);

						if (phraseSim >= Double.parseDouble(rule.split(":")[1])) {
							System.out.println("     Score is high enough, will be continued");
							found = true;
						} else {
							if (w.isLeaf()) {
								System.out.println("     Reached leaf but still not enough score");
								w = null;
							} else {
								w = w.children.get(0);
							}
						}
					}
					last_Actual_pbRef = w.getRightMostLeaf();
				}
			}

			// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			generatePermute<T> generatePermute = new generatePermute<T>();
			List<int[]> permutesList = generatePermute.permute(order_rules, currentExp);
			///System.out.println("pass node id " + currentExp.nodeID);
			
			if (permutesList != null) {
				System.out.println("handleing unorder");//DEBUG
				
				boolean matched = false;
				NLPTreeNode<T> tmp_next_Actual_pbRef = next_Actual_pbRef;
				NLPTreeNode<T> tmp_last_Actual_pbRef = last_Actual_pbRef;
				
				for (int[] permute : permutesList) {
					boolean permuteMatched = true;
					//stop at here, need copy the usedToken and restore it after one permute match//
					boolean[] tmp_usedToken = new boolean[currentExp.usedToken.length];
					System.arraycopy(currentExp.usedToken, 0, tmp_usedToken, 0, currentExp.usedToken.length);
					
					List<NLPTreeNode<T>> reindexedChildren = new ArrayList<NLPTreeNode<T>>();
					for (int index : permute) {
						reindexedChildren.add(currentExp.children.get(index));
					}
					///// the remain code should place here and replace currentExp.children by
					///// reindexedChildren
					///// the problem the problem is I don't know how to use the next_Actual_pbRef

					// **************************
					// ************************** not test yet
					// **************************

					// code below are the same as case 2 above //not test yet//copy only
					for (NLPTreeNode<T> exp_child : reindexedChildren) {
						if (!compare_diffStructure_ss_p(exp_child, next_Actual_pbRef, last_Actual_pbRef, sim_rules, order_rules)) {
							permuteMatched = false;
							System.out.println("000000000000000000");
							for(boolean x : currentExp.usedToken) {
								System.out.print(" " + x);
							}
						}
						
					}

					// System.out.println("should not show");//DEBUG
					System.out.println("	all_expToken_used = " + all_expToken_used);// DEBUG
					// System.out.println("all_actToken_used= = =" + all_actToken_used);//DEBUG
					// System.out.println(next_Actual_pbRef.data + " " + lastAct.data);
					if (next_Actual_pbRef.tokenID < last_Actual_pbRef.tokenID && all_expToken_used) {
						for (NLPTreeNode<T> token : next_Actual_pbRef.getRoot().leafNodeList()) {
							if (token.tokenID > next_Actual_pbRef.tokenID) {
								System.out.println(token.data);
								if (!isIgnore(token.data)) {
									System.out.print("unuse act_token is/are not ignore: ");// DEBUG
									while (token != null) {
										System.out.print(token.data + " ");
										token = token.nextToken;
									}
									System.out.println();
									permuteMatched =  false;
								}
							}
						}
					}
					
					//System.out.println("sssssssssss" + permuteMatched);//DEBUG
					
					if(permuteMatched) {
						return true;
					}
					// **************************
					// **************************
					// **************************
					if(!permuteMatched) {
						next_Actual_pbRef = tmp_next_Actual_pbRef;
						last_Actual_pbRef =  tmp_last_Actual_pbRef;
						System.arraycopy(tmp_usedToken, 0, currentExp.usedToken, 0, tmp_usedToken.length);
					}
				}
				System.out.println("all permutes NOT match");
				for(boolean x :currentExp.usedToken) {
					
					System.out.println(x);
				}
				
				return false;
			}
			// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

			///////////////////////////////////
			//////////////////////////////////
			/////////////////////////////////

			// code below are the same as case 2 above //not test yet//copy only
			for (NLPTreeNode<T> exp_child : currentExp.children) {
				if (!compare_diffStructure_ss_p(exp_child, next_Actual_pbRef, last_Actual_pbRef, sim_rules,
						order_rules))// not sure for this line, should it call different structure or different
										// structure with ss
					return false;
			}

			// System.out.println("should not show");//DEBUG
			System.out.println("	all_expToken_used = " + all_expToken_used);// DEBUG
			// System.out.println("all_actToken_used= = =" + all_actToken_used);//DEBUG
			// System.out.println(next_Actual_pbRef.data + " " + lastAct.data);
			if (next_Actual_pbRef.tokenID < last_Actual_pbRef.tokenID && all_expToken_used) {
				for (NLPTreeNode<T> token : next_Actual_pbRef.getRoot().leafNodeList()) {
					if (token.tokenID > next_Actual_pbRef.tokenID) {
						System.out.println(token.data);
						if (!isIgnore(token.data)) {
							System.out.print("unuse act_token is/are not ignore: ");// DEBUG
							while (token != null) {
								System.out.print(token.data + " ");
								token = token.nextToken;
							}
							System.out.println();
							return false;
						}
					}
				}
			}
			///////////////////////////////////
			//////////////////////////////////
			/////////////////////////////////
			
			/*testing*//*skip token by ignore*/currentExp.usedToken[currentExp.tokenID] = true;
			return true;
		}
	}

	///

	public boolean isIgnore(T word) {
		if (stop_word.contains(word))
			return true;
		return false;
	}

	/*
	 * public void permute(int i, int n, int[] A) { if(i == n-1) { for(int x : A) {
	 * System.out.println(x); } return; } permute(i+1, n, A); int tmp = A[i];
	 * for(int j = i+1; j<=n-1; j++) { A[i] = A[j]; A[j] = tmp; permute(i+1, n, A);
	 * A[j] = A[i]; A[i] = tmp; } return; }
	 */

	public int[] permute(int i, int n, int[] A, int[] allPermute) {
		if (allPermute == null) {
			allPermute = new int[A.length * factorial(n)];
			Arrays.fill(allPermute, -1);
		}
		if (i == n - 1) {
			for (int x : A) {
				// System.out.println("added: " + x);//DEBUG
				// printArray(allPermute);//DEBUG
				for (int j = 0; j < allPermute.length; j++) {
					if (allPermute[j] == -1) {
						allPermute[j] = x;
						break;
					}
				}
			}
			return allPermute;
		}
		permute(i + 1, n, A, allPermute);
		int tmp = A[i];
		for (int j = i + 1; j <= n - 1; j++) {
			A[i] = A[j];
			A[j] = tmp;
			permute(i + 1, n, A, allPermute);
			A[j] = A[i];
			A[i] = tmp;
		}
		return allPermute;
	}

	public int factorial(int n) {
		int fact = 1;
		for (int i = 2; i <= n; i++) {
			fact = fact * i;
		}
		return fact;
	}

	public void printArray(int[] a) {
		int i = 0;
		for (int x : a) {
			System.out.print(x + " ");
			i++;
		}
		System.out.println();
		System.out.println("num of int:" + i);
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
		newCompare<String> compare = new newCompare();

		int test_case = 4;
		List<NLPTreeNode<String>> test_exp = NLPTree.parseSentence("I ate apple and banana");
		List<NLPTreeNode<String>> test_act = NLPTree.parseSentence("I ate banana and apple");

		System.out.println("exp: " + test_exp.get(0).subtreeToString());
		test_exp.get(0).printWholeTree();
		System.out.println("act: " + test_act.get(0).subtreeToString());
		test_act.get(0).printWholeTree();
		System.out.println("-----------------------------------------------------");

		// Case One: Same structure
		if (test_case == 1) {
			System.out.println(compare.compare_sameStructure(test_exp.get(0), test_act.get(0)));
		}

		// Case Two: Different structure, no SS
		if (test_case == 2) {
			NLPTreeNode<String> a = test_act.get(0).getRoot().leafNodeList().get(0); // First Token
			NLPTreeNode<String> b = test_act.get(0).getRoot().leafNodeList()
					.get(test_act.get(0).getRoot().leafNodeList().size() - 1); // Last Token
			System.out.println(compare.compare_diffStructure(test_exp.get(0), a, b));
		}
		// Case Three: Different structure with SS(alpha)
		if (test_case == 3) {
			NLPTreeNode<String> a = test_act.get(0).getRoot().leafNodeList().get(0); // First Token
			NLPTreeNode<String> b = test_act.get(0).getRoot().leafNodeList().get(test_act.get(0).getRoot().leafNodeList().size() - 1); // Last Token

			List<String> sim_rules = Arrays.asList("5:0.8");
			List<String> order_rules = Arrays.asList("5:0,1"); // parentNodeID:childID1,childID2 e.g. 5:0,1

			System.out.println(compare.compare_diffStructure_ss(test_exp.get(0), a, b, sim_rules, order_rules));
		}

		// permute test
		if (test_case == 10) {
			int[] a = new int[] { 1, 2, 3, 4 };
			int i = 0;
			int n = a.length;
			// compare.permute(0, 3, a);
			// System.out.println("_________________________");

			int[] all = compare.permute(i, n, a, null);
			compare.printArray(all);
		}
		
		// Case Four: Different S=structure with SS(alpha) and permute
		//testing with permute only
		if (test_case == 4) {
			NLPTreeNode<String> a = test_act.get(0).getRoot().leafNodeList().get(0); // First Token
			NLPTreeNode<String> b = test_act.get(0).getRoot().leafNodeList().get(test_act.get(0).getRoot().leafNodeList().size() - 1); // Last Token

			//List<String> sim_rules = Arrays.asList("5:0.8");
			List<String> sim_rules = Arrays.asList();
			List<String> order_rules = Arrays.asList("8:0,2"); // parentNodeID:childID1,childID2 e.g. 5:0,1

			System.out.println(compare.compare_diffStructure_ss_p(test_exp.get(0), a, b, sim_rules, order_rules));
		}
	}

}
