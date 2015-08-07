cxf.benchmarks
==============

Contains performance benchmarks for CXF, Karaf deployment

#Scenario SOAP over http:
features:addurl mvn:org.talend.ps.benchmark/benchmark.features/0.1-SNAPSHOT/xml
features:install benchmark-provider

features:install cxf-http-async
features:addurl mvn:org.talend.ps.benchmark/benchmark.features/0.1-SNAPSHOT/xml
features:install benchmark-consumer

#Scenario SOAP over http (Locator):
tesb:start-locator
features:addurl mvn:org.talend.ps.benchmark/benchmark.features/0.1-SNAPSHOT/xml
features:install benchmark-provider-locator

features:addurl mvn:org.talend.ps.benchmark/benchmark.features/0.1-SNAPSHOT/xml
features:install benchmark-consumer-locator

#Scenario SOAP over http (SAM):
tesb:start-sam
features:addurl mvn:org.talend.ps.benchmark/benchmark.features/0.1-SNAPSHOT/xml
features:install benchmark-provider-sam

features:addurl mvn:org.talend.ps.benchmark/benchmark.features/0.1-SNAPSHOT/xml
features:install benchmark-consumer-sam

#Scenario SOAP over JMS:
features:install activemq-spring
activemq:create-broker
features:addurl mvn:org.talend.ps.benchmark/benchmark.features/0.1-SNAPSHOT/xml
features:install benchmark-provider-jms

features:addurl mvn:org.talend.ps.benchmark/benchmark.features/0.1-SNAPSHOT/xml
features:install benchmark-consumer-jms
