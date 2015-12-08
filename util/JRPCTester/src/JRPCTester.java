import com.googlecode.jsonrpc4j.JsonRpcHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JRPCTester {
    public static void main(String[] args) throws Throwable {
        JsonRpcHttpClient client;
        System.out.println("Enter your user: default is root");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String username = br.readLine();
        if (username.equals(""))
            username = "root";
        System.out.println("Enter your password: default is root");
        String password = br.readLine();
        if (password.equals(""))
            password = "root";

        // Authentication.
        try {
            System.out.println("Enter your URL:default is http://localhost:8080/");
            String url = br.readLine();
            if (!url.equals("")) {
                client = new JsonRpcHttpClient(new URL(url));
            } else {
                client = new JsonRpcHttpClient(new URL("http://localhost:8080/"));
            }
            String token = client.invoke("authenticateUser", new Object[]{username, password}, String.class);
            if (token.equals("Failed")) {
                System.out.print("Auth Failed");
                System.exit(1);
            }
            System.out.println("your token is: " + token);

            while (true) {
                System.out.println("*******************************");
                System.out.println("1: getSwitchesUsageStatus");
                System.out.println("2: getVMsUsageStatus");
                System.out.println("3: getOrdersList");
                System.out.println("4: getOrderInfoByID");
                System.out.println("5: getRunningSlices");
                System.out.println("6: orderSlice");
                System.out.println("7: deleteOrderByID");
                System.out.println("8: getRunningSliceInfoByID");
                System.out.println("0: exit");
                System.out.println("*******************************");
                int cmd = Integer.parseInt(br.readLine());
                switch (cmd) {
                    case 1:
                        System.out.println("switchesUsageStatus:\n" + client.invoke("getSwitchesUsageStatus", new Object[]{token}, String.class));
                        break;
                    case 2:
                        System.out.println("vmsUsageStatus:\n" + client.invoke("getVMsUsageStatus", new Object[]{token}, String.class));
                        break;
                    case 3:
                        System.out.println("getOrdersList:\n" + client.invoke("getOrdersList", new Object[]{token}, String.class));
                        break;
                    case 4:
                        System.out.println("Enter orderID:");
                        String orderID = br.readLine();
                        System.out.println("getOrderInfoByID:\n" + client.invoke("getOrderInfoByID", new Object[]{token, orderID}, String.class));
                        break;
                    case 5:
                        System.out.println("getRunningSlices:\n" + client.invoke("getRunningSlices", new Object[]{token}, String.class));
                        break;
                    case 6:
                        System.out.println("Enter order JSON file path(default is $CLASSPATH/order.json):");
                        byte[] encoded = Files.readAllBytes(Paths.get("util/JRPCTester/order.json"));
                        String order = new String(encoded, Charset.defaultCharset());
                        String orderingStatus = client.invoke("orderSlice", new Object[]{token, order}, String.class);
                        System.out.println("OrderingStatus:\n" + orderingStatus);
                        break;
                    case 7:
                        System.out.println("Enter orderId");
                        String orderId = br.readLine();
                        String status = client.invoke("deleteOrderByID", new Object[]{token, orderId}, String.class);
                        System.out.println(status);
                        break;
                    case 8:
                        System.out.println("Enter orderId");
                        String orderId1 = br.readLine();
                        String status1 = client.invoke("getRunningSliceInfoById", new Object[]{token, orderId1}, String.class);
                        System.out.println(status1);
                        break;
                    case 0:
                        System.out.println("Bye");
                        System.exit(1);
                    default:
                        System.out.println("Error");
                }
            }
        } catch (Exception e) {
            System.out.println(e);
            System.exit(1);
        }
    }
}
