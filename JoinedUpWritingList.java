import java.util.*;
import java.io.*; 

/*
 * Etude 7: Joined Up Writing 
 *
 * Authors: Mika Smith and Mathew Boyes
 * 
 * A program that takes two words and returns the words
 * that are singly joined up or doubly joined up, where 
 * two words join up if a proper suffix of one is a proper
 * prefix of the next. 
 * 
 * Returns for a given dictionary, a shortest sequence of 
 * joined up words that link a beginning word to an end word. 
 */



public class JoinedUpWritingList{
	
	public static class Node {
		   String data;
		   ArrayList<String> alternativeMatches = new ArrayList<String>(); 
		   ArrayList<String> ignoreList = new ArrayList<String>(); 
		   Node next;
		   int numAlternatives; 
		   
		   public Node(){
			   data = null; 
			   numAlternatives = 0; 
		   }
		   public Node(String data){
			   this.data = data; 
			   numAlternatives = 0; 
		   }
		   
		   public Node(String data, int numAlternatives){
			   this.data = data; 
			   this.numAlternatives = numAlternatives; 
		   }
		   
		   public Node(String data, int numAlternatives, ArrayList<String> alternativeMatches){
			   this.data = data; 
			   this.numAlternatives = numAlternatives;
			   this.alternativeMatches = alternativeMatches; 
		   }
	}
	
	public static ArrayList<String> dict;
	public static int singlyCount;
	public static int doublyCount;
	public static final String NOTFOUND = "INVALID";
	public static Node head; 
	public static Boolean backOneStep;
	public static String ignore; 
	
	public static void main(String[]args){
		Scanner sc = new Scanner(System.in);
		while(sc.hasNextLine()){
			String a = args[0];
			String b = args[1];
			dict = new ArrayList<String>();//scan in a given dictionary - up to 100,000 words
			while(sc.hasNext()){
				dict.add(sc.next());
			}
			System.out.println(singlyJoined(a, b));
			System.out.println(doublyJoined(a, b));
		}
		sc.close(); 
	}
	
	//The common part is at least half as long as one of the two words
	public static String singlyJoined(String a, String b){
		StringBuilder result = new StringBuilder(); 
		StringBuilder chainBuilder = new StringBuilder(); 
		String chain = findSingleChain(a, b, chainBuilder); 
		
		if(chain.equals("INVALID")){
			return "0";
		}else{
		//	result.append("SINGLY ");
			result.append(singlyCount + " ");
			result.append(a + " ");
			result.append(chain);
			result.append(b);
			return result.toString(); 
		}
	}
	
	//This method recursively finds a chain of singly joined strings from 
	//the starting word to END 
	public static String findSingleChain(String a, String END, StringBuilder res){
		String xTarget;
		String target; 
		String endTarget;
		int minSize;
		
		minSize = Math.min(a.length(), END.length())/2; 
		//STOPPING CASE - if a word whose suffix is a prefix of END, stop. 
		for(int i=minSize; i< Math.min(a.length(), END.length()); i++){
		//	System.out.println("Min size is " + i);
			target = a.substring(a.length()-i, a.length());  //(inclusive, exclusive) suffix
		//	System.out.println("Target is " + target);
			
			endTarget = (i >= END.length()) ? END : END.substring(0, i); //is this necessary 
			
			if(target.equals(endTarget)){//suffix == prefix
			//	System.out.println("CHAIN FINISHED \"" + a + "\" matched with \"" + END+"\" with -" + target + "-");
				singlyCount= singlyCount + 2; //first word is matched to last word
				return res.toString();  //a = b; stopping case 
			}
		}
		
		for(String x : dict){ //for every word in dictionary
			//find a word X which starts with the same minSize or more letters as A ends with. 
			
			minSize = Math.min(a.length(), x.length())/2; 
			//minsize changes with every word pair
			for(int i=minSize; i < Math.min(a.length(), x.length()); i++){
			//	System.out.println("Min size is " + i);
				target = a.substring(a.length()-i, a.length());  //(inclusive, exclusive) 
			//	System.out.println("Target is " + target);
				
				xTarget = (i >= x.length()) ? x : x.substring(0, i);  //is this necessary?
				
				if(target.equals(xTarget)){ //Found a pair - these two words are singly joined 
			//		System.out.println("\"" + a + "\" matched with \"" + x + "\" with -" + target + "-");
					res.append(x +" "); //append the word to the resulting chain
					singlyCount++; 
					return findSingleChain(x, END, res);
					//use this new word as A and find another word X -call recursively until we match END 
				}
				//If not found, look at for a larger common part of the same two words
			}
			//if not found, keep looping to the next word
			
		}//If we have looked through the whole dictionary, stop and return invalid. 
		
		return NOTFOUND; 
	}
	
	public static void appendNode(String data){
		if(head==null){ //No head exists so we create one
			head = new Node(data);
			return;
		}
		Node current = head; //pointer that starts at head of linked list
		while(current.next!=null){ //go through list
			current = current.next;
		}
		current.next= new Node(data);
	}
	
	public static void appendNode(String data, int numMatches, ArrayList<String> alternativeMatches){
		if(head==null){ //No head exists so we create one
			head = new Node(data, numMatches, alternativeMatches);
	//		System.out.println("Added node " + head.data);
			return;
		}
		Node current = head; //pointer that starts at head of linked list
		while(current.next!=null){ //go through list
			current = current.next;
		}
		current.next= new Node(data, numMatches, alternativeMatches);
//		System.out.println("Added node "  + current.next.data);
	}
	
	
	public static void replaceValue(String oldData, String newData){
		if(head == null) return;
		
		if(head.data == oldData){ //if we need to delete the head
			head.data = newData; 
			return;
		}
		//walk though linked list and stop one 
		//before the element we want to delete
		Node curr = head;
		while(curr.next !=null){
			//if we've found a node which 
			//matches the one we want to delete
			if(curr.next.data == oldData){//cut out next value
			//	System.out.println("Replacing "+ oldData + " with " + newData);
				curr.next.data = newData; 
				return;
				//walk around the element.
			}
			curr = curr.next; //continue walking
		}
	}
	
	public static int getNumMatches(String data){
		if(head == null) return -1;
		
		if(head.data == data){
			return head.numAlternatives;
		}
		//walk though linked list and stop one 
		//before the element we want to delete
		Node curr = head;
		while(curr.next !=null){
			//if we've found a node which 
			//matches the one we want to delete
			if(curr.next.data ==data){//cut out next value
				return curr.next.numAlternatives; 
				//walk around the element.
			}
			curr = curr.next; //continue walking
		}
		return -1; 
	}
	
	public static void addToIgnore(Node n, String data){
		if(head == n){
			head.ignoreList.add(data);
		}
		//walk though linked list and stop one 
		//before the element we want to delete
		Node curr = head;
		while(curr.next !=null){
			//if we've found a node which 
			//matches the one we want to delete
			if(curr.next == n){//cut out next value
				curr.next.ignoreList.add(data);
				//walk around the element.
			}
			curr = curr.next; //continue walking
		}
	}
	
	public static Node getCurrentNode(){
		//walk though linked list and stop one 
		//before the element we want to delete
		Node current = head;
		while(current.next!=null){ //go through list
			current = current.next;
		}
		return current; //should return head if there is only the head  
	}
	
	public static String printNodes(){
		StringBuilder sb = new StringBuilder(); 
		if(head != null){ 
	        Node current = head; 
	        while(current != null){
	            sb.append(current.data + " ");
	            current = current.next;
	        }
		}
		return sb.toString();
	}
	
	//The common part is at least half as long as both of the words
	public static String doublyJoined(String a, String b){
		StringBuilder result = new StringBuilder(); 
		StringBuilder chainBuilder = new StringBuilder();
		String previous = ""; //pass an intermediate string
		backOneStep = false; 
	//	Node head = null; 
		appendNode(a);
		
		String chain = findDoubleChain(a, b, previous, chainBuilder); 
		
		if(chain.equals("INVALID")){
			return "0";
		}else{
			//result.append("DOUBLY ");
			//result.append(doublyCount + " ");
			//result.append(a + " ");
		//	result.append(chain);
		//	result.append(b);
			//return result.toString(); 
			return doublyCount + " " + printNodes();
		}
	}
	
	//This method recursively finds a chain of doubly joined strings from 
	//the starting word to END 
	public static String findDoubleChain(String a, String END, String prev, StringBuilder res){
		String xPrefix; 
		String ySuffix;
		String target; 
		String endTarget;
		String prefixTarget; 
		String suffixTarget; 
		int commonPartSize = a.length()/2; 
		Node thisNode = getCurrentNode();
		int numMatches = 0;
		String thisMatch = "";
		ArrayList<String> otherOptions = new ArrayList<String>(); 
	
		for(int i=commonPartSize; i<= Math.min(a.length(), END.length()); i++){
			target = a.substring(a.length()-i, a.length());
	//		System.out.println("\nIS CHAIN COMPLETE?... Comparing " + a + " with " + END + " using target " + target);
			
			endTarget = END.substring(0, i);
			
			if(i >= (END.length()/2)){
				if(target.equals(endTarget)){
			//		System.out.println("CHAIN FINISHED \"" + a + "\" matched with \"" + END+"\" with -" + target + "-");
					doublyCount= doublyCount + 2;
					appendNode(END);
					return res.toString();  //a = b; stopping case 
				}
			}

		}
		
		NEXT_WORD: for(String x : dict){
			if(a.equals(x)){ //If same word, dont need to check
				continue;
			}
			
			if(backOneStep){ //If we are backtracking
				if(! thisNode.alternativeMatches.contains(x)){
				//	System.out.println("Word not an option");
					continue NEXT_WORD;
				}
			}
			
			//For every string in the ignore list of thisNode
			for(String y : thisNode.ignoreList){
				if(x.equals(y)){
				//	System.out.println("Ignore this word " + y);
					continue NEXT_WORD;
				}
			}
			
		/*	String check = "";
			check += prev;
			check += a; */
		//	System.out.println("Does " + x + " fit in  intermediate containing " + check);
			
			//TODO: to make more efficient, only go into this for loop if x and a share common letters? 
			for(int i=commonPartSize; i<= Math.min(a.length(), x.length());i++){

				//look for a word whose prefix == a's suffix 
				prefixTarget = a.substring(a.length()-i, a.length());
		//		System.out.println("Comparing \"" + a + "\" with \"" + x + "\" with target " + prefixTarget);
				xPrefix = x.substring(0, i);
				
				//if the minSize is greater or equal to half of x 
				if(i >= ((x.length()/2))){ //if x is 5 letters, this is true. 
					//System.out.println("The common part is size " + i + " and is at least half of word " + x);
			
					//if backOneStep is true, this means we just came backwards. 
					if(prefixTarget.equals(xPrefix)){
				//		System.out.println("\"" + a + "\" matched with \"" + x + "\" through common part -" + prefixTarget + "-");
						
						if(thisMatch.equals("")){ //only first time enter this loop. 
							thisMatch = x; 
							//System.out.println("First match was " + thisMatch);
						}else{
							otherOptions.add(x);
							//System.out.println("Added " + x + " to other options");
							numMatches++; //Other words that matched the suffix
						}
				
						continue; //break out of this loop
					}
				}
			}
		}
		
		//There was a match! 
		if(!thisMatch.equals("")){
			if(backOneStep){ //We are recovering from reverse
				replaceValue(prev, thisMatch); //If we replace rather than delete, we can keep ignore list and other info
				backOneStep = false; 
			}else{
				appendNode(thisMatch, numMatches, otherOptions);
				doublyCount++; 
			}
			res.append(thisMatch + " "); 
			return findDoubleChain(thisMatch, END, a, res);
		}
		
		//System.out.println(a + " was a DEAD END - add to ignore list");
		if(doublyCount > 0){ //If we have added at least one word to the chain
			
			//look at numMatches of previous node. if more than one, then we examine
			int previousAlternatives = getNumMatches(a); 
			if(previousAlternatives > 0){ //if there were other alternatives
				backOneStep = true;
				addToIgnore(thisNode, a); //Add this word to the ignore list for the next search to ensure we never come back here. 
				
				return findDoubleChain(prev, END, a, res);
			}
		}
			
			//minSize is of the common part, not the length of the word. 
			//the suffix/prefix of the next word needs to be at least half as long
			//as the word itself as well as the word we are comparing to. 

			//for every word in the dictionary, pair it with A and find 
			//another word Y which is at least minSize long and has its prefix
			//matching the suffix of A, and its suffix matching the prefix of X
		return NOTFOUND;  
	}
}