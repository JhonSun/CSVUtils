package com.ruoyi.web.controller.tool;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ruoyi.common.config.Global;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.AjaxResult.Type;
import com.ruoyi.common.utils.file.FileUploadUtils;


/**
 * 文件分块上传并合并
 * 
 * @author sunqf
 *
 */
@Controller
@RequestMapping("/fileUpload/blockUpload")
public class FileBlockUploadController {

	
	/**
	 * 上传分块文件
	 * 
	 * @param file 每块文件
	 * @param name 文件名称
	 * @param chunk 文件索引
	 * @param guid 分块文件标识
	 * @return
	 */
	@PostMapping("/upload")
	@ResponseBody
	public AjaxResult upload(MultipartFile file, String name, String chunk, String guid) {
		String dir = Global.getUploadPath() + "/";// 文件上传目录
		dir += guid;// 临时保存分块的目录
		
		String filePath = dir + "/" + chunk;// 文件名为索引名称
		
		File newFile = new File(filePath);
		if (!newFile.getParentFile().exists()) {
			newFile.getParentFile().mkdirs();
		}
		
		try {
			if (!newFile.exists()) {
				newFile.createNewFile();
			}
			
			file.transferTo(newFile);
			return AjaxResult.success();
		} catch (IOException e) {
			e.printStackTrace();
			return AjaxResult.error();
		}
	}
	
	/**
	 * 合并文件
	 * 
	 * @param guid 文件分块标识
	 * @param fileName // 文件名称
	 * @return
	 */
	@PostMapping("/merge")
	@ResponseBody
	public AjaxResult merge(String guid, String fileName, Long evaluationId) {
		String dir = Global.getUploadPath() + "/";// 文件上传目录
		dir += guid;// 临时保存分块的目录
		
		// 拿到文件夹下所有文件的路径
		File parentFile = new File(dir);
		List<String> fileNameList = Arrays.asList(parentFile.list());
		
		// 根据索引排序
		fileNameList.sort((temp1, temp2) -> 
			Integer.valueOf(temp1) - Integer.valueOf(temp2)
		);
		
		try {
			String newFileRelativePath = FileUploadUtils.extractFilename(fileName, fileName.substring(fileName.lastIndexOf(".")));// 新文件相对路径
			String writeFileName = Global.getUploadPath() + "/" + newFileRelativePath;// 新文件绝对路径，用于写入
			
			// 新建保存文件
            File outputFile = new File(writeFileName);

            // 创建目录
            if (!outputFile.getParentFile().exists()) {
            	outputFile.getParentFile().mkdirs();
            }
            
            // 创建文件
            if (!outputFile.exists()) {
            	outputFile.createNewFile();
            }
			
			// 输出流
            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
            FileChannel outChannel = fileOutputStream.getChannel();

            // 合并
            FileChannel inChannel;
			for (int i = 0;i < fileNameList.size();i++) {
				
				String readFileName = dir + "/" + fileNameList.get(i);// 源文件绝对路径
				File blockFile = new File(readFileName);
				inChannel = new FileInputStream(blockFile).getChannel();
                inChannel.transferTo(0, inChannel.size(), outChannel);
                inChannel.close();
				
				if (blockFile.exists()) {
					blockFile.delete();
				}
			}
			
			// 关闭流
            fileOutputStream.close();
            outChannel.close();
			
			if (parentFile.exists()) {
				parentFile.delete();
			}
			
			return new AjaxResult(Type.SUCCESS, "上传成功", filePath);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return AjaxResult.error("上传失败");
		} catch (IOException e) {
			e.printStackTrace();
			return AjaxResult.error("上传失败");
		}
		
	}
}
