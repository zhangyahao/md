//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import javax.servlet.http.HttpServletResponse;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.OutputStream;
//import java.math.BigDecimal;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import java.util.zip.ZipEntry;
//import java.util.zip.ZipOutputStream;
//
///**
// * Created by kzyuan on 16/12/5.
// */
//public class ExportExcel {
//    private static final Logger LOGGER = LoggerFactory.getLogger(ExportExcel.class);
//
//    public static void generateExcel(HttpServletResponse response, Map<String, Object[]> data, String fileName, String sheetName, int[] columnWidths) throws IOException {
//
//        XSSFWorkbook workbook = new XSSFWorkbook();
//
//        XSSFSheet sheet = workbook.createSheet(sheetName);
//
//        for (int i = 0; i < columnWidths.length; ++i) {
//            sheet.setColumnWidth(i, columnWidths[i] * 256);
//        }
//
//        //Iterate over data and write to sheet
//        Set<String> keyset = data.keySet();
//        int rownum = 0;
//        for (String key : keyset) {
//            Row row = sheet.createRow(rownum++);
//            Object[] objArr = data.get(key);
//            int cellnum = 0;
//            for (Object obj : objArr) {
//                Cell cell = row.createCell(cellnum++);
//                setCellValue(obj, cell);
//            }
//        }
//
//        fileName = new String(fileName.getBytes("GBK"), "ISO-8859-1");
//        response.setContentType("application/vnd.ms-excel");
//        response.setHeader("Content-disposition", "attachment;filename="
//                + fileName);
//        OutputStream ouputStream = response.getOutputStream();
//        workbook.write(ouputStream);
//        ouputStream.flush();
//        ouputStream.close();
//    }
//
//    private static void setCellValue(Object obj, Cell cell) {
//        if (obj instanceof String) {
//            cell.setCellValue((String) obj);
//        } else if (obj instanceof Integer) {
//            cell.setCellValue((Integer) obj);
//        } else if (obj instanceof BigDecimal) {
//            cell.setCellValue(obj.toString());
//        }
//    }
//
//    /**
//     * @param srcfile 文件名数组
//     * @param zipfile 压缩后文件
//     */
//    public static void zipFiles(File[] srcfile, File zipfile) {
//        byte[] buf = new byte[1024];
//        try {
//            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
//                    zipfile));
//            for (int i = 0; i < srcfile.length; i++) {
//                FileInputStream in = new FileInputStream(srcfile[i]);
//                out.putNextEntry(new ZipEntry(srcfile[i].getName()));
//                int len;
//                while ((len = in.read(buf)) > 0) {
//                    out.write(buf, 0, len);
//                }
//                out.closeEntry();
//                in.close();
//            }
//            out.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//    /**
//     * 文件删除
//     *
//     * @param fileNames
//     * @param zipPath
//     */
//    public static void deleteFile(List<String> fileNames, String zipPath) {
//        String sPath = null;
//        File file = null;
//        boolean flag = false;
//        try {
//            // 判断目录或文件是否存在
//            for (int i = 0; i < fileNames.size(); i++) {
//                sPath = fileNames.get(i);
//                file = new File(sPath);
//                if (file.exists()) {
//                    file.delete();
//                }
//            }
//            file = new File(zipPath);
//            if (file.exists()) {
//                file.delete();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     *  导出zip方法（需要有excel文件夹）
//     * @param fileNames
//     * @param path
//     * @param zip
//     * @param out
//     * @param fileName
//     * @throws IOException
//     */
//    public static void exportZip(List<String> fileNames, String path, File zip, OutputStream out, String fileName) throws IOException {
//
//        File[] srcfile = new File[fileNames.size()];
//        for (int i = 0, n = fileNames.size(); i < n; i++) {
//            srcfile[i] = new File(fileNames.get(i));
//        }
//        ExportExcle.zipFiles(srcfile, zip);
//        FileInputStream inStream = new FileInputStream(zip);
//        try {
//            byte[] buf = new byte[4096];
//            int readLength;
//            while ((readLength = inStream.read(buf)) != -1) {
//                out.write(buf, 0, readLength);
//            }
//            inStream.close();
//            ExportExcle.deleteFile(fileNames, path + "excel/" + fileName + ".zip");
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            inStream.close();
//        }
//
//    }
//}
//
