package handlers;

import adapters.BackendServiceAirtimeAdapter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class RestAirtimeHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // /api/airtime?phone=...&amount=...
        String query = exchange.getRequestURI().getQuery();
        String result = BackendServiceAirtimeAdapter.handleAirtimeRequest(query);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, result.getBytes(StandardCharsets.UTF_8).length);
        OutputStream os = exchange.getResponseBody();
        os.write(result.getBytes(StandardCharsets.UTF_8));
        os.close();
    }
}
