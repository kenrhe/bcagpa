package com.heroku.bcagpa;

import java.sql.Connection;
import java.sql.DriverManager;

public class Users {
	public static void Connect() throws Exception {
		Connection c = null;
		Class.forName("org.postgre.Driver");
		c = DriverManager.getConnection("jdbc:postgresql://ec2-54-225-101-4.compute-1.amazonaws.com:5432/daepv9eslp2f6n", "ufurjjnyaoogst", "JuF24BEoj43syFdrNMp-DOkRQ6");
	}
}
