
public class newCompare<T> {
	public boolean match(NLPTreeNode<T> u_exp, NLPTreeNode<T> u_act) {
		if (u_exp.isLeaf()) {
			return compare(u_exp, u_act);////compare or match?
		}
		for (NLPTreeNode<T> v_exp: u_exp.children) {
			//let v_act == corresponding child of u__act
			
			if (!match(v_exp, v_act)) {
				return false;
			}
		}
		return true;
	}
	
	public boolean compare(NLPTreeNode<T> a, NLPTreeNode<T> b) {
		if (a.isLeaf() && b.isLeaf()) {
			if(a.data)
		}
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

	
	
}
