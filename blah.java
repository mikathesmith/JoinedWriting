public static String findDoubleChain(LinkedList.Node curr, String END){
		String xPrefix; 
		String target; 
		String endTarget;
		String prefixTarget;
		int numMatches = 0;
		String thisMatch = "";
		ArrayList<String> otherOptions = new ArrayList<String>(); 
		
		String a = curr.data;
		int commonPartSize = a.length()/2; 
		
		//STOPPING CASE: Check if we are complete - if curr's suffix == END's prefix
		String check = ""; 
		if(curr.prev != null){
			check = overlappingConcat(curr.prev.data, END);
			if(check.contains(curr.data)){
				for(int i=commonPartSize; i<= Math.min(a.length(), END.length()); i++){
					target = a.substring(a.length()-i, a.length());
					endTarget = END.substring(0, i);
					
					if(i >= (END.length()/2)){
						if(target.equals(endTarget)){
						//	System.out.println("CHAIN FINISHED \"" + a + "\" matched with \"" + END+"\" with -" + target + "-");
							doublyCount= doublyCount + 2;
							chain.appendNode(END);
							return chain.printNodes();
						}
					}
				}
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
		}else{ //NOT backtracking - going forward 
		//	System.out.println("\nSearching for matches for "+ curr.data + "...");
			//System.out.println("CURRENT CHAIN: " + chain.printNodes() + "\n");
	
			NEXT_WORD: for(String x : dict){
				
				
				if(chain.containsNode(x)){//If the word has already been used in the chain, dont use it to avoid loops
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
				
				for(int i=commonPartSize; i<= Math.min(a.length(), x.length());i++){
					//look for a word whose prefix == a's suffix 
					prefixTarget = a.substring(a.length()-i, a.length());
					xPrefix = x.substring(0, i);
					
					//if the minSize is greater or equal to half of x 
					if(i >= ((x.length()/2))){
						if(prefixTarget.equals(xPrefix)){
							
							if(thisMatch.equals("")){ //only first time enter this loop. 
								thisMatch = x; 
							}else{
								otherOptions.add(x); //these will be added to next node
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
				chain.addAlternatives(curr, otherOptions);
			}
			
			chain.appendNode(thisMatch);
			doublyCount++; 	
			//System.out.println("New tail is " + chain.getCurrentNode().data);
			return findDoubleChain(chain.getCurrentNode(), END);
		}else{ //there was no match so go back a step - We have NOT added a new node. 
		//	System.out.println(curr.data + " was a DEAD END - need to go back a step");
			
			while(curr.prev != null){ //mark this node to be ignored by the one before. 
				LinkedList.Node prevNode = curr.prev; 
				if(curr.prev.alternativeMatches.size() > 0){ //if there are alternative matches
					curr.prev.ignoreList.add(curr.data); 
					doublyCount--; 
					chain.removeNode(curr);
					backOneStep = true;
					return findDoubleChain(prevNode, END);
				}
				
				//this previous node did not alternatives, so look at the one before
				curr.prev.ignoreList.add(curr.data);  //TODO: This prevents it ever going back in this direction?
				chain.removeNode(curr);
				curr = curr.prev;
			}
		}
		return NOTFOUND;  
	}