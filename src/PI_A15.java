import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import java.awt.FlowLayout;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


// Pág. web referencia: https://openweathermap.org/city/3117735
// Pág. web comandos: https://openweathermap.org/api/one-call-3#how

public class PI_A15 {

	private static String parseValue(String jsonResponse, String key, String delimiter) {
        int startIndex = jsonResponse.indexOf(key) + key.length();
        int endIndex = jsonResponse.indexOf(delimiter, startIndex);
        if (endIndex == -1) {
            endIndex = jsonResponse.length();
        }
        return jsonResponse.substring(startIndex, endIndex).trim();
    }
	
	private static String parseWeatherDescription(String jsonResponse) {
        String weatherArrayKey = "\"wind\":";
        int weatherArrayStart = jsonResponse.indexOf(weatherArrayKey) + weatherArrayKey.length();
        int weatherArrayEnd = jsonResponse.indexOf("]", weatherArrayStart) + 1;
        String weatherArrayString = jsonResponse.substring(weatherArrayStart, weatherArrayEnd);

        return parseValue(weatherArrayString, "\"speed\":\"", "\"");
    }
	
	private static String WeatherDescription(String jsonResponse) {
		String weatherArrayKey = "\"weather\":";
        int weatherArrayStart = jsonResponse.indexOf(weatherArrayKey) + weatherArrayKey.length();
        int weatherArrayEnd = jsonResponse.indexOf("]", weatherArrayStart) + 1;
        String weatherArrayString = jsonResponse.substring(weatherArrayStart, weatherArrayEnd);

        return parseValue(weatherArrayString, "\"description\":\"", "\"");
    }
	
	private static String APIKey1="1587f1c706adc7607d141a21f87bd306";	//Colocación de la API Key de OpenWeather
	private static double Lat=40.416775;
	private static double Long=-3.703790;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		try {
		URL url=new URL("https://api.openweathermap.org/data/2.5/forecast?lat="+Lat+"&lon="+Long+"&lang=es&exclude=daily&units=metric&appid="+APIKey1);
		//Se especifica la logitud, latitud, la respuesta diaria del tiempo, las unidades métricas, y el lenguaje.
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();	
		connection.setRequestMethod("GET");
		connection.connect();
		
		int respuestaCode=connection.getResponseCode();	//Comentario de todos los erres que puede haber
		if (respuestaCode==400) {
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
		else{
			System.out.println(respuestaCode);
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder infoString = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                infoString.append(inputLine);
            }
            System.out.println(infoString);
            in.close();

            // Procesa la respuesta
            String jsonResponse = infoString.toString();
            String temperature = parseValue(jsonResponse, "\"temp\":", ",");
            String humidity = parseValue(jsonResponse, "\"humidity\":", ",");
            String wind = parseWeatherDescription(jsonResponse);
            String description = WeatherDescription(jsonResponse);  
            String time=parseValue(jsonResponse, "\"dt_txt\":", ",");

            
            System.out.println("Data & Time: " + time);
            System.out.println("Temperature: " + temperature + " °C");
            System.out.println("Weather Description: " + description);	
            //RACIACION UV
            System.out.println("Humidity: " + humidity + "%");
            System.out.println("Wind Speed: " + wind +" km/h");

		}

		} catch (Exception e){
			e.printStackTrace();
		}
		
//		En esta parte se creará una ventana donde se monstrarán los resultados obtendos.
		JFrame cuadroTexto=new JFrame ("Proyecto Ingeniería: API Meteorológica");
		cuadroTexto.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		//En esta parte si cerremos la ventana, se finaliazrá el programa

		cuadroTexto.setSize(400,300);	//Tamaño de la ventana
		cuadroTexto.setLayout(new FlowLayout());	//?
		
		JButton fetchButton=new JButton ("Actualizar");
		JTextArea climaDisplay=new JTextArea (30, 90);
		climaDisplay.setEditable(false);
		
		cuadroTexto.add(new JLabel("Pare Meteorológico de las Coordenadas: "+Lat+", "+Long));

		cuadroTexto.add(fetchButton);
		cuadroTexto.add(climaDisplay);
		
		cuadroTexto.setVisible(true);	//??	
	
			
	
	}

}
