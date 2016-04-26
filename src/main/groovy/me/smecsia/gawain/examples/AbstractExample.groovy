package me.smecsia.gawain.examples

import me.smecsia.gawain.Gawain

/**
 * @author Ilya Sadykov
 */
abstract class AbstractExample {
    final int nodesCount

    AbstractExample(int nodesCount) {
        this.nodesCount = nodesCount
    }

    abstract Closure node(String name, String arg)

    Closure client(int nodesCount) {}

    void map(Gawain router, String arg) {}

    void reduce(Gawain router, String arg) {}
}
