package com.ruoyi.common.utils.video;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.ruoyi.common.utils.StringUtils;

public class VideoFormat {
	
	// ffmpeg.exe 默认路径
	private static final String FFMPEG_PATH = "\\ruoyi-common\\src\\main\\java\\com\\ruoyi\\common\\utils\\video\\";

	public static void main(String[] args) {
		String currPath = getPath();

		if (!checkfile("E:\\test\\start\\6.rm")) {
			System.out.println("E:\\test\\start\\6.rm" + " is not file");
			return;
		}
		if (process("E:\\test\\start\\6.rm","E:\\test\\end\\",currPath+"\\src\\main\\java\\com\\ruoyi\\common\\utils\\video\\")) {
			System.out.println("ok");
		}
	}
	
	public static boolean videoFormat(String inputPath,String outputPath,String ffmpegPath) {
		if (StringUtils.isBlank(ffmpegPath)) {
			ffmpegPath = FFMPEG_PATH;
		}
		
		String currPath = getPath();

		if (!checkfile(inputPath)) {
			System.out.println(inputPath+" is not file");
			return false;
		}
		if (process(inputPath,outputPath,currPath+ffmpegPath)) {
			System.out.println("ok");
			return true;
		}else {
			return false;
		}
	}

	public static String getPath() {
		// 先获取当前项目路径，在获得源文件、目标文件、转换器的路径
		File diretory = new File("");
		try {
			String currPath = diretory.getAbsolutePath();
			currPath = currPath.substring(0, currPath.lastIndexOf("\\"));
//			inputPath = "E:\\test\\start\\6.rm";//需要转换格式的视频文件路径
//			outputPath = "E:\\test\\end\\";//转换后的MP4文件路径
//			ffmpegPath = "E:\\test\\min\\";//ffmpeg插件路径
			System.out.println(currPath+" 1111111is not file");
			return currPath;
		} catch (Exception e) {
			System.out.println("getPath出错");
			return null;
		}
	}

	public static boolean process(String inputPath,String outputPath,String ffmpegPath) {
		// int type = checkContentType();
		boolean status = false;
		System.out.println("直接转成mp4格式");
		status = processMp4(inputPath,outputPath,ffmpegPath);// 直接转成mp4格式
		return status;
	}

	private static boolean checkfile(String path) {
		File file = new File(path);
		if (!file.isFile()) {
			return false;
		}
		return true;
	}

	private static boolean processMp4(String inputPath,String outputPath,String ffmpegPath) {

		if (!checkfile(inputPath)) {
			System.out.println(inputPath + " is not file");
			return false;
		}
		List<String> command = new ArrayList<String>();
		command.add(ffmpegPath + "ffmpeg.exe");
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
		try {

			// 方案1
			// Process videoProcess = Runtime.getRuntime().exec(ffmpegPath + "ffmpeg -i " +
			// oldfilepath
			// + " -ab 56 -ar 22050 -qscale 8 -r 15 -s 600x500 "
			// + outputPath + "a.flv");

			// 方案2
			Process videoProcess = new ProcessBuilder(command).redirectErrorStream(true).start();

			new PrintStream(videoProcess.getErrorStream()).start();

			new PrintStream(videoProcess.getInputStream()).start();

			videoProcess.waitFor();

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// private static int checkContentType() {
	// String type = inputPath.substring(inputPath.lastIndexOf(".") + 1,
	// inputPath.length()).toLowerCase();
	// // ffmpeg能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等）
	// if (type.equals("avi")) { // ok
	// return 0;
	// } else if (type.equals("mpg")) {// ok
	// return 0;
	// } else if (type.equals("wmv")) {// ok
	// return 0;
	// } else if (type.equals("3gp")) { // ok
	// return 0;
	// } else if (type.equals("mov")) { // ok
	// return 0;
	// } else if (type.equals("mp4")) {
	// return 0;
	// } else if (type.equals("flv")) { // ok
	// return 0;
	// } else if (type.equals("mkv")) { // ok
	// return 0;
	// } else if (type.equals("vob")) { // ok
	// return 0;
	// } else if (type.equals("swf")) { // ok
	// return 0;
	// } else if (type.equals("gif")) { // ok
	// return 0;
	// } else if (type.equals("rm")) { // ok
	// return 0;
	// } else if (type.equals("rmvb")) { // ok
	// return 0;
	// }
	// return 9;
	// }

}
