package JoinedWriting; 
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
	public static ArrayList<String> dict;
	public static int singlyCount;
	public static int doublyCount;
	public static final String NOTFOUND = "INVALID";
	public static LinkedList chain; 
	public static Boolean backOneStep;
	
	public static void main(String[]args){

		chain = new LinkedList(); 
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
		String completeChain = findSingleChain(a, b, chainBuilder); 
		
		if(completeChain.equals("INVALID")){
			return "0";
		}else{
			result.append(singlyCount + " ");
			result.append(a + " ");
			result.append(completeChain);
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
			
			endTarget = END.substring(0, i); 
			
			if(target.equals(endTarget)&& target.length()>0){//suffix == prefix
			//System.out.println("CHAIN FINISHED \"" + a + "\" matched with \"" + END+"\" with -" + target + "-");
				singlyCount= singlyCount + 2; //first word is matched to last word
				return res.toString();  //a = b; stopping case 
			}
		}
		
		for(String x : dict){ //for every word in dictionary
			//find a word X which starts with the same minSize or more letters as A ends with. 
			//check = overlappingConcat(prev, x);
			minSize = Math.min(a.length(), x.length())/2; 
			//minsize changes with every word pair
			for(int i=minSize; i < Math.min(a.length(), x.length()); i++){
				target = a.substring(a.length()-i, a.length()); 
				
				xTarget = x.substring(0, i);  
				
				if(target.equals(xTarget) && target.length()>0){ //Found a pair - these two words are singly joined 
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
	
	//This method returns a chain of doubly joined words, where the common part
	//in each pair is at least half as long as both of the words.
	public static String doublyJoined(String a, String b){
		String previous = ""; 
		backOneStep = false; 
		chain.appendNode(a);
		
		String completeChain = findDoubleChain(chain.getCurrentNode(), b); 
		
		if(completeChain.equals("INVALID")){
			return "0";
		}else{
			return doublyCount + " " + chain.printNodes();
		}
	}
	
	//This method returns the merged string, a + b, where the overlapping 
	//characters are not duplicated. 
	public static String overlappingConcat(String a, String b) {                              
		  for(int i = 0; i < a.length(); i++) {
		    if(b.startsWith(a.substring(i))) {
		      return a.substring(0, i) + b;
		    }
		  }
		  return a + b;
	}
	
	//This method recursively calls itself to find a chain of words which 
	//are doubly joined from start to END.
	public static String findDoubleChain(LinkedList.Node curr, String END){
		String xPrefix; 
		String target; 
		String endTarget;
		String prefixTarget;
		int numMatches = 0;
		String thisMatch = "";
		ArrayList<String> otherOptions = new ArrayList<String>(); 
		
		String a = curr.data;
		int commonPartSize = a.length()/2; 
		
		//STOPPING CASE: Check if we are complete - if curr's suffix == END's prefix
		String check = ""; 
		if(curr.prev != null){
			check = overlappingConcat(curr.prev.data, END);
			if(check.contains(curr.data)){
				for(int i=commonPartSize; i<= Math.min(a.length(), END.length()); i++){
					target = a.substring(a.length()-i, a.length());
					endTarget = END.substring(0, i);
					
					if(i >= (END.length()/2)){
						if(target.equals(endTarget)){
						//	System.out.println("CHAIN FINISHED \"" + a + "\" matched with \"" + END+"\" with -" + target + "-");
							doublyCount= doublyCount + 2;
							chain.appendNode(END);
							return chain.printNodes();
						}
					}
				}
			}
		}
		
		if(backOneStep){ //If we are backtracking
			for(String x : curr.alternativeMatches){
				if(curr.ignoreList.contains(x) || curr.data.equals(x)){
					continue;  //skip the word if it is in the ignore list or if we are comparing to itself
				}
				thisMatch = x; 
				break;
			}
		}else{ //NOT backtracking - going forward 
		//	System.out.println("\nSearching for matches for "+ curr.data + "...");
			//System.out.println("CURRENT CHAIN: " + chain.printNodes() + "\n");
	
			NEXT_WORD: for(String x : dict){
				
				
				if(chain.containsNode(x)){//If the word has already been used in the chain, dont use it to avoid loops
					continue NEXT_WORD;
				}
				
				if(a.equals(x)){ //If same word, dont need to check
					continue NEXT_WORD;
				}
				
				if(curr.prev != null){ 
					check = overlappingConcat(curr.prev.data, x);
					if(!check.contains(curr.data)){ //Only go forward if they have similar letters
						continue NEXT_WORD;  
					}
				}
				
				for(int i=commonPartSize; i<= Math.min(a.length(), x.length());i++){
					//look for a word whose prefix == a's suffix 
					prefixTarget = a.substring(a.length()-i, a.length());
					xPrefix = x.substring(0, i);
					
					//if the minSize is greater or equal to half of x 
					if(i >= ((x.length()/2))){
						if(prefixTarget.equals(xPrefix)){
							
							if(thisMatch.equals("")){ //only first time enter this loop. 
								thisMatch = x; 
							}else{
								otherOptions.add(x); //these will be added to next node
							}
							continue NEXT_WORD; //break out of this loop
						}
					}
				}
			}
		}
		
		//There was a match and we have not backtracked.  
		if(!thisMatch.equals("")){
			if(backOneStep){
				backOneStep = false; 
			}else{
				chain.addAlternatives(curr, otherOptions);
			}
			
			chain.appendNode(thisMatch);
			doublyCount++; 	
			//System.out.println("New tail is " + chain.getCurrentNode().data);
			return findDoubleChain(chain.getCurrentNode(), END);
		}else{ //there was no match so go back a step - We have NOT added a new node. 
		//	System.out.println(curr.data + " was a DEAD END - need to go back a step");
			
			while(curr.prev != null){ //mark this node to be ignored by the one before. 
				LinkedList.Node prevNode = curr.prev; 
				if(curr.prev.alternativeMatches.size() > 0){ //if there are alternative matches
					curr.prev.ignoreList.add(curr.data); 
					chain.removeNode(curr);
					backOneStep = true;
					
					return findDoubleChain(prevNode, END);
				}
				
				//this previous node did not alternatives, so look at the one before
				curr.prev.ignoreList.add(curr.data);  //TODO: This prevents it ever going back in this direction?
				chain.removeNode(curr);
				curr = curr.prev;
			}
		}
		return NOTFOUND;  
	}
}