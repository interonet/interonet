package org.interonet.gdm.LDMConnector.LDMTask;

public class LDMTaskReturn {
    private String sliceId;
    private Boolean isSuccess;

    public LDMTaskReturn(String sliceId) {
        this.sliceId = sliceId;
        this.isSuccess = false;
    }

    public LDMTaskReturn(String sliceId, Boolean isSuccess) {
        this.sliceId = sliceId;
        this.isSuccess = isSuccess;
    }

    public String getSliceId() {
        return sliceId;
    }

    public void setSliceId(String sliceId) {
        this.sliceId = sliceId;
    }

    public Boolean getSuccess() {
        return isSuccess;
    }

    public void setSuccess(Boolean success) {
        isSuccess = success;
    }

    @Override
    public String toString() {
        return "LDMTaskReturn{" +
                "sliceId='" + sliceId + '\'' +
                ", isSuccess=" + isSuccess +
                '}';
    }
}
