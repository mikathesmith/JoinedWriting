package JoinedWriting; 

import java.io.*;
import java.util.*;


public class LinkedList{
	public static Node head; 
	
	public static class Node {
		   Node next;
		   Node prev; 
		   String data;
		   ArrayList<String> alternativeMatches = new ArrayList<String>(); 
		   ArrayList<String> ignoreList = new ArrayList<String>(); 
		   int numAlternatives; 
		   
		   public Node(){
			   data = null; 
			   numAlternatives = 0; 
			   prev = null; 
		   }
		   
		   public Node(String data){
			   this.data = data; 
		   }
		   
		   public Node(String data, int numAlternatives, ArrayList<String> alternativeMatches){
			   this.data = data; 
			   this.numAlternatives = numAlternatives;
			   this.alternativeMatches = alternativeMatches; 
		   }
	}
	
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
	}
	
	public static void appendNode(String data, int numMatches, ArrayList<String> alternativeMatches){
		if(head==null){ //No head exists so we create one
			head = new Node(data, numMatches, alternativeMatches);
	//		System.out.println("Added node " + head.data);
			return;
		}
		Node current = head; //pointer that starts at head of linked list
		Node temp; 
		while(current.next!=null){ //go through list
			current = current.next;
		}
		
		current.next= new Node(data, numMatches, alternativeMatches);
		current.next.prev = current; 
		System.out.print("ADDED NODE "  + current.next.data + " TO CHAIN");
		System.out.print(" with other alternatives : ");
		for(String s : current.next.alternativeMatches){
			System.out.print(s + " ");
		}
	}
	
	public static void replaceData(String newData, Node curr){
		System.out.println("REPLACED NODE "+ curr + " WITH " + newData);
		curr.data = newData; 
	}
	
	
	public static void replaceValue(String oldData, String newData){
		if(head == null) return;
		
		if(head.data == oldData){ //if we need to delete the head
			head.data = newData; 
			return;
		}
		//walk though linked list and stop one 
		//before the element we want to delete
		Node curr = head;
		while(curr.next !=null){
			//if we've found a node which 
			//matches the one we want to delete
			if(curr.next.data == oldData){//cut out next value
				System.out.println("REPLACED NODE "+ oldData + " WITH " + newData);
				curr.next.data = newData; 
				return;
				//walk around the element.
			}
			curr = curr.next; //continue walking
		}
	}
	
	public static int getNumMatches(Node curr){
		return curr.numAlternatives;
	}

	public static Node getCurrentNode(){
		//walk though linked list and stop one 
		//before the element we want to delete
		Node current = head;
		while(current.next!=null){ //go through list
			current = current.next;
		}
		return current; //should return head if there is only the head  
	}
	
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