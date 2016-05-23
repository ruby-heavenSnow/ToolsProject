/**
 * @Description: TODO
 * @author: {Zhou Haitao.  }
 * @version: 1.0
 * @see 
 */

package net.iaf.framework.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * 文件处理的工具类
 * @author Bob
 *
 */
public class FileUtils {

	/**
	 * 
	 * Deletes all files and subdirectories under "dir".
	 * 
	 * @param dir
	 *            Directory to be deleted
	 * 
	 * @return boolean Returns "true" if all deletions were successful.
	 * 
	 *         If a deletion fails, the method stops attempting to
	 * 
	 *         delete and returns "false".
	 */
	public static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			if (children.length == 0) {
				return dir.delete();
			}
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		// The directory is now empty so now it can be smoked
		return dir.delete();

	}

	/**
	 * 获取文件或文件夹的大小
	 * 
	 * @param file 文件或文件夹
	 * @return  单位byte
	 */
	public static long getFileSize(File file) {
		long size = 0;
		File flist[] = file.listFiles();
		if (flist != null) {
			for (int i = 0; i < flist.length; i++) {
				if (flist[i].isDirectory()) {
					size = size + getFileSize(flist[i]);
				} else {
					size = size + flist[i].length();
				}
			}
		}
		return size;
	}

	/**
	 * 返回sd卡根路径，如果没有sd卡则返回cache的根路径
	 * @param context 当前上下文
	 * @return
	 */
	public static String getRoot(Context context)
	{
		String root = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())?
				Environment.getExternalStorageDirectory().getPath():context.getCacheDir().getPath();
		return root;
	}

	/**
	 *
	 * @param dir 子路径
	 * @param parent 根路径
	 * @return 得到缓存路径下的子路径
	 */
	public static File getCacheFile(Context context,String parent,String dir)
	{
		File file = new File(getRoot(context), parent);
		if(!file.exists())
		{
			file.mkdir();
		}
		File subFile = new File(file,dir);
		if(!subFile.exists())
		{
			subFile.mkdir();
		}
		return subFile;
	}

	/**
	 *
	 * @param filePath  要删除的文件路径
	 */
	public static void deleteFile(String filePath) {
		File file = new File(filePath);
		if(file != null && file.exists()) {
			file.delete();
		}
	}


	
}
