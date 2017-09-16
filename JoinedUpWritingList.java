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
	public static int singlyCount;
	public static int doublyCount;
	public static final String NOTFOUND = "INVALID";
	
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
	
	//The common part is at least half as long as both of the words
	public static String doublyJoined(String a, String b){
		StringBuilder result = new StringBuilder(); 
		StringBuilder chainBuilder = new StringBuilder();
		String intermediate = ""; //pass an intermediate string
		String chain = findDoubleChain(a, b, intermediate, chainBuilder); 
		
		if(chain.equals("INVALID")){
			return "0";
		}else{
			//result.append("DOUBLY ");
			result.append(doublyCount + " ");
			result.append(a + " ");
			result.append(chain);
			result.append(b);
			return result.toString(); 
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
		int commonPartSize; 
		Boolean skip = false; 
		
		//STOPPING CASE
		//minSize = a.length()/2
		commonPartSize = a.length()/2; //IS THIS CORRECT!!?
		
		//change what i goes up to?  - NOT COMING BACK IN THIS LOOP WHEN 
		//FOUND ENTIRE TO COMPARE TO IRE??? 
		//commonPartSize = (Math.max(a.length()/2, END.length()/2));
		//THIS IS THE FIRST THING THAT SHOULD BE CHECKED
		for(int i=commonPartSize; i<= Math.min(a.length(), END.length()); i++){
			target = a.substring(a.length()-i, a.length());
	//		System.out.println("\nIS CHAIN COMPLETE?... Comparing " + a + " with " + END + " using target " + target);
			
			//System.out.println("Min size is " + i);
		
			//System.out.println("Target is " + target);
			
			//endTarget = (i >= END.length()) ? END : END.substring(0, i); 
			endTarget = END.substring(0, i);
			
			//System.out.println("Is " + target + " greater than half of " + a);
			if(i >= (END.length()/2)){
		//		System.out.println("YES");
				if(target.equals(endTarget)){
			//		System.out.println("CHAIN FINISHED \"" + a + "\" matched with \"" + END+"\" with -" + target + "-");
					doublyCount= doublyCount + 2;
					return res.toString();  //a = b; stopping case 
				}
			}else{
	//			System.out.println("NO");
			}

		}
		//check that x contains the intermediate??
		
	//	System.out.println("\nSTILL BUILDING CHAIN\n");
		
		for(String x : dict){ //look for word to pair - A Y X
			if(a.equals(x)){
				continue;
			}
			
			String check = "";
			check += prev;
			check += a; 
			System.out.println("Does " + x + " fit in  intermediate containing " + check);
			
			commonPartSize = a.length()/2;
			//minSize = (a.length())/2; //if a has 6 letters, minsize of common part is 3
			//commonPartSize = (Math.max(a.length()/2, END.length()/2));
			//if same word, dont bother checking?? 
			
			
			//need to check if the whole word is being used - eg fixture should not be used to join suffix and agent
			//maybe if we've chosen a wrong one, we can go back a step??
			
			
			//too make more efficient, only go into this for loop if x and a share common letters? 
			for(int i=commonPartSize; i<= Math.min(a.length(), x.length());i++){

				//look for a word whose prefix == a's suffix 
				prefixTarget = a.substring(a.length()-i, a.length()); //In total!! thi
		//		System.out.println("Comparing \"" + a + "\" with \"" + x + "\" with target " + prefixTarget);
				xPrefix = x.substring(0, i);
				
				//if the minSize is greater or equal to half of x - fix should not match fixture
			//	System.out.println("Is " + xPrefix + " greater than half of " + x + " with " + x.length());
				if(i >= ((x.length()/2))){ //if x is 5 letters, this is true. 
					//System.out.println("The common part is size " + i + " and is at least half of word " + x);
					//size of common part must be equal or greater than the length/2
			//		System.out.println("YES");
					if(prefixTarget.equals(xPrefix)){
				//		System.out.println("\"" + a + "\" matched with \"" + x + "\" through common part -" + prefixTarget + "-");
						res.append(x + " "); 
						doublyCount++; 
						
						//x needs to be contained in a and the next one
						return findDoubleChain(x, END, a, res);
					}
				}else{
	//				System.out.println("NO");
				}
		//		System.out.println();
			}
		}
		
		if(doublyCount > 0){ //have added to chain
			//go back a step? skip a word 
			skip = true; 
		//	return findDoubleChain();
			
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