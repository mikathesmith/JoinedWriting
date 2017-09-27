//package JoinedWriting; 
import java.util.*;

import java.io.*; 



/*
 * Etude 7: Joined Up Writing 
 *
 * Authors: Mika Smith and Matthew Boyes
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
	public static final String NOTFOUND = "INVALID";
	//public static LinkedList singleChain; 
	//public static LinkedList doubleChain; 
//	/public static Boolean backOneStep;
	public static String first="";
	public static String last=""; 
	
	//Storing words as trees of characters 
    public static TreeNode tree = new TreeNode();
	
	public static void main(String[]args){

		
		Scanner sc = new Scanner(System.in);
		while(sc.hasNextLine()){
			first = args[0];
			last = args[1];
			if(first.equals(last)){
				System.out.println("1 " + first + " " + last);
				break;
			}
			
			String word; 
			dict = new ArrayList<String>();//scan in a given dictionary - up to 100,000 words
			while(sc.hasNextLine()){
				word = sc.nextLine(); 
				if(!word.equals("")){
					dict.add(word);
				}
			}
			
			for (String w : dict) {
	            tree.addNode(w, 0);
	        }
			//Collections.sort(dict); //Sort the dictionary alphabetically
			
		   // System.out.println(singlyJoined(a, b)); //this affects the double chain?
			//System.out.println(doublyJoined(a, b));
			printList(getSinglyJoinedArray());
	        printList(getDoublyJoinedArray());
		}
		sc.close(); 
	}
	
	 public static ArrayList<String> getDoublyJoinedArray() {
	        // If the first AND second word overlaps by half or more,
	        // we want to include it
		 	Boolean doublyJoined = true; 
	        return getLinkedArray(doublyJoined);
	    }
	 
	 public static ArrayList<String> getSinglyJoinedArray() {
	        // If the first AND second word overlaps by half or more,
	        // we want to include it
		 	Boolean doublyJoined = false; 
	        return getLinkedArray(doublyJoined);
	    }
	 
	 public static void printList(ArrayList<String> list) {
	        System.out.print(list.size());
	        for (String item : list) {
	            System.out.print(" " + item);
	        }
	        System.out.println();
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
	 
	 public static ArrayList<String> getLinkedArray(Boolean doublyJoined){
		 String xPrefix="";
		 String prefixTarget="";
		 String check="";
		 int commonPartSize=0;
		 
         HashSet<String> seen = new HashSet<String>(dict.size());
         seen.add(first);
        

	        // Queue of paths
         Queue<ArrayList<String>> paths = new LinkedList<ArrayList<String>>();
        // We start with an initial path: [first]
         ArrayList<String> initial = new ArrayList<String>();
         initial.add(first);
      //  System.out.println(first);
        // Add this to our paths queue
         paths.add(initial);
		 
	 	 while(!paths.isEmpty()){
	 		 
			ArrayList<String> currentPath = paths.remove(); 
			
		/*	System.out.print("Path is : ");
			for(String w: currentPath){
				System.out.print(w+" ");
			}
			System.out.println();
			
			System.out.print("Seen is : ");
			for(String w: seen){
				System.out.print(w+" ");
			}
			System.out.println();*/
			//get last word in the chain
			String currentWord = currentPath.get(currentPath.size()-1); 
			if(currentWord.equals(last)) return currentPath; //stopping case
			//System.out.println(currentWord);
			String prevWord = "";
			if(currentPath.size() > 2){
				prevWord = currentPath.get(currentPath.size()-2);
			//	System.out.println("prevword is " + prevWord);
			}
			
			int currentWordLength = currentWord.length(); 
			 
		

			//check for alternative 
			//for(int i = 0; i < currentWordLength; i++){
		//	prefixTarget = currentWord.substring(currentWord.length()-i, currentWord.length());
			
				//use tree.wordsWithPRefix(prefixTarget)
			
			NEXT_WORD: for(String x : dict){
			//	System.out.println("Is " + x + " a potential match for " + currentWord);
				
				
				if(x.equals(currentWord)){
				//	System.out.println("skipped same");
					continue NEXT_WORD;
				}
			
				if(seen.contains(x)){
				//	System.out.println("skipped seen");
					continue NEXT_WORD; 
				}
				
				
			/*	if(doublyJoined && !prevWord.equals("")){
					check = overlappingConcat(prevWord, x);
					if(!check.contains(currentWord)){ //Only go forward if they have similar letters
					//	System.out.println("skipped no overlap of " + currentWord + " between " + prevWord + " and " + x);
						continue NEXT_WORD;  
					}
				}*/ 
				
				if(doublyJoined){
				//	System.out.println("Doubly");
					commonPartSize = Math.max(currentWord.length()/2, x.length()/2);
					if(Math.max(currentWord.length(), x.length()) % 2 == 1){ //is odd
						commonPartSize++; 
					}
				}else{
					//System.out.println("Singly");
					commonPartSize = Math.min(currentWord.length(), x.length())/2; 
					if(Math.min(currentWord.length(), x.length()) % 2 == 1){ //is odd
						commonPartSize++; 
					}
				}
				
		//		System.out.println("Comparing " + currentWord + " to "+ x);
				
				for(int i=commonPartSize; i<= Math.min(currentWord.length(), x.length()); i++){
					//look for a word where prefix == a's suffix 
					prefixTarget = currentWord.substring(currentWord.length()-i, currentWord.length());
					xPrefix = x.substring(0, i);
					
					
					//if the minSize is greater or equal to half of x 
					if(doublyJoined){
						if(i >= ((x.length()%2 == 1) ? x.length()/2 + 1 : x.length()/2));
					}
					
						//turn this into BFS?
					if(prefixTarget.equals(xPrefix) && prefixTarget.length() > 0){
					//	System.out.println("\"" + currentWord + "\" matched with \"" + x + "\" with -" + prefixTarget + "-");
						//if there is overlap
						seen.add(x);
						L path = new ArrayList<String>();
				
						path.addAll(currentPath);
						path.add(x);
						paths.add(path);
						
						continue NEXT_WORD;
						//to there 
					//	continue NEXT_WORD; //break out of this loop
					}
					
				}
			
		//}
			}
	 	}
	 	return new ArrayList<String>();
	 }
	 /*
	 static class TreeNode{
			private static HashMap<Character, TreeNode> children = new HashMap<Character, TreeNode>();
	        private static String word = null;
			
			
			public static void addNode(String s, int level){
				if(s.length() == level){
					word = s;
				}else{
					if(!children.containsKey(s.charAt(level))){
						children.put(s.charAt(level), new TreeNode());
					}
					children.get(s.charAt(level)).addNode(s, level + 1);
				}
			}
			
			public static ArrayList<String> wordsWithPrefix(String suffix) {
	            ArrayList<String> matchedWords = new ArrayList<String>();

	            if(suffix.isEmpty()) {
	                if(word != null){
	                	matchedWords.add(word);
	                }

	                for(TreeNode child : children.values()){
	                	matchedWords.addAll(child.wordsWithPrefix(""));
	                }
	            }else{
	                if(children.containsKey(suffix.charAt(0))) {
	                	matchedWords.addAll(children.get(suffix.charAt(0)).wordsWithPrefix(suffix.substring(1)));
	                }
	            }
	            return matchedWords;
	        }
			
		}*/
	    static class TreeNode {
	        private static HashMap<Character, TreeNode> children = new HashMap<Character, TreeNode>();
	        public static String word = null;

	        /**
	         * Returns a list of words that start with the remaining characters. If empty,
	         * returns a list of all child words.
	         * @param remainingChars remaining characters
	         * @return A List of all words starting with {@code remainingChars}
	         */
	        public static ArrayList<String> wordsWithPrefix(String remainingChars) {
	            ArrayList<String> result = new ArrayList<String>();

	            if (remainingChars.isEmpty()) {
	                if (word != null) result.add(word);

	                for (TreeNode child : children.values()) {
	                    result.addAll(child.wordsWithPrefix(""));
	                }
	            } else {
	                if (children.containsKey(remainingChars.charAt(0))) {
	                    result.addAll(children.get(remainingChars.charAt(0)).wordsWithPrefix(remainingChars.substring(1)));
	                }
	            }

	            return result;
	        }

	        private static void addNode(String word, int depth) {
	            if (depth == word.length()) {
	                word = word;
	            } else {
	                if (!children.containsKey(word.charAt(depth))) {
	                    children.put(word.charAt(depth), new TreeNode());
	                }
	                children.get(word.charAt(depth)).addNode(word, depth + 1);
	            }
	        }

	        /**
	         * Puts a word into the search tree
	         * @param word Word to insert into the tree
	         */
	        public static void addNode(String word) {
	        	addNode(word, 0);
	        }
	    }
}

/*
	
	//The common part is at least half as long as one of the two words
	public static String singlyJoined(String a, String b){
		StringBuilder result = new StringBuilder(); 
		StringBuilder chainBuilder = new StringBuilder(); 
		
		if(a.equals(b)){
			return "1 " + a + " " + b;
		}
		
		singleChain.destroy();
		doubleChain.destroy();
		
		backOneStep = false;
		singleChain = new LinkedList(); 
		singleChain.appendNode(a);
		String completeChain = findSingleChain(singleChain.getCurrentNode(), b); 
		
		if(completeChain.equals("INVALID")){
			return "0";
		}else{
			return completeChain;
		}
	}
	
	public static String findSingleChain(LinkedList.Node curr, String END){
		String xTarget;
		String target; 
		String endTarget;
		int commonPartSize;
		String thisMatch = "";
		ArrayList<String> otherOptions = new ArrayList<String>(); 
		
		String a = curr.data; 
		commonPartSize = Math.min(a.length(), END.length())/2;  //Only has to be as big as half of 
		if(Math.min(a.length(), END.length()) % 2 == 1){ //is odd
			commonPartSize++; 
		}
		//the smaller word
	//	System.out.println("\nCURRENT CHAIN: " + singleChain.printNodes());
		//System.out.println("Looking at " + curr.data);
		
		//STOPPING CASE - if a word whose suffix is a prefix of END, stop. 
		for(int i=commonPartSize; i< Math.min(a.length(), END.length()); i++){
		//	System.out.println("Min size is " + i);
			target = a.substring(a.length()-i, a.length());  //(inclusive, exclusive) suffix
		//	System.out.println("Target is " + target);
			
			endTarget = END.substring(0, i); 
			
			if(target.equals(endTarget)&& target.length()>0){//suffix == prefix
			//System.out.println("CHAIN FINISHED \"" + a + "\" matched with \"" + END+"\" with -" + target + "-");
				singleChain.appendNode(END);
				return singleChain.numNodes() + " " + singleChain.printNodes();   //a = b; stopping case 
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
		}else{
			NEXT_WORD: for(String x : dict){ //for every word in dictionary
				//find a word X which starts with the same commonPartSize or more letters as A ends with. 
				//check = overlappingConcat(prev, x);
				if(singleChain.containsNode(x)){//If the word has already been used in the chain, dont use it to avoid loops
					continue NEXT_WORD;
				}
				
				if(a.equals(x)){ //If same word, dont need to check
					continue NEXT_WORD;
				}
				
				commonPartSize = Math.min(a.length(), x.length())/2; 
				if(Math.min(a.length(), x.length()) % 2 == 1){ //is odd
					commonPartSize++; 
				}
			//	System.out.println("Chose "+ commonPartSize + " as the minsize of common part, between " + a + " and " + x);
				//minsize changes with every word pair
				for(int i=commonPartSize; i < Math.min(a.length(), x.length()); i++){
					target = a.substring(a.length()-i, a.length()); 
					xTarget = x.substring(0, i);  
					
					//System.out.println("C" + commonPartSize);
					
					if(target.equals(xTarget) && target.length() > 0){ //Found a pair - these two words are singly joined 
					//	System.out.println("\"" + a + "\" matched with \"" + x + "\" with -" + target + "-");
						//res.append(x +" "); //append the word to the resulting chain
						
						if(thisMatch.equals("")){ //only first time enter this loop. 
							thisMatch = x; 
						}else{
							otherOptions.add(x); //these will be added to next node
						}
						continue NEXT_WORD; //break out of this loop
						
				//		singleChain.appendNode(x);
					//	singlyCount++; 
						//return findSingleChain(singleChain.getCurrentNode(), END);
						//use this new word as A and find another word X -call recursively until we match END 
					}
					//If not found, look at for a larger common part of the same two words
				}
				//if not found, keep looping to the next word
			}
		}
		
		if(!thisMatch.equals("")){
			if(backOneStep){
				backOneStep = false; 
			}else{
				singleChain.addAlternatives(curr, otherOptions);
			}
			
			singleChain.appendNode(thisMatch);
			//System.out.println("New tail is " + chain.getCurrentNode().data);
			return findSingleChain(singleChain.getCurrentNode(), END);
		}else{ //there was no match so go back a step - We have NOT added a new node. 
		//	System.out.println(curr.data + " was a DEAD END - need to go back a step");
			
			while(curr.prev != null){ //mark this node to be ignored by the one before. 
				LinkedList.Node prevNode = curr.prev; 
				if(curr.prev.alternativeMatches.size() > 0){ //if there are alternative matches
					curr.prev.ignoreList.add(curr.data); 
					singleChain.removeNode(curr);
					backOneStep = true;
					return findSingleChain(prevNode, END);
				}
				
				//this previous node did not alternatives, so look at the one before
				curr.prev.ignoreList.add(curr.data);  //TODO: This prevents it ever going back in this direction?
				singleChain.removeNode(curr);
				curr = curr.prev;
			}
		}			
		//If we have looked through the whole dictionary, stop and return invalid. 
		
		return NOTFOUND; 
	}
	
	//This method returns a chain of doubly joined words, where the common part
	//in each pair is at least half as long as both of the words.
	public static String doublyJoined(String a, String b){
		if(a.equals(b)){
			return "1 " + a + " " + b;
		}
		
		singleChain.destroy();
		doubleChain.destroy();
		
		doubleChain = new LinkedList();
		doubleChain.appendNode(a);
		backOneStep = false; 
		String completeChain = findDoubleChain(doubleChain.getCurrentNode(), b); 
		
		if(completeChain.equals("INVALID")){
			return "0";
		}else{
			return doubleChain.numNodes() + " " + completeChain;
		}
	}

	 public void printList(ArrayList<String> list) {
	        System.out.print(list.size());
	        for (String item : list) {
	            System.out.print(" " + item);
	        }
	        System.out.println();
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
	
	//should not be longer than 100!!
	public static String findDoubleChain(LinkedList.Node curr, String END){
		//Queue<Array
		String xPrefix; 
		String target; 
		String endTarget;
		String prefixTarget;
		
	/*	HashSet<String> seenWords = new HashSet<String>(dict.size());
		seenWords.add(firstWord);
		
		Queue<ArrayList<String>> paths = new LinkedList<ArrayList<String>>();
	        // We start with an initial path: [first]
        ArrayList<String> initial = new ArrayList<String>();
        initial.add(first);
        // Add this to our paths queue
        paths.add(initial);
        
       
	
		
		
	
		ArrayList<String> otherOptions = new ArrayList<String>(); 
		
		//BFS of all possible chains 
	/*	while(!paths.isEmpty()){
			ArrayList<String> currentPath = paths.remove(); 
			//get last word in the chain
			String currentWord = currentPath.get(currentPath.size()-1); 
			String prevWord = currentPath.get(currentPath.size()-2);
			int currentWordLength = currentWord.length(); 
			
			if(currentWord.equals(last)) return currentPath; 
			
			//check for alternative 
			for(int i = 0; i < currentWordLength; i++){
				
			
				
				
				NEXT_WORD: for(String x : dict){
				
					if(seen.contains(x); 
					
					check = overlappingConcat(prevWord, x);
					if(!check.contains(currentWord)){ //Only go forward if they have similar letters
						continue NEXT_WORD;  
					}
					
					for(int i=commonPartSize; i<= Math.min(a.length(), x.length());i++){
						//look for a word where prefix == a's suffix 
						prefixTarget = currentWord.substring(currentWord.length()-i, currentWord.length());
						xPrefix = x.substring(0, i);
						
						//if the minSize is greater or equal to half of x 
						if(i >= ((x.length()%2 == 1) ? x.length()/2 + 1 : x.length()/2)){ 
							//turn this into BFS?
							if(prefixTarget.equals(xPrefix) && prefixTarget.length()>0){
					//			System.out.println("\"" + a + "\" matched with \"" + x + "\" with -" + prefixTarget + "-");
								//if there is overlap
								ArrayList<String> path = new ArrayList<String>();
								path.addAll(currentPath);
								path.add(word);
								paths.add(path);
								//to there 
								continue NEXT_WORD; //break out of this loop
							}
						}
				
				}
				
				
				
				 
			}
			
			
			
			
		}
		
		
		String a = curr.data;
		int commonPartSize = Math.max(a.length()/2, END.length()/2); 
		if(Math.max(a.length(), END.length()) % 2 == 1){ //is odd
			commonPartSize++; 
		}
		
		
		//System.out.println("CURRENT CHAIN: " + doubleChain.printNodes()); 
	//	System.out.println("Looking at " + curr.data +"\n");
		
		//STOPPING CASE: Check if we are complete - if curr's suffix == END's prefix
		String check = ""; 
		if(curr.prev != null){
			check = overlappingConcat(curr.prev.data, END);
			if(check.contains(curr.data)){
				for(int i=commonPartSize; i<= Math.min(a.length(), END.length()); i++){
					target = a.substring(a.length()-i, a.length());
					endTarget = END.substring(0, i);
					
					if(i >= ((END.length()%2 == 1) ? END.length()/2 + 1 : END.length()/2)){ //ensures the common part size is also larger than end.length/2
						if(target.equals(endTarget) && target.length()>0){
//					//		System.out.println("CHAIN FINISHED \"" + a + "\" matched with \"" + END+"\" with -" + target + "-");
							doubleChain.appendNode(END);
							return doubleChain.printNodes();
						}
					}
				}
			}
		}
		
		String thisMatch = "";
		
		if(backOneStep){ //If we are backtracking
			for(String x : curr.alternativeMatches){
				if(curr.ignoreList.contains(x) || curr.data.equals(x)){
					continue;  //skip the word if it is in the ignore list or if we are comparing to itself
				}
			
				thisMatch = x; 
			//	System.out.println("Chose " + x + " as an alternative to match to " + curr.prev.data);
				break;
			}
		}else{ //NOT backtracking - going forward 
		//	System.out.println("\nSearching for matches for "+ curr.data + "...");
			//System.out.println("CURRENT CHAIN: " + chain.printNodes() + "\n");
			
	
			NEXT_WORD: for(String x : dict){
				
				
				if(doubleChain.containsNode(x)){//If the word has already been used in the doubleChain, dont use it to avoid loops
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
				
				//If the word we are looking at brings us one step closer to the end word, then use it?
				//eg if we are on aboard and we see ardent - we look at the end of ardent and find that 
				//it matches END so we use aboard and ardent?? 
				
				
				//the common part needs to be the larger of the two common part sizes
				commonPartSize = Math.max(a.length()/2, x.length()/2);
				if(Math.max(a.length(), x.length()) % 2 == 1){ //is odd
					commonPartSize++; 
				}
		//		System.out.println("Chose "+ commonPartSize + " as the minimum size the common part must be between " + a + " and " + x);
				//WRONG MATCHES!:  ableeze ea abacus
				//ba to abada? 
				//fth to heteroplasty?
				
				//look at all letters from minimum common part size to the length of the shorter word
				for(int i=commonPartSize; i<= Math.min(a.length(), x.length());i++){
					//look for a word whose prefix == a's suffix 
					prefixTarget = a.substring(a.length()-i, a.length());
					xPrefix = x.substring(0, i);
					
					//if the minSize is greater or equal to half of x 
					if(i >= ((x.length()%2 == 1) ? x.length()/2 + 1 : x.length()/2)){ 
						//turn this into BFS?
						if(prefixTarget.equals(xPrefix) && prefixTarget.length()>0){
				//			System.out.println("\"" + a + "\" matched with \"" + x + "\" with -" + prefixTarget + "-");
							if(thisMatch.equals("")){ //only first time enter this loop. 
								thisMatch = x; 
					//			System.out.println("Exploring " + x + " first");
							}else{
								otherOptions.add(x); //these will be added to next node
						//		System.out.println("Exploring "+ x + " later");
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
				doubleChain.addAlternatives(curr, otherOptions);
			//	System.out.println("Adding " + thisMatch);
			}
			
			
			doubleChain.appendNode(thisMatch);
			//System.out.println("New tail is " + chain.getCurrentNode().data);
			return findDoubleChain(doubleChain.getCurrentNode(), END);
		}else{ //there was no match so go back a step - We have NOT added a new node. 
		//	System.out.println(curr.data + " was a DEAD END - need to go back a step");
			
			while(curr.prev != null){ //mark this node to be ignored by the one before. 
				LinkedList.Node prevNode = curr.prev; 
				if(curr.prev.alternativeMatches.size() > 0){ //if there are alternative matches
					
					curr.prev.ignoreList.add(curr.data); 
					doubleChain.removeNode(curr);
				//	System.out.println("CURRENT CHAIN: " + doubleChain.printNodes()); 
					backOneStep = true;
				//	System.out.println("Recovering from " + curr.data);
					return findDoubleChain(prevNode, END);
					//return findDoubleChain(doubleChain.getCurrentNode(), END);
				}
				
				//this previous node did not alternatives, so look at the one before
				curr.prev.ignoreList.add(curr.data);  //TODO: This prevents it ever going back in this direction?
				doubleChain.removeNode(curr);
				curr = curr.prev;
			}
		}
		//return new ArrayList<String>();
		return NOTFOUND; 
	}
	
	//an abstraction over a non-binary search tree for words by character
	
}*/