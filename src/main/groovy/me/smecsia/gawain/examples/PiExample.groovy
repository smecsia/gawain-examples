package me.smecsia.gawain.examples

import groovy.transform.InheritConstructors
import me.smecsia.gawain.Gawain

import static java.lang.Integer.parseInt
import static java.lang.System.exit
import static me.smecsia.gawain.Gawain.*
import static me.smecsia.gawain.util.MathUtil.dec
import static me.smecsia.gawain.util.MathUtil.piApproximate

/**
 * @author Ilya Sadykov
 */
@InheritConstructors
class PiExample extends AbstractExample {

    @Override
    void map(Gawain router, String arg) {
        for (long val = 2; val < Long.valueOf(arg); val += 4) {
            router.to('map', val)
        }
    }

    @Override
    void reduce(Gawain router, String arg) {
        router.broadcast('reduce', 0)
    }

    @Override
    Closure client(int nodesCount) {
        { Gawain g ->
            g.aggregator 'total', key { 'all' }, aggregate { s, e ->
                s.value = (s.value ?: dec(3.0d)).add(e as BigDecimal)
                s.total = (s.total ?: 0) + 1
                if (s.total >= nodesCount) {
                    println "Pi approx is ${s.value}"
                    exit(0)
                }
            }
        }
    }

    @Override
    Closure node(String nodeId, String precision) {
        { Gawain g ->
            g.processor('map').to(nodeId)

            g.aggregator nodeId, key { "${it}" }, aggregate { s, e ->
                s.value = piApproximate(e as long, parseInt(precision))
            }, consumers: 30

            g.processor 'reduce', process {
                def sum = dec(0.0d)
                g.repo(nodeId).withEach { k, s -> sum = sum.add(s.value as BigDecimal) }
                g.repo(nodeId).clear()
                g.broadcast('total', sum)
            }
        }
    }
}
