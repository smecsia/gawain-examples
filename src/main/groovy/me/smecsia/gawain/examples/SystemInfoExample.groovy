package me.smecsia.gawain.examples

import groovy.transform.InheritConstructors
import me.smecsia.gawain.Gawain

import static java.lang.Runtime.runtime
import static java.lang.System.exit
import static me.smecsia.gawain.Gawain.*

/**
 * @author Ilya Sadykov
 */
@InheritConstructors
class SystemInfoExample extends AbstractExample {

    @Override
    void reduce(Gawain router, String arg) {
        router.broadcast('reduce', 0)
    }

    @Override
    Closure client(int nodesCount) {
        { Gawain g ->
            g.aggregator 'info', key { 'all' }, aggregate { s, e ->
                println e
                s.total = (s.total ?: 0) + 1
                if (s.total >= nodesCount) {
                    println "All ${nodesCount} nodes done"
                    exit(0)
                }
            }
        }
    }

    @Override
    Closure node(String node, String arg) {
        { Gawain g ->
            g.processor('reduce').to(node)

            g.processor node, process {
                g.to('info', [
                        node    : node,
                        cpus    : getRuntime().availableProcessors(),
                        free    : getRuntime().freeMemory(),
                        max     : getRuntime().maxMemory(),
                        jvmtotal: getRuntime().totalMemory()
                ])
                println node
                exit(0)
            }
        }
    }
}
