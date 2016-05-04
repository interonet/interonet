package org.interonet.gdm.LDMConnector.LDMTask;

public class LDMTaskReturn {
    private String sliceId;
    private Boolean isSuccess;
    private Throwable throwable;

    public LDMTaskReturn() {
    }

    public LDMTaskReturn(String sliceId) {
        this.sliceId = sliceId;
        this.isSuccess = false;
        this.throwable = null;
    }

    public LDMTaskReturn(String sliceId, Boolean isSuccess) {
        this.sliceId = sliceId;
        this.isSuccess = isSuccess;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
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
