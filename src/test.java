import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class test {
	public static void main(String[] augs) {
		coreNLPOutput NLPTree = new coreNLPOutput();
		test testing = new test();
		
		List<NLPTreeNode<String>> test_exp = NLPTree.parseSentence("Input the interest rate");
		NLPTreeNode<String> currentExp = test_exp.get(5);
		List<String> order_rules = Arrays.asList("5:1,2");
		/*
		System.out.println("currentExp:" + currentExp.nodeID);
		for(NLPTreeNode<String> x: currentExp.children) {
			System.out.println("child:"+ x.nodeID +" "+ x.childID);
		}
		*/
		List<int[]> a = testing.permuteTest(order_rules, currentExp);
		System.out.println("++++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println(a.size());
		
		for(int[] intArray : a) {
			for(int x : intArray) {
				System.out.print(x);
			}
			System.out.println();
		}
	}
	///////////////////////////////
	public List<int[]> permuteTest(List<String> order_rules, NLPTreeNode<String> currentExp) {
		List<int[]> permuteList = null;
		for(String rule : order_rules) {
			if(currentExp.nodeID == Integer.parseInt(rule.split(":")[0])) {
				System.out.println("In permute process, currentExp:" + currentExp.childID + " " + currentExp.data);//DEBUG
				System.out.println("rule:" + rule);//DEBUG
				permuteList = null;
				int numOfUnorderNode = 0;
				
				//create 0-n array//e.g.permute[]{0,1,2}
				int[] permute = new int[currentExp.children.size()];
				for(int i = 0; i < permute.length; i++) {
					permute[i] = i;
				}
//				System.out.println("the raw permute(should from 0 - 1)");//DEBUG
//				printArray(permute);//DEBUG
				
				//get numOfUnorderNode++;
				for(String childIDString : rule.split(":")[1].split(",") ) {
					numOfUnorderNode++;
				}
//				System.out.println("number of unorder node:" + numOfUnorderNode);//DEBUG
				
				
				
				
				
				//array for unorder node index
				int[] unoderNodes = new int[numOfUnorderNode];
				int unoderNodesIndex = 0;
	
				//replace index in rule by -1//e.g.permute[]{0,1,2} with UO rule node:0,2 -> permute[]{-1,1,-1}
				//fill unoderNodes[] by unorder nodes//e.g. unoderNodes[]{0,2}
				for(String childIDString : rule.split(":")[1].split(",") ) {
					int childID = Integer.parseUnsignedInt(childIDString);
					unoderNodes[unoderNodesIndex++] = childID;
					permute[childID] = -1;
				}
//				System.out.println("permute with -1 index");//DEBUG
//				printArray(permute);//DEBUG
				
				
				int[] permutesOfUONode = permute(0,unoderNodes.length, unoderNodes, null);
//				System.out.println("permutesOfUONode:");//DEBUG
//				printArray(permutesOfUONode);//DEBUG
				
				///* stop at this part, let me think think
				int pCount = 0;//counter to separate pairs
				int[] permuteWithSpace = null;
				for(int index : permutesOfUONode) {
//					System.out.println("current index is " + index);//DEBUG
					
					pCount++;
//					System.out.println("pCount is " + pCount);//DEBUG
					
					if(permuteWithSpace == null) {
						//deep copy permute to permuteWithSpace
						permuteWithSpace = new int[currentExp.children.size()];
						for(int i = 0; i < permute.length; i++) {
							permuteWithSpace[i] = permute[i];
						}
					}
//					printArray(permuteWithSpace);//DEBUG
					
					if(permuteWithSpace != null) {
						for(int i = 0 ; i < permuteWithSpace.length; i++) {
							if(permuteWithSpace[i] == -1) {
								permuteWithSpace[i] = index;
								break;
							}
						}
					}
					
					boolean noSpace = true;
					for(int x : permuteWithSpace) {
						if(x == -1) {
							noSpace = false;
							break;
						}
					}
					
					if(noSpace) {
						if(permuteList == null) {
							permuteList = new ArrayList<int[]>();
							permuteList.add(permuteWithSpace);
							permuteWithSpace = null;
						}else {
							permuteList.add(permuteWithSpace);
							permuteWithSpace = null;
						}
					}
					
					//if(pCount % unoderNodes.length == 0) {}
				}
				//stop at this part, let me think think*/
				
				//////////////need to change the permute() output to continue
				
			}
		}
		return permuteList;

	}
	///////////////////////////////
	public int[] permute(int i, int n, int[] A, int[] allPermute) {
		if (allPermute==null) {
			allPermute = new int[A.length * factorial(n)];
			Arrays.fill(allPermute, -1);
		}
		if(i == n-1) {
			for(int x : A) {
				//System.out.println("added: " + x);//DEBUG
				//printArray(allPermute);//DEBUG
				for(int j = 0; j < allPermute.length; j++) {
					if(allPermute[j] == -1) {
						allPermute[j] = x;
						break;
					}
				}
			}
			return allPermute;
		}
		permute(i+1, n, A, allPermute);
		int tmp = A[i];
		for(int j = i+1; j<=n-1; j++) {
			A[i] = A[j];	A[j] = tmp;
			permute(i+1, n, A, allPermute);
			A[j] = A[i];	A[i] = tmp;
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
	///////////////////////////////
}
