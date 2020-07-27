package com.covid.spchacker.service;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.translate.*;
import com.aspose.pdf.DocSaveOptions;
import com.aspose.pdf.Document;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

import java.util.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;


import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.BufferedWriter;
public class PDFReader {

	private static String image="C:\\Backup\\PDF\\SampleVoterList\\SampleVoterList-01.jpg";

	public static void imageToDoc() {
		Document doc = new Document(image);
		DocSaveOptions saveOptions = new DocSaveOptions();
		saveOptions.setFormat(DocSaveOptions.DocFormat.DocX);
		doc.save("C:\\Backup\\PDF\\resultant_1.docx", saveOptions);

	}
	public static void  another() throws IOException {
		XWPFDocument doc = new XWPFDocument();
		String pdf = "C:\\Backup\\PDF\\SampleVoterList.pdf";
		PdfReader reader = new PdfReader(pdf);
		PdfReaderContentParser parser = new PdfReaderContentParser(reader);
		for (int i = 1; i <= reader.getNumberOfPages(); i++) {
			TextExtractionStrategy strategy =
					parser.processContent(i, new SimpleTextExtractionStrategy());
			String text = strategy.getResultantText();
			XWPFParagraph p = doc.createParagraph();
			XWPFRun run = p.createRun();
			run.setText(text);
			run.addBreak(BreakType.PAGE);
		}
		FileOutputStream out = new FileOutputStream("C:\\Backup\\PDF\\Sample.docx");
		doc.write(out);
	}
	public static void convertPDFtoWord() {

		Document doc = new Document("C:\\Backup\\PDF\\SampleVoterList.pdf");
		DocSaveOptions saveOptions = new DocSaveOptions();
		saveOptions.setFormat(DocSaveOptions.DocFormat.DocX);
		doc.save("C:\\Backup\\PDF\\resultant_1.txt", saveOptions);
	}
	private static String translate(String langFrom, String langTo, String text) throws IOException {
		// INSERT YOU URL HERE
		String urlStr = "https://your.google.script.url" +
				"?q=" + URLEncoder.encode(text, "UTF-8") +
				"&target=" + langTo +
				"&source=" + langFrom;
		URL url = new URL(urlStr);
		StringBuilder response = new StringBuilder();
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestProperty("User-Agent", "Mozilla/5.0");
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		return response.toString();
	}


	public static void readDocs() {
		File file = null;
		try
		{
			File fout = new File("C:\\Backup\\PDF\\filename_1.txt");
			FileOutputStream fos = new FileOutputStream(fout);

			BufferedWriter myWriter = new BufferedWriter(new OutputStreamWriter(fos));

			//file = new File("C:\\Backup\\PDF\\FinalRoll_ACNo_63PartNo_146.docx");
			file = new File("C:\\Backup\\PDF\\SampleVoterList.docx");
			FileInputStream fis = new FileInputStream(file.getAbsolutePath());
			XWPFDocument document = new XWPFDocument(fis);
			List<XWPFParagraph> paragraphs = document.getParagraphs();
			for ( int i =0; i <paragraphs.size() ; i++) {
				XWPFParagraph para  = paragraphs.get(i); 
				if(para.getText().contains("निर्वाचक का नाम") && (para.getText().contains("पिता का नाम") || para.getText().contains("पति का नाम"))) {
					String[] values = para.getText().split(":");
					String name = values[1];
					if(name.contains("पिता का नाम ")) {
						name = name.substring(0, name.indexOf("पिता का नाम "));
					}
					System.out.println("निर्वाचक का नाम : " +  name);
					myWriter.write("निर्वाचक का नाम : " +  name);
					myWriter.newLine();
					System.out.println("पति का नाम /पिता का नाम : " +  values[2] );
					myWriter.write("पति का नाम /पिता का नाम : " +  values[2]);
					myWriter.newLine();
					String[] houseData = paragraphs.get(i+1).getText().split(":");
					i++;
					if(houseData.length>4) {
						System.out.println("गृह संख्या : " +houseData[1]  );
						myWriter.write("गृह संख्या : " +houseData[1]);
						myWriter.newLine();
						System.out.println("उम्र : " +  houseData[3]);
						myWriter.write("उम्र : " +  houseData[3]);
						myWriter.newLine();
						System.out.println("लिंग : " +  houseData[5]);
						myWriter.write("लिंग : " +  houseData[5]);
						myWriter.newLine();
					}
				}
				else if(para.getText().contains("निर्वाचक का नाम")) {
					String[] values = para.getText().split(":");

					if(values.length > 5) {
						System.out.println("निर्वाचक का नाम : " +  values[1]);
						myWriter.write("निर्वाचक का नाम : " +  values[1]);
						myWriter.newLine();
						System.out.println("पति का नाम  पिता का नाम : " +  values[2].substring(0,  values[2].indexOf("गृह संख्या")));
						myWriter.write("पति का नाम  पिता का नाम : " +  values[2].substring(0,  values[2].indexOf("गृह संख्या")));
						myWriter.newLine();
						System.out.println("उम्र : " +  values[4].substring(0,  values[4].indexOf("लिंग")));
						myWriter.write("उम्र : " +  values[4].substring(0,  values[4].indexOf("लिंग")));
						myWriter.newLine();
						System.out.println("लिंग : " +  values[5]);
						myWriter.write("लिंग : " +  values[5]);
						myWriter.newLine();
						continue;
					}
					System.out.println("निर्वाचक का नाम : " +  values[1] );
					myWriter.write("निर्वाचक का नाम : " +  values[1] );
					myWriter.newLine();
					String[] houseData = paragraphs.get(i+1).getText().split(":");
					i++;
					if(houseData.length>4) {
						String fname = houseData[1] ;
						if(fname.contains("Photo is गृह संख्या")) {
							fname = fname.substring(0, fname.indexOf("Photo is गृह संख्या"));
						}
						if(fname.contains("Photo is")) {
							fname = fname.substring(0, fname.indexOf("Photo is"));
						}
						System.out.println("पति का नाम /पिता का नाम : " + fname);
						myWriter.write("पति का नाम /पिता का नाम : " + fname);
						myWriter.newLine();
						String age =  houseData[3];
						if(age.contains("लिंग")) {
							age= age.substring(0, age.indexOf("लिंग"));
						}
						System.out.println("उम्र : " +  age);
						myWriter.write("उम्र : " +  age);
						myWriter.newLine();
						System.out.println("लिंग : " +  houseData[4]);
						myWriter.write("लिंग : " +  houseData[4]);
						myWriter.newLine();
					} else {
						String fname="";
						if(houseData.length >1)
							fname = houseData[1] ;
						else {
							fname ="test";						}
						if(fname.contains("Photo is गृह संख्या")) {
							fname = fname.substring(0, fname.indexOf("Photo is गृह संख्या"));
						}
						if(fname.contains("Photo is")) {
							fname = fname.substring(0, fname.indexOf("Photo is"));
						}
						System.out.println("पति का नाम /पिता का नाम : " + fname);
						myWriter.write("पति का नाम /पिता का नाम : " + fname);
						myWriter.newLine();
						String[] gData = paragraphs.get(i+1).getText().split(":");
						i++;
						String age = "";
						if(gData.length>1)
							age =  gData[1];
						if(age.contains("लिंग")) {
							age= age.substring(0, age.indexOf("लिंग"));
						}
						System.out.println("उम्र : " +  age);
						myWriter.write("उम्र : " +  age);
						myWriter.newLine();
						if(gData.length>2) {
							System.out.println("लिंग : " +  gData[2]);
							myWriter.write("लिंग : " +  gData[2]);
							myWriter.newLine();
						}
					}
				}

			}
			fis.close();
			myWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Translate  getGoogleTranslate() throws FileNotFoundException, IOException {
		//Resource resource = new ClassPathResource("My Project 19213-c10e3fe988ee.json");
		//InputStream input = resource.getInputStream();
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		InputStream input = classloader.getResourceAsStream("My Project 19213-c10e3fe988ee.json");
		 Translate translate = TranslateOptions .newBuilder() .setCredentials(
				 ServiceAccountCredentials .fromStream(input))
				 .build().getService(); 
		 
		 return translate;
	}

	public static void main(String args[]) throws IOException { //another();

		/*
		 * Translate translate = TranslateOptions .newBuilder() .setCredentials(
		 * ServiceAccountCredentials .fromStream(new FileInputStream(
		 * "C:\\Users\\Anand\\Downloads\\My Project 19213-c10e3fe988ee.json")))
		 * .build().getService();
		 */
		 Translation translation = getGoogleTranslate().translate("¡Hola Mundo!");
		  System.out.printf("Translated Text:\n\t%s\n",
		  translation.getTranslatedText()); //System.out.println(text);
		  Translation translation1 =  getGoogleTranslate().translate("बद्री चौधरी",  Translate.TranslateOption.sourceLanguage("hi"),Translate.TranslateOption.targetLanguage("en"),Translate.TranslateOption.model("nmt"));
		  System.out.println("Translated text: " +translation1.getTranslatedText());
		  //readDocs(); //convertPDFtoWord();
		  readDocs_1();
	}

	
	public static void readDocs_1() {
		File file = null;
		try
		{
			File fout = new File("C:\\Backup\\PDF\\filename_1.txt");
			FileOutputStream fos = new FileOutputStream(fout);
			file = new File("C:\\Backup\\PDF\\SampleVoterList.docx");
			FileInputStream fis = new FileInputStream(file.getAbsolutePath());
			XWPFDocument document = new XWPFDocument(fis);
			List<XWPFParagraph> paragraphs = document.getParagraphs();
			for ( int i =0; i <paragraphs.size() ; i++) {
				XWPFParagraph para  = paragraphs.get(i); 
				  Translation translation1 =  getGoogleTranslate().translate(para.getText(),  Translate.TranslateOption.sourceLanguage("hi"),Translate.TranslateOption.targetLanguage("en"),Translate.TranslateOption.model("nmt"));
				System.out.println(translation1.getTranslatedText());
			}
		} catch(Exception ex) {
			
		}
	}
}
