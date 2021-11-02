//package com.example.highlevel.excel;
//
//import com.example.highlevel.pojo.Customer;
//import com.example.highlevel.pojo.ExcelData;
//import org.apache.poi.hssf.usermodel.HSSFCell;
//import org.apache.poi.hssf.usermodel.HSSFCellStyle;
//import org.apache.poi.hssf.usermodel.HSSFDataFormat;
//import org.apache.poi.hssf.usermodel.HSSFFont;
//import org.apache.poi.hssf.usermodel.HSSFRow;
//import org.apache.poi.hssf.usermodel.HSSFSheet;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.ss.usermodel.WorkbookFactory;
//
//import javax.servlet.http.HttpServletResponse;
//import java.io.BufferedOutputStream;
//import java.io.FileInputStream;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.apache.poi.ss.usermodel.CellType.BOOLEAN;
//import static org.apache.poi.ss.usermodel.CellType.ERROR;
//import static org.apache.poi.ss.usermodel.CellType.NUMERIC;
//import static org.apache.poi.ss.usermodel.CellType.STRING;
//
///**
// * @author Sebastian
// */
//public class ExcelUtil {
//
//    /**
//     * 方法名：exportExcel
//     * 功能：导出Excel
//     * 描述：
//     */
//    public static void exportExcel(HttpServletResponse response, ExcelData data) {
//        try {
//            //实例化HSSFWorkbook
//            HSSFWorkbook workbook = new HSSFWorkbook();
//            //创建一个Excel表单，参数为sheet的名字
//            HSSFSheet sheet = workbook.createSheet("sheet");
//            //设置表头
//            setTitle(workbook, sheet, data.getHead());
//            //设置单元格并赋值
//            setData(sheet, data.getData());
//            //设置浏览器下载
//            setBrowser(response, workbook, data.getFileName());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 方法名：setTitle
//     * 功能：设置表头
//     * 描述：
//     */
//    private static void setTitle(HSSFWorkbook workbook, HSSFSheet sheet, String[] str) {
//        try {
//            HSSFRow row = sheet.createRow(0);
//            //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
//            for (int i = 0; i <= str.length; i++) {
//                sheet.setColumnWidth(i, 15 * 256);
//            }
//            //设置为居中加粗,格式化时间格式
//            HSSFCellStyle style = workbook.createCellStyle();
//            HSSFFont font = workbook.createFont();
//            font.setBold(true);
//            style.setFont(font);
//            style.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));
//            //创建表头名称
//            HSSFCell cell;
//            for (int j = 0; j < str.length; j++) {
//                cell = row.createCell(j);
//                cell.setCellValue(str[j]);
//                cell.setCellStyle(style);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 方法名：setData
//     * 功能：表格赋值
//     * 描述：
//     */
//    private static void setData(HSSFSheet sheet, List<String[]> data) {
//        try{
//            int rowNum = 1;
//            for (int i = 0; i < data.size(); i++) {
//                HSSFRow row = sheet.createRow(rowNum);
//                for (int j = 0; j < data.get(i).length; j++) {
//                    row.createCell(j).setCellValue(data.get(i)[j]);
//                }
//                rowNum++;
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 方法名：setBrowser
//     * 功能：使用浏览器下载
//     * 描述：
//     */
//    private static void setBrowser(HttpServletResponse response, HSSFWorkbook workbook, String fileName) {
//        try {
//            //清空response
//            response.reset();
//            //设置response的Header
//            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
//            OutputStream os = new BufferedOutputStream(response.getOutputStream());
//            response.setContentType("application/vnd.ms-excel;charset=gb2312");
//            //将excel写入到输出流中
//            workbook.write(os);
//            os.flush();
//            os.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//
//    /**
//     * 方法名：importExcel
//     * 功能：导入
//     * 描述：
//     */
//    public static List<Object[]> importExcel(String fileName) {
//        try {
//            List<Object[]> list = new ArrayList<>();
//            InputStream inputStream = new FileInputStream(fileName);
//            Workbook workbook = WorkbookFactory.create(inputStream);
//            Sheet sheet = workbook.getSheetAt(0);
//            //获取sheet的行数
//            int rows = sheet.getPhysicalNumberOfRows();
//            for (int i = 0; i < rows; i++) {
//                //过滤表头行
//                if (i == 0) {
//                    continue;
//                }
//                //获取当前行的数据
//                Row row = sheet.getRow(i);
//                // row.getPhysicalNumberOfCells() 是为了获取不为空列的个数
//                Object[] objects = new Object[row.getPhysicalNumberOfCells()];
//                int index = 0;
//                list.add(objects);
//                for (Cell cell : row) {
//                    if (cell.getCellType().equals(NUMERIC)) {
//                        objects[index] = (long) cell.getNumericCellValue();
//                    }
//                    if (cell.getCellType().equals(STRING)) {
//                        objects[index] = cell.getStringCellValue();
//                    }
//                    if (cell.getCellType().equals(BOOLEAN)) {
//                        objects[index] = cell.getBooleanCellValue();
//                    }
//                    if (cell.getCellType().equals(ERROR)) {
//                        objects[index] = cell.getErrorCellValue();
//                    }
//                    index++;
//                }
//            }
//            return list;
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public static void main(String[] args) {
//        String fileName = "E:\\testExcel.xlsx";
//        List<Object[]> list = importExcel(fileName);
//        for (int i = 0; i < list.size(); i++) {
//            Customer customer = new Customer();
//            customer.setName((String) list.get(i)[0]);
//            customer.setPhone(String.valueOf(list.get(i)[1]));
//            customer.setAge((long) list.get(i)[2]);
//            customer.setAddress((String) list.get(i)[3]);
//            System.out.println(customer);
//        }
//    }
//}
