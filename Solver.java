//kc18182 - 1803189

package eight_puzzle;

import java.io.File;
import java.io.PrintWriter;
import java.util.*;

/*
   Solver is a class that contains the methods used to search for and print solutions
   plus the data structures needed for the search.
 */

public class Solver {
    //Priority Queue for open list.
    private PriorityQueue<Node> openList = new PriorityQueue<>();
    //Priority Queue for closed list.
    private PriorityQueue<Node> closedList = new PriorityQueue<>();
    //output file.
    private File outFile = new File("output.txt");
    //PrintWriter for that file
    private PrintWriter output = new PrintWriter(outFile);
    //start time of the program
    private long startTime;


    // Solver is a constructor that sets up an instance of the class with a node corresponding to the initial state as the root node.
    Solver(int[] initialBoard, int[] goalBoard) throws Exception {
        if (!isSolvable(initialBoard)) {
            output.println("Solution: Not Solvable");
            output.close();
            return;
        }
        //current time in milliseconds
        startTime = System.currentTimeMillis();
        //initial state
        GameState initialState = new GameState(initialBoard);
        //goal state
        GameState goalState = new GameState(goalBoard);
        //root node
        Node rootNode = new Node(initialState);
        //end node
        Node endNode = new Node(goalState);
        //run the a star algorithm
        AStar(rootNode, endNode); // Search for and print the solution
        //close the PrintWriter
        output.close();
    }

    private void AStar(Node start, Node goal) {
        //add start node to the open list
        openList.add(start);
        //set f to 0 for start node
        start.setF(0);
        //set g to 0 for start node
        start.setG(0);

        //while open list is not empty
        while (!openList.isEmpty()) {
            //fetch and remove first element in the queue and set it to a new node (current).
            Node current = openList.poll();
            //add the node (current) to the closed list.
            closedList.add(current);

            //if current is the goal state
            if (current.state.isGoal()) {
                //call report solution method with the print writer.
                reportSolution(current, output);
                return;
            }

            //remove current from the open list.
            openList.remove(current);
            //new ArrayList that has gameState as the attribute declared as the possible moves from the current node
            ArrayList<GameState> moveList = current.state.possibleMoves();
            //for every neighbour in moveList
            for(GameState neighbour : moveList) {
                //if the node is not in openList or closedList
                if ((Node.findNodeWithState(openList, neighbour) == null) && (Node.findNodeWithState(closedList, neighbour) == null)) { // If it is not already on either expanded or unexpanded node list then
                    //set cost to current cost + 1
                    int cost = current.getCost() + 1;
                    //make a new neighbour node
                    Node neighbourNode = new Node(neighbour, current, cost, Integer.MAX_VALUE, Integer.MAX_VALUE, h(current, goal));
                    //tempG is equal to current g and the cost of going from current ot neighbour node.
                    int tempG = current.getG() + h(current, neighbourNode);

                    //if tempG is smaller than the neighbours G
                    if (tempG < neighbourNode.getG()) {
                        //set neighbourG to tempG
                        neighbourNode.setG(tempG);
                        //set neighbourH to cost of going from neighbour node to goal
                        neighbourNode.setH(h(neighbourNode, goal));
                        //set neighbourF to neighbourG + neighbourH
                        neighbourNode.setF(neighbourNode.getG() + neighbourNode.getH());
                        //if openList does not contain neighbour node
                        if (!openList.contains(neighbourNode)) {
                            //add neighbour node to openList
                            openList.add(neighbourNode);
                        }
                    }
                }
            }
        }
        //no solution
        output.println("Solution: Not Found");
    }

    //heuristic
    private int h(Node current, Node goal) {
        var distance = 0;
        //act as if the 1d array is 2d
        for (var y = 0; y < 3; y++) {
            for (var x = 0; x < 3; x++) {
                //if an index is not 0 (blank tile)
                if (current.state.board[y+(x*3)] != 0) {
                    //used to break out of the loop
                    var found = false;
                    //for every tile
                    for (var i = 0; i < 3; i++) {
                        for (var j = 0; j < 3; j++) {
                            //if the index in current board is equal to the index in goal board
                            if (current.state.board[y+(x*3)] == goal.state.board[i+(j*3)]) {
                                //manhattan distance
                                distance += Math.abs(j - x);
                                distance += Math.abs(i - y);
                                //found
                                found = true;
                                //break
                                break;
                            }
                        }
                        //found so break out
                        if (found) break;
                    }
                }
            }
        }
        //return the manhattan distance
        return distance;
    }

    /*
       printSolution is a recursive method that prints all the states in a solution.
       It uses the parent links to trace from the goal to the initial state then prints
       each state as the recursion unwinds.
       Node n should be a node representing the goal state.
       The Printwriter argument is used to specify where the output should be directed.
     */
    private void printSolution(Node n, PrintWriter output) {
        if (n.parent != null) printSolution(n.parent, output);
        output.println(n.state);
    }

    /*
       reportSolution prints the solution together with statistics on the number of moves
       and the number of expanded and unexpanded nodes.
       The Node argument n should be a node representing the goal state.
       The Printwriter argument is used to specify where the output should be directed.
     */
    public void reportSolution(Node n, PrintWriter output) {
        printSolution(n, output);
        output.println("Solution: Found");
        output.println("Moves: " + n.getCost());
        output.println("Expanded nodes: " + this.closedList.size());
        output.println("Unexpanded nodes: " + this.openList.size());
        output.println("Duration: " + (System.currentTimeMillis() - startTime) + "ms");
        output.println();
    }

    static boolean isSolvable(int[] board) {
        int count = 0;
        for (int i = 0; i < 8; i++) {
            //if position is not blank
            if (board[i] > 0) {
                count++;
            }
        }
        return (count % 2 == 0);
    }

    public static void main(String[] args) throws Exception {
        //Runs the program.
        new Solver(GameState.INITIAL_STATE, GameState.GOAL_STATE);
    }
}