package vo;

public class Page {
    private int pageNumber;
    private int pageSize;
    private String sort;
    private String sortOrder;

    public Page() {
    }

    public Page(int pageNumber, int pageSize, String sort, String sortOrder) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.sort = sort;
        this.sortOrder = sortOrder;
    }

    @Override
    public String toString() {
        return "Page{" +
                "pageNum=" + pageNumber +
                ", pageSize=" + pageSize +
                ", sort='" + sort + '\'' +
                ", sortOrder='" + sortOrder + '\'' +
                '}';
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }
}
