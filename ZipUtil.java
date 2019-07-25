package com.ruoyi.common.utils.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * zip压缩工具
 * 
 * @author sunqf
 *
 */
public class ZipUtil {
	
	private ZipOutputStream zipOut;
	
	/**
	 * 初始化压缩工具
	 * 
	 * @param zipFilePath 压缩后文件路径
	 */
	public ZipUtil(String zipFilePath) {
		try {
			zipOut = new ZipOutputStream(new FileOutputStream(zipFilePath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 单个文件压缩
	 * 
	 * @param filePath 需要压缩的文件路径
	 * @param isDelete 压缩成功之后是否删除原文件
	 */
	public void zipFile(String filePath, boolean isDelete) {
		File file = new File(filePath);
		if (!file.exists()) {
			System.out.println("压缩文件不存在----------" + filePath);
			return;
		}
		
		String path = filePath.substring(filePath.indexOf("_") + 1);//压缩文件里面的文件名，可任意
		ZipEntry entry = new ZipEntry(path);
		try {
			zipOut.putNextEntry(entry);
			FileInputStream instream = new FileInputStream(file);
			int len = 0;
			while ((len = instream.read()) != -1) {
				zipOut.write(len);
			}
			instream.close();
			if (isDelete) {
				file.delete();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 多个文件压缩
	 * 
	 * @param filePathList
	 * @param isDelete 压缩成功之后是否删除原文件
	 */
	public void zipFiles(List<String> filePathList, boolean isDelete) {
		for (String filePath : filePathList) {
			this.zipFile(filePath, isDelete);
		}
	}
	
	/**
	 * 关闭压缩流
	 */
	public void close() {
		try {
			zipOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
