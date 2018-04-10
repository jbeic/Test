package com.jbeic.tinify;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import com.tinify.Source;
import com.tinify.Tinify;



public class JTinify {

	public static void main(String[] args) {
		String key = "IIb1kj40rhodJZKq5ZMIS1tSXCgKtGdO";
		String spath = "D:\\a.png";
		String topath = "D:\\b.png";
		/*if (JTinify.toFile(key, spath, topath, true)) {
			System.err.println("ѹ���ɹ�");
		} else {
			System.err.println("ѹ��ʧ��");
		}*/
		if (JTinify.toFile(key, spath, topath, 1,true)) {
			System.err.println("ѹ���ɹ�");
		} else {
			System.err.println("ѹ��ʧ��");
		}
	}

	/**
	 * ѹ��һ��ͼƬ�ļ�
	 * 
	 * @param key
	 *            Tinify ��ȨKEY
	 * @param spath
	 *            Դ�ļ���ַ
	 * @param topath
	 *            Ŀ���ļ���ַ
	 * @param showMsg
	 *            �Ƿ���ʾ��ϸ��Ϣ
	 * @return boolean �Ƿ�ѹ���ɹ�
	 */
	public static boolean toFile(String key, String spath, String topath, Boolean showMsg) {

		try {
			long sSize = 0;
			long toSize = 0;
			Tinify.setKey(key);
			if (showMsg) {
				if (Tinify.validate()) {
					msg(showMsg, "KEY��֤�ɹ�", true);
				} else {
					msg(showMsg, "KEY��֤ʧ��", false);
					return false;
				}
			}
			File sFile = new File(spath);
			if (sFile.exists()) {
				sSize = sFile.length();
				msg(showMsg, "Դ�ļ���С��" + toformat(1.0 * sSize / 1024) + "KB", true);
			} else {
				msg(showMsg, "Դ�ļ�������", false);
				return false;
			}

			Source source = Tinify.fromFile(spath);
			source.toFile(topath);

			File toFile = new File(topath);
			if (toFile.exists()) {
				toSize = toFile.length();
				msg(showMsg, "Ŀ���ļ���С��" + toformat(1.0 * toSize / 1024) + "KB", true);
			} else {
				msg(showMsg, "Ŀ���ļ�������", false);
				return false;
			}
			msg(showMsg, "ѹ���ʣ�" + toformat(1.0 * (sSize - toSize) / sSize * 100) + "%", true);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * ���ѹ��ͼƬ�ļ�����ͼƬ������ѹ����С
	 * 
	 * @param key
	 *            Tinify ��ȨKEY
	 * @param spath
	 *            Դ�ļ���ַ
	 * @param topath
	 *            Ŀ���ļ���ַ
	 * @param showMsg
	 *            �Ƿ���ʾ��ϸ��Ϣ
	 * @param pzip
	 *            ѹ���Ƚ�С�ڴ�ֵʱ���ٽ���ѹ�� 0~100
	 * @return boolean �Ƿ�ѹ���ɹ�
	 */
	public static boolean toFile(String key, String spath, String topath, int pzip, Boolean showMsg) {

		try {
			long ySize =-1;//��¼ԭʼ��С
			long sSize = 0;//ѹ��ԭʼ��С
			long toSize = 0;//ѹ�����С
			double npzip = 100;
			Tinify.setKey(key);
			if (showMsg) {
				if (Tinify.validate()) {
					msg(showMsg, "KEY��֤�ɹ�", true);
				} else {
					msg(showMsg, "KEY��֤ʧ��", false);
					return false;
				}
			}
			int count=1;
			while (npzip > pzip) {
				msg(showMsg, "***��"+count+"��ѹ��***", false);
				File sFile = new File(spath);
				if (sFile.exists()) {
					sSize = sFile.length();
					//��¼ԭʼ��С
					if(ySize<0){
						ySize=sSize;
					}
					msg(showMsg, "Դ�ļ���С��" + toformat(1.0 * sSize / 1024) + "KB", true);
				} else {
					msg(showMsg, "Դ�ļ�������", false);
					return false;
				}

				Source source = Tinify.fromFile(spath);
				source.toFile(topath);

				File toFile = new File(topath);
				if (toFile.exists()) {
					toSize = toFile.length();
					msg(showMsg, "Ŀ���ļ���С��" + toformat(1.0 * toSize / 1024) + "KB", true);
				} else {
					msg(showMsg, "Ŀ���ļ�������", false);
					return false;
				}
				spath=topath;
				npzip=1.0 * (sSize - toSize) / sSize * 100;
				msg(showMsg, "ѹ���ʣ�" + toformat(npzip) + "%", true);
				count++;
				Thread.sleep(100);
			}
			npzip=1.0 * (ySize - toSize) / ySize * 100;
			msg(showMsg, "*��ѹ���ʣ�" + toformat(npzip) + "%", true);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static void msg(boolean showMsg, String msg, boolean isError) {
		if (showMsg) {
			if (isError) {
				System.out.println(msg);
			} else {
				System.err.println(msg);
			}
		}
	}

	public static String toformat(double num) {
		DecimalFormat df = new DecimalFormat("#.00");
		return df.format(num);
	}

}
