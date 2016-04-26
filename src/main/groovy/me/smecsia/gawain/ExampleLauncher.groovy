package me.smecsia.gawain

import groovy.transform.CompileStatic
import me.smecsia.gawain.activemq.ActivemqBroadcastBuilder
import me.smecsia.gawain.activemq.ActivemqQueueBuilder
import me.smecsia.gawain.examples.*
import org.apache.activemq.ActiveMQConnectionFactory

import static java.lang.System.exit
/**
 * @author Ilya Sadykov
 */
@CompileStatic
class ExampleLauncher {
    static final Map<String, Class<? extends AbstractExample>> CLAZZ = [
            factorial: FactorialExample,
            pi       : PiExample,
            sysinfo  : SystemInfoExample,
            timer    : TimerExample
    ]
    static final ActiveMQConnectionFactory activeMQ
    static {
        activeMQ = new ActiveMQConnectionFactory()
        activeMQ.brokerURL = 'tcp://localhost:61617'
    }

    public static void main(String[] args) {
        if (args.length < 3) {
            println("It is required to pass at least 3 arguments!")
            exit(-1)
        }
        Class<? extends AbstractExample> clazz = CLAZZ[args[0]]
        def type = args[1]
        def nodesCount = args[2] as int
        def arg = args.length > 3 ? args[3] : ''

        def example = clazz.newInstance(nodesCount)
        switch (type) {
            case 'node':
                router(example.node("${UUID.randomUUID()}", arg))
                break;
            case 'map':
                example.map(router(example.client(nodesCount)), arg)
                exit(0)
                break;
            case 'reduce':
                example.reduce(router(example.client(nodesCount)), arg)
                break;
        }
    }

    protected static Gawain router(Closure routes) {
        Gawain.run { Gawain g ->
            g.useQueueBuilder(new ActivemqQueueBuilder(activeMQ))
            g.useBroadcastBuilder(new ActivemqBroadcastBuilder(activeMQ))
            g.failOnMissingQueue(false)
            g.with(routes)
        }
    }
}
