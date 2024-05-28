import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

// Pág. web referencia: https://openweathermap.org/city/3128760
// Pág. web comandos: https://openweathermap.org/api/one-call-3#how

public class PI_A15 {

/*
 * Con 
 * 
 */
	
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
	
	private static String parseValueWind(String jsonResponse) {
        String weatherArrayKey = "\"wind\":";
        int weatherArrayStart = jsonResponse.indexOf(weatherArrayKey) + weatherArrayKey.length();
        int weatherArrayEnd = jsonResponse.indexOf("}", weatherArrayStart) + 1;
        String weatherArrayString = jsonResponse.substring(weatherArrayStart, weatherArrayEnd);
 
        return parseValue(weatherArrayString, "\"speed\":", ",");
    }
 
    private static String parseValueRain(String jsonResponse) {
        String rainKey = "\"rain\":{\"3h\":";
        return parseValue(jsonResponse, rainKey, "}");
    }
	
	private static String parseDescription(String jsonResponse) {
		String weatherArrayKey = "\"weather\":";
        int weatherArrayStart = jsonResponse.indexOf(weatherArrayKey) + weatherArrayKey.length();
        int weatherArrayEnd = jsonResponse.indexOf("]", weatherArrayStart) + 1;
        String weatherArrayString = jsonResponse.substring(weatherArrayStart, weatherArrayEnd);

        return parseValue(weatherArrayString, "\"description\":\"", "\"");
    }
	

	
	private static String APIKey1="1587f1c706adc7607d141a21f87bd306";	//Colocación de la API Key de OpenWeather para más comodidad
	private static double Lat=41.36417;
	private static double Long=2.1505;
//	Coordenadas de la Torre de Comunicaciones de Montjuïc
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		try {
		URL url=new URL("https://api.openweathermap.org/data/2.5/forecast?lat="+Lat+"&lon="+Long+"&lang=es&exclude=hourly,daily,aletrs&units=metric&appid="+APIKey1);
		//Se especifica la logitud, latitud, la respuesta diaria del tiempo, las unidades métricas, posibles alertas y el lenguaje.
		
		//Llamada a la API a través de una conexión URL
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();	
		connection.setRequestMethod("GET");
		connection.connect();
		
		int respuestaCode=connection.getResponseCode();	
		if (respuestaCode != 200) {
			System.out.println("Code Error: "+respuestaCode);	//Comentario del código de error que puede haber de la API
		}
		else{
/*
 * El método BufferReader se utiliza para leer la respuesta de la solicitud HTTP realizada a una API y almacenar 
 * esa respuesta en una cadena String nombrada "infoString"
 */
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;	//La variable "inputLine" se utilizará para almacenar cada línea de texto leída de la respuesta HTTP.
            StringBuilder infoString = new StringBuilder();	//Se utiliza para construir la cadena final que contendrá toda la respuesta HTTP

            while ((inputLine = in.readLine()) != null) {	//Se leerá cada línea de la respuesta HTTP
                infoString.append(inputLine);	//Esta línea añade cada línea leída al StringBuilder (infoString).
            }
//            System.out.println(infoString);		//Print de comprobación de la llamada a la API
            in.close();

            String jsonResponse = infoString.toString();
/* 
 * Creación de un String para contener la información de la llamada API
 * 
 * A partr de este String, seleccionaremos los datos que se quieran visualizar gracias al método parseValue y el 
 *   dato que se quiere obtener.
 */
            String temperature = parseValue(jsonResponse, "\"temp\":", ",");
            String temperaturemin = parseValue(jsonResponse, "\"temp_min\":", ",");
            String temperaturemax = parseValue(jsonResponse, "\"temp_max\":", ",");
            String humidity = parseValue(jsonResponse, "\"humidity\":", ",");
            String wind = parseValueWind(jsonResponse);
            String description = parseDescription(jsonResponse);  
            String rain= parseValueRain(jsonResponse);  
            String pressure=parseValue(jsonResponse, "\"pressure\":", ",");
            String time=parseValue(jsonResponse, "\"dt_txt\":", ",");

//    		En esta parte se creará una ventana donde se monstrarán los resultados obtendos.
    		JFrame cuadroTexto=new JFrame ("Proyecto Ingeniería: API Meteorológica");	//Nombre principal de la ventana
    		cuadroTexto.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		//En esta parte si cerremos la ventana, se finalizará el programa

    		cuadroTexto.setSize(400,300);	//Tamaño de la ventana
    		cuadroTexto.setLocation(560,300);	//Localización de la ventana
    		cuadroTexto.setResizable(false);	//Cancelación de redimensión de ventana
    		cuadroTexto.setLayout(new FlowLayout());	
    		
    		JButton fetchButton=new JButton ("Actualizar");		//Creación del botón "Actualizar"
    		JTextArea climaDisplay=new JTextArea (11, 30);	//Tamaño de la subventana donde se mostrará la información
    		climaDisplay.setEditable(false);	//La subventana no será editable mediante texto introducido
    		
    		cuadroTexto.add(new JLabel("Parte Meteorológico de las Coordenadas: "+Lat+", "+Long));
    		
    		ImageIcon icon = new ImageIcon("weather.png"); 	//Cambio del icono de la ventana de JAVA por un icomo WEATHER
            Image image = icon.getImage();					//dentro de la carpeta del proyecto
            cuadroTexto.setIconImage(image);				//Insertar la imagen

    		cuadroTexto.add(fetchButton);	//Creación de un botón para actualizar la información del tiempo
    		cuadroTexto.add(climaDisplay);	//Creación de una subventana donde se mostrará la información del tiempo
    			climaDisplay.setText("---------------------Pulse el Botón Actualizar---------------------");
    		fetchButton.addActionListener(new ActionListener() {	//Le diremos al botón que hacer después de activarse
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					String climaText ="Fecha & Hora: " + time + "\n";
                    climaText +="Descripción: " + description + "\n";
                    climaText +="Temperatura: " + temperature + " °C\n";
                    climaText +="Temperatura Mínima: " + temperaturemin + " °C\n";
                    climaText +="Temperatura Máxima: " + temperaturemax + " °C\n";
                    climaText +="Humedad: " + humidity + "%\n";
                    climaText +="Viento: " + wind + " m/s\n";
                    climaText +="Lluvia: " + rain + " mm\n";
                    climaText +="Presión atmosférica: " + pressure + " hPa\n";
                    climaText +="\nAPI Code: "+ respuestaCode;
                    climaDisplay.setText(climaText);	//Todos los datos serán impresos en la subventana
				}
    			
    		});	
    		
    		cuadroTexto.setVisible(true);	//Hace visible el JFrame cuadroTexto	
    	
		}

		} catch (Exception e){
			e.printStackTrace();
//			Impresión de errores posibles
		}
	
	}

}
