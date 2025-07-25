package handlers;

import adapters.BackendServiceSavingsLoansAdapter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class XmlSavingsLoansHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // /xml/savingsloans?phone=...&action=save|loan&amount=...
        String query = exchange.getRequestURI().getQuery();
        String result = BackendServiceSavingsLoansAdapter.handleSavingsLoansRequestXml(query);
        exchange.getResponseHeaders().add("Content-Type", "application/xml");
        exchange.sendResponseHeaders(200, result.getBytes(StandardCharsets.UTF_8).length);
        OutputStream os = exchange.getResponseBody();
        os.write(result.getBytes(StandardCharsets.UTF_8));
        os.close();
    }
}
