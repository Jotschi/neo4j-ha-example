package de.jotschi.examples.neo4j.cluster;

import org.neo4j.cluster.ClusterSettings;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseBuilder;
import org.neo4j.graphdb.factory.HighlyAvailableGraphDatabaseFactory;
import org.neo4j.helpers.Settings;
import org.neo4j.kernel.ha.HaSettings;

public class TestNeo4JClusterSlave extends AbstractNeo4jCluster {
	protected final static String DB_LOCATION = "target/graph-slave";
	protected final static String SERVER_ID = "2";

	public static void main(String[] args) throws InterruptedException {
		new TestNeo4JClusterSlave();
	}

	public TestNeo4JClusterSlave() throws InterruptedException {
		GraphDatabaseBuilder builder = new HighlyAvailableGraphDatabaseFactory().newHighlyAvailableDatabaseBuilder(DB_LOCATION);

		builder.setConfig(ClusterSettings.server_id, SERVER_ID);
		builder.setConfig(HaSettings.ha_server, "localhost:6002");
		builder.setConfig(ClusterSettings.cluster_server, "localhost:5002");
		builder.setConfig(HaSettings.slave_only, Settings.TRUE);
		builder.setConfig(ClusterSettings.initial_hosts, "localhost:5001,localhost:5002");

		graphDb = builder.newGraphDatabase();
		engine = new ExecutionEngine(graphDb);
		Label name = DynamicLabel.label("testnode");

		while (true) {
			try (Transaction tx = graphDb.beginTx()) {
				Node node = graphDb.createNode(name);
				node.setProperty("name", "id_" + Math.random());
				tx.success();
				System.out.println("Added node");
			}
			executeQuery("MATCH n RETURN count(n) as count");
			Thread.sleep(1000);
		}
	}

}
