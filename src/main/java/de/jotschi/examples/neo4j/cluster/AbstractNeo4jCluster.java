package de.jotschi.examples.neo4j.cluster;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;

public abstract class AbstractNeo4jCluster {
	GraphDatabaseService graphDb;
	ExecutionEngine engine;

	public void executeQuery(String query) {
		ExecutionResult result;

		try (Transaction ignored = graphDb.beginTx()) {
			result = engine.execute(query);
			System.out.println(result.dumpToString());
		}

	}
}
