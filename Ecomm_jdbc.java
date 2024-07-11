
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Ecomm_jdbc {
public static void main(String[] args) {
	try {
		Class.forName("oracle.jdbc.driver.OracleDriver");
		Connection con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","system");
		Statement smt=con.createStatement();
		smt.executeLargeUpdate("create table customer(Id int,Name varchar(20))");
		System.out.println("Table created successfully------");
		} catch (Exception e) {
		System.out.println(e);
	}
	
}
}
