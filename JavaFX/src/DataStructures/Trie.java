package DataStructures;

import java.util.HashMap;
import java.util.*;

public class Trie {
    //static final int ALPHABET_SIZE = 26;

    public class TrieNode {
        //TrieNode[] children = new TrieNode[ALPHABET_SIZE];
        private HashMap<Character, TrieNode> children = new HashMap<>();
        private boolean isEndOfWord = false;
        private String content;  // TODO: ???
    }

    private TrieNode root = new TrieNode();

    // If not present, inserts key into trie
    // If the key is prefix of trie node,
    // just marks leaf node
/*    public void insert(String key) {
        int level;
        int length = key.length();
        int index;

        TrieNode pCrawl = root;

        for (level = 0; level < length; level++) {
            index = key.charAt(level) - 'a';
            if (pCrawl.children[index] == null)
                pCrawl.children[index] = new TrieNode();

            pCrawl = pCrawl.children[index];
        }

        // mark last node as leaf
        pCrawl.isEndOfWord = true;
    }*/

    public void insert(String word) {

        TrieNode current = root;

        for (char l: word.toCharArray()) {
            current = current.children.computeIfAbsent(l, c -> new TrieNode());
        }
        current.isEndOfWord = true;
    }

    // Returns true if key presents in trie, else false
/*    static boolean search(String key) {
        int level;
        int length = key.length();
        int index;
        TrieNode pCrawl = root;

        for (level = 0; level < length; level++) {
            index = key.charAt(level) - 'a';

            if (pCrawl.children[index] == null)
                return false;

            pCrawl = pCrawl.children[index];
        }

        return (pCrawl.isEndOfWord);
    }*/

    public boolean find (String word) {

        TrieNode current = root;

        for (Character ch : word.toCharArray()) {
            TrieNode node = current.children.get(ch);
            if (node == null) {
                return false;
            }
            current = node;
        }

        return current.isEndOfWord;
    }

    private List<String> getAll(String prefix, TrieNode node) {
        List<String> r = new ArrayList<>();
        if (node.isEndOfWord) {
            r.add(prefix);
        }
        for (Map.Entry<Character, TrieNode> child : node.children.entrySet()) {
            List<String> subSuffix = getAll(prefix + child.getKey(), child.getValue());
            r.addAll(subSuffix);
        }
        return r;
    }

    public List<String> returnAllChildren(String word){
        List<String> r = new ArrayList<>();
        TrieNode current = root;

        for (Character ch : word.toCharArray()) {
            TrieNode node = current.children.get(ch);
            if (node == null) // Not found
                return r;

            current = node;
        }
        return getAll(word, current);
    }
}
