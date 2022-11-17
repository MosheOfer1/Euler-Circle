# Eulerian circuit
https://en.wikipedia.org/wiki/Eulerian_path

An educational program that animates the famous Euler Circle. At first the user draws as many points as he likes on the board, and then if the 2 following conditions are fulfilled, the animation starts.

1. All degrees are even.
2. The graph is a connected graph.
<img width="540" alt="Euler_Circle3" src="https://user-images.githubusercontent.com/107894139/202496035-df5bb13c-4f54-4f1c-af6c-f90056a0f634.png">
For instance in this Graph we'll see this message:
<img width="219" alt="error" src="https://user-images.githubusercontent.com/107894139/202504143-5115b9af-1320-4e8b-b0aa-0afba46a4ac8.png">

### Algorithm:
Let G be the graph now, because all the degrees are even, a simple circuit exists in the graph.

We find this circuit by "traveling" along the graph and stopping just when we have arrived at the vertex we started with.

Let **c** be this circuit.

Let **G'** be the following subtraction: **G-c**, Now If **G'** is an empty graph, we have the required circuit.

Else, we choose one of the vertices **v∈c**, such that **v∈G'** and start in recursion the same process.

Finally, we add those two circuits in the right place and we have the required circuit.


<img width="659" alt="Euler_Circle2" src="https://user-images.githubusercontent.com/107894139/202496043-ed77b380-2669-4374-b62b-1230b5db7822.png" border="5">
The green lines is the animation of the Euler circuit 
