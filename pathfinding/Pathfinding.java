package pathfinding;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 *
 * @author Olen Shofiti email: olenshofiti@gmail.com
 *
 * The Pathfinding application is a visualization of 4 searching/pathfinding
 * algorithms. Namely, BFS, DFS, A* and GBFS (Greedy Best-First Search) and two
 * distance metrics, Manhattan and Euclidean.
 *
 * The user can choose between a graph with obstacles or one without, then
 * select any of the 4 algorithms, and lastly, choose one of the two distance
 * metrics.
 *
 * Every time the user chooses to create a new grid, obstacles and
 * starting/ending point, are generated randomly.
 *
 * The nodes are explored in real-time and in the end the path is drawn (the
 * shortest path for BFS and A* since they are both optimal algorithms)
 *
 * The obstacles can be removed by right-clicking on them, and new obstacles can
 * be generated by clicking and dragging the mouse.
 */
@SuppressWarnings("unchecked")
public class Pathfinding extends JPanel implements MouseMotionListener, MouseListener {

    private static ArrayList<Point> obstacles;
    private static boolean flag = true;
    private static Point start, goal;
    private static JButton solveBtn, gridWithObstaclesBtn, gridWithoutObstaclesBtn;
    private static int nrOfExploredNodes = 0;
    private static int nrOfNodesInPath = 0;
    private static LinkedHashMap<Point, Point> path;
    private static LinkedHashMap<Point, Integer> cost;
    private static Queue<Point> queue;
    private static Stack<Point> stack;
    private static JRadioButton rdBFS, rdDFS, rdAstar, rdGreedy, rdManhattan, rdEuclidean;
    private static JLabel label;
    private static ButtonGroup groupBtnAlgos, groupBtnDist;
    public JPanel radioBtnAlgoPanel, radioBtnDistPanel, mainPanel, buttonsPanel, nodesExploredPanel;
    private static PriorityQueue<PriorityQ> frontier;

    /**
     * The constructor initializes all the required components of the
     * application.
     */
    public Pathfinding() {

        obstacles = new ArrayList<Point>();
        gridWithObstaclesBtn = new JButton("New Grid With Obstacles");
        gridWithoutObstaclesBtn = new JButton("New Grid Without Obstacles");
        solveBtn = new JButton("Find Path");
        solveBtn.setName("SolveButton");
        solveBtn.setForeground(Color.green);

        rdBFS = new JRadioButton("Breadth First Search");
        rdDFS = new JRadioButton("Depth First Search");
        rdAstar = new JRadioButton("A* Search");
        rdGreedy = new JRadioButton("Greedy Best First Search");
        rdBFS.setSelected(true);

        rdManhattan = new JRadioButton("Manhattan Distance");
        rdEuclidean = new JRadioButton("Euclidean Distance");
        rdManhattan.setSelected(true);

        label = new JLabel();
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        label.setText("<html><font size=5 color=\"Black\"> &#9673; &#9758; Nodes explored: ___ </font><br><font size=5 color=\"Lime\"> &#9673; &#9758; Nodes in path: ___ </font></html>");

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.gray);
        mainPanel.setBorder(BorderFactory.createEtchedBorder());

        groupBtnAlgos = new ButtonGroup();
        groupBtnDist = new ButtonGroup();

        groupBtnAlgos.add(rdBFS);
        groupBtnAlgos.add(rdDFS);
        groupBtnAlgos.add(rdAstar);
        groupBtnAlgos.add(rdGreedy);

        groupBtnDist.add(rdManhattan);
        groupBtnDist.add(rdEuclidean);

        rdManhattan.setSelected(true);

        radioBtnAlgoPanel = new JPanel();
        radioBtnAlgoPanel.setBorder(BorderFactory.createTitledBorder("Algorithms"));
        radioBtnAlgoPanel.setLayout(new BoxLayout(radioBtnAlgoPanel, BoxLayout.Y_AXIS));
        radioBtnAlgoPanel.setBackground(Color.gray);

        radioBtnAlgoPanel.add(rdBFS);
        radioBtnAlgoPanel.add(rdDFS);
        radioBtnAlgoPanel.add(rdAstar);
        radioBtnAlgoPanel.add(rdGreedy);

        radioBtnDistPanel = new JPanel();
        radioBtnDistPanel.setBorder(BorderFactory.createTitledBorder("Distance Metric"));
        radioBtnDistPanel.setLayout(new BoxLayout(radioBtnDistPanel, BoxLayout.Y_AXIS));
        radioBtnDistPanel.setBackground(Color.gray);
        radioBtnDistPanel.add(rdManhattan);
        radioBtnDistPanel.add(rdEuclidean);

        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.setBackground(Color.gray);
        buttonsPanel.add(gridWithObstaclesBtn);
        buttonsPanel.add(gridWithoutObstaclesBtn);
        buttonsPanel.add(solveBtn);

        mainPanel.add(buttonsPanel);
        mainPanel.add(radioBtnAlgoPanel);
        mainPanel.add(radioBtnDistPanel);

        nodesExploredPanel = new JPanel();
        nodesExploredPanel.setLayout(new BorderLayout());
        nodesExploredPanel.setBackground(Color.gray);
        nodesExploredPanel.add(label, BorderLayout.SOUTH);

        mainPanel.add(nodesExploredPanel);

        listeners();
    }

    /**
     * This helper method includes all the listeners of the class.
     */
    public void listeners() {
        this.addMouseMotionListener(this);
        this.addMouseListener(this);

        gridWithObstaclesBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                obstacles.clear();
                flag = true;
                repaint();

            }
        });

        gridWithoutObstaclesBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                obstacles.clear();
                flag = false;
                repaint();

            }
        });

        solveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                flag = false;
                nrOfExploredNodes = 0;
                nrOfNodesInPath = 0;

                if (rdBFS.isSelected()) {
                    try {
                        bfs();

                    } catch (InterruptedException ex) {
                        System.out.println("ERROR IN solveBtn BFS.... " + e);
                    }
                } else if (rdDFS.isSelected()) {
                    try {

                        dfs();

                    } catch (InterruptedException ex) {
                        System.out.println("ERROR IN solveBtn DFS.... " + e);
                    }
                } else if (rdAstar.isSelected()) {
                    try {
                        aStar();

                    } catch (InterruptedException ex) {
                        System.out.println("ERROR IN solveBtn A*.... " + e);
                    }
                } else if (rdGreedy.isSelected()) {
                    try {
                        greedy();

                    } catch (InterruptedException ex) {
                        System.out.println("ERROR IN solveBtn GBFS.... " + e);
                    }
                }
                label.setText(String.format("<html><font size=5 color=\"Black\"> &#9673; &#9758; Nodes explored:   %d </font><br><font size=5 color=\"Lime\"> &#9673; &#9758; Nodes in path:   %d </font></html>", nrOfExploredNodes, nrOfNodesInPath));

            }
        });
    }

    /**
     * The overridden paintComponent here calls two methods; generateGrid() and
     * generateStartGoalPoints(). Those two methods are passed the Graphics
     * object and draw to it a grid and two points. The starting point (S) and
     * the goal (G). If the 'flag' global variable is true, then the method
     * calls the generateObstacles() method as well, otherwise generates a grid
     * without obstacles.
     *
     * @param g The Graphics object.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        generateGrid(g);
        generateStartGoalPoints(g);

        if (flag) {

            generateObstacles(g);

        }
    }

    /**
     * Calculating the Manhattan distance between two points.
     *
     * @param p1 corresponds to one point
     * @param p2 corresponds to the other point
     * @return the sum of the absolute value of the difference between the two
     * coordinates, of each point,
     */
    public int manhattan(Point p1, Point p2) {

        return Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y);
    }

    /**
     * Calculating the Euclidean distance, i.e the straight-line distance
     * between two points.
     *
     * @param p1 corresponds to one point
     * @param p2 corresponds to the other point
     * @return the square root of the sum of the squares of the differences of
     * the two coordinates of each points
     */
    public int euclidean(Point p1, Point p2) {
        return (int) Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
    }

    /**
     * Method that expands the map/graph by visiting the neighbors of the
     * current point.
     *
     * @param coordinate The current point (x-y coordinates).
     * @return An array of neighbors (Left,Right,Top,Bottom) of the current
     * point.
     */
    public static Point[] getNeighbors(Point coordinate) {
        Point neighbors[] = new Point[4];

        int left = ((coordinate.x < 20) ? 20 : coordinate.x - 20);
        int right = ((coordinate.x > 580) ? 580 : coordinate.x + 20);
        int top = ((coordinate.y < 20) ? 20 : coordinate.y - 20);
        int bottom = ((coordinate.y > 580) ? 580 : coordinate.y + 20);

        Point leftCoordinate = new Point(left, coordinate.y);
        Point rightCoordinate = new Point(right, coordinate.y);
        Point topCoordinate = new Point(coordinate.x, top);
        Point bottomCoordinate = new Point(coordinate.x, bottom);

        neighbors[0] = leftCoordinate;
        neighbors[1] = rightCoordinate;
        neighbors[2] = topCoordinate;
        neighbors[3] = bottomCoordinate;

        return neighbors;

    }

    /**
     * The rectangles that represent the 30x30 grid (900 rectangles). Every time
     * width equals 600, the height variable is increased , and the width
     * variable becomes 0.
     *
     * @param g The graphics.
     */
    public void generateGrid(Graphics g) {

        int width = 0;
        int height = 0;

        g.setColor(Color.black);

        for (int i = 0; i < 900; i++) {
            if (i != 0 && (width % 600) == 0) {

                height += 20;
                width = 0;
            }

            if (width < 600) {
                g.drawRect(width, height, 20, 20);
                width += 20;
            }
        }

    }

    /**
     * This helper method generates obstacles in the grid, randomly, by calling
     * another helper method, the generatePoint(). Another helper method is
     * called, the isPresent(), to check if the generated point is already in
     * the obstacles list.
     *
     * @param g The graphics.
     */
    public void generateObstacles(Graphics g) {

        g.setColor(Color.black);

        while (obstacles.size() < 200) {

            Point point = generatePoint();

            if (!isPresent(point, obstacles)) {
                obstacles.add(point);
                g.fillRect(point.x, point.y, 20, 20);
            }
        }
    }

    /**
     * The randomly generated starting and finishing points in the grid. A while
     * loop runs until the generated point is not in the obstacles list. If the
     * start point is not in the list, it is added, the boolean variable flag
     * becomes true and the loop is terminated. Then another loop is ran while
     * the boolean flag is false and it terminates if the goal point is found
     * and the flag becomes true.
     *
     * @param g The graphics
     */
    public void generateStartGoalPoints(Graphics g) {

        boolean flag = true;

        while (flag) {
            start = generatePoint();
            if (!isPresent(start, obstacles)) {
                g.setColor(Color.white);
                g.fillOval(start.x, start.y, 20, 20);
                g.setColor(Color.black);
                g.drawOval(start.x, start.y, 20, 20);
                g.drawString("S", start.x + 7, start.y + 15);
                obstacles.add(start);
                flag = false;
            }
        }

        while (!flag) {
            goal = generatePoint();

            if (!isPresent(goal, obstacles) && !goal.equals(start)) {
                g.setColor(Color.black);
                g.fillOval(goal.x, goal.y, 20, 20);
                g.setColor(Color.white);
                g.drawOval(goal.x, goal.y, 20, 20);
                g.drawString("G", goal.x + 5, goal.y + 15);
                obstacles.add(goal);
                flag = true;
            }
        }
    }

    /**
     * First a random number is generated with Math.random() in the range [0.0,
     * 1.0) Then this number is multiplied with the difference of the upperbound
     * with the lowerbound +1. The randomly generated number is subtracted by
     * the remainder of itself divided by the step +lowerbound .
     *
     * @return A random point between lower (0) and upper (600) bounds.
     */
    public static Point generatePoint() {

        int randNumber;

        int lowerBound = 0;
        int upperBound = 600;
        int step = 20;
        int x;
        int y;

        randNumber = (int) (Math.random() * (upperBound - lowerBound + 1));
        x = randNumber - (randNumber % step + lowerBound);

        randNumber = (int) (Math.random() * (upperBound - lowerBound + 1));
        y = randNumber - (randNumber % step + lowerBound);

        return new Point(x, y);
    }

    /**
     * Helper method that determines if the expanded node (point) is already
     * present in the list. Meaning that the point has already been visited.
     *
     * @param point The point to check if it is in the list.
     * @param list The list with the already visited points.
     * @return True if the point is in the list. False otherwise.
     */
    public boolean isPresent(Point point, ArrayList<Point> list) {

        for (int i = 0; i < list.size(); i++) {
            if (point.equals(list.get(i))) {
                return true;
            }
        }

        return false;
    }

    /**
     * A helper method that draws the nodes explored.
     *
     * @param graphics The graphics object.
     * @param algoName The algorithms name.
     */
    public void drawNodes(Graphics graphics, String algoName) {

        int size = path.size();
        graphics.setColor(Color.black);

        for (int i = 0; i < size; i++) {
            Point p = (Point) path.keySet().toArray()[i];
            if (algoName.equals("BFS")) {
                if (queue.isEmpty() && !p.equals(goal)) {
                    graphics.setColor(Color.white);
                    graphics.setFont(new Font("Arial", 1, 25));
                    graphics.drawString("NO PATH AVAILABLE", 180, 300);

                    break;
                }
            } else if (algoName.equals("DFS")) {
                if (stack.isEmpty() && !p.equals(goal)) {
                    graphics.setColor(Color.white);
                    graphics.setFont(new Font("Arial", 1, 25));
                    graphics.drawString("NO PATH AVAILABLE", 180, 300);

                    break;
                }
            } else if (algoName.equals("A*")) {
                if (frontier.isEmpty() && !p.equals(goal)) {
                    graphics.setColor(Color.white);
                    graphics.setFont(new Font("Arial", 1, 25));
                    graphics.drawString("NO PATH AVAILABLE", 180, 300);

                    break;
                }
            } else if (algoName.equals("GBFS")) {
                if (frontier.isEmpty() && !p.equals(goal)) {
                    graphics.setColor(Color.white);
                    graphics.setFont(new Font("Arial", 1, 25));
                    graphics.drawString("NO PATH AVAILABLE", 180, 300);

                    break;
                }
            }

            if (!p.equals(start) && !p.equals(goal)) {

                int x = p.x - (p.x % 20) + 2;
                int y = p.y - (p.y % 20) + 2;

                graphics.fillOval(x, y, 16, 16);

            }
        }
    }

    /**
     * The Breadth-First Search algorithm. A Queue Data Structure (FIFO) is used
     * for the implementation. When the algorithm finishes, the method calls the
     * drawNodes() method to draw the points.
     *
     * @throws InterruptedException
     */
    public void bfs() throws InterruptedException {

        path = new LinkedHashMap<>();
        queue = new LinkedList<>();
        Graphics g = this.getGraphics();
        queue.offer(start);
        path.put(start, null);

        obstacles.remove(start);
        obstacles.remove(goal);
        while (!queue.isEmpty()) {
            Point current = queue.poll();

            for (Point neighbor : getNeighbors((current))) {

                if (neighbor.equals(goal)) {
                    path.put(neighbor, current);
                    path.put(null, neighbor);

                    Point value = path.get((Point) path.values().toArray()[path.size() - 1]);

                    g.setColor(Color.green);
                    while (!value.equals(start)) {
                        nrOfNodesInPath++;
                        int x = value.x - (value.x % 20) + 5;
                        int y = value.y - (value.y % 20) + 5;
                        g.fillOval(x, y, 10, 10);
                        value = path.get(value);

                    }
                    Thread.sleep(10); // THIS SLEEP HERE IS ADDED BECAUSE THE METHOD RETURNS BEFORE THE PATH IS BEING DRAWN (PROBABLY A SWING BUG)
                    return;
                }
                if (!path.containsKey(neighbor) && !obstacles.contains(neighbor) && neighbor.y < 600 && neighbor.x < 600) {
                    nrOfExploredNodes++;
                    queue.offer(neighbor);
                    path.put(neighbor, current);
                }
            }
            drawNodes(g, "BFS");
        }
    }

    /**
     * The Depth-First Search algorithm. A Stack Data Structure (LIFO) is used
     * for the implementation. When the algorithm finishes, the method calls the
     * drawNodes() method to draw the points.
     *
     * @throws InterruptedException
     */
    public void dfs() throws InterruptedException {

        path = new LinkedHashMap<>();
        stack = new Stack();
        Graphics g = this.getGraphics();
        stack.push(start);
        path.put(start, null);

        obstacles.remove(start);
        obstacles.remove(goal);

        while (!stack.isEmpty()) {
            Point current = stack.pop();

            for (Point neighbor : getNeighbors((current))) {

                if (neighbor.equals(goal)) {
                    path.put(neighbor, current);
                    path.put(null, neighbor);

                    Point value = path.get((Point) path.values().toArray()[path.size() - 1]);

                    g.setColor(Color.green);
                    while (!value.equals(start)) {

                        nrOfNodesInPath++;
                        int x = value.x - (value.x % 20) + 5;
                        int y = value.y - (value.y % 20) + 5;
                        g.fillOval(x, y, 10, 10);
                        value = path.get(value);

                    }
                    Thread.sleep(10); // THIS SLEEP HERE IS ADDED BECAUSE THE METHOD RETURNS BEFORE THE PATH IS BEING PAINTED (PROBABLY A SWING BUG)
                    return;

                }

                if (!path.containsKey(neighbor) && !obstacles.contains(neighbor) && neighbor.y < 600 && neighbor.x < 600) {

                    nrOfExploredNodes++;
                    stack.push(neighbor);
                    path.put(neighbor, current);

                }

            }

            drawNodes(g, "DFS");
        }

    }

    /**
     * The A* Search algorithm. Implemented with a Priority Queue which gives
     * also a priority to each visited node. The node with the highest priority
     * is visited, not the first in the queue, nor the last in the queue. The
     * formula for the calculation of the priority (cost) is f(n) = g(n) + h(n)
     * where g(n) is the cost of the path from the start node to n, and h(n) is
     * the heuristic that estimates the cost from n to the goal node. When the
     * algorithm finishes, the method calls the drawNodes() method to draw the
     * points.
     *
     * @throws InterruptedException
     */
    public void aStar() throws InterruptedException {

        obstacles.remove(start);
        obstacles.remove(goal);

        frontier = new PriorityQueue<>();
        Graphics g = this.getGraphics();
        frontier.add(new PriorityQ(start, 0));
        path = new LinkedHashMap<>();
        cost = new LinkedHashMap<>();

        path.put(start, null);
        cost.put(start, 0);

        while (!frontier.isEmpty()) {

            Point current = frontier.poll().point;

            if (current.equals(goal)) {

                path.put(null, current);
                Point value = path.get((Point) path.values().toArray()[path.size() - 1]);

                g.setColor(Color.green);

                while (!value.equals(start)) {

                    int x = value.x - (value.x % 20) + 5;
                    int y = value.y - (value.y % 20) + 5;
                    g.fillOval(x, y, 10, 10);
                    value = path.get(value);
                    nrOfNodesInPath++;

                }
                Thread.sleep(10); // THIS SLEEP HERE IS ADDED BECAUSE SOMETIMES THE METHOD RETURNS BEFORE THE PATH IS PAINTED. (PROBABLY A SWING BUG...)
                return;

            }

            for (Point neighbor : getNeighbors((current))) {

                int newCost;
                if (rdManhattan.isSelected()) {
                    newCost = manhattan(neighbor, goal) + cost.get(current);
                } else {
                    newCost = euclidean(neighbor, goal) + cost.get(current);
                }
                if ((!cost.containsKey(neighbor) || newCost < cost.get(neighbor)) && !obstacles.contains(neighbor) && neighbor.y < 600 && neighbor.x < 600) {

                    cost.put(neighbor, newCost);

                    frontier.add(new PriorityQ(neighbor, newCost));
                    path.put(neighbor, current);
                    nrOfExploredNodes++;

                }
            }

            drawNodes(g, "A*");
        }
    }

    /**
     * The Greedy Best-First Search algorithm. Implemented with a Priority Queue
     * which gives also a priority to each visited node. The node with the
     * highest priority is visited, not the first in the queue, nor the last in
     * the queue. The formula for the calculation of the priority (cost) is,
     * f(n) = h(n), where h(n) is the smallest estimation from n to the goal.
     * When the algorithm finishes, the method calls the drawNodes() method to
     * draw the points.
     *
     * @throws InterruptedException
     */
    public void greedy() throws InterruptedException {

        obstacles.remove(start);
        obstacles.remove(goal);

        frontier = new PriorityQueue<>();
        Graphics g = this.getGraphics();
        frontier.add(new PriorityQ(start, 0));
        path = new LinkedHashMap<>();
        cost = new LinkedHashMap<>();

        path.put(start, null);
        cost.put(start, 0);

        while (!frontier.isEmpty()) {
            Point current = frontier.poll().point;

            if (current.equals(goal)) {
                path.put(null, current);
                Point value = path.get((Point) path.values().toArray()[path.size() - 1]);

                g.setColor(Color.green);
                while (!value.equals(start)) {
                    nrOfNodesInPath++;

                    int x = value.x - (value.x % 20) + 5;
                    int y = value.y - (value.y % 20) + 5;
                    g.fillOval(x, y, 10, 10);
                    value = path.get(value);

                }
                Thread.sleep(10); // THIS SLEEP HERE IS ADDED BECAUSE THE METHOD RETURNS BEFORE THE PATH IS BEING PAINTED. (PROBABLY A SWING BUG...)
                return;

            }

            for (Point neighbor : getNeighbors((current))) {

                int newCost;
                if (rdManhattan.isSelected()) {
                    newCost = manhattan(neighbor, goal);
                } else {
                    newCost = euclidean(neighbor, goal);
                }
                if ((!cost.containsKey(neighbor) || newCost < cost.get(neighbor)) && !obstacles.contains(neighbor) && neighbor.y < 600 && neighbor.x < 600) {

                    cost.put(neighbor, newCost);
                    nrOfExploredNodes++;
                    frontier.add(new PriorityQ(neighbor, newCost));
                    path.put(neighbor, current);

                }

            }

            drawNodes(g, "GBFS");
        }

    }

    /**
     * New obstacles are generated by clicking and dragging the mouse.This
     * overridden method takes the x and y coordinates of the mouse and
     * subtracts from them the remainder of their value divided by 20 (because
     * each rectangle in the graph is 20x20)
     *
     * @param e The event representing the dragging of the mouse.
     */
    @Override
    public void mouseDragged(MouseEvent e) {

        Graphics g = this.getGraphics();
        g.setColor(Color.black);

//      ASSIGNING TO x AND y MULTIPLES OF 20 -> 20,40,60,320,480,520,,,,,
        int x = (e.getX() - (e.getX() % 20)); // x - (x % 20) | --> GIVES A MULTIPLE OF 20
        int y = (e.getY() - (e.getY() % 20)); // y - (y % 20) |

        Point point = new Point(x, y);

        g.fillRect(x, y, 20, 20);

        if (!obstacles.contains(point)) {

            obstacles.add(point);

        }

    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    /**
     * Whenever an obstacle is right-clicked, the obstacle is removed from the
     * graph and from the list. This overridden method makes use of the
     * getGraphics() method of the object in order to be able to draw on it.
     *
     * @param e
     */
    @Override
    public void mouseClicked(MouseEvent e) {

        if (e.getButton() == MouseEvent.BUTTON3) {

            int x = (e.getX() - (e.getX() % 20)); // x - (x % 20) --> GIVES A MULTIPLE OF 20
            int y = (e.getY() - (e.getY() % 20)); // y - (y % 20)

            Point p = new Point(x, y);

            obstacles.remove(p);

            Graphics gr = this.getGraphics();
            gr.setColor(Color.gray);
            gr.fillRect(x, y, 20, 20);
            gr.setColor(Color.black);
            gr.drawRect(x, y, 20, 20);

        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    /**
     * This private class is the implementation of a Priority Queue ADT. It
     * returns 1 if the cost of the first point is greater than the cost of the
     * second. -1 if the cost is smaller, or 0 if they are equal.
     *
     */
    private class PriorityQ implements Comparable<PriorityQ> {

        Point point;
        int cost;

        public PriorityQ(Point point, int cost) {
            this.point = point;
            this.cost = cost;
        }

        @Override
        public int compareTo(PriorityQ point) {
            if (cost < point.cost) {
                return -1;
            } else if (cost > point.cost) {
                return 1;
            }
            return 0;
        }
    }
}
