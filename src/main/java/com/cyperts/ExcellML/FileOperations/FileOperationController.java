package com.cyperts.ExcellML.FileOperations;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.cyperts.ExcellML.ExcellFiles.FileRepository;
import com.cyperts.ExcellML.ExcellFiles.FileServiceImpl;
import com.cyperts.ExcellML.ExcellFiles.TemplateFile;
import com.cyperts.ExcellML.ExcellFiles.TemplateFileRepository;
import com.cyperts.ExcellML.UserAndRole.User;
import com.cyperts.ExcellML.UserAndRole.UserRepository;

@Controller
@RequestMapping("/api/files")
@CrossOrigin(origins = { ("http://72.167.32.189"), ("http://localhost:3000"), ("https://beautyblush.ca") })
public class FileOperationController {

	@Autowired
	private FileServiceImpl fileServiceImpl;
	@Autowired
	private FileOperationService fileOperationService;
	@Autowired
	private FileOperationRepository fileOperationRepository;
	@Autowired
	FileRepository fileRepo;
	@Autowired
	private TemplateFileRepository templateFileRepo;
	@Autowired
	FileHeaderRepository fileHeaderRepo;

	@Autowired
	UserRepository userRepo;

//	to read the excell file data
	@CrossOrigin(origins = { ("http://72.167.32.189"), ("http://localhost:3000"), ("https://beautyblush.ca") })
	@PostMapping("/fileOperation")
	public ResponseEntity<List<FileOperations>> uploadFileData(@RequestParam long userId) {
		try {

			File file = fileServiceImpl.getUserFileByUserId(userId);

//			File convFile = new File(file.getOriginalFilename());
//			fos.write(file.getBytes());
//			fos.close();
//			String fileContentType = file.getContentType();
//			byte[] sourceFileContent = file.getBytes();
//			String fileName = file.getOriginalFilename();
//			TemplateFile fileModal = new TemplateFile(fileName, sourceFileContent, fileContentType, userId);
			String fileExtension = file.getName().substring(file.getName().lastIndexOf("."));
			List<FileOperations> uploadFileOperation = fileOperationService.uploadFileOperation(file, userId,
					fileExtension);

//			fileServiceImpl.saveFile(fileModal);
			return new ResponseEntity<List<FileOperations>>(uploadFileOperation, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Transactional
	@CrossOrigin(origins = { ("http://72.167.32.189"), ("http://localhost:3000"), ("https://beautyblush.ca") })
	@PostMapping("/fileOperation/header")
	public ResponseEntity<List<FileHeaders>> uploadHeaderData(@RequestBody MultipartFile file,
			@RequestParam long userId) {
		try {
			File convFile = new File(file.getOriginalFilename());
			FileOutputStream fos = new FileOutputStream(convFile);
			fos.write(file.getBytes());
			fos.close();
			String fileExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
			List<FileHeaders> saveHeaders = fileOperationService.saveHeaders(convFile, userId, fileExtension);
			return new ResponseEntity<List<FileHeaders>>(saveHeaders, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

//	to update header mapping
	@CrossOrigin(origins = { ("http://72.167.32.189"), ("http://localhost:3000"), ("https://beautyblush.ca") })
	@PutMapping("/fileOperation/headerMapping")
	public ResponseEntity<FileHeaders> UploadHeaderMappings(@RequestBody FileHeaders fileHeaders) {
		try {
			FileHeaders updateHeaderMapping = fileOperationService.updateHeaderMapping(fileHeaders);

			return new ResponseEntity<FileHeaders>(updateHeaderMapping, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	// to get List of file headers
	@CrossOrigin(origins = { ("http://72.167.32.189"), ("http://localhost:3000"), ("https://beautyblush.ca") })
	@GetMapping("/fileHeaders/{userId}")
	public ResponseEntity<List<FileHeaders>> getFileHeaderDataByUserId(@PathVariable long userId) {
		try {

			List<FileHeaders> fileHeaderList = fileOperationService.getFileHeaderDataByUserId(userId);
			return new ResponseEntity<List<FileHeaders>>(fileHeaderList, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	@CrossOrigin(origins = { ("http://72.167.32.189"), ("http://localhost:3000"), ("https://beautyblush.ca") })
	@GetMapping("/downloadDataInExcel")
	public ResponseEntity<byte[]> downloadDataInExcel() {
		try {
			List<FileOperations> fileOperations = fileOperationRepository.findAll();

			Workbook workbook = new XSSFWorkbook();
			Sheet sheet = workbook.createSheet("File Operations");

			Row headerRow = sheet.createRow(0);
			headerRow.createCell(0).setCellValue("Data ID");
			headerRow.createCell(1).setCellValue("Product Name");
			headerRow.createCell(2).setCellValue("UPC");
			headerRow.createCell(3).setCellValue("Price");
			headerRow.createCell(4).setCellValue("Quantity");
			headerRow.createCell(5).setCellValue("Organisation Name");

			int rowNum = 1;
			for (FileOperations fileOperation : fileOperations) {
				Row row = sheet.createRow(rowNum++);
				row.createCell(0).setCellValue(fileOperation.getDataId());
				row.createCell(1).setCellValue(fileOperation.getProductName());
				row.createCell(2).setCellValue(fileOperation.getUpc());
				row.createCell(3).setCellValue(fileOperation.getPrice());
				row.createCell(4).setCellValue(fileOperation.getQuantity());
				Long userId = fileOperation.getUserId();
				User user = userRepo.findById(userId).orElse(null);
				String organisationName = (user != null) ? user.getOrganisationName() : "Unknown Organisation";
				row.createCell(5).setCellValue(organisationName);
				System.out.println("Organisation name::: e" + organisationName);
			}

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			workbook.write(outputStream);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(
					MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
			headers.setContentDispositionFormData("attachment", "BeautyBlush.xlsx");

			return ResponseEntity.ok().headers(headers).body(outputStream.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	// to download File
	@CrossOrigin(origins = { ("http://72.167.32.189"), ("http://localhost:3000"), ("https://beautyblush.ca") })
	@GetMapping("/downloadExcel/{userId}")
	public ResponseEntity<ByteArrayResource> downloadExcelData(@PathVariable long userId) {
		try {
			TemplateFile fileData = fileRepo.getFileDataByUserId(userId);
			byte[] fileContent = fileData.getContent();
			String fileName = fileData.getFileName();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			headers.setContentDispositionFormData("attachment", fileName);
			ByteArrayResource resource = new ByteArrayResource(fileContent);

			return ResponseEntity.ok().headers(headers).contentLength(fileContent.length).body(resource);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

	}

	// for searching all data only
	@CrossOrigin(origins = { ("http://72.167.32.189"), ("http://localhost:3000"), ("https://beautyblush.ca") })
	@GetMapping("/file-operations/search")
	public ResponseEntity<List<FileOperations>> searchALLdata(@RequestParam String searchTerm) {
		String searchTrim = searchTerm.trim();
		List<FileOperations> fileOperationsList = fileOperationRepository.findBySearchTerm(searchTrim);

		if (fileOperationsList != null && !fileOperationsList.isEmpty()) {
			return new ResponseEntity<>(fileOperationsList, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
		}
	}

	@CrossOrigin(origins = { ("http://72.167.32.189"), ("http://localhost:3000"), ("https://beautyblush.ca") })
	@GetMapping("/templateFile/getByUserId/{userId}")
	public ResponseEntity<List<TemplateFile>> getDataFromTemplateFileByUserId(@PathVariable long userId) {
		List<TemplateFile> data = templateFileRepo.getDataByUserId(userId);
		if (data != null) {
			return new ResponseEntity<>(data, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}

	@CrossOrigin(origins = { ("http://72.167.32.189"), ("http://localhost:3000"), ("https://beautyblush.ca") })
	@GetMapping("/file-operations/sort/{order}/{pageNumber}")
	public ResponseEntity<List<FileOperations>> sortDataByDataId(@PathVariable int pageNumber,
			@PathVariable String order) {
		try {
			int pageSize = 20;
			Pageable pageable = PageRequest.of(pageNumber, pageSize);

			Page<FileOperations> fileOperationsPage = fileOperationRepository.findAll(pageable);
			List<FileOperations> fileOperations = new ArrayList<>(fileOperationsPage.getContent());

			if (!fileOperations.isEmpty()) {
				Comparator<FileOperations> comparator = Comparator.comparing(FileOperations::getDataId);
				if (order.equalsIgnoreCase("desc")) {
					comparator = comparator.reversed();
				}
				fileOperations.sort(comparator);

				return ResponseEntity.ok().body(fileOperations);
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	// to delete headers by Id and userId
	@CrossOrigin(origins = { ("http://72.167.32.189"), ("http://localhost:3000"), ("https://beautyblush.ca") })
	@DeleteMapping("/fileHeaders/{userId}/{id}")
	public ResponseEntity<Long> deleteFileHeaderDataByUserId(@PathVariable long userId, @PathVariable long id) {
		try {
			long deletedHeaderId = fileOperationService.deleteFileHeaderDataByUserIdAndId(userId, id);
			return new ResponseEntity<Long>(deletedHeaderId, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	@CrossOrigin(origins = { ("http://72.167.32.189"), ("http://localhost:3000"), ("https://beautyblush.ca") })
	@DeleteMapping("/delete/{userId}/{id}")
	public ResponseEntity<String> deleteFileByUserId(@PathVariable long userId, @PathVariable long id) {
		try {
			fileOperationService.deleteFileByUserId(userId, id);

			return new ResponseEntity<>("File deleted successfully....!!", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	// to count whole rows for pagination
	@CrossOrigin(origins = { ("http://72.167.32.189"), ("http://localhost:3000"), ("https://beautyblush.ca") })
	@GetMapping("/file-operations/count")
	public ResponseEntity<Integer> getTotalNumberOfRows() {
		try {
			List<FileOperations> sortedFileOperations = fileOperationRepository.findAll();
			int rowCount = sortedFileOperations.size();
			return ResponseEntity.ok(rowCount);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body(0);
		}
	}

	@CrossOrigin(origins = { ("http://72.167.32.189"), ("http://localhost:3000"), ("https://beautyblush.ca") })
	@GetMapping("/getAllData/{pageNumber}")
	public ResponseEntity<List<FileOperations>> getAllFileDataByUserId(@PathVariable int pageNumber) {
		try {
			Pageable pageable = PageRequest.of(pageNumber - 1, 20);
			Page<FileOperations> fileOperationsPage = fileOperationRepository.findAll(pageable);
			List<FileOperations> fileOperations = fileOperationsPage.getContent();
			return ResponseEntity.ok().body(fileOperations);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}
}
//	