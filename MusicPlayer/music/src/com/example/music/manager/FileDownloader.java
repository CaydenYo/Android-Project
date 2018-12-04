package com.example.music.manager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;

public class FileDownloader extends Thread {
	String _urlStr;//Ҫ���ص�·��
	String _dirName;//Ҫ�����Ŀ���ļ���
	Handler _myHandler;
	
	public FileDownloader(String urlStr,String dirName,Handler myHandler)
	{
		_urlStr = urlStr;
		_dirName = dirName;
		_myHandler = myHandler;
	}
	
	@Override
	public void run() {
		//׼��ƴ���µ��ļ����������ڴ洢������ļ�����
		String newFilename = _urlStr.substring(_urlStr.lastIndexOf("/")+1);
		newFilename = _dirName + newFilename;
		File file = new File(newFilename);
		//���Ŀ���ļ��Ѿ����ڣ���ɾ�����������Ǿ��ļ���Ч��
		if(file.exists())
		{
			file.delete();
		}
		try {
			   // ����URL    
			    URL url = new URL(_urlStr);    
			    // ������    
			    URLConnection con = url.openConnection(); 
			    //����ļ��ĳ���
			    int contentLength = con.getContentLength();
			    System.out.println("���� :"+contentLength);
			    // ������    
			    InputStream is = con.getInputStream();   
			  
			    int hasRead = 0;//�Ѿ���ȡ�˶���
			    int progress = 0;
			    
			    // 1K�����ݻ���    
			    byte[] bs = new byte[128];    
			    // ��ȡ�������ݳ���    
			    int len;    
			   // ������ļ���    
			    OutputStream os = new FileOutputStream(newFilename);    
			    // ��ʼ��ȡ    
			    while ((len = is.read(bs)) != -1) {    
			      os.write(bs, 0, len); 
			      
			      //��¼����˵Ķ���
			      hasRead +=len;
			      progress = (int)((double)hasRead/(double)contentLength * 100);//��ɵİٷֱ�
			      //����֪ͨ
			      Message msg = _myHandler.obtainMessage();
			      msg.arg1 = progress;
			      msg.sendToTarget();
			      
			      Thread.sleep(500);//�����ӳ٣���Ȼ�������ܵ�̫�쿴�����
			    }   
			    // ��ϣ��ر���������    
			    os.close();   
			    is.close(); 
			
		} catch (Exception e) {
				e.printStackTrace();
		}

	}
}
