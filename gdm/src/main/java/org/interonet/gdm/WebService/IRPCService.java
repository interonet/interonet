package org.interonet.gdm.WebService;

public interface IRPCService {
    String authenticateUser(String username, String password);

    String getSwitchesUsageStatus(String authToken);

    String getVMsUsageStatus(String authToken);

    String orderSlice(String authtoken, String order);

    String getOrdersList(String authtoken);

    String getOrderInfoByID(String authtoken, String orderID);

    String deleteOrderByID(String authtoken, String orderID);

    String getRunningSlices(String authtoken);

//    public String stopRunningSliceByID(String authtoken, String sliceID);

    String getRunningSliceInfoById(String authToken, String sliceID);
}
