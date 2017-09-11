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
	
	public static void main(String[]args){
		Scanner sc = new Scanner(System.in);
		while(sc.hasNextLine()){
			String a = args[0]; //not being scanned! 
			//System.out.println("a is " + a);
			String b = args[1];
			//System.out.println("b is " + b);
			dict = new ArrayList<String>();//scan in a given dictionary - up to 100,000 words
			while(sc.hasNext()){
				dict.add(sc.next());
			}
			System.out.println(singlyJoined(a, b));
			//System.out.println(doublyJoined(a, b, dict));
		}
		sc.close(); 
	}
	
	//The common part is at least half as long as one of the two words
	public static String singlyJoined(String a, String b){
		int minCommonLength = Math.min(a.length(), b.length())/2; 
		StringBuilder result = new StringBuilder(); 
		
		//append number of singly joined words (at least 2) 
		
		result.append(a + " ");
		
		//recursively find a chain of words that reach b and append them all. 
		String r = "";
		String chain = findChain(a, b, r, minCommonLength, 0); 
		result.append(chain);
		//find all strings which have prefix as the suffix of a
		//for(String s: dict){
			
			//result.append(s +" ");
	//	}
		
		result.append(b);
		
		
		
		return result.toString(); 
	}
	
	
	public static String findChain(String a, String END, String res, int minSize, int count){
		String r = "";
		r += res; 
		
		
		//if we have looked through the whole dict, stop. 
		
		//if a word whose suffix is a prefix of END, stop. 
		
		//for every word in dictionary
			//find a word X which starts with the same minSize or more letters as A ends with. 
				//if found, 
					//append the word to result and increment count. 
					//use this new word as A and find another word X -call recursively until we find word end. 
				//if not found, keep looping to the next word
		
		//look at last minSize letters or greater of a; 
		System.out.println("Min size is " + minSize);
		
		String target = a.substring(a.length()-(minSize+1), a.length());  //(inclusive, exclusive) 
		System.out.println("Target is " + target);
		
		//seeing different lengths of common part
	/*	for(int i=minSize; i< a.length(); i++){
			
		}*/
		
		if(target.equals(END.substring(0, minSize+1))){ //or minsize + 2?? 
			System.out.println("CHAIN FINISHED");
			System.out.println("Result " + r);
			return r;  //a = b; stopping case 
		}else{
			System.out.println("...still searching...");
			for(String x : dict){
				
				//System.out.println(x);
				if(target.equals(x.substring(0, minSize+1))){ //then these two words are singly joined 
					System.out.println(a + " matched with " + x);
					r += x; 
					return findChain(x, END, res, minSize, count++);
				}
			}
		
			String notFound = "Searched entire dictionary: Chain not found ";
			return notFound; 
		}
		
		
	}
	
	
	
	
	//The common part is at least half as long as both of the words
	public static String doublyJoined(String a, String b, ArrayList<String> dict){
		int minCommonLength = (a.length()+b.length())/2;
		StringBuilder result = new StringBuilder(); 
		
		//append number of doubly joined words (at least 2) 
		
		return result.toString(); 
	}
	
	
	
}