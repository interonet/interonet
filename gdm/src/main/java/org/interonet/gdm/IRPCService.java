package org.interonet.gdm;

public interface IRPCService {
    public String authenticateUser(String username, String password);

    public String getSwitchesUsageStatus(String authToken);

    public String getVMsUsageStatus(String authToken);

    public String orderSlice(String authtoken, String order);

    public String getOrdersList(String authtoken);

    public String getOrderInfoByID(String authtoken, String orderID);

    public String deleteOrderByID(String authtoken, String orderID);

    public String getRunningSlices(String authtoken);
//    public String stopRunningSliceByID(String authtoken, String sliceID);
}
