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
        if (endIndex == -2) {
            endIndex = jsonResponse.length();
        }
        return jsonResponse.substring(startIndex, endIndex).trim();
    }
	
	private static String parseWeatherDescription(String jsonResponse) {
        String weatherArrayKey = "\"wind\":";
        int weatherArrayStart = jsonResponse.indexOf(weatherArrayKey) + weatherArrayKey.length();
        int weatherArrayEnd = jsonResponse.indexOf("]", weatherArrayStart) + 1;
        String weatherArrayString = jsonResponse.substring(weatherArrayStart, weatherArrayEnd);

        return parseValue(weatherArrayString, "\"speed\": \"", "\"");
    }
	
	private static String rainDescription(String jsonResponse) {
		String weatherArrayKey = "\"rain\":";
        int weatherArrayStart = jsonResponse.indexOf(weatherArrayKey) + weatherArrayKey.length();
        int weatherArrayEnd = jsonResponse.indexOf("]", weatherArrayStart) + 1;
        String weatherArrayString = jsonResponse.substring(weatherArrayStart, weatherArrayEnd);

        return parseValue(weatherArrayString, "\"3h\": \"", "\"");
    }
	
	private static String WeatherDescription(String jsonResponse) {
		String weatherArrayKey = "\"weather\":";
        int weatherArrayStart = jsonResponse.indexOf(weatherArrayKey) + weatherArrayKey.length();
        int weatherArrayEnd = jsonResponse.indexOf("]", weatherArrayStart) + 1;
        String weatherArrayString = jsonResponse.substring(weatherArrayStart, weatherArrayEnd);

        return parseValue(weatherArrayString, "\"description\":\"", "\"");
    }
	

	
	private static String APIKey1="1587f1c706adc7607d141a21f87bd306";	//Colocación de la API Key de OpenWeather
	private static double Lat=52.6289;
	private static double Long=4.74403;
//	Coordenadas de la Torre de Comunicaciones de Montjuïc	41.36417	2.15056
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		try {
		URL url=new URL("https://api.openweathermap.org/data/2.5/forecast?lat="+Lat+"&lon="+Long+"&lang=es&exclude=hourly,daily,aletrs&units=metric&appid="+APIKey1);
		//Se especifica la logitud, latitud, la respuesta diaria del tiempo, las unidades métricas, y el lenguaje.
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();	
		connection.setRequestMethod("GET");
		connection.connect();
		
		int respuestaCode=connection.getResponseCode();	//Comentario de todos los erres que puede haber
		if (respuestaCode != 200) {
			System.out.println("Code Error: "+respuestaCode);
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
            String temperaturemin = parseValue(jsonResponse, "\"temp_min\":", ",");
            String temperaturemax = parseValue(jsonResponse, "\"temp_max\":", ",");
            String humidity = parseValue(jsonResponse, "\"humidity\":", ",");
            String wind = parseWeatherDescription(jsonResponse);
            String description = WeatherDescription(jsonResponse);  
            String rain= rainDescription(jsonResponse);  
            String time=parseValue(jsonResponse, "\"dt_txt\":", ",");

            System.out.println("\n"+"Día & Hora: " + time);
            System.out.println("Descripción: " + description);	
            System.out.println("Temperatura: " + temperature + " °C");
            System.out.println("Temperatura Mínima: " + temperaturemin + " °C");
            System.out.println("Temperatura Máxima: " + temperaturemax + " °C");
            System.out.println("Humedad: " + humidity + "%");
            System.out.println("Viento: " + wind +" km/h");
            System.out.println("Lluvia: " + rain +" mm");

		}

		} catch (Exception e){
			e.printStackTrace();
		}
	/*	
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
	
		*/	
	
	}

}
