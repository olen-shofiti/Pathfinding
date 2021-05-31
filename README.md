# Pathfinding Algorithms

1. Breadth-First Search
2. Depth-First Search
3. Greedy Best-First Search
4. A* Search.

This java application visualises the four searching/pathfinding algorithms mentioned above.\
The nodes are expanded in real-time and in the end the path is displayed. Information about the number of nodes expanded and the number of nodes in the path from start to goal, are also displayed.\
Individual obstacles can be removed by right-clicking on them or new obstacles can be added by left-clicking and dragging the mouse.

#####################################################################################
# Short Explanation of the Algorithms

### BFS
*BFS* (uninformed search) is **optimal** *(given that the cost of the path is a non-decreasing function of the depth of the path)* and **complete** *(given that the depth is finite)* meaning that if there is a path, BFS will eventually find it. It is implemented by using a *Queue* data structure **(FIFO)**. The closest nodes to the root node are explored first. Next, the closest nodes to those explored nodes are explored, and so on.
- Time Complexity: **O(b<sup>d+1</sup>)**
- Space Complexity: **O(b<sup>d</sup>)**\
Where *b* is the number of nodes and *d* is the depth of the explored node.

![bfs](https://user-images.githubusercontent.com/35272873/120218293-2dfb3500-c242-11eb-8377-e532e065da1d.gif)

### DFS
*DFS* (uninformed search)is **complete** when searching a *Graph* but **incomplete** when searching a Tree, and is always **non-optimal** (it doesn't find the shortest path). It is implemented by using a *Stack* data structure and explores first the newly added nodes to the queue. As a result, the first added node will be explored last **(LIFO)**
- Time Complexity of a Graph: **Depends on the size of the state (nodes in the queue)**
- Time Complexity of a Tree: **O(b<sup>m</sup>)**
- Space Complexity: **O(bm)**\
Where *b* is the number of nodes and *m* is the maximum depth of any node.

![dfs](https://user-images.githubusercontent.com/35272873/120219749-626ff080-c244-11eb-9989-569a35edcd71.gif)

### GBFS
*Greedy Best-First Search* (informed search) is both **non-optimal**, since most likely won't find the overall shortest path, but the current shortest path (from node *n* to node *n+1*, and it is **incomplete** because it expands the closest node to the current node which of course may lead to a dead end (infinite loop). It explores first the nodes that are closest to the goal node, and the idea is that it will lead to the fastest solution. It uses the heuristic function **f(n) = h(n)**, where h(n) is the heuristic used for the evaluation (for example a straight-line distance)
- Time Complexity: **O(b<sup>m</sup>)**
- Space Complexity: **O(b<sup>m</sup>)**\
Where *b* is the number of nodes and *m* is the maximum depth of the search space.

![gbfs](https://user-images.githubusercontent.com/35272873/120219689-4cfac680-c244-11eb-80ec-a852222c2ef7.gif)

### A*
*A-Star* (informed search) is both **complete** and **optimal**, given that the heuristic used is **admissible** (never overestimates the cost to reach the goal). It uses the formula **f(n) = g(n) + h(n)** to find the shortest path from *starting point* to the *goal*, where g(n) is the cost from *start* to node *n* and *h(n)* is the estimated cost from node *n* to the *goal*.
- Time Complexity: **O(b<sup>d</sup>)**
- Space Complexity: **O(b<sup>d</sup>)**\
Where *b* is the branching factor (the average number of successors per state) and *d* is the solution depth.

![a-star](https://user-images.githubusercontent.com/35272873/120219727-597f1f00-c244-11eb-906a-2ffcdab81150.gif)

#### Source (Book): **Artificial Intelligence - A Modern Approach by Peter Norvig and Stuart J. Russell**
