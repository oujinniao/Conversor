import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner; // Asegúrate de importar Scanner

import com.google.gson.Gson;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import com.google.gson.JsonObject; // Importación corregida

public class ConversorDeDivisas {

    private static final String API_KEY = "a3f6cf9b4e68d27096eceab1";
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/latest/";

    public static double convertir(String monedaOrigen, String monedaDestino, double cantidad) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(BASE_URL + monedaOrigen + "?symbols=" + monedaDestino);
        CloseableHttpResponse response = httpClient.execute(request);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))) {
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            System.out.println("Respuesta de la API: " +result.toString());

            JsonObject jsonObject = new Gson().fromJson(result.toString(), JsonObject.class);

            // Verifica si la clave "rates" existe

            if (jsonObject.has("conversion_rates")) {
                JsonObject rates = jsonObject.getAsJsonObject("conversion_rates");
                if (rates.has(monedaDestino)) {
                    double tasaCambio = rates.get(monedaDestino).getAsDouble();
                    return cantidad * tasaCambio;
                } else {
                    throw new Exception("La moneda de destino no se encontró en la respuesta.");
                }
            } else {
                throw new Exception("No se encontró la clave 'rates' en la respuesta.");
            }
        } catch (Exception e) {
            System.err.println("Error al obtener la tasa de cambio: " + e.getMessage());
            throw e;
        }
    }

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        int opcion;
        double cantidad = 0;

        do {
            System.out.println("Bienvenido al Conversor de Divisas");
            System.out.println("1. Dólar a Euros (USD -> EUR)");
            System.out.println("2. Euro a Dólares (EUR -> USD)");
            System.out.println("3. Peso Mexicano a Dólar (MXN -> USD)");
            System.out.println("4. Real Brasileño a Dólar (BRL -> USD)");
            System.out.println("5. Yen Japonés a Dólar (JPY -> USD)");
            System.out.println("6. Salir");
            System.out.print("Ingresa una opción: ");

            while (!scanner.hasNextInt()) {
                System.out.println("Opción no válida. Ingresa un número del 1 al 6.");
                scanner.next();
            }
            opcion = scanner.nextInt();

            if (opcion != 6) {
                System.out.print("Ingresa la cantidad a convertir: ");
                cantidad = scanner.nextDouble(); // Solicita la cantidad a convertir
            }

            switch (opcion) {
                case 1:
                    double resultado1 = convertir("USD", "EUR", cantidad);
                    System.out.println("El resultado es: " + resultado1);
                    break;
                case 2:
                    double resultado2 = convertir("EUR", "USD", cantidad);
                    System.out.println("El resultado es: " + resultado2);
                    break;
                case 3:
                    double resultado3 = convertir("MXN", "USD", cantidad);
                    System.out.println("El resultado es: " + resultado3);
                    break;
                case 4:
                    double resultado4 = convertir("BRL", "USD", cantidad);
                    System.out.println("El resultado es: " + resultado4);
                    break;
                case 5:
                    double resultado5 = convertir("JPY", "USD", cantidad);
                    System.out.println("El resultado es: " + resultado5);
                    break;
                case 6:
                    System.out.println("Gracias por utilizar el Conversor");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 6);

        scanner.close(); // Cierra el scanner al final
    }
}
