package ecnu.compiling.compilingmate.lex.directlymethod;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class REtoDFA {
	static REtoDFA rEtoDFASingleton = null;
	private char leftBracket = '(';
	private char rightBracket = ')';
	private char starSymbol = '*';
	private char orSymbol = '|';
	private char andSymbol = '.';
	private char nullSymbol = 'ε';
	private char endSymbol = '#';
	private String re = null;
	private String reNow = null; //加了endSymbol的re
	private int number = 1; //用于给每个字符标号
	private Node tree = null;
	private Set<Object>[] followpos = null;
	private char[] numberToChar = null; //下标+1=字符标号 和字符对应
	private ArrayList<Set<Object>> states = new ArrayList<Set<Object>>();
	private Map<String, Object> result = new HashMap<>();
	private int logcount = 1;
	
	private REtoDFA(){
		printLogMessage("RetoDFA constructed:NULL");
	}
	
	private REtoDFA(String re) {
		this.re = re.replaceAll(String.valueOf(endSymbol),"");
		this.reNow =this.re+endSymbol;
		printLogMessage("RetoDFA constructed:"+this.re);
		printLogMessage("RetoDFA add endSymbol:"+reNow);
	}
	
	static public REtoDFA getREtoDFA(){
		if (rEtoDFASingleton == null)
			return new REtoDFA();
		return rEtoDFASingleton;
	}
	
	static public REtoDFA getREtoDFA(String re){
		if (rEtoDFASingleton == null)
			return new REtoDFA(re);
		rEtoDFASingleton.setRe(re);
		return rEtoDFASingleton;
	}

	@SuppressWarnings("unchecked")
	public String toDFA(){
		//构建树
		tree = constructTree(0,reNow.length()-1);
		if (tree == null)
			return "constructTree failed";
		followpos = new Set[tree.getRightNode().getNumber()];
		numberToChar = new char[tree.getRightNode().getNumber()];
		//printTree(); 
		result.put("Tree", getTree());
		printLogMessage("constructTree success\n");
		
		//计算nullable，firstpos，lastpos
		if (!computeAllNFP(tree))
			return "computeAllNFP failed";
		printLogMessage("computeAllNFP success\n");
		
		//计算followpos
		for (int i = 0;i < followpos.length;i++)
			followpos[i] = new HashSet<Object>();
		if (!computeAllFollowPos(tree))
			return "computeAllFollowPos failed";
		printFollowPos();
		printLogMessage("computeAllFollowPos success\n");
		
		//生成状态变迁表
		if (!constructStates())
			return "constructStates failed";
		printLogMessage("constructStates success\n");
		
		printLogMessage("REtoDFA success\n");
		return "success";
	}
	
	public Node constructTree(int startPosition,int overPosition){
		/*
		 * 递归构造RE树
		 */
		if(startPosition == overPosition){
			char charNow = reNow.charAt(startPosition);
			if (charNow == leftBracket || charNow == rightBracket 
			|| charNow == starSymbol || charNow == orSymbol)
				return null;
			return new Node(charNow, number++);
		}
		else {
			Node leftNodes = null,rightNodes = null,fatherNode = null;
			int nextPosition = getNextPart(startPosition,overPosition);
			if (nextPosition == -1)
				return null;
			else if (reNow.charAt(startPosition) == orSymbol)
				return null;
			else if (nextPosition == startPosition)
				leftNodes = constructTree(startPosition, nextPosition);
			else if (reNow.charAt(nextPosition) == starSymbol){
				leftNodes = new Node(starSymbol);
				Node childNode =constructTree(startPosition, nextPosition-1);
				if ( childNode != null){
					leftNodes.setLeftNode(childNode);
				}
				else 
					return null;
			}
			else if (reNow.charAt(startPosition) == leftBracket 
					&& reNow.charAt(nextPosition) == rightBracket){
				if (startPosition+1 == nextPosition)
					return null;
				else
					leftNodes = constructTree(startPosition+1, nextPosition-1);
			}
			
			if (leftNodes == null)
				return null;
			
			for (startPosition = nextPosition+1;
					startPosition <= overPosition;
					startPosition = nextPosition+1){
				nextPosition = getNextPart(startPosition,overPosition);
				if (nextPosition == -1)
					return null;
				else if (reNow.charAt(startPosition) == orSymbol){
					fatherNode = new Node(orSymbol);
					startPosition++;
				}
				else 
					fatherNode = new Node(andSymbol);
				
				if (nextPosition == startPosition)
					rightNodes = constructTree(startPosition, nextPosition);
				else if (reNow.charAt(nextPosition) == starSymbol){
					rightNodes = new Node(starSymbol);
					Node childNode =constructTree(startPosition, nextPosition-1);
					if ( childNode != null){
						rightNodes.setLeftNode(childNode);
					}
					else 
						return null;
				}
				else if (reNow.charAt(startPosition) == leftBracket 
						&& reNow.charAt(nextPosition) == rightBracket){
					if (startPosition+1 == nextPosition)
						return null;
					else
						rightNodes = constructTree(startPosition+1, nextPosition-1);
				}
				if (rightNodes == null)
					return null;
				fatherNode.setLeftNode(leftNodes);
				fatherNode.setRightNode(rightNodes);
				leftNodes = fatherNode;
				fatherNode = null;
				rightNodes = null;
			}
			return leftNodes;
		}
	}
	
	private int getNextPart(int startPostion,int overPosition){
		/* 获取下一个模块的终点位置，
		 * 得到结果为-1 . .* (...) (...)* |. |.* |(...) |(...)*
		 */
		char charnow = reNow.charAt(startPostion);
		if (charnow == leftBracket){
			for (int i = startPostion+1,count=0;i <= overPosition;i++){
				if (reNow.charAt(i) == leftBracket)
					count++;
				else if (reNow.charAt(i) == rightBracket ){
					if (count == 0){
						if (i < overPosition && reNow.charAt(i+1) == starSymbol)
							return i+1;
						return i;
					}
					else 
						count--;
				}
			}
			return -1;
		}
		else if (charnow == rightBracket){
			return -1;
		}
		else if (charnow == starSymbol){
			return -1;
		}
		else if (charnow == orSymbol){
			if (startPostion < overPosition 
					&& reNow.charAt(startPostion+1) != orSymbol
					&& reNow.charAt(startPostion+1) != endSymbol)
				return getNextPart(startPostion+1, overPosition);
			else 
				return -1;
		}
		else {
			if (startPostion < overPosition && reNow.charAt(startPostion+1) == starSymbol)
				return startPostion+1;
			return startPostion;
		}
	}
	
	public void printTree(){
		if (tree == null)
			printLogMessage("RetoDFA printTree:failed");
		else{
			printLogMessage("\ntree:");
			Queue<Node> queue = new LinkedList<Node>();
			queue.add(tree);
			queue.add(new Node(' '));
			String logMessage = "";
			while(!queue.isEmpty()){
				Node node = queue.poll();
				if (node.getKey() != ' '){
					logMessage += node.toString();
					if (node.getLeftNode() != null)
						queue.add(node.getLeftNode());
					if (node.getRightNode() != null)
						queue.add(node.getRightNode());
				}
				else {
					printLogMessage(logMessage);
					logMessage = "";
					if (!queue.isEmpty())
						queue.add(node);
				}
				
			}
		}
	}
	
	public boolean computeAllNFP(Node fatherNode){
		/*
		 * 递归计算所有节点NFP
		 */
		if (fatherNode != null){
			boolean result1=computeAllNFP(fatherNode.getLeftNode());
			boolean result2=computeAllNFP(fatherNode.getRightNode());
			computeNFP(fatherNode);
			return true && result1 && result2;
		}
		return true;
	}
	
	private void computeNFP(Node fatherNode){
		/*
		 * 计算nullable、firstpos、lastpos
		 */
		if (fatherNode.getKey() == starSymbol){
			fatherNode.setNullable(true);
			Node leftNode = fatherNode.getLeftNode();
			fatherNode.setFirstpos(new HashSet<>(leftNode.getFirstpos()));
			fatherNode.setLastpos(new HashSet<>(leftNode.getFirstpos()));
		}
		else if (fatherNode.getKey() == orSymbol){
			Node leftNode = fatherNode.getLeftNode();
			Node rightNode = fatherNode.getRightNode();
			Set<Object> firstPos=new HashSet<>(leftNode.getFirstpos());
			firstPos.addAll(new HashSet<>(rightNode.getFirstpos()));
			Set<Object> lastPos=new HashSet<>(leftNode.getLastpos());
			lastPos.addAll(new HashSet<>(rightNode.getLastpos()));
			
			fatherNode.setNullable(leftNode.isNullable() || rightNode.isNullable());
			fatherNode.setFirstpos(firstPos);
			fatherNode.setLastpos(lastPos);
		}
		else if (fatherNode.getKey() == andSymbol){
			Node leftNode = fatherNode.getLeftNode();
			Node rightNode = fatherNode.getRightNode();
			Set<Object> firstPos=new HashSet<>(leftNode.getFirstpos());
			if (leftNode.isNullable())
				firstPos.addAll(new HashSet<>(rightNode.getFirstpos()));
			
			Set<Object> lastPos=new HashSet<>(rightNode.getLastpos());
			if (rightNode.isNullable())
				lastPos.addAll(new HashSet<>(leftNode.getLastpos()));
			
			fatherNode.setNullable(leftNode.isNullable() && rightNode.isNullable());
			fatherNode.setFirstpos(firstPos);
			fatherNode.setLastpos(lastPos);
		}
		else if (fatherNode.getKey() == nullSymbol) {
			fatherNode.setNullable(true);
			numberToChar[fatherNode.getNumber()-1] = nullSymbol;
		}
		else {
			fatherNode.setNullable(false);
			fatherNode.getFirstpos().add(fatherNode.getNumber());
			fatherNode.getLastpos().add(fatherNode.getNumber());
			numberToChar[fatherNode.getNumber()-1] = fatherNode.getKey();
		}
		String logMessage =fatherNode.toString()+":"+fatherNode.isNullable()+" "
				+fatherNode.getFirstpos()+" "+fatherNode.getLastpos();
		printLogMessage("REtoDFA computeNFPresult:"+logMessage);
	}
	
	public boolean computeAllFollowPos(Node fatherNode){
		/*
		 * 递归计算所有followpos
		 */
		if (fatherNode != null){
			boolean result1=computeAllFollowPos(fatherNode.getLeftNode());
			boolean result2=computeAllFollowPos(fatherNode.getRightNode());
			computeFollowPos(fatherNode);
			return true && result1 && result2;
		}
		return true;
	}
	
	private void computeFollowPos(Node fatherNode){
		/*
		 * 计算followpos
		 */
		final Set<Object> lastpos;
		final Set<Object> firstpos;
		if (fatherNode.getKey() == starSymbol){
			lastpos = fatherNode.getLastpos();
			firstpos = fatherNode.getFirstpos();
		}
		else if (fatherNode.getKey() == andSymbol) {
			lastpos = fatherNode.getLeftNode().getLastpos();
			firstpos = fatherNode.getRightNode().getFirstpos();
		}
		else 
			return;
		for(Iterator<Object> iterator = lastpos.iterator(); iterator.hasNext();){
			int n = (int)iterator.next();
			if(followpos[n-1].addAll(firstpos)){
			String logMessage = "followpos["+(n)+"]="+followpos[n-1];
			printLogMessage("REtoDFA computeFollowPosResult:"+logMessage);
			}
		}
	}
	
	public void printFollowPos(){
		printLogMessage("\nFollowPos(n):");
		for (int i = 0;i < followpos.length;i++ )
			printLogMessage((i+1)+"-"+numberToChar[i]+" followpos["+(i+1)+"]="+followpos[i]);
	}
	
	public boolean constructStates(){
		states.add(new HashSet<>(tree.getFirstpos())); //S0
		String logMessage = "S0=firstpos(root)="+states.get(0);
		printLogMessage(logMessage);
		
		int markCount = 0; //当前mark的状态
		logMessage = "Mark S0";
		printLogMessage(logMessage);
		
		Set<Object> charSet = new HashSet<>();
		for (int i = 0;i < numberToChar.length;i++){
			if (numberToChar[i] != endSymbol)
				charSet.add(numberToChar[i]);
		}
		while(markCount < states.size()){
			final Set<Object> stateSet = states.get(markCount);
			
			for (Iterator<Object> iteratorChar = charSet.iterator();
					iteratorChar.hasNext();){
				char charNow = (char) iteratorChar.next();
				logMessage = charNow+":";
				Set<Object> newstateSet = new HashSet<>();
				
				for (Iterator<Object> iteratorState = stateSet.iterator();
						iteratorState.hasNext();){
					int numberNow = (int) iteratorState.next();
					if (numberToChar[numberNow-1] == charNow){
						newstateSet.addAll(followpos[numberNow-1]);
						logMessage += "followpos("+numberNow+") ";
					}
				}
				
				if (!newstateSet.isEmpty()){
					int newState = containsState(newstateSet);
					logMessage += "=" + newstateSet + "=S" + newState+ "    move(S" 
									+ markCount + "," + charNow + ")=S" + newState;
					if (newState == states.size())
						states.add(newstateSet);
				}
				printLogMessage(logMessage);
				logMessage = new String();
			}
			if (++markCount < states.size())
				printLogMessage("Mark S"+markCount);
		}
		return true;
	}
	
	private int containsState(Set<Object> newstateSet){
		for (int i = 0;i < states.size();i++){
			Set<Object> setNow = new HashSet<>(states.get(i));
			if (!setNow.addAll(newstateSet))
				return i;
		}
		return states.size();
	}
	
	public String getRe() {
		return re;
	}

	public void setRe(String re) {
		this.re = re;
		this.reNow = re+endSymbol;
		this.number = 1;
		this.tree = null;
		this.followpos = null;
		this.numberToChar = null;
		this.states = new ArrayList<Set<Object>>();
		this.result = new HashMap<>();
		this.logcount = 1;
	}

	public void printLogMessage (String logMessage){
		System.out.println(logMessage);
		result.put("Log"+logcount++, logMessage);
	}
	
	public Map<String, Object> getResult() {
		return result;
	}

	public void setResult(Map<String, Object> result) {
		this.result = result;
	}

	public int getLogcount() {
		return logcount;
	}

	public void setLogcount(int logcount) {
		this.logcount = logcount;
	}

	public String getReNow() {
		return reNow;
	}

	public void setReNow(String reNow) {
		this.reNow = reNow;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public ArrayList<Set<Object>> getStates() {
		return states;
	}

	public void setStates(ArrayList<Set<Object>> states) {
		this.states = states;
	}

	public Node getTree() {
		return tree;
	}

	public void setTree(Node tree) {
		this.tree = tree;
	}

	
	public Set<Object>[] getFollowpos() {
		return followpos;
	}

	public void setFollowpos(Set<Object>[] followpos) {
		this.followpos = followpos;
	}

	public char[] getNumberToChar() {
		return numberToChar;
	}

	public void setNumberToChar(char[] numberToChar) {
		this.numberToChar = numberToChar;
	}

	public char getLeftBracket() {
		return leftBracket;
	}

	public void setLeftBracket(char leftBracket) {
		this.leftBracket = leftBracket;
	}

	public char getRightBracket() {
		return rightBracket;
	}

	public void setRightBracket(char rightBracket) {
		this.rightBracket = rightBracket;
	}

	public char getStarSymbol() {
		return starSymbol;
	}

	public void setStarSymbol(char starSymbol) {
		this.starSymbol = starSymbol;
	}

	public char getOrSymbol() {
		return orSymbol;
	}

	public void setOrSymbol(char orSymbol) {
		this.orSymbol = orSymbol;
	}

	public char getAndSymbol() {
		return andSymbol;
	}

	public void setAndSymbol(char andSymbol) {
		this.andSymbol = andSymbol;
	}

	public char getNullSymbol() {
		return nullSymbol;
	}

	public void setNullSymbol(char nullSymbol) {
		this.nullSymbol = nullSymbol;
	}

	public char getEndSymbol() {
		return endSymbol;
	}

	public void setEndSymbol(char endSymbol) {
		this.endSymbol = endSymbol;
	}

}

class Node{
	private char key;
	private int number=-1;
	private Node leftNode = null;
	private Node rightNode = null;
	private boolean nullable = false;
	private Set<Object> firstpos = new HashSet<Object>();
	private Set<Object> lastpos = new HashSet<Object>();
	
	
	Node(char key){
		this.key = key;
	}
	
	Node(char key,int number){
		this.key = key;
		this.number = number;
	}
	
	Node(char key,Node leftNode,Node rightNode,Node fatherNode){
		this.key = key;
		this.leftNode = leftNode;
		this.rightNode = rightNode;
	}
	
	@Override
	public String toString(){
		if (number != -1)
			return String.valueOf(key)+number+" ";
		else
			return " "+key+" ";
	}
	
	public char getKey() {
		return key;
	}

	public void setKey(char key) {
		this.key = key;
	}

	public Node getLeftNode() {
		return leftNode;
	}

	public void setLeftNode(Node leftNode) {
		this.leftNode = leftNode;
	}

	public Node getRightNode() {
		return rightNode;
	}

	public void setRightNode(Node rightNode) {
		this.rightNode = rightNode;
	}

	public boolean isNullable() {
		return nullable;
	}

	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	public Set<Object> getFirstpos() {
		return firstpos;
	}

	public void setFirstpos(Set<Object> firstpos) {
		this.firstpos = firstpos;
	}

	public Set<Object> getLastpos() {
		return lastpos;
	}

	public void setLastpos(Set<Object> lastpos) {
		this.lastpos = lastpos;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

}