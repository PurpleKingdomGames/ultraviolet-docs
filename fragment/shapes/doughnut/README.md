# Doughnut

This demo renders a doughnut shape, by taking the annular (like an outline of a drawing) of the SDF of a circle.

However its intended use was as a height map, where the doughnut smoothly rises out of the background and sinks back into it.

To achieve that we use a smoothing function like this:

`((cos(x * PI * 2.0) * 0.5) + 0.5) * min(step(-0.5, x), 1.0 - step(0.5, x))`

Which you can see in action using GraphToy:

https://graphtoy.com/?f1(x,t)=((cos(x%20*%20PI%20*%202.0)%20*%200.5)%20+%200.5)%20*%20min(step(-0.5,%20x),%201.0%20-%20step(0.5,%20x))&v1=true&f2(x,t)=&v2=false&f3(x,t)=&v3=false&f4(x,t)=&v4=false&f5(x,t)=&v5=false&f6(x,t)=&v6=false&grid=1&coords=0,0,12
