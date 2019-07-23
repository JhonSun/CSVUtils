package com.ruoyi.common.utils.video;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.ruoyi.common.utils.StringUtils;
package com.ruoyi.common.utils.video;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VideoFormat {
	
	public static final Logger log = LoggerFactory.getLogger(VideoFormat.class);
	// ffmpeg.exe 默认路径
//	private static final String FFMPEG_PATH = "/home/ffmpeg/bin/./ffmpeg";
	private static final String FFMPEG_PATH = "E:\\program\\cram\\lib\\ffmpeg.exe";

	public static void main(String[] args) {

		if (!checkfile("E:\\test\\start\\11.rmvb")) {
			System.out.println("E:\\test\\start\\11.rmvb" + " is not file");
			return;
		}
		if (process("E:\\test\\start\\11.rmvb","E:\\test\\start\\")) {
			System.out.println("ok");
		}
	}
	
	public static boolean videoFormat(String inputPath,String outputPath) {
		
		if (!checkfile(inputPath)) {
			log.info("checkfile不通过");
			System.out.println(inputPath+" is not file");
			return false;
		}
		log.info("视频上传转MP4开始插件路径："+ FFMPEG_PATH);
		if (process(inputPath,outputPath)) {
			System.out.println("ok");
			return true;
		}else {
			return false;
		}
	}

	public static boolean process(String inputPath,String outputPath) {
		// int type = checkContentType();
		boolean status = false;
		System.out.println("直接转成mp4格式");
		log.info("直接转成mp4格式"+inputPath+"----"+outputPath+"----"+FFMPEG_PATH);
		status = processMp4(inputPath,outputPath);// 直接转成mp4格式
		return status;
	}

	private static boolean checkfile(String path) {
		File file = new File(path);
		log.info("file===="+file);
		if (!file.isFile()) {
			return false;
		}
		return true;
	}

	private static boolean processMp4(String inputPath,String outputPath) {
		log.info("进入转换");
		List<String> command = new ArrayList<String>();
		command.add(FFMPEG_PATH);
		command.add("-i");
		command.add(inputPath);
		command.add("-c:v");
		command.add("libx264");
		command.add("-mbd");
		command.add("0");
		command.add("-c:a");
		command.add("aac");
		command.add("-strict");
		command.add("-2");
		command.add("-pix_fmt");
		command.add("yuv420p");
		command.add("-movflags");
		command.add("faststart");
		command.add(outputPath + ".mp4");
	     StringBuffer test=new StringBuffer();    
	     for(int i=0;i<command.size();i++) {
	    	 test.append(command.get(i)+" "); 
	     }    
	     log.info("视频转换test"+test);
	    try {
			  Runtime rt = Runtime.getRuntime();
                          Process proc = rt.exec(test.toString());
			  InputStream stderr = proc.getErrorStream();
			  InputStreamReader isr = new InputStreamReader(stderr);
			  BufferedReader br = new BufferedReader(isr);
			  String line = null;
			  while ( (line = br.readLine()) != null) {
			        System.out.println(line);
			  };
		
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		log.info("视频转换成功");
		return true;
	}

	

}

