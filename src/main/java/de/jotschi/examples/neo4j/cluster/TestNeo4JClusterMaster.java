package de.jotschi.examples.neo4j.cluster;

import org.neo4j.cluster.ClusterSettings;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.graphdb.factory.GraphDatabaseBuilder;
import org.neo4j.graphdb.factory.HighlyAvailableGraphDatabaseFactory;
import org.neo4j.helpers.Settings;
import org.neo4j.kernel.ha.HaSettings;

public class TestNeo4JClusterMaster extends AbstractNeo4jCluster {
	protected final static String DB_LOCATION = "target/graph-master";
	protected final static String SERVER_ID = "1";

	public static void main(String[] args) throws InterruptedException {
		new TestNeo4JClusterMaster();
	}

	public TestNeo4JClusterMaster() throws InterruptedException {
		GraphDatabaseBuilder builder = new HighlyAvailableGraphDatabaseFactory().newHighlyAvailableDatabaseBuilder(DB_LOCATION);

		builder.setConfig(ClusterSettings.server_id, SERVER_ID);
		builder.setConfig(HaSettings.ha_server, "localhost:6001");
		builder.setConfig(HaSettings.slave_only, Settings.FALSE);
		builder.setConfig(ClusterSettings.cluster_server, "localhost:5001");
		builder.setConfig(ClusterSettings.initial_hosts, "localhost:5001,localhost:5002");

		graphDb = builder.newGraphDatabase();
		engine = new ExecutionEngine(graphDb);
		while (true) {
			Thread.sleep(1000);
			executeQuery("MATCH n RETURN count(n) as count");
		}
	}
}
