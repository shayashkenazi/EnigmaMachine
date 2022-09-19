package DataStructures;

import java.util.HashMap;
import java.util.*;

public class Trie {

    public class TrieNode {

        private HashMap<Character, TrieNode> children = new HashMap<>();
        private boolean isEndOfWord = false;
    }

    private TrieNode root = new TrieNode();

    public void insert(String word) {

        word = word.toUpperCase();
        TrieNode current = root;

        for (char l: word.toCharArray()) {
            current = current.children.computeIfAbsent(l, c -> new TrieNode());
        }
        current.isEndOfWord = true;
    }

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
