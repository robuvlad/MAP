package sample.gui.pdf;



import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.util.Pair;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

public class PdfGroup extends Pdf{

    public PdfGroup(){
        System.out.println("PDF Group");
    }

    public void set(List<Pair<Integer, Double>> list, FileOutputStream f){
        Document document = new Document();
        try{
            PdfWriter pdfWriter = PdfWriter.getInstance(document, f);
            document.open();

            String parString = "This report shows data about the average per each group." +
                    "On the left side are the groups, whereas on the right side are the averages.";
            addImage(document);
            addTitle(document, "Detailed Average Report");
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
    public void addTitle(Document document, String titleString){
        super.addTitle(document, titleString);
    }

    @Override
    public void addTopParagraph(Document document, String parString){
        super.addTopParagraph(document, parString);
    }

    public void addData(Document document, List<Pair<Integer, Double>> list){
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
