import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

public class ExchangeRate {

    private String timeLastUpdateUtc;
    private String codigoBase;
    private String codigoObjetivo;
    private Map<String, Double> ratiosDeConvercion;
    private double resultadoConvercion;

    public double getResultadoConvercion() {
        return resultadoConvercion;
    }

    public String getCodigoBase() {
        return codigoBase;
    }


    public String getCodigoObjetivo() {
        return codigoObjetivo;
    }

    public Map<String, Double> getRatiosDeConvercion() {
        return ratiosDeConvercion;
    }

    public ExchangeRate (ExchangeRateRecord exchange) {
        this.timeLastUpdateUtc = exchange.time_last_update_utc();
        this.codigoBase = exchange.base_code();
        this.ratiosDeConvercion = exchange.conversion_rates();
    }

    public ExchangeRate() {
    } // Costructor Vacío;

    public ExchangeRateRecord consultar () throws IOException, InterruptedException {
        URI direccion = URI.create("https://v6.exchangerate-api.com/v6/146e6a7a86c2e1a8e2a42f9c/latest/USD");
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(direccion)
                .build();

        HttpResponse<String> response = client
                .send(request, HttpResponse.BodyHandlers.ofString());

        String json = response.body();

        Gson gson = new GsonBuilder().create();

        ExchangeRateRecord miExchangeRateRecord = gson.fromJson(json, ExchangeRateRecord.class);
        return miExchangeRateRecord;
    }
public double exchangeTarget (double cantidad, String codigoMoneda) { // EJ: de USD a ARS
        return cantidad * getRatiosDeConvercion().get(codigoMoneda);
}

public double exchangeBack (double cantidad, String codigoMoneda) {
        return cantidad / getRatiosDeConvercion().get(codigoMoneda);
}

public double exchangeOverflow (double cantidad, String codigoMonedaBase, String codigoMonedaDestino) {
        return cantidad * (getRatiosDeConvercion().get(codigoMonedaDestino) / getRatiosDeConvercion().get(codigoMonedaBase));
}

}
