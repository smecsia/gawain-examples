package me.smecsia.gawain.examples

import groovy.transform.InheritConstructors
import me.smecsia.gawain.Gawain

import static java.lang.Integer.parseInt
import static java.lang.System.exit
import static me.smecsia.gawain.Gawain.*
import static me.smecsia.gawain.util.MathUtil.bigInt

/**
 * @author Ilya Sadykov
 */
@InheritConstructors
class FactorialExample extends AbstractExample {

    @Override
    void map(Gawain router, String arg) {
        def total = parseInt(arg)
        (1..total as List).each { router.to('map', it) }
    }

    @Override
    void reduce(Gawain router, String arg) {
        router.broadcast('reduce', 0)
    }

    @Override
    Closure client(int nodesCount) {
        { Gawain g ->
            g.aggregator 'total', key { 'all' }, aggregate { s, e ->
                s.value = (s.value ?: bigInt(1) as BigInteger).multiply(e as BigInteger)
                s.total = (s.total ?: 0) + 1
                if (s.total >= nodesCount) {
                    println "Result is ${s.value}"
                    exit(0)
                }
            }
        }
    }

    @Override
    Closure node(String node, String arg) {
        { Gawain g ->
            g.processor('map').to(node)

            g.aggregator node, key { 'res' }, aggregate { s, e ->
                s.value = (s.value ?: bigInt(1) as BigInteger).multiply(bigInt(e as int))
            }

            g.processor 'reduce', process {
                g.broadcast('total', g.repo(node)['res'] ?: bigInt(1))
                exit(0)
            }
        }
    }
}
