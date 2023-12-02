import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BooleanSearchEngine implements SearchEngine {
    private final List<PdfDocument> pdfList = new ArrayList<>();
    private final Map<String, List<PageEntry>> pageEntryMap = new HashMap<>();// слово - PageEntry

    public BooleanSearchEngine(File pdfsDir) {
        addPdfs(pdfsDir);
        String[] wordArray = null;
        for (PdfDocument document : pdfList) {

            for (int i = 1; i < document.getNumberOfPages() + 1; i++) { // перебор страниц

                Map<String, Integer> frequencies = new HashMap<>(); // ключ - слово, значение - частоты
                String text = PdfTextExtractor.getTextFromPage(document.getPage(i));
                wordArray = text.split("\\P{IsAlphabetic}+");

                for (String word : wordArray) { //перебор слов
                    if (word.isEmpty()) {
                        continue;
                    }
                    word = word.toLowerCase();
                    frequencies.put(word, frequencies.getOrDefault(word, 0) + 1);
                }

                for (String word : frequencies.keySet()) {
                    if (!pageEntryMap.containsKey(word)) {
                        List<PageEntry> listForChangingMap = new ArrayList<>();
                        listForChangingMap.add(new PageEntry(document.getDocumentInfo().getTitle(), i, frequencies.get(word)));
                        pageEntryMap.put(word, listForChangingMap);
                    } else {
                        pageEntryMap.get(word).add(new PageEntry(document.getDocumentInfo().getTitle(), i, frequencies.get(word)));
                    }
                }
            }
        }
    }

    public void addPdfs(File pdfsDir) {
        try {
            for (File file : Objects.requireNonNull(pdfsDir.listFiles())) {
                pdfList.add(new PdfDocument(new PdfReader(file.getPath())));
            }
        } catch (IOException e) {
            System.err.println("addPdfs err " + e.getMessage());
        }
    }

    @Override
    public List<PageEntry> search(String word) {
        return pageEntryMap.get(word.toLowerCase());
    }
}
