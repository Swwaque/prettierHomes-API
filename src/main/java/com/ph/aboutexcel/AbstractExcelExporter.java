package com.ph.aboutexcel;


import com.ph.exception.customs.ConflictException;
import com.ph.exception.customs.DataExportException;
import com.ph.exception.customs.DirectoryCreationException;
import com.ph.exception.customs.MissingArgumentException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public abstract class AbstractExcelExporter {

    protected abstract CellStyle buildHeaderStyle(Workbook workbook);

    protected abstract CellStyle buildDataCellStyle(Workbook workbook);

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AbstractExcelExporter.class);

    public <T> ResponseEntity<Resource> writeToExcel(List<T> exportDataList, String fileName, String sheetName) {

        checkMethodParameters(exportDataList, fileName, sheetName);

        try (Workbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet(sheetName);
            CellStyle headerStyle = buildHeaderStyle(workbook);
            CellStyle dataCellStyle = buildDataCellStyle(workbook);

            Row header = sheet.createRow(0);
            int cellIndex = 0;
            List<Field> fields = getIndexedAnnotatedFields(exportDataList.get(0).getClass(), ExportToExcel.class);
            for (Field field : fields) {
                ExportToExcel annotation = field.getAnnotation(ExportToExcel.class);
                String columnName = annotation.headerText();
                int columnWidth = annotation.width();

                sheet.setColumnWidth(cellIndex, columnWidth);

                Cell headerCell = header.createCell(cellIndex++);
                headerCell.setCellValue(columnName);
                headerCell.setCellStyle(headerStyle);
            }


            int rowIndex = 1;
            for (T exportData : exportDataList) {
                Row dataRow = sheet.createRow(rowIndex++);
                cellIndex = 0;
                for (Field field : fields) {
                    field.setAccessible(true);
                    Object value = field.get(exportData);

                    Cell dataCell = dataRow.createCell(cellIndex++);
                    dataCell.setCellValue(String.valueOf(value));
                    dataCell.setCellStyle(dataCellStyle);
                }
            }

            ByteArrayResource resource = writeWorkbookToByteArrayResource(workbook);

            ContentDisposition contentDisposition = ContentDisposition.attachment().filename(fileName + ".xlsx").build();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentDisposition(contentDisposition);
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);

        } catch (IOException e) {
            log.error("Excel data export error : {}", e.getMessage());
            throw new DataExportException(e.getMessage());
        } catch (IllegalAccessException e) {
            log.error("Excel data export error : {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private ByteArrayResource writeWorkbookToByteArrayResource(Workbook workbook) throws IOException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            workbook.write(byteArrayOutputStream);
            return new ByteArrayResource(byteArrayOutputStream.toByteArray());
        }
    }

    protected <T> void checkMethodParameters(List<T> exportDataList, String fileName, String sheetName) {
        if (exportDataList == null) {
            throw new MissingArgumentException("Export to Excel data missing. Data list null");
        }
        if (exportDataList.isEmpty()) {
            throw new MissingArgumentException("There is no data to export to Excel. Data list empty");
        }
        if (fileName == null || sheetName == null) {
            throw new MissingArgumentException("Appropriate filename and page name must be provided. File name or Sheet name null");
        }
    }

    protected void createDirectories(String path) {
        Path directoryPath = Paths.get(path);

        if (!Files.exists(directoryPath)) {
            try {
                Files.createDirectories(directoryPath);
            } catch (IOException e) {
                log.error("Directory creation error: {}", e.getMessage());
                throw new DirectoryCreationException(String.format("Creation Error: Could not create folder in the file path : %s", e.getMessage()));
            }
        }
    }

    protected List<Field> getIndexedAnnotatedFields(Class<?> clazz, Class<? extends Annotation> annotationClass) {
        List<Field> annotatedFields = new ArrayList<>();
        Set<Integer> indexSet = new HashSet<>();

        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(annotationClass)) {
                int index = field.getAnnotation(ExportToExcel.class).index();
                if (indexSet.contains(index)) {
                    throw new ConflictException("ExportToExcel.class duplicate index value: " + index);
                } else if (index == -1) {
                    throw new MissingArgumentException("ExportToExcel.class index value missing: " + index);
                }
                annotatedFields.add(field);
                indexSet.add(index);
            }
        }

        annotatedFields.sort(Comparator.comparingInt(field -> field.getAnnotation(ExportToExcel.class).index()));
        return annotatedFields;
    }

}


