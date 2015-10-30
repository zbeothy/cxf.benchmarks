#CXF benchmarks

- - -

Contains performance benchmarks for CXF, Karaf deployment

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
Message Size | Message Count | Thread Count
--- | ---
small | 5000 | 60
medium | 5000 | 40
large | 500 | 10
<p>Provider</p>
<pre><code>
feature:repo-add mvn:org.talend.ps.benchmark/benchmark.features/0.1-SNAPSHOT/xml
feature:install benchmark-provider
</code></pre>
<p>Consumer</p>
<pre><code>
feature:repo-add mvn:org.talend.ps.benchmark/benchmark.features/0.1-SNAPSHOT/xml
feature:install benchmark-consumer-dom
</code></pre>

###SOAP over http (sax/streaming):
This scenario is executed with the following settings, which can be adjusted in *benchmark.consumer.cfg*. 
Message Size | Message Count | Thread Count
--- | ---
small | 5000 | 60
medium | 5000 | 40
large | 500 | 10
<p>Provider</p>
<pre><code>
feature:repo-add mvn:org.talend.ps.benchmark/benchmark.features/0.1-SNAPSHOT/xml
feature:install benchmark-provider
</code></pre>
<p>Consumer</p>
<pre><code>
feature:repo-add mvn:org.talend.ps.benchmark/benchmark.features/0.1-SNAPSHOT/xml
feature:install benchmark-consumer-sax
</code></pre>

###SOAP over http (Locator):
<p>Provider</p>
<pre><code>
tesb:start-locator
feature:repo-add mvn:org.talend.ps.benchmark/benchmark.features/0.1-SNAPSHOT/xml
feature:install benchmark-provider-locator
</code></pre>
<p>Consumer</p>
<pre><code>
feature:repo-add mvn:org.talend.ps.benchmark/benchmark.features/0.1-SNAPSHOT/xml
feature:install benchmark-consumer-locator-dom
</code></pre>

###Scenario SOAP over http (SAM):
<p>Provider</p>
<pre><code>
tesb:start-sam
feature:repo-add mvn:org.talend.ps.benchmark/benchmark.features/0.1-SNAPSHOT/xml
feature:install benchmark-provider-sam
</code></pre>
<p>Consumer</p>
<pre><code>
feature:repo-add mvn:org.talend.ps.benchmark/benchmark.features/0.1-SNAPSHOT/xml
feature:install benchmark-consumer-sam-dom
</code></pre>

###SOAP over JMS:
<p>Provider</p>
<pre><code>
feature:repo-add mvn:org.talend.ps.benchmark/benchmark.features/0.1-SNAPSHOT/xml
feature:install benchmark-provider-jms
</code></pre>
<p>Consumer</p>
<pre><code>
feature:repo-add mvn:org.talend.ps.benchmark/benchmark.features/0.1-SNAPSHOT/xml
feature:install benchmark-consumer-jms-dom
</code></pre>

###SOAP over http (JPA):
<p>Provider</p>
<pre><code>
feature:repo-add mvn:org.talend.ps.benchmark/benchmark.features/0.1-SNAPSHOT/xml
feature:install benchmark-provider-jpa
</code></pre>
<p>Consumer</p>
<pre><code>
feature:repo-add mvn:org.talend.ps.benchmark/benchmark.features/0.1-SNAPSHOT/xml
feature:install benchmark-consumer-dom
</code></pre>
