# <img src="https://github.com/danieljnchen/autonomous-lawnmower/blob/master/src/application/img/logo-wide.png" width="300">
IntelliMow is an algorithm for generating paths robots can follow in order to cover a two-dimensional area. The scenario this algorithm handles is for mowing lawns.

## Features
* Minimal double coverage
* Obstacle avoidance
* Multiple lawn configurations
* Editor for creating lawn configurations with ease

## How To Use
* The user inputs the boundaries of their lawn through the editor, then saves the boundaries
* The user then opens up their lawn, and clicks a point on the boundary for the robot to start from
* The algorithm plans a path and displays the path and draws a robot following the generated path

## How It Works
* The algorithm proceeds from a start point on a boundary selected by the user
* The algorithm then raycasts left and right, adding the furthest hit points to the robot's path nodes
* If the direct path between path nodes crosses any boundaries, the algorithm adds intermediate path nodes to direct the robot around the boundary
* The algorithm searches between the left and right hit points to determine the start point for the next iteration
* The algorithm repeats this process until it reaches theh end of the boundary

This was created by two students as a high school senior engineering project.
