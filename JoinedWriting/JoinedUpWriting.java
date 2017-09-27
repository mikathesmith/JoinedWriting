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

public class JoinedUpWriting{
	public static ArrayList<String> dict;
	public static final String NOTFOUND = "INVALID";
	private static Trie charsOfWords = new Trie();
	
	public static void main(String[]args){
		Scanner sc = new Scanner(System.in);
		while(sc.hasNextLine()){
			String first  = args[0];
			String end = args[1];
			
			//If the two inputs are the same, return 1 and exit. 
			if(first.equals(end)){ 
				System.out.println("1 " + first + " " + end);
				System.exit(0);
			}
			
			String word; 
			dict = new ArrayList<String>();//scan in a given dictionary - up to 100,000 words
			while(sc.hasNextLine()){
				word = sc.nextLine(); 
				if(!word.equals("")){
					dict.add(word);
				}
			}
			Collections.sort(dict); //Sort the dictionary in alphabetical order to increase efficiency
			
			//Create a trie, or prefix tree, to hold all the words for easy prefix lookup
			for (String w : dict) {
	            charsOfWords.addNode(w, 0);
	        }
			
		    System.out.println(singlyJoined(first, end));
			System.out.println(doublyJoined(first, end));
		}
		sc.close(); 
	}
	
	//This method returns a chain of singly joined words, where the common part
	//is at least half as long as one of the words in the pair.
	public static String singlyJoined(String first, String end){
		String completeChain = findSingleChain(first, end); 
		
		if(completeChain.equals("INVALID")){
			return "0";
		}else{
			return completeChain;
		}
	}
	
	//This method returns a chain of doubly joined words, where the common part
	//in each pair is at least half as long as both of the words.
	public static String doublyJoined(String first, String end){
		String completeChain = findDoubleChain(first, end); 
		if(completeChain.equals("INVALID")){
			return "0";
		}else{
			return completeChain;
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
	
	//This method returns a string of the number of nodes followed
	//by the nodes that make up this chain. 
	public static String printNodes(LinkedList<String> l){
		  StringBuilder sb = new StringBuilder(); 
		  sb.append(l.size()+ " "); 
		  for(int i=0; i < l.size();i++){
			  sb.append(l.get(i) + " ");
		  }
		  return sb.toString(); 
	}
	
	//This method creates a doubly joined chain from word first to word end
	public static String findDoubleChain(String first, String end){
		//initialising variables
		String prefixTarget;
		String target;
		String endTarget;
		String check = "";
		int commonPartSize=0;
		//Keep record of words we've come across to avoid loops
		ArrayList<String> wordsEncountered = new ArrayList<String>();
		wordsEncountered.add(first); 
		
		//Create a queue of all the possible chains we could explore to ensure we find the optimal one. 
		Queue<LinkedList<String>> possibleChains = new ArrayDeque<LinkedList<String>>();
        LinkedList<String> startChain =  new LinkedList<String>(); //Create a starting chain
        startChain.add(first); //add our initial word to it
        possibleChains.add(startChain); //add this starting chain to the queue so that the queue is not empty and we can start with it
        
		//While we still have a possible chain to explore. 
		while(! possibleChains.isEmpty()){
			  
			  LinkedList<String> currentPath = possibleChains.remove(); //remove the chain at the head of the queue
	          String currentWord = currentPath.getLast().toString(); //get the tail of the chain
	          
	          //STOPPING CASE - if a word whose suffix is a prefix of END, stop. 
	          commonPartSize = currentWord.length()/2;
	          if(currentWord.length() % 2 == 1){ //is odd
	        	  commonPartSize++; 
	  		  }
	          for(int i=commonPartSize; i< Math.min(currentWord.length(), end.length()); i++){
	        	  target = currentWord.substring(currentWord.length()-i, currentWord.length());  //(inclusive, exclusive) suffix
	        	  endTarget = end.substring(0, i); 
	  			
	        	  if(target.equals(endTarget)&& target.length()>0){//suffix == prefix
	        		  currentPath.add(end);
	        		  return printNodes(currentPath); //We have found a match to the end word so can return the current path that got us here
	        	  }
	          }
	          
	          //Need to search for prefixes of differing sizes
	          for (int i = commonPartSize; i < currentWord.length(); i++) {
	        	  
	        	  prefixTarget = currentWord.substring(currentWord.length()-i, currentWord.length());
	        	//  System.out.println("Prefix target is " + prefixTarget);
	        	  NEXT_WORD: for (String word : charsOfWords.wordsWithPrefix(prefixTarget)) {
	        		  if (wordsEncountered.contains(word)){
	        			  continue NEXT_WORD;
	        		  }
                	
	        		//If there are at least 2 nodes added, we can do an additional check to see if there is overlap, eg suffix fixage agent
	        		  if(currentPath.size() > 2){ 
	        			  check = overlappingConcat(currentPath.get(currentPath.size()-2), word);
	        			  if(!check.contains(currentWord)){ //Only go forward if hold same letters, otherwise go to next word
	        				  continue NEXT_WORD;  
	        			  }
	        		  }		
					
	        		  if(currentPath.contains(word)){//If the word has already been used in the doubleChain, dont use it to avoid loops
	        			  continue NEXT_WORD;
	        		  }
					
	        		  if(currentWord.equals(word)){ //If same word, dont need to check
	        			  continue NEXT_WORD;
	        		  }	
    				
	        		  //If we have a match where the common part size is greater than both of the words
	        		  if(i >= ((word.length()%2 == 1) ? word.length()/2 + 1 : word.length()/2)) {
	        			  wordsEncountered.add(word); //ensure we dont come back to this word again to prevent loops
	        			  
	        			  LinkedList<String> chain = new LinkedList<String>(); // Create a new chain
	        			  chain.addAll(currentPath); //Add all the previous nodes to this chain
	        			  chain.add(word); //add this new match to the end of the chain
	        			  possibleChains.add(chain); //add to the entire chain to the queue to be explored later (BFS)
	        		  }
	                }
	            }
			}
		return NOTFOUND; //If we have explored all options, a chain does not exist. 
	}
	
	public static String findSingleChain(String first, String end){
		String prefixTarget;
		String target;
		String endTarget;
		String check = "";
		ArrayList<String> wordsEncountered = new ArrayList<String>();
		wordsEncountered.add(first); //add the first word to words we've come across
		int commonPartSize=0;  
		
		
		Queue<LinkedList<String>> possibleChains = new ArrayDeque<LinkedList<String>>();
        LinkedList<String> startingChain =  new LinkedList<String>();
        startingChain.add(first);
        possibleChains.add(startingChain);
        
        //While there is another chain we could explore
		while(! possibleChains.isEmpty()){
			  LinkedList<String> currentPath = possibleChains.remove(); //dequeue the first 
	          String currentWord = currentPath.getLast().toString(); //get the tail of the chain 
	       
	          //STOPPING CASE - if a word whose suffix is a prefix of END, stop. 
	          commonPartSize = Math.min(currentWord.length(), end.length())/2;  //Only has to be as big as half of 
	          if(Math.min(currentWord.length(), end.length()) % 2 == 1){ //is odd
	  				commonPartSize++; 
	          }
	          for(int i=commonPartSize; i< Math.min(currentWord.length(), end.length()); i++){
	  			target = currentWord.substring(currentWord.length()-i, currentWord.length());  //(inclusive, exclusive) suffix
	  			endTarget = end.substring(0, i); 
	  			
	  			//Found a match
	  			if(target.equals(endTarget)&& target.length()>0){//suffix == prefix
	  				currentPath.add(end);
	  				return printNodes(currentPath);
	  			}
	          }
	         
	          commonPartSize = 1; //start with the smallest common part size as it could be from either first or second word
	          Boolean firstWordUsed = false; //keep track of whether the common part size is greater than the first word
	          
	          //for differing prefix lengths
	          for (int i = commonPartSize; i < currentWord.length(); i++) {
	        	  
	        	  if(i >= ((currentWord.length()%2 == 1) ? currentWord.length()/2 + 1 : currentWord.length()/2)){
	        		  firstWordUsed = true; //is true if the common part size is greater than half of current/first word
	        	  }
	        	  
	        	  prefixTarget = currentWord.substring(currentWord.length()-i, currentWord.length());
	        	  //Look at an arraylist of words from the dictionary that have prefix prefixTarget
	        	  NEXT_WORD: for (String word : charsOfWords.wordsWithPrefix(prefixTarget)) {
	        		  
	        		  if (wordsEncountered.contains(word)){ //If we've already come across this word then ignore it
                		continue NEXT_WORD;
	        		  }
                	
	        		  if(currentPath.contains(word)){//If the word has already been used in the chain, dont use it to avoid loops
						continue NEXT_WORD;
	        		  }
					
	        		  if(currentWord.equals(word)){ //If same word, dont need to check
						continue NEXT_WORD;
	        		  }	
	        		  
	        		  //If the common part size is either larger than the first word or second word, then there is a match
	        		  if(firstWordUsed || (!firstWordUsed && (i >= ((word.length()%2 == 1) ? word.length()/2 + 1 : word.length()/2)))) {
           
	        			wordsEncountered.add(word);
                        
                        // Create a new chain
                        LinkedList<String> chain = new LinkedList<String>(); 
                        chain.addAll(currentPath);
                        chain.add(word);
                        possibleChains.add(chain);
	        		  }
	                }
	            }
			}
		return NOTFOUND;
	}

	/*
	 * A trie is a prefix tree for easy lookup of words that match a given prefix. 
	 */
	 public static class Trie {
		 
	        public String word = null;
	        public HashMap<Character, Trie> charsFollowing = new HashMap<Character, Trie>(); //The children of the current char
 
	        //Add a word to the trie by adding its chars at different layers
	        public void addNode(String word, int level) {
	            if(level == word.length()){
	                this.word = word;
	            }else{
	                if(!charsFollowing.containsKey(word.charAt(level))){
	                    charsFollowing.put(word.charAt(level), new Trie());
	                }
	                charsFollowing.get(word.charAt(level)).addNode(word, level + 1);
	            }
	        }
	        
	        //Return a list of all the strings that match the given prefix
	        public ArrayList<String> wordsWithPrefix(String prefixTarget){
	            ArrayList<String> matches = new ArrayList<String>();

	            if(prefixTarget.isEmpty()){
	                if (word != null) matches.add(word);

	                for(Trie child : charsFollowing.values()){
	                    matches.addAll(child.wordsWithPrefix(""));
	                }
	            }else{
	                if(charsFollowing.containsKey(prefixTarget.charAt(0))){
	                    matches.addAll(charsFollowing.get(prefixTarget.charAt(0)).wordsWithPrefix(prefixTarget.substring(1)));
	                }
	            }

	            return matches;
	        }
	 }
}