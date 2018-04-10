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
			System.err.println("压缩成功");
		} else {
			System.err.println("压缩失败");
		}*/
		if (JTinify.toFile(key, spath, topath, 1,true)) {
			System.err.println("压缩成功");
		} else {
			System.err.println("压缩失败");
		}
	}

	/**
	 * 压缩一次图片文件
	 * 
	 * @param key
	 *            Tinify 授权KEY
	 * @param spath
	 *            源文件地址
	 * @param topath
	 *            目标文件地址
	 * @param showMsg
	 *            是否显示详细信息
	 * @return boolean 是否压缩成功
	 */
	public static boolean toFile(String key, String spath, String topath, Boolean showMsg) {

		try {
			long sSize = 0;
			long toSize = 0;
			Tinify.setKey(key);
			if (showMsg) {
				if (Tinify.validate()) {
					msg(showMsg, "KEY验证成功", true);
				} else {
					msg(showMsg, "KEY验证失败", false);
					return false;
				}
			}
			File sFile = new File(spath);
			if (sFile.exists()) {
				sSize = sFile.length();
				msg(showMsg, "源文件大小：" + toformat(1.0 * sSize / 1024) + "KB", true);
			} else {
				msg(showMsg, "源文件不存在", false);
				return false;
			}

			Source source = Tinify.fromFile(spath);
			source.toFile(topath);

			File toFile = new File(topath);
			if (toFile.exists()) {
				toSize = toFile.length();
				msg(showMsg, "目标文件大小：" + toformat(1.0 * toSize / 1024) + "KB", true);
			} else {
				msg(showMsg, "目标文件不存在", false);
				return false;
			}
			msg(showMsg, "压缩率：" + toformat(1.0 * (sSize - toSize) / sSize * 100) + "%", true);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 多次压缩图片文件，将图片尽可能压到最小
	 * 
	 * @param key
	 *            Tinify 授权KEY
	 * @param spath
	 *            源文件地址
	 * @param topath
	 *            目标文件地址
	 * @param showMsg
	 *            是否显示详细信息
	 * @param pzip
	 *            压缩比较小于此值时不再进行压缩 0~100
	 * @return boolean 是否压缩成功
	 */
	public static boolean toFile(String key, String spath, String topath, int pzip, Boolean showMsg) {

		try {
			long ySize =-1;//记录原始大小
			long sSize = 0;//压缩原始大小
			long toSize = 0;//压缩后大小
			double npzip = 100;
			Tinify.setKey(key);
			if (showMsg) {
				if (Tinify.validate()) {
					msg(showMsg, "KEY验证成功", true);
				} else {
					msg(showMsg, "KEY验证失败", false);
					return false;
				}
			}
			int count=1;
			while (npzip > pzip) {
				msg(showMsg, "***第"+count+"次压缩***", false);
				File sFile = new File(spath);
				if (sFile.exists()) {
					sSize = sFile.length();
					//记录原始大小
					if(ySize<0){
						ySize=sSize;
					}
					msg(showMsg, "源文件大小：" + toformat(1.0 * sSize / 1024) + "KB", true);
				} else {
					msg(showMsg, "源文件不存在", false);
					return false;
				}

				Source source = Tinify.fromFile(spath);
				source.toFile(topath);

				File toFile = new File(topath);
				if (toFile.exists()) {
					toSize = toFile.length();
					msg(showMsg, "目标文件大小：" + toformat(1.0 * toSize / 1024) + "KB", true);
				} else {
					msg(showMsg, "目标文件不存在", false);
					return false;
				}
				spath=topath;
				npzip=1.0 * (sSize - toSize) / sSize * 100;
				msg(showMsg, "压缩率：" + toformat(npzip) + "%", true);
				count++;
				Thread.sleep(100);
			}
			npzip=1.0 * (ySize - toSize) / ySize * 100;
			msg(showMsg, "*总压缩率：" + toformat(npzip) + "%", true);
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
