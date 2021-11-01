package com.example.highlevel.pojo;

import java.util.List;

/**
 * @author Sebastian
 */
public class ExcelData {
    
    private String[] head;
    
    private List<String[]> data;
    
    private String fileName;

    public String[] getHead() {
        return head;
    }

    public void setHead(String[] head) {
        this.head = head;
    }

    public List<String[]> getData() {
        return data;
    }

    public void setData(List<String[]> data) {
        this.data = data;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
