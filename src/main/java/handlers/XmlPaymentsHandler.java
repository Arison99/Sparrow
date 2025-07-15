package handlers;

import adapters.BackendServicePaymentsAdapter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class XmlPaymentsHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // /xml/payments?phone=...&biller=...&amount=...
        String query = exchange.getRequestURI().getQuery();
        String result = BackendServicePaymentsAdapter.handlePaymentsRequestXml(query);
        exchange.getResponseHeaders().add("Content-Type", "application/xml");
        exchange.sendResponseHeaders(200, result.getBytes(StandardCharsets.UTF_8).length);
        OutputStream os = exchange.getResponseBody();
        os.write(result.getBytes(StandardCharsets.UTF_8));
        os.close();
    }
}
