import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class Jdbc_insert {
	public static void main(String[] args) {
		  try {
				Class.forName("oracle.jdbc.driver.OracleDriver");
				Connection con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1251:xe","system","system");
				PreparedStatement psmte=con.prepareStatement("insert into customer values(?,?)");
				
				BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
				
				while(true)
				{
					System.out.println("Enter customer id");
					int custId=Integer.parseInt(br.readLine());
					System.out.println("Enter customer name1");
					String custName=(br.readLine());
					psmte.setInt(1,custId);
					psmte.setString(2, custName);
					int count=psmte.executeUpdate();
					if(count>0)
					{
						System.out.println(count+"record inserted");
					}
					else
					{
						System.out.println("no record inserted");
					}
					System.out.println("Do you wants to insert more records");
					String ch=br.readLine();
				    if(ch.equalsIgnoreCase("no"))//yes=YES
				    break;
				    
				
				}
		  }
		  catch(Exception e) {
			System.out.println(e);  
		  }
		  
	}

}
