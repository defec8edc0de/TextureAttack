package de.tud.textureAttack.model.io;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import util.FileUtil;
import util.ImageIOUtils;

public class IOUtils {

    /**
     * Return list of all files in directory
     * 
     * @param dir starting directory
     * @return list of File in dir (not including dir)
     */
    public static List<File> directoryList (File dir)
    {
    	List<File> list = new ArrayList<File>();
    	if (dir.isFile())
    		list.add(dir);
    	else list = Arrays.asList(dir.listFiles());
        return list;
    }


    /**
     * Recursively descends directory and returns a list of all 
     * files reachable from directory dir
     * 
     * @param dir starting directory
     * @return list of all files in the the folder dir
     */
    public static List<File> recursiveList (File dir)
    {
        List<File> results = new ArrayList<File>();
        List<File> files = directoryList(dir);
        for (File current : files)
        {
        	if (current.isFile()){
        		if (isFileTypeSupported(current))
        		results.add(current);
        	}
        	else {
        		List<File> subdirs = recursiveList(current);
        		for (File file : subdirs){
        			results.add(file);
        		}
        		
        	}
        }
        return results;
    }
	
    
	public static String getFileNameFromPath(String path) {
		StringBuilder result = new StringBuilder(path);
		result.delete(0,
				result.lastIndexOf(System.getProperty("file.separator")) + 1);
		return result.toString();
	}

	public static boolean isFileTypeSupported(final File file) {
//		if (ImageIOUtils.isImageIOSupported(file)
//				|| FileUtil.getFileSuffix(file).contains("tex")
//				|| FileUtil.getFileSuffix(file).contains("tga"))
//			return true;
//
//		return false;
		if (FileUtil.getFileSuffix(file).contains("dds"))
			return true;
			else return false;
		
	}


	public static File[] getAllTextureFiles(File[] selectedFiles) {
		List<File> result = new ArrayList<File>();
		for (int i=0;i<selectedFiles.length;i++){
			result.addAll(recursiveList (selectedFiles[i]));
		}
		File[] files = new File[result.size()];
		for (int i = 0; i < result.size();i++){
			files[i] = result.get(i);
		}
		return files;
	}

	
	/**
	 * returns the base dir of the given files/folders
	 * @param selectedFiles
	 * @return
	 */
	public static String getBaseDir(File[] selectedFiles) {
		String baseDir = "";
		if (selectedFiles[0] != null){
			baseDir = selectedFiles[0].getParent();
		}
			return baseDir == null  ? "" : baseDir;
	}
	
}
