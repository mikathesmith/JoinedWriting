
/*
    William Sanson
    James Douglas
 */
import java.util.*;

public class NewJoin {

    private String start;
    private String finish;
    private boolean[] repeatWords;
    private boolean doublesFound = false;
    private boolean singlesFound = false;
    private ArrayList<String> dictionary = new ArrayList<String>();
    private ArrayList<String> singlesBest = new ArrayList<String>();
    private LinkedList<Node> doublesQueue = new LinkedList<Node>();
    private LinkedList<Node> singlesQueue = new LinkedList<Node>();
    private ArrayList<String> doublesBest = new ArrayList<String>();
    private ArrayList<Integer> joined = new ArrayList<Integer>();

    public static void main(String[] args) {

        if (args.length == 2) {
            String start = args[0];
            String finish = args[1];

            if (start.compareTo(finish) == 0) {
                System.out.println(1 + " " + start + " " + finish);
                System.out.println(1 + " " + start + " " + finish);
                return;
            }
            new NewJoin().go(start, finish);
        }
    }

    public void go(String start, String finish) {
        this.start = start;
        this.finish = finish;
        createDictionary();
        if (checkDoubles(start, finish)) {
            singlesFound = true;
            doublesFound = true;
            doublesBest.clear();
            doublesBest.add(start);
            doublesBest.add(finish);
            singlesBest = doublesBest;
        } else if (checkSingles(start, finish)) {
            singlesFound = true;
            singlesBest.clear();
            singlesBest.add(start);
            singlesBest.add(finish);
        }
        long startTime = System.nanoTime();
        if (doublesFound) {
            singlesBest = doublesBest;
        } else {
            repeatWords = new boolean[dictionary.size()];
            searchDoubles();
            if (doublesBest.size() == 4 && !singlesFound) {
                singlesBest = doublesBest;
                singlesFound = true;
            }
        }
        long endTime = System.nanoTime();
        long durationD = (endTime - startTime);
        startTime = System.nanoTime();
        if (!singlesFound) {
            repeatWords = new boolean[dictionary.size()];
            searchSingles();
        }
        endTime = System.nanoTime();
        printQueue(singlesBest);
        System.out.println("\n" + (endTime - startTime) / 1000000);
        System.out.print("\n");
        printQueue(doublesBest);
        System.out.println("\n" + durationD / 1000000);
    }

    // Performs a BFS to find the shortest singly joined chain
    private void searchSingles() {
        getSinglyJoined(start);
        ArrayList<Integer> joinedWords = joined;
        ArrayList<Integer> currentJoins = new ArrayList<Integer>();
        for (int i = 0; i < joinedWords.size(); i++) {
            Node word = new Node(joinedWords.get(i), new ArrayList<Integer>());
            singlesQueue.add(word);
        }
        ArrayList<String> best = new ArrayList<String>();
        while (!singlesQueue.isEmpty()) {
            Node current = singlesQueue.peekFirst();
            if (checkSingles(dictionary.get(current.dIndex), finish)) {
                best.add(start);
                for (int i = 0; i < current.joinedWords.size(); i++) {
                    best.add(dictionary.get(current.joinedWords.get(i)));
                }
                best.add(dictionary.get(current.dIndex));
                best.add(finish);
                singlesBest = best;
                return;
            } else {
                getSinglyJoined(dictionary.get(current.dIndex));
                joinedWords = joined;
                currentJoins = current.joinedWords;
                currentJoins.add(current.dIndex);
                for (int i = 0; i < joinedWords.size(); i++) {
                    Node word = new Node(joinedWords.get(i),
                            new ArrayList<Integer>(currentJoins));
                    singlesQueue.add(word);
                }
            }
            singlesQueue.removeFirst();
        }
    }

    // Performs a BFS to find the shortest doubly joined chain
    private void searchDoubles() {
        ArrayList<Integer> currentJoins = new ArrayList<Integer>();
        for (int j = 0; j < dictionary.size(); j++) {
            String next = dictionary.get(j);

            if (!repeatWords[j] && checkDoubles(start, next)) {
                if (next.compareTo(start) != 0) {
                    Node word = new Node(j,
                            new ArrayList<Integer>());
                    doublesQueue.add(word);
                    repeatWords[j] = true;
                }
            }
        }
        ArrayList<String> best = new ArrayList<String>();
        while (!doublesQueue.isEmpty()) {

            Node current = doublesQueue.peekFirst();
            if (checkDoubles(dictionary.get(current.dIndex), finish)) {
                best.add(start);
                for (int i = 0; i < current.joinedWords.size(); i++) {
                    best.add(dictionary.get(current.joinedWords.get(i)));
                }
                best.add(dictionary.get(current.dIndex));
                best.add(finish);
                doublesBest = best;
                return;
            } else {

                String s = dictionary.get(current.dIndex);

                currentJoins = current.joinedWords;
                currentJoins.add(current.dIndex);
                for (int j = 0; j < dictionary.size(); j++) {
                    String next = dictionary.get(j);
                    if (!repeatWords[j] && checkDoubles(s, next)) {
                        if (next.compareTo(s) != 0) {
                            Node word = new Node(j,
                                    new ArrayList<Integer>(currentJoins));
                            doublesQueue.add(word);
                            repeatWords[j] = true;
                        }
                    }

                }
                doublesQueue.removeFirst();
            }
        }
    }

    // Adds all the doubly joined words to an array
    public void getDoublyJoined(String word) {
        joined.clear();
        for (int j = 0; j < dictionary.size(); j++) {
            String next = dictionary.get(j);

            if (!repeatWords[j] && checkDoubles(word, next)) {
                if (next.compareTo(word) != 0) {
                    joined.add(j);
                    repeatWords[j] = true;
                }
            }
        }
    }

    // Adds all the singly joined words to an array
    public void getSinglyJoined(String word) {
        joined.clear();
        for (int j = 0; j < dictionary.size(); j++) {
            String next = dictionary.get(j);

            if (!repeatWords[j] && checkSingles(word, next)) {
                if (next.compareTo(word) != 0) {
                    joined.add(j);
                    repeatWords[j] = true;
                }
            }
        }
    }

    // Checks to see if two words are singly joined
    private boolean checkSingles(String w1, String w2) {
        int word1Size = w1.length();
        int word2Size = w2.length();
        int prefix = findPrefix(w1, word1Size, w2, word2Size) * 2;
        return prefix >= word1Size || prefix >= word2Size;
    }

    // Checks to see if two words are doubly joined
    private boolean checkDoubles(String w1, String w2) {
        int word1Size = w1.length();
        int word2Size = w2.length();
        int prefix = findPrefix(w1, word1Size, w2, word2Size) * 2;
        return prefix >= word1Size && prefix >= word2Size;
    }

    // Returns the length of a matching prefix of two words
    private int findPrefix(String w, int wLength, String d, int dLength) {

        for (int i = 0; i < wLength; i++) {
            int dIndex = 0;
            int wIndex = i;
            while (wIndex < wLength && dIndex < dLength
                    && w.charAt(wIndex) == d.charAt(dIndex)) {
                if (wIndex == wLength - 1) {
                    return dIndex + 1;
                }
                wIndex++;
                dIndex++;
            }
        }
        return 0;
    }

    // Adds words to the dictionary and checks for any direct joining words
    private void createDictionary() {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String d = scanner.nextLine();
            if (!singlesFound) {
                if (checkSingles(start, d)) {
                    if (checkSingles(d, finish)) {
                        singlesBest.add(start);
                        singlesBest.add(d);
                        singlesBest.add(finish);
                        singlesFound = true;

                    }
                }
            }
            if (!doublesFound) {
                if (checkDoubles(start, d)) {
                    if (checkDoubles(d, finish)) {
                        doublesBest.add(start);
                        doublesBest.add(d);
                        doublesBest.add(finish);
                        doublesFound = true;

                    }
                }
            }
            dictionary.add(d);
        }
        scanner.close();
        Collections.sort(dictionary);
    }

    private void printQueue(ArrayList<String> best) {
        System.out.print(best.size() + " ");
        for (String s : best) {
            System.out.print(s + " ");
        }
    }
}

// Class represents a word in the tree
class Node {

    int dIndex;
    ArrayList<Integer> joinedWords;

    public Node(int data, ArrayList<Integer> joinedWords) {
        this.dIndex = data;
        this.joinedWords = joinedWords;
    }
}
