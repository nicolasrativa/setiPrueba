package com.example.pruebaSpring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.ArrayList;
@RestController
@SpringBootApplication
public class PruebaSpringApplication {

	public static double calcularPayback(double a, double I, double b, double f  ){
		double PayBack;
		PayBack = (a + ((I-b)/f));
		return PayBack;
	}

	public static void main(String[] args ) throws ClassNotFoundException, SQLException {
		SpringApplication.run(PruebaSpringApplication.class, args);


		int idmodulo = 1;
		
		/*-------------------------------CONECTARSE A LA BASES DE DATOS CON CREDENCIALES----------------------------------*/

		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		String connectionUrl ="jdbc:sqlserver://seti-tech-test.cyaguswpnoyv.us-east-1.rds.amazonaws.com:1433;databasename=setitechtest;user=dev67281;password=D3vS3t12023$;";
		Connection con = DriverManager.getConnection(connectionUrl);
		System.out.println("Nos conectamos");

		/*--------------------------------------------------------------------------------------------------------------------------------*/
		System.out.println("---------- array ---------------"); 

		ArrayList<ArrayList<Integer>> outer = new ArrayList<ArrayList<Integer>>();

		PreparedStatement sts = con.prepareStatement("SELECT * FROM Broker\n" +
				"INNER JOIN InvestmentProject\n" +
				"ON Broker.BrokerId = 1 AND InvestmentProject.BrokerId = 1"
		);

		ResultSet rp = sts.executeQuery();

		/*-------------CREAMOS EL ARRAY QUE CONTIENE LOS PROJECT ID DEL BROKER 1 -------------*/
		while (rp.next()) {
			ArrayList<Integer> inner = new ArrayList<Integer>();
			inner.add(rp.getInt("ProjectId"));
			outer.add(inner);
		}



		/*--------------------------------------------------------------------------------------------------------------------------------
		System.out.println("---------- pruebass ---------------");

		/*--------------------------------------------------------------------------------------------------------------------------------
		for (int i = 1; i <= outer.size() ;i++){
			PreparedStatement sttt = con.prepareStatement("SELECT *\n" +
					"fROM InvestmentProject\n" +
					"INNER JOIN ProjectMovement\n" +
					"On InvestmentProject.ProjectId = " + i +"AND ProjectMovement.ProjectId = " + i +""
			);
			ResultSet rss = sttt.executeQuery();
			while (rss.next()){
				System.out.println(rss.getInt("PeriodID") - 200);
			}
		}*/


		/*--------------------------------------------------------------------------------------------------------------------------------*/
		System.out.println("---------- Tiempo De Recuperación De La Inversión ---------------");

		System.out.println("---- PAYBACK DE LOS 100 Primeros Proyectos del Primer Broker: ---");

		double paybacksum = 0;
		double paybacksumdivided ;

		for (int i = 1; i <= 100 ;i++){

			PreparedStatement stmt = con.prepareStatement("SELECT *\n" +
					"fROM InvestmentProject\n" +
					"INNER JOIN ProjectMovement\n" +
					"On InvestmentProject.ProjectId = "+ i +" AND ProjectMovement.ProjectId = "+i+""
			);

			ResultSet rs = stmt.executeQuery();
			double suma = 0;
			double mes = 0;
			double investment=0;
			double sumah = 0;
			double ft = 0;
			double ftt = 0;

			while (rs.next()){
				/*System.out.println("MovementAmount numero  " + rs.getLong("MovementID" ) +" =  "+ rs.getLong("MovementAmount" )  );*/   /*MOSTRAR PROCEDIMIEMTO*/
				investment = rs.getLong("InvestmentAmount" );
				suma = suma + rs.getLong("MovementAmount" );

				if (suma <= investment ){
					mes = rs.getLong("MovementID" );
					sumah = suma;
				}

				ft = mes +1;
				if (rs.getLong("MovementID") == ft){
					ftt = rs.getLong("MovementAmount" );
				}



			}

			rs.close();
			stmt.close();

			double PayBack = 0;
			PayBack = calcularPayback(mes,investment ,sumah,ftt);
			paybacksum = paybacksum+ PayBack;
			System.out.println("PAYBACK ="+ PayBack + " sumaFinal =  " + paybacksum );


		/*System.out.println("MovementID ="+mes);
		System.out.println("InvestmentAmount ="+investment);
		System.out.println("Suma de flujos hasta terminar el periodo a ="+sumah);
		System.out.println("MovementID cuando se recupera la inversion inicial ="+ftt);*/

		}

		paybacksumdivided = paybacksum/100;
		System.out.println("PROMEDIO "+paybacksumdivided);




		/*--------------------------------------------------------------------------------------------------------------------------------*/
		System.out.println("---------- Beneficio Generado Por La Inversión ---------------");
		System.out.println("----VAN DE LOS 100 Primeros Proyectos del Primer Broker: ---");

		for (int i = 1; i <= 100 ;i++){

			PreparedStatement st = con.prepareStatement(
					"SELECT *\n" +
					"FROM Period \n" +
					"INNER JOIN DiscountRate\n" +
					"ON Period.PeriodId = DiscountRate.PeriodId\n" +
					"INNER JOIN ProjectMovement\n" +
					"ON Period.PeriodId = ProjectMovement.PeriodId WHERE ProjectId = '"+ i +"'\n"
			);
			ResultSet rm = st.executeQuery();

			double van = 0;
			int g = 1 ;
			int y = 1 ;
			double investment=0;
			double decimal = 0;

			while (rm.next()){

				double total  ;
				decimal =(double)(rm.getDouble("DiscountRatePercentage" )/100);

				double a =rm.getLong("MovementAmount" )/Math.pow((1+decimal),g++);
				van = van + a;

				/*System.out.println((long)rm.getLong("MovementAmount")+" / (1 + " +decimal+") ^ "+y++ +" = "+(long) a);*/     /*MOSTRAR PROCEDIMIEMTO*/
			}

			double VAN = -investment + van;
			System.out.println("VAN == "+ (long)VAN);

			rm.close();
			st.close();

		}
	}
}
