package com.covid.spchacker.service;


import com.google.api.gax.longrunning.OperationFuture;
import com.google.api.services.storage.model.Bucket;
import com.google.cloud.vision.v1.AnnotateFileRequest;
import com.google.cloud.vision.v1.AnnotateFileResponse;
import com.google.cloud.vision.v1.AnnotateFileResponse.Builder;
import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.AsyncAnnotateFileRequest;
import com.google.cloud.vision.v1.AsyncAnnotateFileResponse;
import com.google.cloud.vision.v1.AsyncBatchAnnotateFilesResponse;
import com.google.cloud.vision.v1.BatchAnnotateFilesRequest;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.Block;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.cloud.vision.v1.GcsDestination;
import com.google.cloud.vision.v1.GcsSource;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.ImageSource;
import com.google.cloud.vision.v1.InputConfig;
import com.google.cloud.vision.v1.LocalizedObjectAnnotation;
import com.google.cloud.vision.v1.OperationMetadata;
import com.google.cloud.vision.v1.OutputConfig;
import com.google.cloud.vision.v1.Page;
import com.google.cloud.vision.v1.Paragraph;
import com.google.cloud.vision.v1.Symbol;
import com.google.cloud.vision.v1.TextAnnotation;
import com.google.cloud.vision.v1.Word;
import com.google.cloud.vision.v1p4beta1.BatchAnnotateFilesResponse;
import com.google.cloud.*;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.Storage.BlobListOption;
import com.google.cloud.storage.StorageOptions;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translation;
import com.google.protobuf.ByteString;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.util.JsonFormat;
import com.itextpdf.text.pdf.PdfReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
public class Detect {

	public static void main(String[] as) throws IOException {
		readDocs_1();
		//detectDocumentText("C:\\Backup\\PDF\\SampleVoterList\\SampleVoterList-01.jpg");
		//batchAnnotateFiles("C:\\Backup\\PDF\\SampleVoterList.pdf");
	}
	public static void detectDocumentText(String filePath) throws IOException {
	    List<AnnotateImageRequest> requests = new ArrayList<>();

	    ByteString imgBytes = ByteString.readFrom(new FileInputStream(filePath));

	    Image img = Image.newBuilder().setContent(imgBytes).build();
	    Feature feat = Feature.newBuilder().setType(Type.DOCUMENT_TEXT_DETECTION).build();
	    AnnotateImageRequest request =
	        AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
	    requests.add(request);

	    // Initialize client that will be used to send requests. This client only needs to be created
	    // once, and can be reused for multiple requests. After completing all of your requests, call
	    // the "close" method on the client to safely clean up any remaining background resources.
	    try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
	      BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
	      List<AnnotateImageResponse> responses = response.getResponsesList();
	      client.close();

	      for (AnnotateImageResponse res : responses) {
	        if (res.hasError()) {
	          System.out.format("Error: %s%n", res.getError().getMessage());
	          return;
	        }

	        // For full list of available annotations, see http://g.co/cloud/vision/docs
	        TextAnnotation annotation = res.getFullTextAnnotation();
	        for (Page page : annotation.getPagesList()) {
	          String pageText = "";
	          for (Block block : page.getBlocksList()) {
	            String blockText = "";
	            for (Paragraph para : block.getParagraphsList()) {
	              String paraText = "";
	              for (Word word : para.getWordsList()) {
	                String wordText = "";
	                for (Symbol symbol : word.getSymbolsList()) {
	                  wordText = wordText + symbol.getText();
	                  System.out.format(
	                      "Symbol text: %s (confidence: %f)%n",
	                      symbol.getText(), symbol.getConfidence());
	                }
	                System.out.format(
	                    "Word text: %s (confidence: %f)%n%n", wordText, word.getConfidence());
	                paraText = String.format("%s %s", paraText, wordText);
	              }
	              // Output Example using Paragraph:
	              System.out.println("%nParagraph: %n" + paraText);
	              System.out.format("Paragraph Confidence: %f%n", para.getConfidence());
	              blockText = blockText + paraText;
	            }
	            pageText = pageText + blockText;
	          }
	        }
	        System.out.println("%nComplete annotation:");
	        System.out.println(annotation.getText());
	      }
	    }
	  }
	
	public static void readDocs_1() throws FileNotFoundException, IOException {
		
		Translate tr = PDFReader.getGoogleTranslate();
		BufferedReader reader;
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet  sheet = workbook.createSheet("Elector Data");
        Font headerFont = workbook.createFont();
		headerFont.setFontHeightInPoints((short) 14);
		headerFont.setColor(IndexedColors.RED.getIndex());
		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFont(headerFont);
		Row headerRow = sheet.createRow(0);
		int i = 0;
		String[] headers= {"Elector Name","Spouse/Father Name","Age","Sex","House No"};
		for (String str : headers) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(str);
			cell.setCellStyle(headerCellStyle);
			i++;
		}
		int rowNum = 1;
		try {
			reader = new BufferedReader(new FileReader("C:\\Backup\\PDF\\filename_11.txt"));
			String line = reader.readLine();
			int count =  0 ;
			 
			while (line != null) {
				Row row = sheet.createRow(rowNum++);
				//System.out.println(line);
				line = reader.readLine();
				if(line == null) {
					continue;
				}
				Translation translation1 = tr.translate(line, Translate.TranslateOption.sourceLanguage("hi"),Translate.TranslateOption.targetLanguage("en"), Translate.TranslateOption.model("nmt"));
				String text = translation1.getTranslatedText().replaceAll("&#39;s", "");
				if (text.contains("Name of Elector")  || text.contains("Elector Name")) {
					System.out.println("processing---" + count++);
					String[] t_1 = text.split(":");
					if( t_1.length>1) {
					System.out.println("Name of Elector" + t_1[1]);
					row.createCell(0).setCellValue(t_1[1]);
					} else {
						System.out.println("Name of Elector" + t_1[0]);
						row.createCell(0).setCellValue(t_1[0]);
					}
				}
				else if (text.contains("Father Name")  || text.contains("Spouse Name") || text.contains("Husband Name")) {
					String[] t_1 = text.split(":");
					if( t_1.length>1) {
					System.out.println("Spouse Name/Father Name" + t_1[1]);
					row.createCell(1).setCellValue(t_1[1]);
					}else {
						System.out.println("Name of Elector" + t_1[0]);
						row.createCell(1).setCellValue(t_1[0]);
					}
				}
				else if (!(text.contains("Age: Age as")) && (text.contains("Age") && text.contains("Sex"))) {
					String[] t_1 = text.split(":");
					System.out.println("Age" + t_1[1].substring(0, t_1[1].indexOf("Sex")));
					row.createCell(2).setCellValue(t_1[1].substring(0, t_1[1].indexOf("Sex")));
					System.out.println("Sex" + t_1[2]);
					row.createCell(3).setCellValue(t_1[2]);
				}
				else if (text.contains("House number") || text.contains("home number")) {
					if(text.contains("-")) {
					String[] t_1 = text.split("-");
					System.out.println("House number" + t_1[1]);
					row.createCell(4).setCellValue(t_1[1]);
					} else {
							String[] t_1 = text.split(":");
							if(t_1.length>1) {
							System.out.println("House number" + t_1[1]);
							row.createCell(4).setCellValue(t_1[1]);
							}
					}
					
				}
				else if ((!text.contains("Age: Age as")) && text.contains("Age")) {
					String[] t_1 = text.split(":");
					System.out.println("Age" + t_1[1]);
					row.createCell(2).setCellValue(t_1[1]);
				}
				
			}
			FileOutputStream fileOut = new FileOutputStream("C:\\Backup\\PDF\\poi-generated-file.xlsx");
			workbook.write(fileOut);
			fileOut.close();
			reader.close();
			
		} catch (Exception ex) {
                 ex.printStackTrace();
		}
	}

	public static void batchAnnotateFiles(String filePath) throws IOException {
		 PdfReader reader = new PdfReader(filePath); 
	     int pages = reader.getNumberOfPages();
	     PrintWriter out = new PrintWriter("C:\\Backup\\PDF\\filename_11.txt");
	     int fraction = pages%4;
	     int intregral = pages/4;
	    try (ImageAnnotatorClient imageAnnotatorClient = ImageAnnotatorClient.create()) {
	      Path path = Paths.get(filePath);
	      byte[] data = Files.readAllBytes(path);
	      ByteString content = ByteString.copyFrom(data);
	      InputConfig inputConfig =
	          InputConfig.newBuilder().setMimeType("application/pdf").setContent(content).build();
           Feature feature = Feature.newBuilder().setType(Feature.Type.DOCUMENT_TEXT_DETECTION).build();
          int count = 0; 
	      for(int i =0 ; i <=intregral ; i++) {
	    	  int p = count + 1 ;
	    	  AnnotateFileRequest fileRequest = null;
	    	  
	          fileRequest =AnnotateFileRequest.newBuilder()
	              .setInputConfig(inputConfig)
	              .addFeatures(feature)
	              .addPages(p) // Process the first page
	              .addPages(p+1) // Process the second page
	              .addPages(p+2) // Process the first page
	              .addPages(p+3) // Process the second page
	              .build();
                  count = 4 + count ;
	      // Add each `AnnotateFileRequest` object to the batch request.
	      BatchAnnotateFilesRequest request =
	          BatchAnnotateFilesRequest.newBuilder().addRequests(fileRequest).build();

	      // Make the synchronous batch request.
	      com.google.cloud.vision.v1.BatchAnnotateFilesResponse response = imageAnnotatorClient.batchAnnotateFiles(request);

	      // Process the results, just get the first result, since only one file was sent in this
	      // sample.
	      for (AnnotateImageResponse imageResponse :
	          response.getResponsesList().get(0).getResponsesList()) {
					/*
					 * Map<FieldDescriptor, Object> map =
					 * imageResponse.getFullTextAnnotation().getAllFields(); for
					 * (Map.Entry<FieldDescriptor, Object> entry : map.entrySet()) {
					 * System.out.println("Key = " + entry.getKey() + ", Value = " +
					 * entry.getValue()); }
					 */
	    	  out.println(imageResponse.getFullTextAnnotation().getText());
	        System.out.format("Full text: %s%n", imageResponse.getFullTextAnnotation().getAllFields());
	        Translation translation1 =  PDFReader.getGoogleTranslate().translate(imageResponse.getFullTextAnnotation().getText(),  Translate.TranslateOption.sourceLanguage("hi"),Translate.TranslateOption.targetLanguage("en"),Translate.TranslateOption.model("nmt"));
		    String text = translation1.getTranslatedText().replaceAll("&#39;s", "");
		   
	       System.out.println(text);
	        
					/*
					 * String[] totalE = text.split(":"); for(int j = 0 ; j <totalE.length; j++ ) {
					 * 
					 * String t = totalE[j]; if(t.contains("Name of Elector") &&
					 * t.contains("Elector Name")) { String[] t_1 = t.split(":") ;
					 * System.out.println("Name of Elector" + t_1[1].substring(0,
					 * t_1[1].indexOf("Elector Name")) ); String td = totalE[i+1]; String[] td_1 =
					 * td.split(":") ; if(td_1[1].contains("Husband Name")) {
					 * System.out.println("Husband Name/Father Name" + td_1[1].substring(0,
					 * t_1[1].indexOf("Husband Name"))); } else {
					 * System.out.println("Husband Name/Father Name" + td_1[1].substring(0,
					 * t_1[1].indexOf("Father Name"))); } System.out.println("House Number" +
					 * td_1[3].substring(0, td_1[3].indexOf("Photo is House Number"))); String ta=
					 * totalE[i+2]; String[] ta_a = ta.split(":"); System.out.println("Age" +
					 * ta_a[1].substring(0, ta_a[1].indexOf("Sex"))); System.out.println("Sex" +
					 * ta_a[2]); }
					 * 
					 * else if(t.contains("Elector Name") ) { System.out.println("Name of Elector" +
					 * t.split(":")[1]); String td = totalE[i+1]; String[] td_1 = td.split(":") ;
					 * System.out.println("Husband Name/Father Name" + td_1[1]);
					 * System.out.println("Home no" + td_1[2]); System.out.println("Age " +
					 * td_1[3]); System.out.println("Gender " + td_1[4]);
					 * 
					 * 
					 * }
					 */
	        //}
	      }
	    }
	    }
	  }
	
	 public static void detectDocumentsGcs(String gcsSourcePath, String gcsDestinationPath)
		      throws Exception {

		    // Initialize client that will be used to send requests. This client only needs to be created
		    // once, and can be reused for multiple requests. After completing all of your requests, call
		    // the "close" method on the client to safely clean up any remaining background resources.
		    try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
		      List<AsyncAnnotateFileRequest> requests = new ArrayList<>();

		      // Set the GCS source path for the remote file.
		      GcsSource gcsSource = GcsSource.newBuilder().setUri(gcsSourcePath).build();

		      // Create the configuration with the specified MIME (Multipurpose Internet Mail Extensions)
		      // types
		      InputConfig inputConfig =
		          InputConfig.newBuilder()
		              .setMimeType(
		                  "application/pdf") // Supported MimeTypes: "application/pdf", "image/tiff"
		              .setGcsSource(gcsSource)
		              .build();

		      // Set the GCS destination path for where to save the results.
		      GcsDestination gcsDestination =
		          GcsDestination.newBuilder().setUri(gcsDestinationPath).build();

		      // Create the configuration for the System.output with the batch size.
		      // The batch size sets how many pages should be grouped into each json System.output file.
		      OutputConfig outputConfig =
		          OutputConfig.newBuilder().setBatchSize(2).setGcsDestination(gcsDestination).build();

		      // Select the Feature required by the vision API
		      Feature feature = Feature.newBuilder().setType(Feature.Type.DOCUMENT_TEXT_DETECTION).build();

		      // Build the OCR request
		      AsyncAnnotateFileRequest request =
		          AsyncAnnotateFileRequest.newBuilder()
		              .addFeatures(feature)
		              .setInputConfig(inputConfig)
		              .setOutputConfig(outputConfig)
		              .build();

		      requests.add(request);

		      // Perform the OCR request
		      OperationFuture<AsyncBatchAnnotateFilesResponse, OperationMetadata> response =
		          client.asyncBatchAnnotateFilesAsync(requests);

		      System.out.println("Waiting for the operation to finish.");

		      // Wait for the request to finish. (The result is not used, since the API saves the result to
		      // the specified location on GCS.)
		      List<AsyncAnnotateFileResponse> result =
		          response.get(180, TimeUnit.SECONDS).getResponsesList();

		      // Once the request has completed and the System.output has been
		      // written to GCS, we can list all the System.output files.
		      Storage storage = StorageOptions.getDefaultInstance().getService();

		      // Get the destination location from the gcsDestinationPath
		      Pattern pattern = Pattern.compile("gs://([^/]+)/(.+)");
		      Matcher matcher = pattern.matcher(gcsDestinationPath);

		      if (matcher.find()) {
		        String bucketName = matcher.group(1);
		        String prefix = matcher.group(2);

		        // Get the list of objects with the given prefix from the GCS bucket
		        com.google.cloud.storage.Bucket bucket = storage.get(bucketName);
		        com.google.api.gax.paging.Page<Blob> pageList = bucket.list(BlobListOption.prefix(prefix));

		        Blob firstOutputFile = null;

		        // List objects with the given prefix.
		        System.out.println("Output files:");
		        for (Blob blob : pageList.iterateAll()) {
		          System.out.println(blob.getName());

		          // Process the first System.output file from GCS.
		          // Since we specified batch size = 2, the first response contains
		          // the first two pages of the input file.
		          if (firstOutputFile == null) {
		            firstOutputFile = blob;
		          }
		        }

		        // Get the contents of the file and convert the JSON contents to an AnnotateFileResponse
		        // object. If the Blob is small read all its content in one request
		        // (Note: the file is a .json file)
		        // Storage guide: https://cloud.google.com/storage/docs/downloading-objects
		        String jsonContents = new String(firstOutputFile.getContent());
		        Builder builder = AnnotateFileResponse.newBuilder();
		        JsonFormat.parser().merge(jsonContents, builder);

		        // Build the AnnotateFileResponse object
		        AnnotateFileResponse annotateFileResponse = builder.build();

		        // Parse through the object to get the actual response for the first page of the input file.
		        AnnotateImageResponse annotateImageResponse = annotateFileResponse.getResponses(0);

		        // Here we print the full text from the first page.
		        // The response contains more information:
		        // annotation/pages/blocks/paragraphs/words/symbols
		        // including confidence score and bounding boxes
		        System.out.format("%nText: %s%n", annotateImageResponse.getFullTextAnnotation().getText());
		      } else {
		        System.out.println("No MATCH");
		      }
		    }
		  }
}
