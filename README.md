# Gawain Examples

The set of examples for [Gawain Framework](https://github.com/smecsia/gawain).


### Multi-node Pi approximation

You can find this example [here](https://github.com/smecsia/gawain-examples/tree/master/src/main/groovy/me/smecsia/gawain/examples/PiExample.groovy).
To launch the example, you need to do the following:

1) Clone project:
```bash
$ git clone https://github.com/smecsia/gawain-examples && cd gawain-examples
```
2) Launch ActiveMQ via docker-compose:
```bash
$ docker-compose up
```
3) Launch two nodes (in different terminals), indicating the total nodes count (2) and required precision (1000):
```bash
$ ./gradlew run -Dargs=pi,node,2,10000
```
4) Launch "map" part of map-reduce, indicating the nodes count and the count of numbers in set. This will randomly distribute
the input set of numbers between our two nodes using the ActiveMQ queue:
```bash
$ ./gradlew run -Dargs=pi,map,2,10000
```
5) Launch "reduce" part of map-reduce, indicating the nodes count and the count of numbers in Nilakant set. This will initialize the reduce 
process on both nodes using the ActiveMQ topic:
```bash
$ ./gradlew run -Dargs=pi,reduce,2,20000
```
6) You should see something like the following on your screen:
```
Pi approx is 3.141592653589543306140794660119395....
```
