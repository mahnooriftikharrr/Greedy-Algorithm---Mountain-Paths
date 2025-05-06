# Greedy Algorithm - Mountain Paths

## Overview
This project demonstrates the use of a greedy algorithm to solve the problem of finding optimal paths through a mountain range. The algorithm calculates a "greedy lowest-elevation-change walk" from one side of a map to the other, aiming to minimize the total elevation change with each step. The project also includes a visualization tool to help users identify the lowest-elevation paths on mountain landscapes.

## Features
- **Pathfinding Algorithm**: Implements a greedy approach to navigate through a 2D grid representing the mountain.
- **Visualization**: Displays the chosen path on the mountain grid for better understanding.
- **Customizable Input**: Reads topographic data from a file and processes it into a 2D array.

## How It Works
1. **Input**: The mountain is represented as a 2D grid where each cell contains an elevation value.
2. **Algorithm**: Starting from a specific point (e.g., the leftmost column), the algorithm chooses the next step based on the smallest elevation change.
3. **Visualization**: The path is drawn on the grid using a visualization tool.
4. **Output**: The algorithm outputs the path and its total elevation change.

## Files
- `MountainPaths.java`: Contains the implementation of the greedy algorithm and visualization logic.
- `ElevationData.java`: Handles reading topographic data into a 2D array.
- `DrawingCanvas.java`: Provides the visualization tool for rendering the mountain grid and paths.
- `Colorado_840x480.dat`: Example input file with elevation data.

## How to Run
1. Place your topographic data file (e.g., `Colorado_840x480.dat`) in the project directory.
2. Compile and run the program:
    ```bash
    javac MountainPaths.java
    java MountainPaths
    ```
3. View the output path and its total elevation change in the terminal and the visualization window.

## Example
Input (`Colorado_840x480.dat`):
```
1 2 3
4 5 6
7 8 9
```

Output:
```
Min value in map: 1
Max value in map: 9
Row with lowest val in col 0: 0
Total elevation change: 4
```

## Future Improvements
- Add support for diagonal movement in the pathfinding algorithm.
- Implement alternative algorithms (e.g., dynamic programming) for comparison.
- Enhance visualization with interactive tools and color-coded paths.

## Acknowledgments
Inspired by classic pathfinding problems and greedy algorithm techniques.