package com.home.liuhao.other.compression;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

/**
 * 视频压缩
 * 
 * @author liuhao
 *
 */
@Repository
public class CompressionVideo {

	/**
	 * 获取路劲
	 * 
	 * @return
	 * @throws IOException
	 */
	private String getFfmpgePath() throws IOException {
		ClassPathResource cpr = new ClassPathResource("/static/peg/ffmpeg/bin/ffmpeg.exe");
		File file = cpr.getFile();

		return file.getAbsolutePath();

	}

	public boolean processMp4(String putFilePath, String outFilePath, String roate) {

		try {
			List<String> command = new ArrayList<String>();
			command.add(getFfmpgePath());
			command.add("-y");
			command.add("-i");
			command.add(putFilePath);
			command.add("-c:v");
			command.add("libx264");
			command.add("-x264opts");
			command.add("force-cfr=1");
			command.add("-c:a");
			command.add("mp2");
			command.add("-b:a");
			command.add("256k");
			command.add("-vsync");
			command.add("cfr");
			command.add("-f");
			command.add("mpegts");
			command.add("-r");
			command.add("25");
			command.add("-b:v");
			command.add("1000k");
			if (roate != null && !roate.equals("")) {
				command.add(roate);
			}

			command.add(outFilePath);

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

	/**
	 * 视频压缩
	 * 
	 * @param putPath
	 * @param outPath
	 * @return
	 */
	public boolean spys(String putPath, String outPath) {

		List<String> commend = new ArrayList<String>();
		try {
			commend.add(getFfmpgePath());
			commend.add("-i");
			commend.add(putPath);
			commend.add("-ab");
			commend.add("64");
			commend.add("-ar");
			commend.add("22050");
			commend.add("-qscale");
			commend.add("6");
			commend.add("-r");
			commend.add("25");
			commend.add("-s");
			commend.add("600x500");
			commend.add("-b");
			commend.add("1500");
			commend.add(outPath);
			// 方案2
			Process videoProcess = new ProcessBuilder(commend).redirectErrorStream(true).start();

			new PrintStream(videoProcess.getErrorStream()).start();

			new PrintStream(videoProcess.getInputStream()).start();

			videoProcess.waitFor();
			return true;
		} catch (Exception e) {

			e.printStackTrace();
		}

		return false;
	}

}
