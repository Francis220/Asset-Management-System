package com.iffi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.DefaultConfiguration;

/**
 * Creates a group of reusable connections to access the database. Gets the
 * connection and puts it back in the queue after use.
 *
 * @author sinezanz & eallen
 */

public class ConnectionPool {

	public static final Logger LOG = LogManager.getLogger(DatabaseLoader.class);

	static {
		Configurator.initialize(new DefaultConfiguration());
		Configurator.setRootLevel(Level.INFO);
	}
	public static final String PARAMETERS = "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

	public static final String USERNAME = "sinezanz";
	public static final String PASSWORD = "uQ9-FN";
	public static final String URL = "jdbc:mysql://cse.unl.edu/" + USERNAME + PARAMETERS;

	public static final int NUM_CONNECTIONS = 20;
	public static BlockingQueue<Connection> POOL = new ArrayBlockingQueue<>(NUM_CONNECTIONS);

	static {
		try {
			for (int i = 0; i < NUM_CONNECTIONS; i++) {
				Connection conn;
				conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
				POOL.add(conn);
			}
		} catch (SQLException e) {
			LOG.error("Error: ", e);
			throw new RuntimeException(e);
		}
	}

	public static Connection getConnection() {
		try {
			return POOL.take();
		} catch (InterruptedException e) {
			LOG.error("Error: ", e);
			throw new RuntimeException(e);
		}
	}

	public static void putConnection(Connection conn) {
		try {
			POOL.put(conn);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
