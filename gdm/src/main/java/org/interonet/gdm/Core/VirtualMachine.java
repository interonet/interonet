package org.interonet.gdm.Core;

public class VirtualMachine {
    private Integer id;
    private String name;
    private String url;

    public VirtualMachine() {
    }

    public VirtualMachine(Integer id, String name, String url) {
        this.id = id;
        this.name = name;
        this.url = url;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "VirtualMachine{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VirtualMachine)) return false;

        VirtualMachine virtualMachine = (VirtualMachine) o;

        return !(id != null ? !id.equals(virtualMachine.id) : virtualMachine.id != null);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
