package sample.gui.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;

import javax.print.Doc;
import java.io.IOException;
import java.time.LocalDate;

public abstract class Pdf {

    public Pdf(){
        System.out.println("PDF");
    }

    public void addEmptyLine(Document doc, int number) {
        for (int i = 0; i < number; i++) {
            try {
                doc.add(new Paragraph(" "));
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }
    }

    public void addImage(Document document){
        try {
            Image logoImage;
            logoImage = Image.getInstance("pdfImages/logoUBB.png");
            logoImage.setAlignment(Element.ALIGN_LEFT);
            logoImage.scaleAbsolute(190, 50);
            document.add(logoImage);
        } catch (BadElementException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public void addDateAndSignature(Document document){
        addEmptyLine(document, 7);

        try{
            LocalDate localDate = LocalDate.now();
            Chunk glue = new Chunk(new VerticalPositionMark());
            PdfPTable table = new PdfPTable(1);
            Phrase p = new Phrase();
            PdfPCell cell = new PdfPCell(p);
            p.add("Date: " + localDate);
            p.add(glue);
            p.add("Signature: " + " .......... ");
            cell.setBorder(PdfPCell.NO_BORDER);
            table.addCell(cell);
            document.add(table);
        } catch (BadElementException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public void addTitle(Document document, String titleString){
        addEmptyLine(document, 1);

        try {
            Paragraph title = new Paragraph(titleString, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12f));
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
        } catch (BadElementException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public void addTopParagraph(Document document, String parString){
        addEmptyLine(document,2);

        try {
            Paragraph paragraph = new Paragraph(parString, FontFactory.getFont(FontFactory.HELVETICA, 12f));
            paragraph.setAlignment(Element.ALIGN_LEFT);
            paragraph.setFirstLineIndent(25);
            document.add(paragraph);
        } catch (BadElementException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
}
