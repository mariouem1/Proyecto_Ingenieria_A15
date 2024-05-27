import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


// Pág. web referencia: https://openweathermap.org/city/3117735
// Pág. web comandos: https://openweathermap.org/api/one-call-3#how

public class PI_A15 {

	private static String parseValue(String jsonResponse, String key, String delimiter) {
        int startIndex = jsonResponse.indexOf(key) + key.length();
        if (startIndex < key.length()) {
            return ""; // Si no encuentra la clave, retorna una cadena vacía
        }
        int endIndex = jsonResponse.indexOf(delimiter, startIndex);
        if (endIndex == -1) {
            endIndex = jsonResponse.length();
        }
        return jsonResponse.substring(startIndex, endIndex).replaceAll("[\"{}]", "").trim();
    }
	
	private static String parseWeatherDescription(String jsonResponse) {
        String weatherArrayKey = "\"wind\":";
        int weatherArrayStart = jsonResponse.indexOf(weatherArrayKey) + weatherArrayKey.length();
        int weatherArrayEnd = jsonResponse.indexOf("}", weatherArrayStart) + 1;
        String weatherArrayString = jsonResponse.substring(weatherArrayStart, weatherArrayEnd);
 
        return parseValue(weatherArrayString, "\"speed\":", ",");
    }
 
    private static String rainDescription(String jsonResponse) {
        String rainKey = "\"rain\":{\"3h\":";
        return parseValue(jsonResponse, rainKey, "}");
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
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder infoString = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                infoString.append(inputLine);
            }
//            System.out.println(infoString);		//Print de comprobación de la llamada a la API
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
            String pressure=parseValue(jsonResponse, "\"pressure\":", ",");
            String time=parseValue(jsonResponse, "\"dt_txt\":", ",");

//    		En esta parte se creará una ventana donde se monstrarán los resultados obtendos.
    		JFrame cuadroTexto=new JFrame ("Proyecto Ingeniería: API Meteorológica");
    		cuadroTexto.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		//En esta parte si cerremos la ventana, se finaliazrá el programa

    		cuadroTexto.setSize(400,300);	//Tamaño de la ventana
    		cuadroTexto.setLocation(560,300);
    		cuadroTexto.setResizable(false);
    		cuadroTexto.setLayout(new FlowLayout());	//?
    		
    		JButton fetchButton=new JButton ("Actualizar");
    		JTextArea climaDisplay=new JTextArea (11, 30);
    		climaDisplay.setEditable(false);
    		
    		cuadroTexto.add(new JLabel("Parte Meteorológico de las Coordenadas: "+Lat+", "+Long));
    		
    		ImageIcon icon = new ImageIcon("weather.png"); 
            Image image = icon.getImage();
            cuadroTexto.setIconImage(image);

    		cuadroTexto.add(fetchButton);
    		cuadroTexto.add(climaDisplay);
    			climaDisplay.setText("Pulse el botón Actualizar");
    		fetchButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					String climaText = "Fecha & Hora: " + time + "\n";
                    climaText += "Descripción: " + description + "\n";
                    climaText += "Temperatura: " + temperature + " °C\n";
                    climaText += "Temperatura Mínima: " + temperaturemin + " °C\n";
                    climaText += "Temperatura Máxima: " + temperaturemax + " °C\n";
                    climaText += "Humedad: " + humidity + "%\n";
                    climaText += "Viento: " + wind + " m/s\n";
                    climaText += "Lluvia: " + rain + " mm\n";
                    climaText += "Presión atmosférica: " + pressure + " hPa\n";
                    climaText += "\nAPI Code: "+ respuestaCode;
                    climaDisplay.setText(climaText);
				}
    			
    		});	
    		
    		cuadroTexto.setVisible(true);	//??	
    	
		}

		} catch (Exception e){
			e.printStackTrace();
		}
	
	}

}
