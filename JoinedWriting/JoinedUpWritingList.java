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
	public static String ignore; 
	
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
		//	System.out.println(singlyJoined(a, b));
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
	
	
	
	//The common part is at least half as long as both of the words
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
	
	public static String overlappingConcat(String a, String b) {                              
		  int i;
		  int l = a.length();
		  for (i = 0; i < l; i++) {
		    if (b.startsWith(a.substring(i))) {
		      return a.substring(0, i) + b;
		    }
		  }
		  return a + b;
	}
	
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
							System.out.println("CHAIN FINISHED \"" + a + "\" matched with \"" + END+"\" with -" + target + "-");
							doublyCount= doublyCount + 2;
							chain.appendNode(END);
							return chain.printNodes();
						}
					}
				}
			}
		}
		
		if(backOneStep){ //If we are backtracking
			a = curr.prev.data; //set the word which we are taking the suffix of to the previous node. 
			System.out.println("WE BACKTRACKED FROM " + curr.data);
			System.out.println("\nSearching for matches for "+ a + "... by looking at alternatives to " + curr.data);
			System.out.println("CURRENT CHAIN: " + chain.printNodes() + ", where we need to replace " + curr.data + "\n");
			
			//the node we are looking at needs to be replaced by an alternative match
			for(String x : curr.alternativeMatches){
				if(curr.ignoreList.contains(x) || curr.data.equals(x)){
					continue;  //skip the word if it is in the ignore list or if we are comparing to itself
				}
				System.out.println(x + " was an alternative to " + curr.data + " that matched " + a);
				
				for(int i=commonPartSize; i<= Math.min(a.length(), x.length());i++){
					prefixTarget = a.substring(a.length()-i, a.length());
					xPrefix = x.substring(0, i);
					
					if(i >= ((x.length()/2))){ 
						if(prefixTarget.equals(xPrefix)){
							thisMatch = x; 
							System.out.println("WE ARE REPLACING " + curr.data + " WITH " + thisMatch);
							curr.data = thisMatch; 
							backOneStep = false; 
							return findDoubleChain(curr, END); //go forward as normal
						}
					}
				}
			}
			//NOTHING MATCHED!! Backtrack again?
		//	System.out.println("Found no match! Backtrack again?");
		//	return findDoubleChain(curr.prev, END);
			/*if(curr.prev.numAlternatives > 0){
				System.out.println("Going back to look at other matches for " + curr.prev.data);
				return findDoubleChain(curr.prev, END);
			}
			*/
		}else{ //NOT backtracking - going forward 
			System.out.println("\nSearching for matches for "+ curr.data + "...");
			System.out.println("CURRENT CHAIN: " + chain.printNodes() + "\n");
	
			NEXT_WORD: for(String x : dict){
				if(a.equals(x)){ //If same word, dont need to check
					continue;
				}
				
				//If the word has already been used in the chain, dont use it again?
			
				if(curr.prev != null){
					check = overlappingConcat(curr.prev.data, x); //this isnt working because when we backtrack, prev is not correct
				//	System.out.println("Is \"" + curr.data + "\" contained in \"" + check + "\"");
					if(! check.contains(curr.data)){
						continue; 
					}
				}
				
				//TODO: to make more efficient, only go into this for loop if x and a share common letters? 
				
				//if either prev has not been initiated, or it has and the check is true
				//could use String.startsWith or String.endsWith, or String.regionMatches
				
			//	if((!prev.equals("") && check.contains(a)) || (prev.equals(""))|| backOneStep){
				//	System.out.println("YES");
				for(int i=commonPartSize; i<= Math.min(a.length(), x.length());i++){
	
					//look for a word whose prefix == a's suffix 
					prefixTarget = a.substring(a.length()-i, a.length());
					//System.out.println("Comparing \"" + a + "\" with \"" + x + "\" with target " + prefixTarget);
					xPrefix = x.substring(0, i);
					
					//if the minSize is greater or equal to half of x 
					if(i >= ((x.length()/2))){ //if x is 5 letters, this is true. 
						//System.out.println("The common part is size " + i + " and is at least half of word " + x);
				
						//if backOneStep is true, this means we just came backwards. 
						if(prefixTarget.equals(xPrefix)){
							System.out.println("\"" + a + "\" matched with \"" + x + "\" through common part -" + prefixTarget + "-");
							
							if(thisMatch.equals("")){ //only first time enter this loop. 
								thisMatch = x; 
								System.out.println("First match was " + thisMatch);
							}else{
								otherOptions.add(x); //these will be added to next node
								System.out.println("Added " + x + " as an alternative to " + thisMatch + " to match to " + a);
								numMatches++; //Other words that matched the suffix
							}
							continue; //break out of this loop
						}
					}
				}
			}
		}
		
		//There was a match and we have not backtracked.  
		if(!thisMatch.equals("")){
			chain.appendNode(thisMatch, numMatches, otherOptions);
			doublyCount++; 	
			return findDoubleChain(chain.getCurrentNode(), END);
		}else{ //there was no match so go back a step - We have NOT added a new node. 
			System.out.println(curr.data + " was a DEAD END - add to ignore list and go back a step");
			if(doublyCount > 0){ //If we have added at least one word to the chain
				LinkedList.Node loop = curr;  
				while(curr.prev!=null){ //loop backwards through chain until we find a 
					if(curr.numAlternatives > 0 && curr.ignoreList.size()!=curr.numAlternatives){
						System.out.println(curr.data + " has " + curr.numAlternatives + " alternatives! lets explore those!");
						System.out.println("we have explored " + curr.ignoreList.size() + " of them");
						backOneStep = true; 
						return findDoubleChain(curr, END);
					}
					curr.prev.ignoreList.add(curr.data);
					curr = curr.prev; 
					System.out.println("gone back to " + curr.data);
				}
				System.out.println("Couldn't find any alternatives");
			}
		}
		
		
				
				
				//look at numMatches of previous node. if more than one, then we examine
			/*	int numAlternatives = curr.numAlternatives;
				System.out.println(curr.data + " had " + numAlternatives + " alternatives");
				System.out.println("we have explored " + curr.ignoreList.size() + " of them");
				if(numAlternatives > 0){ //if there were other alternatives
					backOneStep = true;
					curr.ignoreList.add(a); //should this be a queue?
					//TODO: dont need both these lists? 
					//chain.addToIgnore(curr, a); //Add this word to the ignore list for the next search to ensure we never come back here. 		
					//curr.alternativeMatches.remove(a);
					
					//a should be prev, prev should be a's prev 
				//	System.out.println("This node was " + a  + " and the node before " + chain.getCurrentNode().data + " was " + chain.getCurrentNode().prev.data);
					return findDoubleChain(curr, END); //ISSUE HERE!! - need to be able to go recurisively backwards. 
				}else{
					while(curr.prev!=null && curr.numAlternatives == 0){
						curr.prev.ignoreList.add(curr.data);
						curr = curr.prev; 
						System.out.println("gone back to " + curr.data);
					}
					if(curr.numAlternatives > 0){
						System.out.println(curr.data + " has " + curr.numAlternatives + " alternatives! lets explore those!");
						backOneStep = true; 
						return findDoubleChain(curr, END);
					}
					//as soon as it is not 0 
				}*/
		
				
				
				/*else if(curr.prev.numAlternatives > 0){ //this node had no alternatives, keep going back until we find one that did. 
					System.out.println(curr.data + " had no alternatives. We will backtrack further");
					System.out.println("Going back to look at other matches for " + curr.prev.data);
					backOneStep = true; 
					curr.prev.ignoreList.add(a);
					return findDoubleChain(curr.prev, END);
				}*/
			
				
		//		System.out.println("Found no match! Backtrack again?");
				//	return findDoubleChain(curr.prev, END);
					
				
				//else if(chain.getNumMatches(prev) > 0 ){
					//chain.addToIgnore()
					//chain.addToIgnore(chain.getCurrentNode(), prev);
					//System.out.println("need to go back 2");
		
		
	
		
			//minSize is of the common part, not the length of the word. 
			//the suffix/prefix of the next word needs to be at least half as long
			//as the word itself as well as the word we are comparing to. 

			//for every word in the dictionary, pair it with A and find 
			//another word Y which is at least minSize long and has its prefix
			//matching the suffix of A, and its suffix matching the prefix of X
		return NOTFOUND;  
	}
	
	
	
	
	
	//This method recursively finds a chain of doubly joined strings from 
	//the starting word to END 
	
	//Just use nodes? -  still need end. 
	
	/*
	public static String findDoubleChain(String a, String END, String prev){
		String xPrefix; 
		String target; 
		String endTarget;
		String prefixTarget; 
		int commonPartSize = a.length()/2; 
		//Node thisNode = chain.getCurrentNode();
		int numMatches = 0;
		String thisMatch = "";
		ArrayList<String> otherOptions = new ArrayList<String>(); 
	
		//Check if we are complete. - have word check 
		for(int i=commonPartSize; i<= Math.min(a.length(), END.length()); i++){
			target = a.substring(a.length()-i, a.length());
			endTarget = END.substring(0, i);
			
			if(i >= (END.length()/2)){
				if(target.equals(endTarget)){
			//		System.out.println("CHAIN FINISHED \"" + a + "\" matched with \"" + END+"\" with -" + target + "-");
					doublyCount= doublyCount + 2;
					chain.appendNode(END);
					return chain.printNodes();  //a = b; stopping case 
				}
			}

		}
	
		
		NEXT_WORD: for(String x : dict){
			if(a.equals(x)){ //If same word, dont need to check
				continue;
			}
			
			if(backOneStep){ //If we are backtracking
		//		System.out.println("\nSearching for a match for "+ a + " (came BACK from " + prev + ")");
				if(! chain.getCurrentNode().alternativeMatches.contains(x)){
				//	System.out.println("Word not an option");
					continue NEXT_WORD;
				}
				
				//These are words only within alternativeMatches - dont add them again!
				for(int i=commonPartSize; i<= Math.min(a.length(), x.length());i++){
					prefixTarget = a.substring(a.length()-i, a.length());
		//			System.out.println("Comparing \"" + a + "\" with \"" + x + "\" with target " + prefixTarget);
					xPrefix = x.substring(0, i);
					
					if(i >= ((x.length()/2))){ 
						//we just came backwards. 
						if(prefixTarget.equals(xPrefix)){
							thisMatch = x; 
			//				System.out.println("MATCH was " + thisMatch);
							break NEXT_WORD; 
						}
					}
				}
				System.out.println("Couldnt find a match");
			}else{
			//	System.out.println("\nSearching for a match for "+ a + " (came from " + prev + ")");
			}
			
			//For every string in the ignore list of thisNode
			for(String y : chain.getCurrentNode().ignoreList){
				if(x.equals(y)){
				//	System.out.println("Ignore this word " + y);
					continue NEXT_WORD;
				}
			}
			
			//If the word has already been used in the chain, dont use it again?
			String check; 
			if(chain.getCurrentNode().prev != null){
				check = overlappingConcat(chain.getCurrentNode().prev.data, chain.getCurrentNode().data); //this isnt working because when we backtrack, prev is not correct
				//prev should be prev's prev (do in linkedlist!) 
			//	System.out.println("Is \"" + a + "\" contained in \"" + check + "\"");
				if(! check.contains(a)){
					continue; 
				}
			}else{ //There is no previous element to compare to - we are looking at the head 
				//System.out.println("No previous element");
			}
			
			//TODO: to make more efficient, only go into this for loop if x and a share common letters? 
			
			//if either prev has not been initiated, or it has and the check is true
			//could use String.startsWith or String.endsWith, or String.regionMatches
			
		//	if((!prev.equals("") && check.contains(a)) || (prev.equals(""))|| backOneStep){
			//	System.out.println("YES");
			for(int i=commonPartSize; i<= Math.min(a.length(), x.length());i++){

				//look for a word whose prefix == a's suffix 
				prefixTarget = a.substring(a.length()-i, a.length());
				//System.out.println("Comparing \"" + a + "\" with \"" + x + "\" with target " + prefixTarget);
				xPrefix = x.substring(0, i);
				
				//if the minSize is greater or equal to half of x 
				if(i >= ((x.length()/2))){ //if x is 5 letters, this is true. 
					//System.out.println("The common part is size " + i + " and is at least half of word " + x);
			
					//if backOneStep is true, this means we just came backwards. 
					if(prefixTarget.equals(xPrefix)){
				//		System.out.println("\"" + a + "\" matched with \"" + x + "\" through common part -" + prefixTarget + "-");
						
						if(thisMatch.equals("")){ //only first time enter this loop. 
							thisMatch = x; 
					//		System.out.println("First match was " + thisMatch);
						}else{
							otherOptions.add(x);
						//	System.out.println("Added " + x + " to other options");
							numMatches++; //Other words that matched the suffix
						}
				
						continue; //break out of this loop
					}
				}
			}
			//}
		}
		
		//There was a match! 
		if(!thisMatch.equals("")){
			if(backOneStep){ //We are recovering from reverse
				chain.replaceValue(prev, thisMatch); //If we replace rather than delete, we can keep ignore list and other info
				backOneStep = false; 
			}else{
				chain.appendNode(thisMatch, numMatches, otherOptions);
				doublyCount++; 
			}
			return findDoubleChain(thisMatch, END, a);
		}
		
	//	System.out.println(a + " was a DEAD END - add to ignore list");
		if(doublyCount > 0){ //If we have added at least one word to the chain
			//look at numMatches of previous node. if more than one, then we examine
			int previousAlternatives = chain.getNumMatches(a); 
			if(previousAlternatives > 0){ //if there were other alternatives
				backOneStep = true;
				
				//TODO: dont need both these lists? 
				chain.addToIgnore(chain.getCurrentNode(), a); //Add this word to the ignore list for the next search to ensure we never come back here. 		
				chain.getCurrentNode().alternativeMatches.remove(a);
				
				//a should be prev, prev should be a's prev 
			//	System.out.println("This node was " + a  + " and the node before " + chain.getCurrentNode().data + " was " + chain.getCurrentNode().prev.data);
				return findDoubleChain(prev, END, a); //ISSUE HERE!! - need to be able to go recurisively backwards. 
			}else if(chain.getNumMatches(prev) > 0 ){
				//chain.addToIgnore()
				//chain.addToIgnore(chain.getCurrentNode(), prev);
				//System.out.println("need to go back 2");
				
			}
		}
			
			//minSize is of the common part, not the length of the word. 
			//the suffix/prefix of the next word needs to be at least half as long
			//as the word itself as well as the word we are comparing to. 

			//for every word in the dictionary, pair it with A and find 
			//another word Y which is at least minSize long and has its prefix
			//matching the suffix of A, and its suffix matching the prefix of X
		return NOTFOUND;  
	}*/
}