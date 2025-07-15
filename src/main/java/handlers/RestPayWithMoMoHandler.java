package handlers;

import adapters.BackendServicePayWithMoMoAdapter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class RestPayWithMoMoHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // /api/paywithmomo?phone=...&merchant=...&amount=...
        String query = exchange.getRequestURI().getQuery();
        String result = BackendServicePayWithMoMoAdapter.handlePayWithMoMoRequest(query);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, result.getBytes(StandardCharsets.UTF_8).length);
        OutputStream os = exchange.getResponseBody();
        os.write(result.getBytes(StandardCharsets.UTF_8));
        os.close();
    }
}
