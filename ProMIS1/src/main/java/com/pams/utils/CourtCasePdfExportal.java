package com.pams.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.pams.dto.ProCourtDtlDto;

public class CourtCasePdfExportal {


	public static ByteArrayInputStream officeOrderFixed(ProCourtDtlDto courtdtlDto2) throws MalformedURLException, IOException {

		Document document = new Document();
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		try {

			PdfWriter.getInstance(document, out);
			document.open();

			// Add Text to PDF file ->

			Image image = Image.getInstance(new Utils().getConfigMessage("image.emblemPath"));
			image.setAlignment(Element.ALIGN_CENTER);
			image.scaleToFit(100.0f, 50.0f);

			Font font = FontFactory.getFont(FontFactory.TIMES_BOLD, 12, BaseColor.BLACK);
			Font f8 = FontFactory.getFont(FontFactory.TIMES_BOLD, 10, BaseColor.BLACK);
			Font font1 = FontFactory.getFont(FontFactory.TIMES, 11, BaseColor.BLACK);
			Font label = FontFactory.getFont(FontFactory.TIMES_BOLD,11,Font.UNDERLINE,BaseColor.BLACK);
			   Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
			Paragraph head = new Paragraph();
			head.setAlignment(Element.ALIGN_CENTER);
			head.add(image);
			document.add(image);
			
			Paragraph para = new Paragraph("Government of India\r\n" + "Ministry of Corporate Affairs\r\n"
					+ "Serious Fraud Investigation Office\r\n" + "", font);
			para.setAlignment(Element.ALIGN_CENTER);
			document.add(para);
			
			
			Paragraph para2 = new Paragraph("Court Case Details",subFont);
			para2.setAlignment(Element.ALIGN_LEFT);
			document.add(para2);
            Paragraph para3 = new Paragraph(" Sfio  As : ",label);
			para3.setAlignment(Element.ALIGN_JUSTIFIED);

			document.add(para3);
			document.add(Chunk.NEWLINE);
			
			Paragraph para4 = new Paragraph("Court Type  : "+courtdtlDto2.getProCourtdtl().getCourtType().getCourtName() + "   State  "+courtdtlDto2.getProCourtdtl().getState().getState()  );
			para4.setAlignment(Element.ALIGN_JUSTIFIED);
			document.add(para4);
			document.add(Chunk.NEWLINE);
			
			document.close();
		} catch (DocumentException e) {
		}

		return new ByteArrayInputStream(out.toByteArray());
	}

}
