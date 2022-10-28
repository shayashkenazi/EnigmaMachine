package users;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class HierarchyManager {
    private Map<String,Node> hierarchyNodesSet = new HashMap<>();
    public class Node {
        private String name;
        private Map<String,Node> children = new HashMap<>();
        private Node parent;

        public Node(String name) {
            this.name = name;
        }
        public void addChild(String child,Node childNode){
            this.children.put(child,childNode);
        }
        public void setParent(Node parentNode){
           parent = parentNode;
        }
        public String getName(){
            return name;
        }
    }

    public synchronized void addNode(String name) {
        Node node = new Node(name);
        hierarchyNodesSet.put(name,node);
    }
    public synchronized String getParent(String name) {
        if(name == null)
            return null;
        if(hierarchyNodesSet.get(name) == null)
            return null;
        if(hierarchyNodesSet.get(name).parent == null)
            return null;
        return hierarchyNodesSet.get(name).parent.name;
    }
    public synchronized void connectAgentToParent(String parent,String nameAgent) {

        Node nodeAgent = hierarchyNodesSet.get(nameAgent);
        Node nodeAlly = hierarchyNodesSet.get(parent);

        nodeAgent.setParent(nodeAlly);
        nodeAlly.addChild(nameAgent,nodeAgent);
    }
    public synchronized void connectAllyToUBoat(String parent,String nameAlly) {

        Node nodeAlly = hierarchyNodesSet.get(nameAlly);
        Node nodeUBoat = hierarchyNodesSet.get(parent);

        nodeAlly.setParent(nodeUBoat);
        nodeUBoat.addChild(nameAlly,nodeAlly);
    }
    /*public synchronized void disconnectAllyFromUBoat(String nameAlly) {
        Node nodeAlly = hierarchyNodesSet.get(nameAlly);
        nodeAlly.setParent(null);
    }*/




}
