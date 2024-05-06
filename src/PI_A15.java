import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

// Pág. web referencia: https://openweathermap.org/city/3117735
// Pág. web comandos: https://openweathermap.org/api/one-call-3#how

public class PI_A15 {

	private static String APIKey1="1587f1c706adc7607d141a21f87bd306";	//Colocación de la API Key de OpenWeather
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		try {
		URL url=new URL("https://api.openweathermap.org/data/2.5/forecast?lat=40.416775&lon=-3.703790&lang=es&exclude=daily&appid="+APIKey1+"&units=metric&lang=sp");
		//Se especifica la logitud, latitud, la respuesta diaria del tiempo, las unidades métricas, y el lenguaje.
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();	
		connection.setRequestMethod("GET");
		connection.connect();
		
		int respuestaCode=connection.getResponseCode();	//Comentario de todos los erres que puede haber
		if (respuestaCode!=200) {
			System.out.println("Ha ocurrido un error: "+respuestaCode);
		}
		else{
			StringBuilder infoString=new StringBuilder();
			Scanner scaner=new Scanner(url.openStream());

			while (scaner.hasNextLine()) {
				infoString.append(scaner.nextLine());	
			}
			scaner.close();
			System.out.print(infoString.toString());

		}
/*	if (respuestaCode==400) {
			System.out.println("Error 400. Bad Request. Some mandatory parameters in the request are missing or some of request parameters have incorrect format or values out of allowed range. List of all parameters names that are missing or incorrect will be returned in `parameters`attribute of the `ErrorResponse` object");
		}
		if (respuestaCode==401) {
			System.out.println("Error 401. API token did not providen in the request or in case API token provided in the request does not grant access to this API. You must add API token with granted access to the product to the request before returning it.");
		}
		if (respuestaCode==404) {
			System.out.println("Error 404. Data with requested parameters (lat, lon, date etc) does not exist in service database. You must not retry the same request.");
		}
		if (respuestaCode==429) {
			System.out.println("Error 429. Too Many Requests. Key quota of requests for provided API to this API was exceeded. You may retry request after some time or after extending your key quota.");
		}
		if (respuestaCode==200){
			StringBuilder infoString=new StringBuilder();
				Scanner scaner=new Scanner(url.openStream());
				
				while (scaner.hasNext()) {
					infoString.append(scaner.hasNextLine());
				}
				scaner.close();
				
				System.out.println(infoString);
		}*/
		
		} catch (Exception e){
			e.printStackTrace();
		}
		
		
		
	}

}
