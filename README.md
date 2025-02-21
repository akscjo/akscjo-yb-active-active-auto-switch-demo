# Demo App to showcase automatic failover to DR cluster 
## Goal
Keep the application running even if the primary database goes down by connecting to the secondary database in an Active-Active Bi-Directional Multi-Master setup.

## Deployment Topology

![Topology](src/main/resources/images/topology.png?raw=true "Title")

### Key Points
- Active-Active Multi-Master Bi-directional asynchronous replication is key
- Conflict resolution: When two databases try to write the same data, last writer wins.

### What does this Demo App do?
- App connects to both primary and dr databases.
- Rest API adds new record in customer table. For example:
`    /api/add
`
- There is retry logic built into this transaction. In case of a failure, app will retry the transactions few times with exponential backoff strategy.
- There is monitoring service which checks the health of primary database every 5 seconds (this time can be configured). If Primary cluster is down, datasource will be automatically switched to DR cluster.
- Monitoring service will keep an eye on health of Primary datasource and when it is back up, it will automatically switch the datasource back  to primary.

### How to use this App
- Download the latest release from "releases" section (or build your own from source code -` mvn clean package -DskipTests`)
- Create the schema in your databases using `resources/schema.sql`
- You can override several parameters (see application.yaml)
- Here is one sample command
```
java \
-Dspring.datasource.hikari.username=yugabyte \
-Dspring.datasource.hikari.password=yugabyte \
-Dspring.datasource.hikari.maximumPoolSize=10 \
-Dspring.datasource.hikari.data-source-properties.serverName=127.0.0.2 \
-Dspring.datasource.hikari2.username=yugabyte \
-Dspring.datasource.hikari2.password=yugabyte \
-Dspring.datasource.hikari2.maximumPoolSize=10 \
-Dspring.datasource.hikari2.data-source-properties.serverName=127.0.0.3 \
-jar yb-active-active-auto-switch-demo.jar
```

- Start your simulation using tools like jmeter, postman, etc. Sample jmeter jmx test plan is available in resources/jmeter
```
jmeter -n -t testplan.jmx -l results.jtl
```
- On starting the simulation, you will note that logs indicate that primary datasource is being used
  ![logs](src/main/resources/images/logs1.png?raw=true "Title")
- Failover your primary datasource. You will notice the traffic is automatically routed to DR database.
  ![logs](src/main/resources/images/logs2.png?raw=true "Title")
- Analyze the results. All the transactions should be successful since they will be retried  if primary cluster fails.
  ![jmeter](src/main/resources/images/jmeter-status.png?raw=true "Title")
- You can bring up the primary cluster and notice that after 5 seconds, primary datasource starts getting used again.
  ![Logs](src/main/resources/images/logs3.png?raw=true "Title")


