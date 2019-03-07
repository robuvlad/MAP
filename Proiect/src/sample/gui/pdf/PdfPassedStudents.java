package sample.gui.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.util.Pair;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

public class PdfPassedStudents extends Pdf {

    public PdfPassedStudents(){
        System.out.println("Pdf Passed Students");
    }

    public void set(List<Pair<String, Double>> list, FileOutputStream f){
        Document document = new Document();
        try{
            PdfWriter pdfWriter = PdfWriter.getInstance(document, f);
            document.open();

            String parString = "This report shows data about the students who passed the exam." +
                    "On the left side are the students, whereas on the right side are the averages.";
            addImage(document);
            addTitle(document, "Students who passed the exam");
            addTopParagraph(document, parString);
            addData(document, list);
            addDateAndSignature(document);

            document.close();
            pdfWriter.close();
        } catch(DocumentException ex){
            ex.printStackTrace();
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

    protected void addData(Document document, List<Pair<String, Double>> list){
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
