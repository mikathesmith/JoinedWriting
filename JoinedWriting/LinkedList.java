package JoinedWriting; 

import java.io.*;
import java.util.*;

/*
 * 
 * Etude 7: LinkedList class for JoinedUpWriting
 * 
 * Authors: Mika Smith and Mathew Boyes
 * 
 * This code sets up a linked list of nodes structure for the chain 
 * of doubly linked words
 * 
 */

public class LinkedList{
	public static Node head; 
	
	public static class Node {
		   Node next;
		   Node prev; 
		   String data;
		   ArrayList<String> alternativeMatches = new ArrayList<String>(); 
		   ArrayList<String> ignoreList = new ArrayList<String>(); 
		   
		   /*
		    * Constructors to setup a node
		    */
		   public Node(){}
		   
		   public Node(String data){
			   this.data = data;
			   prev = null; 
		   }  
	}
	
	/*
	 * Adds a node to the tail of the chain and sets up its links. 
	 */
	public static void appendNode(String data){
		if(head==null){ //No head exists so we create one
			head = new Node(data);
			return;
		}
		Node current = head; //pointer that starts at head of linked list
		while(current.next!=null){ //go through list
			current = current.next;
		}
		current.next= new Node(data);
		current.next.prev = current;
	//	System.out.println("ADDED NODE "+ current.next.data);
	}
	
	/*
	 * Removes a node. 
	 */
	public static void removeNode(Node nodeToRemove){
		if(head == null) return;
		if(head == nodeToRemove){ //if we need to delete the head
			head = head.next; //walk around it
			return;
		}
		//walk though linked list and stop one 
		//before the element we want to delete
		Node current = head;
		while(current.next !=null){
			//if we've found a node which 
			//matches the one we want to delete
			if(current.next == nodeToRemove){//cut out next value
	//			System.out.println("REMOVED NODE "+ current.next.data);
				current.next = current.next.next; 
				return;
				//walk around the element.
			}
			current = current.next; //continue walking
		}
		
	}
	
	/*
	 * Returns true if the string has already been used in the chain. 
	 */
	public static Boolean containsNode(String data){
		Node current = head;
		while(current.next!=null){ //go through list
			if(current.data == data){ //If there is a node that contains this data
				return true; 
			}
			current = current.next;
		}
		return false; //If not 
	}

	/*
	 * Adds an arraylist of alternative matches to a node to come back to if
	 * we need to backtrack. 
	 */
	public static void addAlternatives(Node curr, ArrayList<String> alternatives){
		curr.alternativeMatches = alternatives; 
	}
	
	public static int numNodes(){
		int count=0; 
		Node current = head;
		while(current!=null){ //go through list
			current = current.next;
			count++; 
		}
		return count; //returns the tail
	}

	/*
	 * Returns the most recently added node
	 */
	public static Node getCurrentNode(){
		Node current = head;
		while(current.next!=null){ //go through list
			current = current.next;
		}
		return current; //returns the tail
	}
	
	/*
	 * Prints all the nodes in the chain
	 */
	public static String printNodes(){
		StringBuilder sb = new StringBuilder(); 
		if(head != null){ 
	        Node current = head; 
	        while(current != null){
	            sb.append(current.data + " ");
	            current = current.next;
	        }
		}
		return sb.toString();
	}
	
}