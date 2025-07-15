package adapters;

import core.BackendService;
import java.util.Map;
import java.util.*;

public class BackendServiceJsonAdapter {
    public static String getAllUsersJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        boolean first = true;
        for (java.util.Map.Entry<String, BackendService.User> entry : BackendService.getUsers().entrySet()) {
            if (!first) sb.append(",");
            sb.append("{\"phone\":\"").append(entry.getKey()).append("\"}");
            first = false;
        }
        sb.append("]");
        return sb.toString();
    }
}
