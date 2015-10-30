#CXF benchmarks

- - -

Contains performance benchmarks for CXF, Karaf deployment. If the test are executed on Solaris adjust the memory 
allocation by using 
<pre><code>
export LD_PRELOAD_64=/usr/lib/64/libmtmalloc.so
</code></pre>

##Prerequisites
* Setup 2 TESB containers
<br />
**Provider**, running with default configuration.
<br />
**Consumer**, running with C1 port configuration
<pre><code>
source scripts/configureC1.sh
</pre></code>
adjusted system.properties
<pre><code>
http.maxConnections=60
</code></pre>
and adjusted locator log level
<pre><code>
log4j.logger.org.talend.esb.servicelocator=WARN
</pre></code>
* Build the project
* Link the resulting repository in *benchmark.repository* to the containers by adding it to the default repositories in
 *org.ops4j.pax.mvn.cfg*
* Create result output directory */opt/talend/benchmarks*. It is possible to use any other directory but don't 
forget to edit the benchmark.provider and benchmark.consumer configuration files which will be created with the feature
installation.

##Scenarios
Consumer results are saved per run under the previously configured output directory. Provider results are saved 
by executing *benchmark:save* command in the Provider container. Before executing the test you should make sure
that there isn't any 'old' Provider data by running *benchmark:reset* command.
<br/>
<br/>
Tests are triggered with the consumer bundle start. Before starting the actual test you should execute a warm-up run.

###SOAP over http (dom):
This scenario is executed with the following settings, which can be adjusted in *benchmark.consumer.cfg*. 

| Message Size  | Message Count | Thread Count  |
| ------------- | ------------- | ------------- |
| small         | 5000          | 60            |
| medium        | 5000          | 40            |
| large         | 500           | 10            |

<p>Provider</p>
    feature:repo-add mvn:org.talend.ps.benchmark/benchmark.features/0.1-SNAPSHOT/xml
    feature:install benchmark-provider
<p>Consumer</p>
    feature:repo-add mvn:org.talend.ps.benchmark/benchmark.features/0.1-SNAPSHOT/xml
    feature:install benchmark-consumer-dom

###SOAP over http (sax/streaming):
This scenario is executed with the following settings, which can be adjusted in *benchmark.consumer.cfg*.
 
| Message Size  | Message Count | Thread Count  |
| ------------- | ------------- | ------------- |
| small         | 5000          | 60            |

<p>Provider</p>
    feature:repo-add mvn:org.talend.ps.benchmark/benchmark.features/0.1-SNAPSHOT/xml
    feature:install benchmark-provider
<p>Consumer</p>
    feature:repo-add mvn:org.talend.ps.benchmark/benchmark.features/0.1-SNAPSHOT/xml
    feature:install benchmark-consumer-sax

###SOAP over http (Locator):
This scenario is executed with the following settings, which can be adjusted in *benchmark.consumer.cfg*.
 
| Message Size  | Message Count | Thread Count  |
| ------------- | ------------- | ------------- |
| small         | 10000         | 50            |

Furthermore the address has to be changed to locator://.

<p>Provider</p>
    tesb:start-locator
    feature:repo-add mvn:org.talend.ps.benchmark/benchmark.features/0.1-SNAPSHOT/xml
    feature:install benchmark-provider-locator
<p>Consumer</p>
    feature:repo-add mvn:org.talend.ps.benchmark/benchmark.features/0.1-SNAPSHOT/xml
    feature:install benchmark-consumer-locator-dom

###Scenario SOAP over http (SAM):
This scenario is executed with the following settings, which can be adjusted in *benchmark.consumer.cfg*.
 
| Message Size  | Message Count | Thread Count  |
| ------------- | ------------- | ------------- |
| small         | 1000         | 10            |

<p>Provider</p>
    tesb:start-sam
    feature:repo-add mvn:org.talend.ps.benchmark/benchmark.features/0.1-SNAPSHOT/xml
    feature:install benchmark-provider-sam
<p>Consumer</p>
    feature:repo-add mvn:org.talend.ps.benchmark/benchmark.features/0.1-SNAPSHOT/xml
    feature:install benchmark-consumer-sam-dom

###SOAP over JMS:
This scenario is executed with the following settings, which can be adjusted in *benchmark.consumer.cfg*. 

| Message Size  | Message Count | Thread Count  |
| ------------- | ------------- | ------------- |
| small         | 5000          | 50            |
| medium        | 5000          | 50            |
| large         | 100           | 10            |

Furthermore the address has to be changed to jms://. Please find the needed broker configuration in static module of the
project. To switch the persistence mode of the broker add the *persistent=false* flag to *activemq.cfg*.

<p>Provider</p>
    feature:repo-add mvn:org.talend.ps.benchmark/benchmark.features/0.1-SNAPSHOT/xml
    feature:install benchmark-provider-jms
<p>Consumer</p>
    feature:repo-add mvn:org.talend.ps.benchmark/benchmark.features/0.1-SNAPSHOT/xml
    feature:install benchmark-consumer-jms-dom

###SOAP over http (JPA):
This scenario is executed with the following settings, which can be adjusted in *benchmark.consumer.cfg*. 

| Message Size  | Message Count | Thread Count  |
| ------------- | ------------- | ------------- |
| small         | 5000          | 50            |

The database connection can be adjusted in *benchmark.datasource.cfg*.

<p>Provider</p>
    feature:repo-add mvn:org.talend.ps.benchmark/benchmark.features/0.1-SNAPSHOT/xml
    feature:install benchmark-provider-jpa
<p>Consumer</p>
    feature:repo-add mvn:org.talend.ps.benchmark/benchmark.features/0.1-SNAPSHOT/xml
    feature:install benchmark-consumer-dom
