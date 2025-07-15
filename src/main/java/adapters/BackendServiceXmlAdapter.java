package adapters;

import core.BackendService;
import java.util.Map;
import java.util.*;

public class BackendServiceXmlAdapter {
    public static String getAllUsersXml() {
        StringBuilder sb = new StringBuilder();
        sb.append("<users>");
        for (java.util.Map.Entry<String, BackendService.User> entry : BackendService.getUsers().entrySet()) {
            sb.append("<user><phone>").append(entry.getKey()).append("</phone></user>");
        }
        sb.append("</users>");
        return sb.toString();
    }
}
