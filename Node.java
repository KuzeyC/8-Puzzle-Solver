//kc18182 - 1803189

package eight_puzzle;

import java.util.PriorityQueue;

/*
The class Node represents nodes.
Its essential features are a GameState and a reference to the node's parent node. The
latter is used to assemble and output the solution path once the goal sate has been reached.
 */

public class Node implements Comparable<Node>{
    GameState state;    // The state associated with the node
    Node parent;        // The node from which this node was reached.
    private int cost;   // The cost of reaching this node from the initial node.
    private int f;
    private int g;
    private int h;

    //Constructor used to create new nodes.
    public Node(GameState state, Node parent, int cost, int f, int g, int h) {
        this.state = state;
        this.parent = parent;
        this.cost = cost;
        this.f = f;
        this.g = g;
        this.h = h;
    }

    //Constructor used to create initial node.
    Node(GameState state) {
        this(state,null,0, 0, 0, 0);
    }

    int getCost() {
        return cost;
    }

    //returns the F value in the object.
    int getF() {
        return f;
    }

    //returns the G value in the object.
    int getG() {
        return g;
    }

    //returns the H value in the object.
    int getH() {
        return h;
    }

    //Sets the F value in the object.
    void setF(int f) {
        this.f = f;
    }

    //Sets the G value in the object.
    void setG(int g) {
        this.g = g;
    }

    //Sets the H value in the object.
    void setH(int h) {
        this.h = h;
    }

    public String toString() {
        return "Node:" + state + " ";
    }

    /*
      Given a list of nodes as first argument, findNodeWithState searches the list for a node
       whose state is that specified as the second argument.
       If such a node is in the list, the first one encountered is returned.
       Otherwise null is returned.
     */
    public static Node findNodeWithState(PriorityQueue<Node> nodeList, GameState gs) {
        //for each node in the node list
        for (Node node : nodeList) {
            //if that node is the same as the game state parameter
            if (gs.sameBoard(node.state)) {
                //return the node
                return node;
            }
        }
        //otherwise return null
        return null;
    }

    //Compares the F values to order them in the priority queue.
    @Override
    public int compareTo(Node node) {
        return(this.getF() - node.getF());
    }
}
