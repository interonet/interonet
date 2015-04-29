import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import com.googlecode.jsonrpc4j.JsonRpcHttpClient;

public class Main {
    public static void main(String[] args) throws Throwable {
        JsonRpcHttpClient client;
        System.out.println("Enter your user:");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String username = br.readLine();
        System.out.println("Enter your password:");
        String password = br.readLine();
        // Authentication.
        try {
            System.out.println("Enter your URL:default is http://202.117.15.79:8080/");
            String url = br.readLine();
            if (!url.equals("")) {
                client = new JsonRpcHttpClient(new URL(url));
            } else {
                client = new JsonRpcHttpClient(new URL("http://202.117.15.79:8080/"));
            }
            String token = client.invoke("authenticateUser", new Object[]{username, password}, String.class);
            if(token.equals("Failed")) {
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
                        System.out.println("Enter switch num");
                        String switchNum = br.readLine();
                        System.out.println("Enter vm num");
                        String vmNum = br.readLine();
                        System.out.println("Enter begin time");
                        String beginTime = br.readLine();
                        System.out.println("Enter end time");
                        String endTime = br.readLine();
/*
                        String order1 = "{"
                                + "  \"num\": {"
                                + "      \"switchesNum\": \"" + switchNum + "\","
                                + "      \"vmsNum\": \"" + vmNum + "\""
                                + "  },"
                                + "  \"time\": {"
                                + "      \"begin\": \"" + beginTime + "\","
                                + "      \"end\" : \"" + endTime + "\""
                                + "  },"
                                + "  \"topology\": {"
                                + "      \"s0:0\": \"s1:0\","
                                + "      \"h0:0\": \"s0:1\","
                                + "      \"h1:0\": \"s1:1\""
                                + "  },"
                                + "  \"switchConf\": {"
                                + "      \"s0\": \"OF1.3\","
                                + "      \"s1\" : \"OF1.3\""
                                + "  },"
                                + "  \"controllerConf\": {"
                                + "      \"ip\": \"10.0.0.253\","
                                + "      \"port\" : \"6633\""
                                + "  }"
                                + "}";
*/


                        String order1 = "{"
                                + "  \"num\": {"
                                + "      \"switchesNum\": \"" + "1" + "\","
                                + "      \"vmsNum\": \"" + "2" + "\""
                                + "  },"
                                + "  \"time\": {"
                                + "      \"begin\": \"" + beginTime + "\","
                                + "      \"end\" : \"" + endTime + "\""
                                + "  },"
                                + "  \"topology\": {"
                                + "      \"h0:0\": \"s0:0\","
                                + "      \"h1:0\": \"s0:1\""
                                + "  },"
                                + "  \"switchConf\": {"
                                + "      \"s0\": \"OF1.3\","
                                + "      \"s1\" : \"OF1.3\""
                                + "  },"
                                + "  \"controllerConf\": {"
                                + "      \"ip\": \"202.117.15.79\","
                                + "      \"port\" : \"6633\""
                                + "  }"
                                + "}";

                        String orderingStatus1 = client.invoke("orderSlice", new Object[]{token, order1}, String.class);
                        System.out.println("OrderingStatus:\n" + orderingStatus1);
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
