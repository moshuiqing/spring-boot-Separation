package com.home.lh.other.compression.util;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import com.home.lh.other.ftp.util.Ftp;

/**  
 * 概要说明 : 视频截图工具  <br>
 * 详细说明 : 视频截图工具 <br>
 * 创建时间 : 2018年8月29日 上午9:19:20 <br>
 * @author  by liuhao  
 */
public class Screenshots
{
    /**  
     * 概要说明 : 得到视频截图 <br>
     * 详细说明 : 得到视频截图 <br>
     *
     * @param videoUrl  视频地址
     * @param imgUrl    图片地址
     * @return  boolean 类型返回值说明
     * @see  com.jinge.system.utils.Screenshots#getThumbnai()
     * @author  by liuhao @ 2018年8月29日, 上午9:25:11 
     * @throws IOException 
     */
    public static String getThumbnai(String videoUrl, String imgUrl) throws IOException
    {

        Ftp fftp = new Ftp();
        String rname = UUID.randomUUID().toString() + ".jpg";
        boolean logb = fftp.ftpLogin();
        if (!logb)
        {
            throw new RuntimeException("登录FTP服务失败");
        }
        // fftp.makeDir(imgUrl);

        try
        {

            File file2 = new File(videoUrl);
            if (file2.exists())
            {

                @SuppressWarnings("resource")
				FFmpegFrameGrabber ff = new FFmpegFrameGrabber(file2);
                ff.start();
                int ftp = ff.getLengthInFrames();
                int flag = 0;
                Frame frame = null;
                while (flag <= ftp)
                {
                    // 获取帧
                    frame = ff.grabImage();
                    // 过滤前10帧，避免出现全黑图片
                    if ((flag > 100) && (frame != null))
                    {
                        break;
                    }
                    flag++;
                }

                ByteArrayOutputStream os = new ByteArrayOutputStream();
                ImageIO.write(FrameToBufferedImage(frame), "jpg", os);
                InputStream is = new ByteArrayInputStream(os.toByteArray());
                fftp.uploadFileToFtp(is, rname, imgUrl);
                // ImageIO.write(FrameToBufferedImage(frame), "jpg", targetFile);
                ff.flush();
                ff.stop();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }finally {
        	   fftp.ftpLogOut();
        }
     

        return imgUrl + rname;
    }

    /**  
     * 概要说明 : 取得视频流 <br>
     * 详细说明 : 取得视频流 <br>
     *
     * @param frame 视频
     * @return  RenderedImage 类型返回值说明
     * @see  com.jinge.system.utils.Screenshots#FrameToBufferedImage()
     * @author  by liuhao @ 2018年8月29日, 上午9:24:34 
     */
    private static RenderedImage FrameToBufferedImage(Frame frame)
    {
        // 创建BufferedImage对象
        Java2DFrameConverter converter = new Java2DFrameConverter();
        BufferedImage bufferedImage = converter.getBufferedImage(frame);
        return bufferedImage;
    }

    /**  
     * 概要说明 : 下载视频到服务器 <br>
     * 详细说明 : 下载视频到服务器 <br>
     *
     * @param url     视频地址
     * @param webUrl  服务器地址
     * @param name    视频名称
     * @return  boolean 类型返回值说明
     * @see  com.jinge.system.utils.Screenshots#xiazai()
     * @author  by liuhao @ 2018年8月29日, 下午3:28:53 
     */
    public static boolean xiazai(String url, String webUrl, String name)
    {
        Ftp ftp = new Ftp();
        boolean logb = ftp.ftpLogin();
        if (!logb)
        {
            throw new RuntimeException("登录FTP服务失败");
        }
        boolean flag = ftp.downloadFile(name, webUrl, url);
        ftp.ftpLogOut();
        if (flag)
        {
            return true;
        }
        return false;
    }
    
    
    /**
     * 删除单个文件
     *
     * @param fileName
     *            要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                System.out.println("删除单个文件" + fileName + "成功！");
                return true;
            } else {
                System.out.println("删除单个文件" + fileName + "失败！");
                return false;
            }
        } else {
            System.out.println("删除单个文件失败：" + fileName + "不存在！");
            return false;
        }
    }
    
    
    /**
     * 先根遍历序递归删除文件夹
     *
     * @param dirFile 要被删除的文件或者目录
     * @return 删除成功返回true, 否则返回false
     * @throws IOException 
     */
    public static boolean deleteFiles(String fileName) throws IOException {
       
        File dirFile = new File(fileName);
        // 如果dir对应的文件不存在，则退出
        if (!dirFile.exists()) {
            return false;
        }
        FileUtils.deleteDirectory(dirFile);

        /*if (dirFile.isFile()) {
            return dirFile.delete();
        } else {

            for (File file : dirFile.listFiles()) {
                FileUtils.deleteDirectory(file);
            }
        }*/

        return dirFile.delete();
    }
    
    

}
