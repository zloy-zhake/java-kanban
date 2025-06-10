package httpserver;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class HistoryHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) {
        Gson gson = new Gson();
    }
}
