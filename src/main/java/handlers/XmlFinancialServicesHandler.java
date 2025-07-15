package handlers;

import adapters.BackendServiceFinancialServicesAdapter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class XmlFinancialServicesHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // /xml/financialservices?phone=...&service=insurance|investment&amount=...
        String query = exchange.getRequestURI().getQuery();
        String result = BackendServiceFinancialServicesAdapter.handleFinancialServicesRequestXml(query);
        exchange.getResponseHeaders().add("Content-Type", "application/xml");
        exchange.sendResponseHeaders(200, result.getBytes(StandardCharsets.UTF_8).length);
        OutputStream os = exchange.getResponseBody();
        os.write(result.getBytes(StandardCharsets.UTF_8));
        os.close();
    }
}
