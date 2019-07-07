import java.util.ArrayList;
import java.util.List;

public class NLPTreeNode<T> {

	T data;
	String type;
	int level;
	int levelID;
	int nodeID;
	int childID;
	int startPos, endPos;
	NLPTreeNode<T> parent;
	List<NLPTreeNode<T>> children;
	String compOptions;

	public NLPTreeNode(T data, String type) {
		this.data = data;
		this.type = type;
	}

	public NLPTreeNode(T data, String type, int nodeID) {
		this.children = new ArrayList<NLPTreeNode<T>>();
		this.data = data;
		this.type = type;
		this.nodeID = nodeID;
	}

	public NLPTreeNode(T data, String type, int level, int nodeID) {
		this.data = data;
		this.type = type;
		this.level = level;
		this.nodeID = nodeID;
	}

	public NLPTreeNode(T data, String type, int level, int levelID, int childID, int startPos, int endPos,
			String options) {
		this.data = data;
		this.type = type;
		this.level = level;
		this.levelID = levelID;
		this.childID = childID;
		this.startPos = startPos;
		this.endPos = endPos;
		this.compOptions = options;
		this.children = new ArrayList<NLPTreeNode<T>>();
	}

	NLPTreeNode() {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
																		// Tools | Templates.
	}

	public NLPTreeNode<T> addChild(T child, String type) {
		NLPTreeNode<T> childNode = new NLPTreeNode<T>(child, type);
		childNode.parent = this;
		this.children.add(childNode);
		return childNode;
	}

	public NLPTreeNode<T> addChild(T child, String type, int nodeID) {
		NLPTreeNode<T> childNode = new NLPTreeNode<T>(child, type, nodeID);
		childNode.parent = this;
		this.children.add(childNode);
		return childNode;
	}

	public NLPTreeNode<T> addChild(T child, String type, int level, int levelID, int ChildID, int startPos, int endPos,
			String options) {
		NLPTreeNode<T> childNode = new NLPTreeNode<T>(child, type, level, levelID, childID, startPos, endPos, options);
		childNode.parent = this;
		this.children.add(childNode);
		return childNode;
	}

	public void printData() {
		System.out.println(data);
	}
	// other features ...

	// print the tree
	public void printWholeTree() {
		this.assignChildId();
		int level = 0;
		System.out.println(this.data + " " + this.childID);
		level++;
		if (!this.children.isEmpty()) {
			for (NLPTreeNode<T> child : this.children)
				child.printTree(level);
		}
	}

	public void printTree(int level) {
		for (int i = 0; i < level; i++) {
			System.out.print("	");
		}
		System.out.println(this.data + " " + this.childID);
		level++;
		if (!this.children.isEmpty()) {
			for (NLPTreeNode<T> child : this.children)
				child.printTree(level);
		}
	}

	// list all leaf node
	public List<NLPTreeNode<T>> leafNodeList() {
		List<NLPTreeNode<T>> leafNode = new ArrayList<NLPTreeNode<T>>();
		if (this.isLeaf()) {
			leafNode.add(this);
			return leafNode;
		} else {
			if (!this.children.isEmpty()) {
				for (NLPTreeNode<T> child : this.children) {
					leafNode.addAll(child.leafNodeList());
				}
			}
		}
		return leafNode;
		
	}

	// public

	public boolean isLeaf() {
		if (this.type.compareTo("data") == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	//should be called by the root ONLY
	//MUST call after the whole tree was builded
	public void assignChildId() { 
		/////////////
		NLPTreeNode<T> root = this;
		if(root.isLeaf()) {
			return;
		} else {
			if(this.children.size() > 0) {
				int idCount = 0;
				for(NLPTreeNode<T> child: this.children) {
					child.childID = idCount++;
					child.assignChildId();
				}
			}
		}
	}

}