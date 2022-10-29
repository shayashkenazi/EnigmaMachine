package users;

import java.util.HashMap;
import java.util.Map;

public class HierarchyManager {
    private Map<String,Node> hierarchyNodesMap = new HashMap<>();
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
        hierarchyNodesMap.put(name,node);
    }
    public synchronized void removeUboat(String uBoatName){
        Node uboatNode = hierarchyNodesMap.get(uBoatName);
        for(Map.Entry<String,Node> entry : hierarchyNodesMap.entrySet()){
            entry.getValue().setParent(null);
        }
        hierarchyNodesMap.remove(uBoatName);

    }
    public synchronized String getParent(String name) {
        if(name == null)
            return null;
        if(hierarchyNodesMap.get(name) == null)
            return null;
        if(hierarchyNodesMap.get(name).parent == null)
            return null;
        return hierarchyNodesMap.get(name).parent.name;
    }
    public synchronized void connectAgentToParent(String parent,String nameAgent) {

        Node nodeAgent = hierarchyNodesMap.get(nameAgent);
        Node nodeAlly = hierarchyNodesMap.get(parent);

        nodeAgent.setParent(nodeAlly);
        nodeAlly.addChild(nameAgent,nodeAgent);
    }
    public synchronized void connectAllyToUBoat(String parent,String nameAlly) {

        Node nodeAlly = hierarchyNodesMap.get(nameAlly);
        Node nodeUBoat = hierarchyNodesMap.get(parent);

        nodeAlly.setParent(nodeUBoat);
        nodeUBoat.addChild(nameAlly,nodeAlly);
    }
    public synchronized void disconnectAllyFromUBoat(String nameAlly) {
        Node nodeAlly = hierarchyNodesMap.get(nameAlly);
        nodeAlly.parent.children.remove(nameAlly); // remove ally from uboat child
        nodeAlly.setParent(null);
    }




}
