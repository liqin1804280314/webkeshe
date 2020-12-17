package vo;

public class Resource {
    private int resourceId;
    private String resourceName;
    private String url;

    public Resource(int resourceId, String resourceName, String url) {
        this.resourceId = resourceId;
        this.resourceName = resourceName;
        this.url = url;
    }

    public Resource() {
        super();
    }

    @Override
    public String toString() {
        return "Resource{" +
                "resourceId=" + resourceId +
                ", resourceName='" + resourceName + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
