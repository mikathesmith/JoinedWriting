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
 * joined up words that link ka beginning word to an end word. 
 */

public class JoinedUpWriting{
	public static ArrayList<String> dict;
	public static int singlyCount;
	public static int doublyCount;
	
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
			//System.out.println(doublyJoined(a, b));
		}
		sc.close(); 
	}
	
	//The common part is at least half as long as one of the two words
	public static String singlyJoined(String a, String b){
		//int minCommonLength = Math.min(a.length(), b.length())/2; 
		StringBuilder result = new StringBuilder(); 
		StringBuilder chainBuilder = new StringBuilder(); 
		String chain = findSingleChain(a, b, chainBuilder); 
		
		if(chain.equals("INVALID")){
			return "0";
		}else{
			result.append("SINGLY ");
			result.append(singlyCount + " ");
			result.append(a + " ");
			result.append(chain);
			result.append(b);
			return result.toString(); 
		}
	}
	
	
	public static String findSingleChain(String a, String END, StringBuilder res){
		String xTarget;
		String target; 
		String endTarget;
		String notFound = "INVALID";
		int minSize;
		
		//if we have looked through the whole dict, stop. 
		
		//if a word whose suffix is a prefix of END, stop. 
		
		//for every word in dictionary
			//find a word X which starts with the same minSize or more letters as A ends with. 
				//if found, 
					//append the word to result and increment count. 
					//use this new word as A and find another word X -call recursively until we find word end. 
				//if not found, keep looping to the next word
		
		//look at last minSize letters or greater of a; 
		
		
		//minsize should be defined in this method! It changes with every word pair!
		
		//seeing different lengths of common part - OR should I count down?
		

		
		
		//two seperate for loops for checking if target equals? 
		minSize = Math.min(a.length(), END.length())/2; 
		//to the end of the smaller word
		for(int i=minSize; i< Math.min(a.length(), END.length()); i++){
			System.out.println("Min size is " + i);
			target = a.substring(a.length()-i, a.length());  //(inclusive, exclusive) 
			System.out.println("Target is " + target);
			
			endTarget = (i >= END.length()) ? END : END.substring(0, i); //is this necessary 
			
			if(target.equals(endTarget)){
				System.out.println("CHAIN FINISHED \"" + a + "\" matched with \"" + END+"\" with -" + target + "-");
			//	System.out.println("Result " + res);
				singlyCount= singlyCount + 2; //first is matched to second 
			//	doublyCount= doublyCount + 2;
				//	System.out.println("Count " + singlyCount);
				return res.toString();  //a = b; stopping case 
			}
		}
		//		System.out.println("...still searching...");
				
		
		for(String x : dict){
			//for loop for each length of each word
			minSize = Math.min(a.length(), x.length())/2; 
					
			for(int i=minSize; i < Math.min(a.length(), x.length()); i++){
				System.out.println("Min size is " + i);
				target = a.substring(a.length()-i, a.length());  //(inclusive, exclusive) 
				System.out.println("Target is " + target);
				
				xTarget = (i >= x.length()) ? x : x.substring(0, i);  //is this necessary?
				
				if(target.equals(xTarget)){ //then these two words are singly joined 
					System.out.println("\"" + a + "\" matched with \"" + x + "\" with -" + target + "-");
					res.append(x +" "); 
				//	System.out.println("Result " + res.toString());
					//find out whether to use i or minSize
					singlyCount++; 
					return findSingleChain(x, END, res);
					//if we ever cant find anything, should we go back a word? linked list?
				}
			}
		}
		return notFound; 
	}
	
	//The common part is at least half as long as both of the words
	public static String doublyJoined(String a, String b){
		int minCommonLength = (a.length()+b.length())/2;
		//the class wont work! needs to be common on both sides!
		StringBuilder result = new StringBuilder(); 
		StringBuilder chainBuilder = new StringBuilder();
		String intermediate = "";
		//pass an intermediate string
		String chain = findDoubleChain(a, b, intermediate, chainBuilder, minCommonLength); 
		
		if(chain.equals("INVALID")){
			return "0";
		}else{
			result.append("DOUBLY ");
			result.append(doublyCount + " ");
			result.append(a + " ");
			result.append(chain);
			result.append(b);
			return result.toString(); 
		}
	}
	
	public static String findDoubleChain(String a, String END, String intermediate, StringBuilder res, int minSize){
		String xTarget;
		String target; 
		String endTarget;
		
		System.out.println("Min size is " + minSize);
		//seeing different lengths of common part - OR should I count down?
		for(int i=minSize; i< a.length(); i++){
			target = a.substring(a.length()-i, a.length());  //(inclusive, exclusive) 
			System.out.println("Target is " + target);
			
			endTarget = (i >= END.length()) ? END : END.substring(0, i); 
			if(target.equals(endTarget)){
				System.out.println("CHAIN FINISHED \"" + a + "\" matched with \"" + END+"\" with -" + target + "-");
			//	System.out.println("Result " + res);
				singlyCount= singlyCount + 2; //first is matched to second 
				doublyCount= doublyCount + 2;
				//	System.out.println("Count " + singlyCount);
				return res.toString();  //a = b; stopping case 
			}else{
		//		System.out.println("...still searching...");
				
				for(String x : dict){
					xTarget = (i >= x.length()) ? x : x.substring(0, i); 
					
					if(target.equals(xTarget)){ //then these two words are singly joined 
						System.out.println("\"" + a + "\" matched with \"" + x + "\" with -" + target + "-");
						res.append(x +" "); 
					//	System.out.println("Result " + res.toString());
						//find out whether to use i or minSize
						singlyCount++; 
						doublyCount++; 
						return findDoubleChain(x, END, intermediate, res, minSize);
						//if we ever cant find anything, should we go back a word? linked list?
					}
				}
			}
		}
		String notFound = "INVALID";
		return notFound; 
	}
	
	
	
	
}