package com.dhgh.mddoc.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.dhgh.mddoc.util.MDReader.MDDocument;

public class ReadFromFile {
    public static MDDocument readFile(String file) throws IOException{
        File filename = new File(file); // 要读取以上路径的input。txt文件  
        String fileName = filename.getName();
        StringBuffer result = new StringBuffer();
        InputStream is = null;
        try {
        	is = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is)); // 建立一个对象，它把文件内容转成计算机能读懂的语言  
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
            	result.append(line +"\r\n");
            }
            reader.close();
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new MDDocument(fileName, result.toString());
    }
    public static MDDocument parseDocumentByContent(String content) {
        return new MDDocument("", content);
    }
    
}