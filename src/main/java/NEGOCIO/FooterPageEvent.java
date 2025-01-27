/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package NEGOCIO;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

/**
 *
 * @author fpl
 */
public class FooterPageEvent extends PdfPageEventHelper{
    private Font footerFont = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC, BaseColor.GRAY);

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        PdfContentByte canvas = writer.getDirectContent();
        Phrase footer = new Phrase("TECNO WEB GURPO-02-SC Página " + document.getPageNumber(), footerFont);

        // Coloca el pie de página centrado
        ColumnText.showTextAligned(
            canvas, Element.ALIGN_CENTER,
            footer,
            (document.right() - document.left()) / 2 + document.leftMargin(),
            document.bottom() - 10, 0
        );
    }
}
