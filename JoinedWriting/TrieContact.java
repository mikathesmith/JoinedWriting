/*
 * Mika Smith
 * 
 * Creates an address book of contacts 
 * "add" operation = add a contact to trie
 * "find" operation = find how many contact names match the given prefix 
 */

import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class TrieContact {
    
    public static class Node{
        public char data;
        public HashMap<Character, Node> children;
        boolean isCompleteNode; 
        public int numWords; 
        
        public Node(char c){
            this.data = c;
            this.children = new HashMap<>(); //characters that follow the given one to continue a word 
            isCompleteNode = false; //flag to indicate full word
            numWords = 1; 
        }
    }
    
    public static class Trie{

        private Node root; 
        public Trie(){
            root = new Node((char)0); //top of the trie will be the number 0 converted to a character?  
        }

        public void insert(String word){
            Node charNode = root; //set charNode to root of trie 
            for(int i=0; i< word.length();i++){ //for every character of the word 
                HashMap<Character, Node> childList = charNode.children; //get all current children to the root 
                char ch = word.charAt(i); //get the character in the ith position of the word
                if(childList.containsKey(ch)){ //if the child character already exists in the roots' children
                    charNode= childList.get(ch); //get the child node whose character is the same 
                    charNode.numWords++; //increment words that come off of that character
                }else{
                    Node temp = new Node(ch); //create temporary node
                    childList.put(ch, temp); //add the character to the node's children 
                    charNode = temp; //charNode is now the temp node 
                }
            }
            charNode.isCompleteNode = true; //once added entire word, we indicate that it is finished 
        }

        public int getNumMatching(String input){
            int retval = 0; //initiate return value 
            Node charNode = root; //initiate starting node as root 
            for(int i=0; i< input.length();i++){ //for every character of the given input
                char ch = input.charAt(i); 
                HashMap<Character, Node> childList = charNode.children; //look at the children of the given character 
                if(childList.containsKey(ch)){ //if we do have children which contains the given character
                    charNode = childList.get(ch); //get the node with the given character
                    retval = charNode.numWords; //return the number of words that follow the given prefix
                }else return 0;
            }
            return retval; 
        }
        
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        Trie trie = new Trie(); 
        for(int a0 = 0; a0 < n; a0++){
            String op = in.next(); //"add" or "find"
            String contact = in.next();
            if(op.equals("add")){
                trie.insert(contact);
            }else{
                System.out.println(trie.getNumMatching(contact));
            }
        }
    }
}
