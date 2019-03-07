package sample.gui.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.util.Pair;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

public class PdfTeachers extends Pdf {

    public PdfTeachers(){
        System.out.println("Pdf Teachers");
    }

    public void set(List<Pair<String, Long>> list, FileOutputStream f){
        Document document = new Document();
        try{
            PdfWriter pdfWriter = PdfWriter.getInstance(document, f);
            document.open();

            String parString = "This report shows data about the number of students." +
                    "On the left side are the teachers, whereas on the right side is the number of students.";
            addImage(document);
            addTitle(document, "Detailed Report");
            addTopParagraph(document, parString);
            addData(document, list);
            addDateAndSignature(document);

            document.close();
            pdfWriter.close();
        }catch(DocumentException e){
            e.printStackTrace();
        }
    }

    @Override
    public void addTitle(Document document, String titleString) {
        super.addTitle(document, titleString);
    }

    @Override
    public void addTopParagraph(Document document, String parString) {
        super.addTopParagraph(document, parString);
    }

    private void addData(Document document, List<Pair<String, Long>> list){
        addEmptyLine(document,2);

        list.forEach(x -> {
            try {
                Paragraph p = new Paragraph(x.getKey() + " : " + String.valueOf(x.getValue()),
                        FontFactory.getFont(FontFactory.HELVETICA, 12f));
                p.setAlignment(Element.ALIGN_CENTER);
                document.add(p);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        });
    }
}
