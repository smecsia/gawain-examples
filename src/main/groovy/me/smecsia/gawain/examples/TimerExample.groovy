package me.smecsia.gawain.examples

import groovy.transform.InheritConstructors
import me.smecsia.gawain.Gawain
import me.smecsia.gawain.jackson.JacksonStateSerializer
import me.smecsia.gawain.jdbc.JDBCRepoBuilder
import me.smecsia.gawain.jdbc.dialect.PostgresDialect
import me.smecsia.gawain.mongodb.MongodbRepoBuilder

import static java.sql.DriverManager.getConnection
import static java.util.concurrent.TimeUnit.SECONDS
/**
 * @author Ilya Sadykov
 */
@InheritConstructors
class TimerExample extends AbstractExample {

    static final String mongodbUrl = 'mongodb://localhost:27018/database?w=majority'
    static final String mongodbName = 'database'
    static final String postgresUrl = 'jdbc:postgresql://localhost:5433/gawain?user=gawain&password=gawain'

    @Override
    Closure node(String node, String arg) {
        { Gawain g ->
            if (arg == 'postgres') {
                g.useRepoBuilder(new JDBCRepoBuilder(getConnection(postgresUrl), new PostgresDialect()))
            } else {
                g.useRepoBuilder(new MongodbRepoBuilder(mongodbUrl, mongodbName))
                g.defaultOpts(stateSerializer: new JacksonStateSerializer())
            }
            g.doEvery 5, SECONDS, {
                println "Hello from master node!"
            }, global: true
        }
    }
}
