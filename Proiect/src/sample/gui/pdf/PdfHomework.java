package sample.gui.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.util.Pair;

import javax.print.Doc;
import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

public class PdfHomework extends Pdf{

    public PdfHomework() {
        System.out.println("PDF Homework");
    }

    public void set(List<Pair<String, Double>> list, FileOutputStream f){

        Document document = new Document();
        try{
            PdfWriter writer = PdfWriter.getInstance(document, f);
            document.open();

            String parString = "This report shows data about the grades average per each assignment." +
                    " This report also shows the hardest homework over the year, which is the homework with the smallest average.";
            addImage(document);
            addTitle(document, "Detailed Homeworks Report");
            addTopParagraph(document, parString);
            addData(document, list);
            addBottomParagaph(document, list);
            addDateAndSignature(document);

            document.close();
            writer.close();
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

    private void addData(Document document, List<Pair<String, Double>> list){
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

    private void addBottomParagaph(Document document, List<Pair<String, Double>> list){
        addEmptyLine(document, 2);

        try{
            Paragraph finalParagraph = new Paragraph("To summarize, the hardest homework was '" + list.get(0).getKey() +
                    "' with the average " + list.get(0).getValue() + ", whereas the easiest homework was '" +
                    list.get(list.size()-1).getKey() + "' with the average " + list.get(list.size()-1).getValue() + ".",
                    FontFactory.getFont(FontFactory.HELVETICA, 12f));
            finalParagraph.setAlignment(Element.ALIGN_LEFT);
            finalParagraph.setFirstLineIndent(25);
            document.add(finalParagraph);
        } catch (BadElementException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

}
