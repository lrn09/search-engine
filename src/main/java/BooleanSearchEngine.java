import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class BooleanSearchEngine implements SearchEngine {

    Map<String, List<PageEntry>> wordsForSearch = new HashMap<>();

    public BooleanSearchEngine(File pdfsDir) throws IOException {

        File[] pdfs = pdfsDir.listFiles();

        if (pdfs != null) {
            for (File file : pdfs) {
                var doc = new PdfDocument(new PdfReader(file));
                for (int page = 1; page <= doc.getNumberOfPages(); page++) {
                    var text = PdfTextExtractor.getTextFromPage(doc.getPage(page));
                    var words = text.split("\\P{IsAlphabetic}+");

                    Map<String, Integer> freqs = new HashMap<>();
                    for (var word : words) {
                        if (word.isEmpty()) {
                            continue;
                        }
                        freqs.put(word.toLowerCase(), freqs.getOrDefault(word.toLowerCase(), 0) + 1);
                    }

                    for (var word : freqs.keySet()) {
                        List<PageEntry> tmpList = wordsForSearch.computeIfAbsent(word, k -> new ArrayList<>());
                        int count = freqs.get(word);
                        tmpList.add(new PageEntry(file.getName(), page, count));
                    }
                    freqs.clear();
                }
                for (String key : wordsForSearch.keySet()) {
                    Collections.sort(wordsForSearch.get(key));
                }
            }
        }
    }

    @Override
    public List<PageEntry> search(String word) {
        word = word.toLowerCase();
        return wordsForSearch.get(word);
    }
}
