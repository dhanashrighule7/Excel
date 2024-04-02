package com.cyperts.ExcellML.FileOperations;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cyperts.ExcellML.ExcellFiles.FileRepository;
import com.cyperts.ExcellML.ExcellFiles.FileServiceImpl;
import com.cyperts.ExcellML.ExcellFiles.TemplateFile;
import com.cyperts.ExcellML.ExcellFiles.TemplateFileRepository;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class FileOperationService {

	@Autowired
	private FileHeaderRepository fileHeaderRepository;
	@Autowired
	private FileOperationRepository fileOperationRepository;

	@Autowired
	private FileServiceImpl fileServiceImpl;

	@Autowired
	private FileRepository fileRepository;
	@Autowired
	TemplateFileRepository templateFileRepo;

	public static long NonExistingNumber = 1l;
	private static final List<String> headersToCheck = Arrays.asList("Product Name", "Item Name", "Items", "Item",
			"Product", "Product#", "Item Description", "Description", "Details", "Specification", "Dept", "Department",
			"Section", "Division", "Branch", "Compartment", "Segment", "Sector", "UPC", "Casepack", "Selling Price",
			"Cost", "Price", "Usd price", "Avail", "Availability", "Accessibility", "Brand", "Quantity", "Qty", "Stock",
			"Type", "Kind", "Variety", "Design", "Designer", "Hyperlink", "Link", "Order", "Order qty", "Qty order",
			"Barcode");

	public List<FileHeaders> saveHeaders(File file, long userId, String fileExtension) {
		try {

			List<String> previousFileName = fileHeaderRepository.findFileNameByUserId(userId);

			if (!previousFileName.isEmpty() && previousFileName.get(0).equals(file.getName())) {
				
				if (file.exists() && file.isFile()) {
					fileRepository.deleteByUserId(userId);
					System.out.println("return true file");
					String fileContentType = file.getName();
					byte[] sourceFileContent = Files.readAllBytes(file.toPath());
					String fileName = file.getName();
					TemplateFile fileModal = new TemplateFile(fileName, sourceFileContent, fileContentType, userId);

					fileServiceImpl.saveFile(fileModal);
				}
				return fileHeaderRepository.getListOfFileHeaderByUserId(userId);
			} else {

				fileHeaderRepository.deleteFileHeaderByUserId(userId);

				fileOperationRepository.deleteDataByUserId(userId);
				System.out.println("Previous file deleted.......");
				XSSFWorkbook wb = new XSSFWorkbook();

				if (fileExtension.equals(".csv")) {
					System.err.println("Inside the csv::");
					BufferedReader br = new BufferedReader(new FileReader(file));
					CSVReader csvReader = new CSVReaderBuilder(br).build();

					List<String[]> data = csvReader.readAll();

					wb = convertToExcel(data);
				} else if (fileExtension.equals(".xlsx")) {
					System.out.println("Inside excel file::");
					FileInputStream fis = new FileInputStream(file);
					if (fileExtension.equals(".xlsx")) {
						wb = new XSSFWorkbook(fis);
					}
				}

				System.out.println("workbook: " + wb);
				XSSFSheet sheet = wb.getSheetAt(0);
				System.out.println("Name of the Sheet::=>" + sheet.getSheetName());
				int lastRowNum = sheet.getLastRowNum();
				System.out.println("worksheet: " + sheet.getSheetName() + " :: Total row: " + lastRowNum);

				int headerRowNumber = getHeaderRowNumber(sheet, headersToCheck);
				System.out.println("Number of header row in sheet: " + headerRowNumber);

				Map<String, Integer> allHeaders = getAllHeaders(headerRowNumber, sheet);
				System.out.println("All Headers::" + allHeaders);
				ArrayList<FileHeaders> allHeaderData = new ArrayList<FileHeaders>();

				for (Map.Entry<String, Integer> entry : allHeaders.entrySet()) {
					FileHeaders fileHeader = new FileHeaders();
//				System.out.println("Colomn names in sheet " + sheet.getSheetName() + " are " + headers);
					fileHeader.setFileHeader(entry.getKey());
					fileHeader.setColumnIndex(entry.getValue());
					fileHeader.setUserId(userId);
					fileHeader.setFileName(file.getName());
					allHeaderData.add(fileHeader);
				}
				fileHeaderRepository.saveAll(allHeaderData);
				if (file.exists() && file.isFile()) {

					fileRepository.deleteByUserId(userId);
					System.out.println("return true file");
					String fileContentType = file.getName();
					byte[] sourceFileContent = Files.readAllBytes(file.toPath());
					String fileName = file.getName();
					TemplateFile fileModal = new TemplateFile(fileName, sourceFileContent, fileContentType, userId);

					fileServiceImpl.saveFile(fileModal);
				}
				return allHeaderData;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static XSSFWorkbook convertToExcel(List<String[]> data) {
		XSSFWorkbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Sheet1");

		int rowNum = 0;
		for (String[] row : data) {
			Row excelRow = sheet.createRow(rowNum++);
			int cellNum = 0;
			for (String cellValue : row) {
				Cell cell = excelRow.createCell(cellNum++);
				cell.setCellValue(cellValue);
			}
		}

		return workbook;
	}

//	public List<FileOperations> uploadFileOperation(File file, long userId) {
//		try {
//			fileOperationRepository.deleteByUserId(userId);
//			XSSFWorkbook wb = new XSSFWorkbook(file);
//			System.out.println("workbook: " + wb);
//			XSSFSheet sheet = wb.getSheetAt(0);
//			System.out.println("Name of the Sheet::=>" + sheet.getSheetName());
//			int lastRowNum = sheet.getLastRowNum();
//			System.out.println("worksheet: " + sheet.getSheetName() + " :: Total row: " + lastRowNum);
//
//			int headerRowNumber = getHeaderRowNumber(sheet, headersToCheck);
//			System.out.println("Header row number in sheet: " + headerRowNumber);
//
//			ArrayList<FileOperations> fileData = new ArrayList<>();
//
//			List<String> allHeaders = new ArrayList<>();
//
//			List<FileHeaders> fileHeader = fileHeaderRepository.getListOfFileHeaderByUserId(userId);
//			System.out.println("size of fileHeader list " + fileHeader.size());
//			for (FileHeaders fileHeaders : fileHeader) {
//				String fileheaderName = fileHeaders.getHeaderName();
//				System.out.println("Header name value:: " + fileheaderName);
//				allHeaders.add(fileheaderName);
//			}
//
//			System.out.println("Total number of columns in " + sheet.getSheetName() + " is " + allHeaders.size());
//			for (String headers : allHeaders) {
//				System.out.println("Column names in sheet " + sheet.getSheetName() + " are " + headers);
//			}
//			for (int i = headerRowNumber + 1; i <= lastRowNum; i++) {
//				FileOperations fileOperations = new FileOperations();
//				XSSFRow row = sheet.getRow(i);
//
//				for (int columnIndex = 0; columnIndex < allHeaders.size(); columnIndex++) {
//					System.out.println("column index ::: " + columnIndex);
//					Cell cell = row.getCell(columnIndex);
//					if (cell == null) {
//						continue;
//					}
//					System.out.println("cell value :: " + cell);
//					if (cell != null && cell.getCellType() != CellType.BLANK) {
//						String headerName = allHeaders.get(columnIndex);
//						System.out.println("Current header name::: " + headerName + "==" + columnIndex);
//						if (headerName == null) {
//							continue;
//						}
//						switch (headerName.toLowerCase()) {
//						case "item name":
//						case "item":
//						case "items":
//						case "product":
//						case "product#":
//						case "product name":
//							fileOperations.setProductName(cell.getStringCellValue().trim());
//							break;
//						case "item description":
//						case "description":
//						case "details":
//						case "specification":
//							fileOperations.setDescription(cell.getStringCellValue().trim());
//							break;
//						case "dept":
//						case "department":
//						case "section":
//						case "division":
//						case "branch":
//						case "compartment":
//						case "segment":
//						case "sector":
//							fileOperations.setDepartment(cell.getStringCellValue().trim());
//							break;
//						case "order":
//						case "order qty":
//						case "qty order":
//							if (cell.getCellType() == CellType.NUMERIC) {
//								fileOperations.setOrderQty(getNumericCellValue(cell));
//							} else {
//								fileOperations.setOrderQty(cell.getStringCellValue().trim());
//							}
//							break;
//						case "upc":
//						case "barcode":
//							fileOperations.setUpc(getNumericValue(cell));
//							break;
//						case "casepack":
//							fileOperations.setCasepack(getNumericValue(cell));
//							break;
//						case "price":
//						case "usd price":
//						case "selling price":
//						case "cost":
//							if (cell.getCellType() == CellType.NUMERIC) {
//								fileOperations.setPrice(getNumericCellValue(cell));
//							} else {
//								fileOperations.setPrice(cell.getStringCellValue().trim());
//							}
//							break;
//						case "avail":
//						case "availability":
//						case "accessibility":
//							fileOperations.setAvailability(getNumericValue(cell));
//							break;
//						case "type":
//						case "kind":
//						case "variety":
//							if (cell.getCellType() == CellType.NUMERIC) {
//								fileOperations.setType(getNumericCellValue(cell));
//							} else {
//								fileOperations.setType(cell.getStringCellValue().trim());
//							}
//							break;
//						case "quantity":
//						case "qty":
//						case "stock":
//							fileOperations.setQuantity(getNumericValue(cell));
//							break;
//						case "brand":
//						case "brand name":
//						case "brand name fragrances":
//							fileOperations.setBrand(cell.getStringCellValue().trim());
//							break;
//						case "hyperlink":
//						case "link":
//							if (cell.getCellType() == CellType.FORMULA) {
//								fileOperations.setHyperLink(cell.getCellFormula().trim());
//							} else if (cell.getCellType() == CellType.STRING) {
//								fileOperations.setHyperLink(cell.getStringCellValue().trim());
//							} else {
//								fileOperations.setHyperLink(getNumericCellValue(cell));
//							}
//							break;
//						case "designer":
//						case "design":
//							fileOperations.setDesigner(cell.getStringCellValue().trim());
//							break;
//						default:
//							break;
//						}
//					}
//				}
//				fileOperations.setUserId(userId);
//				fileData.add(fileOperations);
//			}
//
//			List<FileOperations> saveAll = fileOperationRepository.saveAll(fileData);
//			System.out.println("all file data size " + saveAll.size());
//			return saveAll;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}

	public List<FileOperations> uploadFileOperation(File file, long userId, String extension) {
		try {
			fileOperationRepository.deleteByUserId(userId);
			XSSFWorkbook wb = new XSSFWorkbook();

			if (extension.equals(".csv")) {
				System.err.println("Inside the csv::");
				BufferedReader br = new BufferedReader(new FileReader(file));
				CSVReader csvReader = new CSVReaderBuilder(br).build();

				List<String[]> data = csvReader.readAll();

				wb = convertToExcel(data);
			} else if (extension.equals(".xlsx")) {
				System.out.println("Inside xlsx ::");
				FileInputStream fis = new FileInputStream(file);
				wb = new XSSFWorkbook(fis);

			} else {
				System.err.println("Unsupported file extension");
				return null;
			}
			System.out.println("workbook: " + wb);
			XSSFSheet sheet = wb.getSheetAt(0);
			System.out.println("Name of the Sheet::=>" + sheet.getSheetName());
			int lastRowNum = sheet.getLastRowNum();
			System.out.println("worksheet: " + sheet.getSheetName() + " :: Total row: " + lastRowNum);

			int headerRowNumber = getHeaderRowNumber(sheet, headersToCheck);
			System.out.println("Header row number in sheet: " + headerRowNumber);

			ArrayList<FileOperations> fileData = new ArrayList<>();

			List<String> allHeaders = new ArrayList<>();

			Map<String, Long> headerMap = new HashMap<>();

			List<FileHeaders> fileHeader = fileHeaderRepository.getListOfFileHeaderByUserId(userId);
			System.out.println("size of fileHeader list " + fileHeader.size());

			for (FileHeaders fileHeaders : fileHeader) {
				String fileheaderName = fileHeaders.getHeaderName();
				long columnIndex = fileHeaders.getColumnIndex();
				headerMap.put(fileheaderName, columnIndex);
				System.out.println("Header name value:: " + fileheaderName);
				allHeaders.add(fileheaderName);
			}

			System.out.println("Total number of columns in " + sheet.getSheetName() + " is " + allHeaders.size());
			for (String headers : allHeaders) {
				System.out.println("Column names in sheet " + sheet.getSheetName() + " are " + headers);
			}

			for (int i = headerRowNumber + 1; i <= lastRowNum; i++) {
				FileOperations fileOperations = new FileOperations();
				XSSFRow row = sheet.getRow(i);

				for (Map.Entry<String, Long> entry : headerMap.entrySet()) {
					Cell cell = row.getCell(entry.getValue().intValue());
					if (cell == null) {
						continue;
					}

					System.out.println("cell value :: " + cell);
					if (cell != null && cell.getCellType() != CellType.BLANK) {

						String headerName = entry.getKey();
						System.out.println("Current header name::: " + headerName + "==" + entry.getValue().intValue());
						if (headerName == null) {
							continue;
						}

						switch (headerName.toLowerCase()) {
						case "item name":
						case "item":
						case "items":
						case "product":
						case "product#":
						case "product name":
							if (cell.getCellType() == CellType.NUMERIC) {
								fileOperations.setProductName(getNumericCellValue(cell));
							} else {
								fileOperations.setProductName(cell.getStringCellValue().trim());
							}
							break;
						case "item description":
						case "description":
						case "details":
						case "specification":
							if (cell.getCellType() == CellType.NUMERIC) {
								fileOperations.setDescription(getNumericCellValue(cell));
							} else {
								fileOperations.setDescription(cell.getStringCellValue().trim());
							}
							break;
						case "dept":
						case "department":
						case "section":
						case "division":
						case "branch":
						case "compartment":
						case "segment":
						case "sector":
							if (cell.getCellType() == CellType.NUMERIC) {
								fileOperations.setDepartment(getNumericCellValue(cell));
							} else {
								fileOperations.setDepartment(cell.getStringCellValue().trim());
							}
							break;
						case "order":
						case "order qty":
						case "qty order":
							if (cell.getCellType() == CellType.NUMERIC) {
								fileOperations.setOrderQty(getNumericCellValue(cell));
							} else {
								fileOperations.setOrderQty(cell.getStringCellValue().trim());
							}
							break;
						case "upc":
						case "barcode":
							fileOperations.setUpc(getNumericValue(cell));
							break;
						case "casepack":
							fileOperations.setCasepack(getNumericValue(cell));
							break;
						case "price":
						case "usd price":
						case "selling price":
						case "cost":
							if (cell.getCellType() == CellType.NUMERIC) {
								fileOperations.setPrice(getNumericCellValue(cell));
							} else {
								fileOperations.setPrice(cell.getStringCellValue().trim());
							}
							break;
						case "avail":
						case "availability":
						case "accessibility":
							fileOperations.setAvailability(getNumericValue(cell));
							break;
						case "type":
						case "kind":
						case "variety":
							if (cell.getCellType() == CellType.NUMERIC) {
								fileOperations.setType(getNumericCellValue(cell));
							} else {
								fileOperations.setType(cell.getStringCellValue().trim());
							}
							break;
						case "quantity":
						case "qty":
						case "stock":
							if (cell.getCellType() == CellType.NUMERIC) {
								fileOperations.setQuantity(getNumericCellValue(cell));
							} else {
								fileOperations.setQuantity(cell.getStringCellValue().trim());
							}
							break;
						case "brand":
						case "brand name":
						case "brand name fragrances":
							fileOperations.setBrand(cell.getStringCellValue().trim());
							break;
						case "hyperlink":
						case "link":
							if (cell.getCellType() == CellType.FORMULA) {
								fileOperations.setHyperLink(cell.getCellFormula().trim());
							} else if (cell.getCellType() == CellType.STRING) {
								fileOperations.setHyperLink(cell.getStringCellValue().trim());
							} else {
								fileOperations.setHyperLink(getNumericCellValue(cell));
							}
							break;
						case "designer":
						case "design":
							if (cell.getCellType() == CellType.FORMULA) {
								fileOperations.setDesigner(cell.getCellFormula().trim());
							} else {
								fileOperations.setDesigner(cell.getStringCellValue().trim());
							}
							break;
						default:
							break;
						}

					}
				}

				fileOperations.setUserId(userId);
				fileData.add(fileOperations);
			}

			List<FileOperations> saveAll = fileOperationRepository.saveAll(fileData);
			System.out.println("all file data size " + saveAll.size());
			return saveAll;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static int countEmptyColumns(Sheet sheet, int rowCount, int columnCount) {
		int emptyColumnCount = 0;

		for (int col = 0; col < columnCount; col++) {
			boolean isEmpty = true;

			for (int row = 0; row < rowCount; row++) {
				Row currentRow = sheet.getRow(row);
				Cell currentCell = currentRow.getCell(col, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

				// Check if the cell is not empty
				if (currentCell.getCellType() != CellType.BLANK) {
					isEmpty = false;
					break;
				}
			}

			if (isEmpty) {
				emptyColumnCount++;
			}
		}

		return emptyColumnCount;
	}

//	private static int countEmptyColumns(Sheet sheet, int rowCount, int columnCount) {
//	    int emptyColumnCount = 0;
//
//	    for (int col = 0; col < columnCount; col++) {
//	        boolean isEmpty = true;
//
//	        for (int row = 0; row < rowCount; row++) {
//	            Row currentRow = sheet.getRow(row);
//	            
//	            // Check if currentRow is null
//	            if (currentRow == null) {
//	                // If row is null, skip this row and continue with the next iteration
//	                continue;
//	            }
//
//	            Cell currentCell = currentRow.getCell(col, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
//
//	            // Check if the cell is not empty
//	            if (currentCell.getCellType() != CellType.BLANK) {
//	                isEmpty = false;
//	                break;
//	            }
//	        }
//
//	        if (isEmpty) {
//	            emptyColumnCount++;
//	        }
//	    }
//
//	    return emptyColumnCount;
//	}

	private static boolean isRowEmpty(Sheet sheet, int rowNum, int columnIndex) {
		Row row = sheet.getRow(rowNum);
		if (row == null) {
			return true; // Row is empty if it's null
		}

		Cell cell = row.getCell(columnIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
		return (cell == null || cell.getCellType() == CellType.BLANK);
	}

	private static int countEmptyRows(Sheet sheet, int columnIndex) {
		int rowCount = sheet.getPhysicalNumberOfRows();
		int emptyRowCount = 0;

		for (int i = 0; i < rowCount; i++) {
			Row row = sheet.getRow(i);

			if (row == null) {
				// Skip null rows
				continue;
			}

			Cell cell = row.getCell(columnIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);

			if (cell == null || cell.getCellType() == CellType.BLANK) {
				// Check if there are any non-empty cells in column 0
				boolean hasNonEmptyCell = false;
				for (int j = 0; j < row.getLastCellNum(); j++) {
					Cell nonEmptyCell = row.getCell(j, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
					if (nonEmptyCell != null && nonEmptyCell.getCellType() != CellType.BLANK) {
						hasNonEmptyCell = true;
						break;
					}
				}
				if (!hasNonEmptyCell) {
					emptyRowCount++;
				}
			}
		}

		return emptyRowCount;
	}

	private static int getHeaderRowNumber(Sheet sheet, List<String> headersToCheck) {
		int rowCount = sheet.getPhysicalNumberOfRows();
		int emptyRowCount = 0;

		for (int i = 0; i < rowCount; i++) {
			Row row = sheet.getRow(i);

			if (row == null) {
				// Skip null rows
				continue;
			}

			boolean isNonEmptyRow = false;

			String headerValue = "";
			for (int j = 0; j < row.getLastCellNum(); j++) {
				Cell headerCell = row.getCell(j, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
				if (headerCell != null && headerCell.getCellType() != CellType.BLANK) {
					if (headerCell.getCellType().equals(CellType.STRING)) {
						headerValue = headerCell.getStringCellValue().trim();
						System.out.println("Header row name: " + headerValue);
					}
					if (headersToCheck.contains(headerValue)) {
						isNonEmptyRow = true;
						System.out.println("row index:: " + headerCell.getRowIndex());
						return headerCell.getRowIndex();
					}
				}

			}

		}
		System.out.println("Empty row count::  " + emptyRowCount);
		return emptyRowCount;
	}

	private static int getColumnCountofEveryRow(Sheet sheet, int i) {
		// Assuming the header row is the first row (you may need to adjust this based
		// on your Excel structure)
		Row headerRow = sheet.getRow(i);

		// Check if the header row is not null
		if (headerRow != null) {
			// Get the number of cells in the header row
			return headerRow.getPhysicalNumberOfCells();
		}

		return 0;
	}

//	public static List<String> getAllHeaders(int emptyRowCount, Sheet sheet) {
//		try {
//			System.out.println("emptyRowCount::" + emptyRowCount);
//			Row headerRow = sheet.getRow(emptyRowCount);
//			if (headerRow != null) {
//				ArrayList<String> headers = new ArrayList<>();
//				// ** to trap the last nonempty cell ***////
//				boolean foudLastCell = false;
//
//				for (int i = 0; i < headerRow.getLastCellNum(); i++) {
//					Cell cell = headerRow.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
//					if (cell != null && cell.getCellType() != CellType.BLANK
//							&& cell.getStringCellValue().trim().length() > 0) {
//						headers.add(cell.getStringCellValue().trim());
//						foudLastCell = true;
//					} else if (foudLastCell) {
//						break;
//					}
//				}
//				System.out.println("Headers::" + headers);
//				return headers;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}

	public static Map<String, Integer> getAllHeaders(int headerRowNumber, Sheet sheet) {
		try {
			System.out.println("emptyRowCount::" + headerRowNumber);
			Row headerRow = sheet.getRow(headerRowNumber);
			if (headerRow != null) {

				Map<String, Integer> headerMap = new HashMap<>();

				ArrayList<String> headers = new ArrayList<>();

				// to trap the last nonempty cell *////
				boolean foudLastCell = false;

				for (int i = 0; i < headerRow.getLastCellNum(); i++) {
					Cell cell = headerRow.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
					if (cell != null && cell.getCellType() != CellType.BLANK
							&& cell.getStringCellValue().trim().length() > 0) {
						headers.add(cell.getStringCellValue().trim());

						headerMap.put(cell.getStringCellValue().trim(), cell.getColumnIndex());

						foudLastCell = true;
					} else if (foudLastCell) {
						break;
					}
				}
				System.out.println("Headers::" + headers);

				return headerMap;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

//	private static int getColumnCellIndex(Sheet sheet, int headerRow, String targetHeader) {
//		// Iterate through cells in the header row
//		try {
//			List<String> allHeaders = getAllHeaders(headerRow, sheet);
//			System.out.println("Header row ::" + headerRow);
//
//			Row rowObj = sheet.getRow(headerRow);
//			short lastCellNum = sheet.getRow(headerRow).getLastCellNum();
//			for (int i = 0; i < lastCellNum; i++) {
//
//				Cell cell = rowObj.getCell(i);
//				for (String headers : allHeaders) {
//					if (cell != null && cell.getCellType() != CellType.BLANK
//							&& cell.getStringCellValue().trim().equalsIgnoreCase(targetHeader)
//							&& headers.equalsIgnoreCase(targetHeader)) {
//						System.out.println("return true");
//						System.out.println(headers);
//						return i; // Return the column index
//					}
//				}
//			}
//
//			return -1; // Return -1 if the header is not found
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return 0;
//	}

	public static String excelReadString(Cell cell) {
		if (cell != null && cell.getCellType() != CellType.BLANK)
			return cell.getStringCellValue();

		return "";
	}

	public static long getNumericValue(Cell cell) {
		long value = 0;
		if (cell == null || cell.getCellType().equals(CellType.BLANK)) {
			return NonExistingNumber;
		}

		else if (cell.getCellType().equals(CellType.NUMERIC)) {
//			System.out.println(cell.getNumericCellValue() + "Numeric values==========================");

			value = (long) cell.getNumericCellValue();
			// System.out.println(cell.getStringCellValue());
		} else if (cell.getCellType().equals(CellType.STRING) && cell.getStringCellValue().trim().length() > 0) {
			String vv = cell.getStringCellValue();
			try {
				value = (long) Double.parseDouble(vv);
			} catch (NumberFormatException ex) {
				value = NonExistingNumber;
			}
		} else
			value = NonExistingNumber;

		return value;
	}

	public static long getNumericCellValueInLong(Cell cell) {
		if (cell != null) {
			CellType cellType = cell.getCellType();
			switch (cellType) {
			case NUMERIC:
				System.out.println("long value==============>" + (long) cell.getNumericCellValue());
				return (long) cell.getNumericCellValue();
			case STRING:
				try {
					System.out.println("long value==============>" + Long.parseLong(cell.getStringCellValue()));
					return Long.parseLong(cell.getStringCellValue());
				} catch (NumberFormatException e) {
					e.printStackTrace();
					return 0L; // Default value in case of parsing error
				}
			default:
				return 0L; // Default value for non-numeric cells
			}
		}
		return 0L; // Default value for null cells
	}

	public static String getNumericCellValue(Cell cell) {
		if (cell != null) {
			CellType cellType = cell.getCellType();
			switch (cellType) {
			case NUMERIC:
				System.out.println("String value==============>" + cell.getNumericCellValue());
				return String.valueOf(cell.getNumericCellValue());
			case STRING:
				System.out.println("String value==============>" + cell.getStringCellValue());
				return cell.getStringCellValue();
			default:
				return ""; // Default value for non-numeric cells
			}
		}
		return ""; // Default value for null cells
	}

	public List<FileOperations> getFileOperationDataByUserId(long userId) {
		try {
			// Retrieve file by userId
			File file = fileServiceImpl.getUserFileByUserId(userId);
			System.out.println(file.getName());

			// Initialize workbook and sheet
			XSSFWorkbook wb = new XSSFWorkbook(file);
			XSSFSheet sheet = wb.getSheetAt(0);

			// Get headers and empty row count
			int emptyRowCount = getHeaderRowNumber(sheet, headersToCheck);
			System.out.println("Number of header row in sheet: " + emptyRowCount);
			List<FileOperations> allOperations = new ArrayList<>();

			List<String> allHeaders = new ArrayList<>();

			List<FileHeaders> fileHeader = fileHeaderRepository.getListOfFileHeaderByUserId(userId);
			System.out.println("size of fileHeader list " + fileHeader.size());
			for (FileHeaders fileHeaders : fileHeader) {
				String fileheaderName = fileHeaders.getHeaderName();
				System.out.println("Header name value:: " + fileheaderName);
				allHeaders.add(fileheaderName);
			}
			System.out.println("Total number of columns in " + sheet.getSheetName() + " is " + allHeaders.size());

			List<FileOperations> fileOperations = fileOperationRepository.getAllUserDataByUserId(userId);

			for (FileOperations fileData : fileOperations) {
				FileOperations operations = new FileOperations();

				for (String header : allHeaders) {
					if (header == null) {
						continue;
					}
					String headerLowerCase = header.toLowerCase().trim();
					switch (headerLowerCase) {
					case "item description":
					case "description":
					case "details":
					case "specification":
						operations.setDescription(fileData.getDescription());
						break;
					case "designer":
					case "design":
						operations.setDesigner(fileData.getDesigner());
						break;
					case "dept":
					case "department":
					case "section":
					case "division":
					case "branch":
					case "compartment":
					case "segment":
					case "sector":
						operations.setDepartment(fileData.getDepartment());
						break;
					case "order":
					case "order qty":
					case "qty order":
						operations.setOrderQty(fileData.getOrderQty());
						break;
					case "upc":
					case "barcode":
						operations.setUpc(fileData.getUpc());
						break;
					case "price":
					case "usd price":
					case "selling price":
					case "cost":
						operations.setPrice(fileData.getPrice());
						break;
					case "avail":
					case "availability":
					case "accessibility":
						operations.setAvailability(fileData.getAvailability());
						break;
					case "item name":
					case "item":
					case "items":
					case "product":
					case "product#":
					case "product name":
						operations.setProductName(fileData.getProductName());
						break;
					case "type":
					case "kind":
					case "variety":
						operations.setType(fileData.getType());
						break;
					case "quantity":
					case "qty":
					case "stock":
						operations.setQuantity(fileData.getQuantity());
						break;
					case "link":
					case "hyperlink":
						operations.setHyperLink(fileData.getHyperLink());
						break;
					case "brand":
					case "brand name":
					case "brand name fragrances":
						operations.setBrand(fileData.getBrand());
						break;
					case "casepack":
						operations.setCasepack(fileData.getCasepack());
						break;
					default:
						break;

					}
				}

				// Set common properties
				operations.setCreatedOn(fileData.getCreatedOn());
				operations.setEditedOn(fileData.getEditedOn());
				operations.setDataId(fileData.getDataId());
				operations.setUserId(userId);

				// Add operations to the list if any relevant data is set
				if (isDataAvailable(operations)) {
					allOperations.add(operations);
				}
			}

			return allOperations;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

//		Method to check if any data is set in FileOperations object
	private boolean isDataAvailable(FileOperations operations) {

		return operations.getDescription() != null || operations.getUpc() != 0 || operations.getPrice() != null
				|| operations.getProductName() != null || operations.getAvailability() != 0
				|| operations.getQuantity() != null || operations.getCasepack() != 0 || operations.getType() != null
				|| operations.getBrand() != null || operations.getDesigner() != null
				|| operations.getDepartment() != null || operations.getHyperLink() != null
				|| operations.getCreatedOn() != 0 || operations.getEditedOn() != 0 || operations.getOrderQty() != null;
	}

	public FileHeaders updateHeaderMapping(FileHeaders fileHeaders) {
		try {
			long id = fileHeaders.getId();
			long userId = fileHeaders.getUserId();

			FileHeaders headers = fileHeaderRepository.getHeadersByIdAndUserId(id, userId);

			if (fileHeaderRepository.existsById(id)) {
				headers.setHeaderName(fileHeaders.getHeaderName());
				FileHeaders updatedHeader = fileHeaderRepository.save(headers);
				return updatedHeader;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

//	to get all file headers
	public List<FileHeaders> getFileHeaderDataByUserId(long userId) {
		try {
			List<FileHeaders> fileHeader = fileHeaderRepository.getListOfFileHeaderByUserId(userId);
			return fileHeader;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public long deleteFileHeaderDataByUserIdAndId(long userId, long id) {
		try {
			if (fileHeaderRepository.existsById(id)) {
				fileHeaderRepository.deleteFileHeaderByUserIdAndId(userId, id);
				return id;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public long deleteFileByUserId(long userId, long id) {
		try {
			fileOperationRepository.deleteDataByUserId(userId);
			fileHeaderRepository.deleteFileHeaderByUserId(userId);
			templateFileRepo.deleteFileDataByUserId(userId);
			if (fileRepository.existsById(id)) {
				fileRepository.deleteByUserIdAndId(userId, id);
				return id;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public void generateExcelFile(List<FileOperations> fileOperations, ByteArrayOutputStream outputStream, long userId)
			throws IOException {
		XSSFWorkbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("File Operations Data");
		Row headerRow = sheet.createRow(0);

		List<FileHeaders> headers = fileHeaderRepository.getListOfFileHeaderByUserId(userId);
		Map<String, Integer> headerIndexMap = new HashMap<>();

		for (int i = 0; i < headers.size(); i++) {
			Cell cell = headerRow.createCell(i);
			String headerName = headers.get(i).getHeaderName();
			cell.setCellValue(headerName);
			headerIndexMap.put(headerName, i);
		}
		int rowNum = 1;
		for (FileOperations operations : fileOperations) {
			Row createRow = sheet.createRow(rowNum++);
			for (FileHeaders fileHeaders : headers) {
				String headerName = fileHeaders.getHeaderName();
				if (headerName == null) {
					continue;
				}
				Integer columnIndex = headerIndexMap.get(headerName);
				if (columnIndex != null) {

					switch (headerName.toLowerCase()) {
					case "dataId":
						createRow.createCell(columnIndex).setCellValue(operations.getDataId());
						break;
					case "item name":
					case "item":
					case "items":
					case "product":
					case "product#":
					case "product name":
						createRow.createCell(columnIndex).setCellValue(operations.getProductName());
						break;
					case "order":
					case "order qty":
					case "qty order":
						createRow.createCell(columnIndex).setCellValue(operations.getOrderQty());
						break;
					case "upc":
					case "barcode":
						createRow.createCell(columnIndex).setCellValue(operations.getUpc());
						break;
					case "item description":
					case "description":
					case "details":
					case "specification":
						createRow.createCell(columnIndex).setCellValue(operations.getDescription());
						break;
					case "price":
					case "usd price":
					case "selling price":
					case "cost":
						createRow.createCell(columnIndex).setCellValue(operations.getPrice());
						break;
					case "quantity":
					case "qty":
					case "stock":
						createRow.createCell(columnIndex).setCellValue(operations.getQuantity());
						break;
					case "userId":
						createRow.createCell(columnIndex).setCellValue(operations.getUserId());
						break;
					case "avail":
					case "availability":
					case "accessibility":
						createRow.createCell(columnIndex).setCellValue(operations.getAvailability());
						break;
					case "casepack":
						createRow.createCell(columnIndex).setCellValue(operations.getCasepack());
						break;
					case "type":
					case "kind":
					case "variety":
						createRow.createCell(columnIndex).setCellValue(operations.getType());
						break;
					case "brand":
					case "brand name":
					case "brand name fragrances":
						createRow.createCell(columnIndex).setCellValue(operations.getBrand());
						break;
					case "dept":
					case "department":
					case "section":
					case "division":
					case "branch":
					case "compartment":
					case "segment":
					case "sector":
						createRow.createCell(columnIndex).setCellValue(operations.getDepartment());
						break;
					case "link":
					case "hyperlink":
						createRow.createCell(columnIndex).setCellValue(operations.getHyperLink());
						break;
					case "designer":
					case "design":
						createRow.createCell(columnIndex).setCellValue(operations.getDesigner());
						break;
					default:
						break;
					}
				}

			}
		}
		workbook.write(outputStream);
		workbook.close();
	}

//	public String findFileNameByUserId(long userId) {
//		List<String> fileNames = fileHeaderRepository.findFileNameByUserId(userId);
//
//		if (fileNames.isEmpty()) {
//			return null;
//		} else {
//			// chosing first fileName 1st
//			return fileNames.get(0);
//		}
//	}

}
