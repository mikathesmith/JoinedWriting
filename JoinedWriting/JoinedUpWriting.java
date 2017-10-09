
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
 * Returns for a given dict, a shortest sequence of 
 * joined up words that link a beginning word to an end word. 
 */

public class JoinedUpWriting{
	public static ArrayList<String> dict = new ArrayList<String>();
	public static final String NOTFOUND = "INVALID";
	public static boolean foundDoubleMatch = false;
    public static boolean foundSingleMatch = false;
	private static Trie charsOfWords = new Trie();
	public static StringBuilder sb = new StringBuilder(); 
	public static ArrayList<String> singleChain = new ArrayList<String>();
	public static ArrayList<String> doubleChain = new ArrayList<String>();
	public static int count;
	public static String first;
	public static String end;
	public static String target = "";
	public static String endTarget = "";
	
	public static void main(String[]args){
		first  = args[0].toLowerCase();
		end = args[1].toLowerCase();
		if(first.equals(end)){ 
			System.out.println("1 " + first + " " + end);
			System.out.println("1 " + first + " " + end);
			System.exit(0);
		}
		
		Scanner sc = new Scanner(System.in);
        while(sc.hasNextLine()){
            String word = sc.nextLine().toLowerCase().replaceAll("\\s+","");
            
            if(word.equals("")){
            	continue; 
            }
            
            if(!foundDoubleMatch || !foundSingleMatch){
            	checkMatch(first, word); //Immediately check to see if this word could like first to end
            }
            dict.add(word);
        }
        sc.close();
        Collections.sort(dict);
        
        //Add all words to prefix tree
  		for(String w : dict){
              charsOfWords.addNode(w, 0);
        }
  		
		long startTime = System.currentTimeMillis();
		
		findChain(); //find the optimal sequences
		
		long estimatedTime = System.currentTimeMillis() - startTime;	
		System.out.println("Time was " + estimatedTime);
	}
	
	//This method returns a chain of singly joined words, where the common part
		//is at least half as long as one of the words in the pair.
		public static ArrayList<String> joinUp(String first, String end, Boolean doubleJoin){
			ArrayList<String> completeChain; 
			if(doubleJoin){
				completeChain = findDoubleChain(first, end); 
			}else{
				completeChain = findSingleChain(first, end); 
			}
			
			if(completeChain.isEmpty()){
				return new ArrayList<String>();
			}else{
				return completeChain;
			}
		}	
	
	//Returns true if two words are singly joined
    private static boolean isSingleJoined(String a, String b) {
    	int commonPartSize = Math.min(a.length(), b.length())/2;  //Only has to be as big as half of 
        if(Math.min(a.length(), b.length()) % 2 == 1){ //is odd
				commonPartSize++; 
        }
        for(int i=commonPartSize; i<= Math.min(a.length(), b.length()); i++){
			target = a.substring(a.length()-i, a.length());  //(inclusive, exclusive) suffix
			endTarget = b.substring(0, i); 
			
			if(target.equals(endTarget) && target.length()>0){
				return true;
			}
        }
        return false; 
    }
    
    //Returns true is two words are doubly joined
    private static boolean isDoubleJoined(String a, String b) {
    	 int commonPartSize = a.length()/2;
         if(a.length() % 2 == 1){ //is odd
       	  	commonPartSize++; //then we need to add one to common part size!
 		 }
         for(int i=commonPartSize; i <= b.length(); i++){
        	if(a.length() < i) break;  
       	  
       	  	target = a.substring(a.length()-i, a.length());  //(inclusive, exclusive) suffix
       	  	endTarget = b.substring(0, i); 
 			
       	  	if((i >= ((b.length()%2 == 1) ? b.length()/2 + 1 : b.length()/2)) && target.equals(endTarget)&& target.length()>0){//suffix == prefix
       	//  		System.out.println(a + " matched " + b);
       	  		return true;//We have found a match to the end word so can return the current path that got us here
       	  	}
         }
         return false; 
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
	public static String printNodes(ArrayList<String> l){
			if(l.isEmpty()){
				return "0";
			}
		  sb.delete(0, sb.length());
		  sb.append(l.size()+ " "); 
		  for(int i=0; i < l.size();i++){
			  sb.append(l.get(i) + " ");
		  }
		  return sb.toString(); 
	}
	
	//This method creates a doubly joined chain from word first to word end
	public static ArrayList<String> findDoubleChain(String first, String end){
		//initialising variables
		String prefixTarget;
		String target;
		String endTarget;
		String check = "";
		int commonPartSize=0;
		ArrayList<String> currentPath = new ArrayList<String>();
		String currentWord = "";
		
		//Keep record of words we've come across to avoid loops
		HashSet<String> wordsEncountered = new HashSet<String>();
		wordsEncountered.add(first); 
		
		//Create a queue of all the possible chains we could explore to ensure we find the optimal one. 
		Queue<ArrayList<String>> possibleChains = new ArrayDeque<ArrayList<String>>();
        ArrayList<String> startChain =  new ArrayList<String>(); //Create a starting chain
        startChain.add(first); //add our initial word to it
        possibleChains.add(startChain); //add this starting chain to the queue so that the queue is not empty and we can start with it
        
		//While we still have a possible chain to explore. 
		while(! possibleChains.isEmpty()){
			  
			  currentPath = possibleChains.remove(); //remove the chain at the head of the queue
	          currentWord = currentPath.get(currentPath.size()-1); //get the tail of the chain
	       //   System.out.println("Chain is " + printNodes(currentPath));
	          if(currentWord.equals(end)){
	        	  return currentPath;
	          }
	          
	          //STOPPING CASE - if a word whose suffix is a prefix of END, stop. 
	         
	          commonPartSize = currentWord.length()/2;
	          if(currentWord.length() % 2 == 1){ //is odd
	        	  commonPartSize++; //then we need to add one to common part size!
	  		  }
	          for(int i=commonPartSize; i <= end.length(); i++){
	        	  if(currentWord.length() < i) break;  
	        	  
	        	  target = currentWord.substring(currentWord.length()-i, currentWord.length());  //(inclusive, exclusive) suffix
	        	  endTarget = end.substring(0, i); 
	  			
	        	  if((i >= ((end.length()%2 == 1) ? end.length()/2 + 1 : end.length()/2)) && target.equals(endTarget)&& target.length()>0){//suffix == prefix
	        		  currentPath.add(end);
	        		  return currentPath; //We have found a match to the end word so can return the current path that got us here
	        	  }
	          }
	          
	          //Need to search for prefixes of differing sizes
	         for (int i = commonPartSize; i <= currentWord.length(); i++) {
	         // for (int i = currentWord.length(); i >= commonPartSize; i--) {
	        	 // if ( > currentWordLength / 2) break;
	        	  prefixTarget = currentWord.substring(currentWord.length()-i, currentWord.length());
	        	//  System.out.println("Prefix target is " + prefixTarget);
	        	 
	        	 if(prefixTarget.equals(end)){
	        		 currentPath.add(end);
	        		 return currentPath;
	        	 }
	        	 
	        	  NEXT_WORD: for (String word : charsOfWords.wordsWithPrefix(prefixTarget)) {
	        		 
	        		  if (wordsEncountered.contains(word)){
	        			  continue NEXT_WORD;
	        		  }
	        		  //do a look ahead? if this word leads to end?
	        		  
                	
	        		//If there are at least 2 nodes added, we can do an additional check to see if there is overlap, eg suffix fixage agent
	        		/*  if(currentPath.size() > 2){ 
	        			  check = overlappingConcat(currentPath.get(currentPath.size()-2), word);
	        			  if(!check.contains(currentWord)){ //Only go forward if hold same letters, otherwise go to next word
	        				  continue NEXT_WORD;  
	        			  }
	        		  }		
					*/
	        		  if(currentPath.contains(word)){//If the word has already been used in the doubleChain, dont use it to avoid loops
	        			  continue NEXT_WORD;
	        		  }
					
	        		  if(currentWord.equals(word)){ //If same word, dont need to check
	        			  continue NEXT_WORD;
	        		  }	
	        		  
	        		  //If we have a match where the common part size is greater than both of the words
	        		  if((i >= ((word.length()%2 == 1) ? word.length()/2 + 1 : word.length()/2))) {
	        			  wordsEncountered.add(word); //ensure we dont come back to this word again to prevent loops
	        			//  System.out.println(currentWord + " matched with " + word);
	        			  ArrayList<String> chain = new ArrayList<String>(); // Create a new chain
	        			  chain.addAll(currentPath); //Add all the previous nodes to this chain
	        			  chain.add(word); //add this new match to the end of the chain
	        			  possibleChains.add(chain); //add to the entire chain to the queue to be explored later (BFS)
	        		  }
	                }
	            }
			}
		return new ArrayList<String>(); //If we have explored all options, a chain does not exist. 
	}
	
	//Finds the single chain
	public static ArrayList<String> findSingleChain(String first, String end){
		String prefixTarget;
		String target;
		String endTarget;
		String check = "";
		ArrayList<String> wordsEncountered = new ArrayList<String>();
		wordsEncountered.add(first); //add the first word to words we've come across
		int commonPartSize=0;  
		count = 0; 
		
		
		Queue<ArrayList<String>> possibleChains = new ArrayDeque<ArrayList<String>>();
        ArrayList<String> startingChain =  new ArrayList<String>();
        startingChain.add(first);
        possibleChains.add(startingChain);
        
        //While there is another chain we could explore
		while(! possibleChains.isEmpty()){
			count++;
				
			ArrayList<String> currentPath = possibleChains.remove(); //dequeue the first 
			String currentWord = currentPath.get(currentPath.size()-1); //get the tail of the chain 
          
			//STOPPING CASE - if a word whose suffix is a prefix of END, stop. 
  			if(isSingleJoined(currentWord, end)){//suffix == prefix
  				currentPath.add(end);
  				return currentPath;
  			}
	  	
	         
	          commonPartSize = 1; //start with the smallest common part size as it could be from either first or second word
	          Boolean firstWordUsed = false; //keep track of whether the common part size is greater than the first word
	          
	          //for differing prefix lengths
	          for (int i = commonPartSize; i <= currentWord.length(); i++) {
	        	  
	        	  if(i >= ((currentWord.length() % 2 == 1) ? currentWord.length()/2 + 1 : currentWord.length()/2)){
	        		  firstWordUsed = true; //is true if the common part size is greater than half of current/first word
	        	  }
	        	  
	        	  prefixTarget = currentWord.substring(currentWord.length()-i, currentWord.length());
	        	  //Look at an arraylist of words from the dict that have prefix prefixTarget
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
                        ArrayList<String> chain = new ArrayList<String>(); 
                        chain.addAll(currentPath);
                        chain.add(word);
                        possibleChains.add(chain);
	        		  }
	                }
	            }
			}
		return new ArrayList<String>();
	}

	/*
	 * A trie is a prefix tree for easy lookup of words that match a given prefix. 
	 */
	 public static class Trie {
		 
	        public String wordToAdd = ""; 
	        public HashMap<Character, Trie> charsFollowing = new HashMap<Character, Trie>(); //The children of the current char
 
	        //Recursively a word to the trie by adding its chars at different layers
	        public void addNode(String wordToAdd, int level) {
	            if(level == wordToAdd.length()){
	                this.wordToAdd = wordToAdd;
	            }else{
	            	char c = wordToAdd.charAt(level);
	                if(!charsFollowing.containsKey(c)){
	                    charsFollowing.put(c, new Trie());
	                }
	                charsFollowing.get(c).addNode(wordToAdd, level+1);
	            }
	        }
	        
	        //Return a list of all the strings that match the given prefix
	        public ArrayList<String> wordsWithPrefix(String prefixTarget){
	            ArrayList<String> matches = new ArrayList<String>();
	            if(prefixTarget.isEmpty()){
	                if (!wordToAdd.equals("")) matches.add(wordToAdd); //Add the word to the list of matches
	                for(Trie c : charsFollowing.values()) matches.addAll(c.wordsWithPrefix(""));
	            }else{
	            	char c = prefixTarget.charAt(0);
	                if(charsFollowing.containsKey(c)){
	                    matches.addAll(charsFollowing.get(c).wordsWithPrefix(prefixTarget.substring(1)));
	                }
	            }
	            return matches;
	        }
	 }
	 
	 //Checks if the word is an immediate match. 
	 public static void checkMatch(String first, String word){
         if(!foundSingleMatch && isSingleJoined(first, word) && isSingleJoined(word, end)){
             singleChain.add(first);
             singleChain.add(word);
             singleChain.add(end);
             
             foundSingleMatch = true;
         }
         
         if(!foundDoubleMatch && isDoubleJoined(first, word) && isDoubleJoined(word, end)){
             doubleChain.add(first);
             doubleChain.add(word);
             doubleChain.add(end);
             
             foundDoubleMatch = true;
         }
	}
	
	 //Calls the methods necessary to find a valid chain 
	public static void findChain(){

		if(isDoubleJoined(first, end)){ //check if these are doubly joined
			doubleChain.clear();
			doubleChain.add(first);
	        doubleChain.add(end); //add to the double chain
	        
	        singleChain = doubleChain;
	         
			//if its doubly joined, it is also singly joined
	        foundDoubleMatch = true;
	        foundSingleMatch = true;
		}else if(isSingleJoined(first, end)){
			singleChain.clear();
			singleChain.add(first);
			singleChain.add(end); //add to the single chain
			
			foundSingleMatch = true;
		}
		
		if(foundDoubleMatch){
			singleChain = doubleChain; 
		}else{ //if we havent already found a match
			doubleChain = joinUp(first, end, true); //find a chain
       
	        if(!foundSingleMatch && doubleChain.size() <= 4){
	        	 //if we havent already found a single match
	            singleChain = doubleChain; //if double, then also single
	            foundSingleMatch = true;
	        }
		}
		  
		if(!foundSingleMatch){
			//if we havent already found a single match, find one. 
			singleChain = joinUp(first, end, false);
		}
		
		//Print out result
		System.out.println(printNodes(singleChain));
		System.out.println(printNodes(doubleChain));
	}
	 
	 public static class Node{
	    int count;
	    ArrayList<Integer> matchedWords;

	    public Node(int data, ArrayList<Integer> matchedWords) {
	        this.count = data;
	        this.matchedWords = matchedWords;
	    }
	}
}