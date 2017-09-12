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
			return "SINGLY 0";
		}else{
			result.append("SINGLY ");
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
			return "DOUBLY 0";
		}else{
			result.append("DOUBLY ");
			result.append(doublyCount + " ");
			result.append(a + " ");
			result.append(chain);
			result.append(b);
			return result.toString(); 
		}
	}
	
	public static String findDoubleChain(String a, String END, String intermediate, StringBuilder res){
		String yPrefix; 
		String ySuffix;
		String target; 
		String endTarget;
		String prefixTarget; 
		String suffixTarget; 
		int minSize; 
		
		//STOPPING CASE
		minSize = (a.length()+END.length())/2; //doubly
		for(int i=minSize; i< Math.min(a.length(), END.length()); i++){
			System.out.println("Min size is " + i);
			
			target = a.substring(a.length()-i, a.length());
			System.out.println("Target is " + target);
			
			endTarget = (i >= END.length()) ? END : END.substring(0, i); 
			if(target.equals(endTarget)){
				System.out.println("CHAIN FINISHED \"" + a + "\" matched with \"" + END+"\" with -" + target + "-");
				doublyCount= doublyCount + 2;
				return res.toString();  //a = b; stopping case 
			}
		}
			
		for(String x : dict){ //look for word to pair - A Y X
			minSize = (a.length()+x.length())/2; //doubly 
			//for every word in the dictionary, pair it with A and find 
			//another word Y which is at least minSize long and has its prefix
			//matching the suffix of A, and its suffix matching the prefix of X
			System.out.println("MinSize is " + minSize);
			//need to look at varying lengths
			
			//these are WRONG ! 5 in total not 5 per pre/suffix
			prefixTarget = a.substring(a.length()-minSize, a.length()); //In total!! thi
			suffixTarget = x.substring(0, minSize); 
			//minsize is too large? 
			
			//need another for loop changing minSize, need to keep track 
			//that if suffix is 1, prefix is 4 
			
			for(String y : dict){ //Look for intermediate words 
				
				if(y.length()>=minSize){
					
					//if yprefix matches prefixTarget
						//check if suffix matches suffixTarget
					System.out.println(y + " is larger than " + minSize);
					yPrefix = y.substring(0, minSize); 
					ySuffix = y.substring((y.length()-minSize), y.length()); 
					
					if((yPrefix == prefixTarget) && (ySuffix == suffixTarget)){
						System.out.println("MATCH"); //the word is a match 
						res.append(y +" " + x + " "); 
						doublyCount++; 
						return findDoubleChain(x, END, y , res);//y or x becomes a???
							
					}
				}
			}
		}
		return NOTFOUND;  
	}
}