public class PageEntry implements Comparable<PageEntry> {

    private final String pdfName;
    private final int page;
    private final int count;

    public PageEntry(String pdfName, int page, int count) {
        this.pdfName = pdfName;
        this.page = page;
        this.count = count;
    }

    @Override
    public int compareTo(PageEntry o) {
        return o.count <= this.count ? -1 : 0;
    }

    @Override
    public String toString() {
        return "pdfName: " + pdfName + "\npage: " + page + "\ncount: " + count + "\n";
    }
}
