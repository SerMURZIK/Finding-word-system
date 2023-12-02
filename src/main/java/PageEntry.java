public class PageEntry implements Comparable<PageEntry> {
    private final String pdfName;
    private final int page;
    private final int count;

    public PageEntry(String pdfName, int page, int count) {
        this.pdfName = pdfName;
        this.page = page;
        this.count = count;
    }

    public int getPage() {
        return page;
    }

    @Override
    public String toString() {
        return "PageEntry{" +
                "pdfName='" + pdfName + '\'' +
                ", page=" + page +
                ", count=" + count +
                '}';
    }

    @Override
    public int compareTo(PageEntry o) {
        if (o.getCount() == count) {
            return Integer.compare(o.getPage(), page);
        } else {
            return Integer.compare(o.getCount(), count);
        }
    }

    public int getCount() {
        return count;
    }
}
