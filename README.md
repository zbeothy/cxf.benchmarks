#CXF benchmarks

- - -

Contains performance benchmarks for CXF, Karaf deployment

##Prerequisites
* Setup 2 TESB containers
<br />
Provider, running with default configuration.
<br />
Consumer, running with C1 configuration.
* Build the project
* Link the resulting repository in *benchmark.repository* to the containers by setting up *org.ops4j.pax.mvn.cfg*
* Create result output directory */opt/talend/benchmarks*. It is possible to use any other directory but don't 
forget to edit the configuration files.

##Scenarios
Consumer results are saved per run under the previously configured output directory. Provider results are saved 
by executing *benchmark:save* command in the Provider container. Before executing the test you should make sure
that there isn't any 'old' Provider data by running *benchmark:reset* command.
<br/>
<br/>
Tests are triggered with the consumer bundle start. Before starting the actual test you should execute a warm-up run.

###SOAP over http:
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

###SOAP over http (streaming):
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
