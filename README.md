cxf.benchmarks
==============

Contains performance benchmarks for CXF, Karaf deployment

#Scenario SOAP over http:
feature:repo-add mvn:org.talend.ps.benchmark/benchmark.features/0.1-SNAPSHOT/xml
feature:install benchmark-provider

feature:repo-add mvn:org.talend.ps.benchmark/benchmark.features/0.1-SNAPSHOT/xml
feature:install benchmark-consumer-dom

#Scenario SOAP over http (Locator):
tesb:start-locator
feature:repo-add mvn:org.talend.ps.benchmark/benchmark.features/0.1-SNAPSHOT/xml
feature:install benchmark-provider-locator

feature:repo-add mvn:org.talend.ps.benchmark/benchmark.features/0.1-SNAPSHOT/xml
feature:install benchmark-consumer-locator-dom

#Scenario SOAP over http (SAM):
tesb:start-sam
feature:repo-add mvn:org.talend.ps.benchmark/benchmark.features/0.1-SNAPSHOT/xml
feature:install benchmark-provider-sam

feature:repo-add mvn:org.talend.ps.benchmark/benchmark.features/0.1-SNAPSHOT/xml
feature:install benchmark-consumer-sam-dom

#Scenario SOAP over JMS:
feature:install activemq-broker
feature:repo-add mvn:org.talend.ps.benchmark/benchmark.features/0.1-SNAPSHOT/xml
feature:install benchmark-provider-jms

feature:repo-add mvn:org.talend.ps.benchmark/benchmark.features/0.1-SNAPSHOT/xml
feature:install benchmark-consumer-jms-dom

#Scenario SOAP over http (JPA):
feature:repo-add mvn:org.talend.ps.benchmark/benchmark.features/0.1-SNAPSHOT/xml
feature:install benchmark-provider-jpa

feature:repo-add mvn:org.talend.ps.benchmark/benchmark.features/0.1-SNAPSHOT/xml
feature:install benchmark-consumer-dom


