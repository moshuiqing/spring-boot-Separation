package com.home.liuhao.other.ftp.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import com.home.liuhao.util.Global;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Ftp {
	
	private FTPClient ftpClient;
	private String strIp;
	private int intPort;
	private String user;
	private String password;

	/* *
	 * Ftp构造函数(不需要传参，但需要框架配置)
	 */
	public Ftp() {
		this.strIp = Global.ftpip;
		this.intPort = Global.ftpport;
		this.user = Global.ftpuser;
		this.password = Global.ftppwd;
		this.ftpClient = new FTPClient();
	}

	/* *
	 * Ftp构造函数(需要ip 端口 帐号  密码)
	 */
	public Ftp(String strIp, int intPort, String user, String Password) {
		this.strIp = strIp;
		this.intPort = intPort;
		this.user = user;
		this.password = Password;
		this.ftpClient = new FTPClient();
	}

	/**
	 * @return 判断是否登入成功
	 * */
	public boolean ftpLogin() {
		boolean isLogin = false;
		FTPClientConfig ftpClientConfig = new FTPClientConfig();
		ftpClientConfig.setServerTimeZoneId(TimeZone.getDefault().getID());
		this.ftpClient.setControlEncoding("GBK");
		this.ftpClient.configure(ftpClientConfig);
		try {
			if (this.intPort > 0) {
				this.ftpClient.connect(this.strIp, this.intPort);
			} else {
				this.ftpClient.connect(this.strIp);
			}
			// FTP服务器连接回答
			int reply = this.ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				this.ftpClient.disconnect();
				log.error("登录FTP服务失败！");
				return isLogin;
			}
			this.ftpClient.login(this.user, this.password);
			// 设置传输协议
			this.ftpClient.enterLocalPassiveMode();
			this.ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			//log.info("恭喜" + this.user + "成功登陆FTP服务器");
			isLogin = true;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(this.user + "登录FTP服务失败！" + e.getMessage());
		}
		this.ftpClient.setBufferSize(1024 * 2);
		this.ftpClient.setDataTimeout(30 * 1000);
		return isLogin;
	}

	/**
	 * @退出关闭服务器链接
	 * */
	public void ftpLogOut() {
		if (null != this.ftpClient && this.ftpClient.isConnected()) {
			try {
				boolean reuslt = this.ftpClient.logout();// 退出FTP服务器
				if (reuslt) {
					log.info("成功退出服务器");
				}
			} catch (IOException e) {
				e.printStackTrace();
				log.warn("退出FTP服务器异常！" + e.getMessage());
			} finally {
				try {
					this.ftpClient.disconnect();// 关闭FTP服务器的连接
				} catch (IOException e) {
					e.printStackTrace();
					log.warn("关闭FTP服务器的连接异常！");
				}
			}
		}
	}
    /** 
     * 递归遍历出目录下面所有文件 
     * @param pathName 需要遍历的目录，必须以"/"开始和结束 
     * @throws IOException 
     */  
    public void ListFileName(String pathName,List<String> list) throws IOException{  
        if(pathName.startsWith("/")&&pathName.endsWith("/")){  
            String directory = pathName;  
            //更换目录到当前目录  
            this.ftpClient.changeWorkingDirectory(directory);  
            String[] files = this.ftpClient.listNames();  
            for(String file:files){  
            	list.add(directory+file);  
            } 
        }  
    }  
	/***
	 * 上传web服务器文件到Ftp服务器
	 * 
	 * @param localFile
	 *            服务器文件
	 * @param romotUpLoadePath上传服务器路径
	 *            - 应该以/结束
	 * */
	public boolean uploadFile(File localFile, String romotUpLoadePath) {
		BufferedInputStream inStream = null;
		boolean success = false;
		try {
			this.ftpClient.changeWorkingDirectory(romotUpLoadePath);// 改变工作路径
			inStream = new BufferedInputStream(new FileInputStream(localFile));
			log.info(localFile.getName() + "开始上传.....");
			success = this.ftpClient.storeFile(localFile.getName(), inStream);
			if (success == true) {
				log.info(localFile.getName() + "上传成功");
				return success;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			log.error(localFile + "未找到");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inStream != null) {
				try {
					inStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return success;
	}
	
	/***
	 * 上传文件到Ftp服务器
	 * 
	 * @param in
	 *            上传文件的流
	 * @param fileName
	 * 			  文件在Ftp的名字
	 * @param romotUpLoadePath上传服务器路径
	 *            - 应该以/结束  例如：跟路径 /  一层 路径：test/  两层路径：test/test/
	 * */
	public boolean uploadFileToFtp(InputStream in,String fileName,String romotUpLoadePath) {
		boolean success = false;
		try {
			this.ftpClient.changeWorkingDirectory("/");//转移根目录
			success = this.ftpClient.changeWorkingDirectory(romotUpLoadePath);// 改变工作路径
			
			if(!success){//如果路径改变失败 表示ftp没有文件夹创建文件夹
				makeDir(romotUpLoadePath);//创建文件夹
				this.ftpClient.changeWorkingDirectory("/");//转移根目录
				success = this.ftpClient.changeWorkingDirectory(romotUpLoadePath);//再次转入路径
				if(!success){
					log.info("再次转入路径失败，请联系管理员");
					return success;
				}
			}
			
			log.info(fileName + "开始上传.....");
			success = this.ftpClient.storeFile(fileName, in);
			if (success == true) {
				log.info(fileName + "上传成功");
				return success;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			log.error("上传失败");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return success;
	}

	

	/***
	 * 下载文件到web服务器
	 * 
	 * @param remoteFileName
	 *            待下载文件名称
	 * @param localDires
	 *            下载到web服务器那个路径下
	 * @param remoteDownLoadPath
	 *            remoteFileName所在的路径 - 应该以/结束
	 * */

	public boolean downloadFile(String remoteFileName, String localDires,
			String remoteDownLoadPath) {
		String strFilePath = localDires + remoteFileName;
		BufferedOutputStream outStream = null;
		boolean success = false;
		try {
			this.ftpClient.changeWorkingDirectory(remoteDownLoadPath);
			outStream = new BufferedOutputStream(new FileOutputStream(
					strFilePath));
			log.info(remoteFileName + "开始下载....");
			success = this.ftpClient.retrieveFile(remoteFileName, outStream);
			if (success == true) {
				log.info(remoteFileName + "成功下载到" + strFilePath);
				return success;
			}else{
				log.info(remoteFileName + "不存在");
				return success;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(remoteFileName + "下载失败");
		} finally {
			if (null != outStream) {
				try {
					outStream.flush();
					outStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if (success == false) {
			log.error(remoteFileName + "下载失败");
		}
		return success;
	}
	
	/***
	 * 返回文件的流
	 * 
	 * @param remoteFileName
	 *            待下载文件名称
	 * @param remoteDownLoadPath
	 *            remoteFileName所在的路径 - 应该以/结束
	 * @throws IOException 
	 * */
	public InputStream getFileStream(String remoteFileName,String remoteDownLoadPath) throws IOException {
		InputStream in = null;
		log.info("正在下载文件:"+remoteFileName);
		in = this.ftpClient.retrieveFileStream(remoteDownLoadPath + remoteFileName);
		return in;
	}
	
	/***
	 * 查看文件是否存在
	 * 
	 * @param remoteFileName
	 *            待查看的文件名称
	 * @param remoteDownLoadPath
	 *            remoteFileName所在的路径 - 应该以/结束
	 * */
	public boolean checkFile(String remoteFileName,String remoteDownLoadPath) {
		boolean flag = false;
		try {
			this.ftpClient.changeWorkingDirectory(remoteDownLoadPath);
			FTPFile[] allFile = this.ftpClient.listFiles();
			for(FTPFile list : allFile){
				if(list.getName().equals(remoteFileName)){
					flag = true;
					return flag;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	/***
	 * @创建文件夹
	 * @param dirStr
	 *           文件夹串 例如： test/test/(两层) test/(一层)
	 * */
	public boolean makeDir(String dirStr){
		boolean flag = false;
		try {
			
			this.ftpClient.changeWorkingDirectory("/");//根路径
			flag = this.ftpClient.changeWorkingDirectory(dirStr);//看是否能改变路径
			if(flag){
				log.info("文件夹已存在");
				return flag;
			}else{
				String[] dirName = dirStr.split("/");
				for (int i = 0; i < dirName.length; i++) {
					this.ftpClient.makeDirectory(dirName[i]);
					this.ftpClient.changeWorkingDirectory(dirName[i]);
				}
				log.info("文件夹创建完成");
				flag = true;
				return flag;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	/***
	 * @上传文件夹
	 * @param localDirectory
	 *            当地文件夹
	 * @param remoteDirectoryPath
	 *            Ftp 服务器路径 以目录"/"结束
	 * */
	public boolean uploadDirectory(String localDirectory,
			String remoteDirectoryPath) {
		File src = new File(localDirectory);
		try {
			remoteDirectoryPath = remoteDirectoryPath + src.getName() + "/";
			this.ftpClient.makeDirectory(remoteDirectoryPath);
			// ftpClient.listDirectories();
		} catch (IOException e) {
			e.printStackTrace();
			log.info(remoteDirectoryPath + "目录创建失败");
		}
		File[] allFile = src.listFiles();
		for (int currentFile = 0; currentFile < allFile.length; currentFile++) {
			if (!allFile[currentFile].isDirectory()) {
				String srcName = allFile[currentFile].getPath().toString();
				uploadFile(new File(srcName), remoteDirectoryPath);
			}
		}
		for (int currentFile = 0; currentFile < allFile.length; currentFile++) {
			if (allFile[currentFile].isDirectory()) {
				// 递归
				uploadDirectory(allFile[currentFile].getPath().toString(),
						remoteDirectoryPath);
			}
		}
		return true;
	}

	/***
	 * @下载文件夹
	 * @param localDirectoryPath本地地址
	 * @param remoteDirectory
	 *            远程文件夹
	 * */
	public boolean downLoadDirectory(String localDirectoryPath,
			String remoteDirectory) {
		try {
			String fileName = new File(remoteDirectory).getName();
			localDirectoryPath = localDirectoryPath + fileName + "//";
			new File(localDirectoryPath).mkdirs();
			FTPFile[] allFile = this.ftpClient.listFiles(remoteDirectory);
			for (int currentFile = 0; currentFile < allFile.length; currentFile++) {
				if (!allFile[currentFile].isDirectory()) {
					downloadFile(allFile[currentFile].getName(),
							localDirectoryPath, remoteDirectory);
				}
			}
			for (int currentFile = 0; currentFile < allFile.length; currentFile++) {
				if (allFile[currentFile].isDirectory()) {
					String strremoteDirectoryPath = remoteDirectory + "/"
							+ allFile[currentFile].getName();
					downLoadDirectory(localDirectoryPath,
							strremoteDirectoryPath);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			log.info("下载文件夹失败");
			return false;
		}
		return true;
	}

	// FtpClient的Set 和 Get 函数
	public FTPClient getFtpClient() {
		return ftpClient;
	}

	public void setFtpClient(FTPClient ftpClient) {
		this.ftpClient = ftpClient;
	}

}
