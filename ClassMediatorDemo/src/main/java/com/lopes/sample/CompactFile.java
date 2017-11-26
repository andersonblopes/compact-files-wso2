package com.lopes.sample;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.mediators.AbstractMediator;

public class CompactFile extends AbstractMediator {

	Log log = LogFactory.getLog(CompactFile.class);

	private String homeDirectory;
	private String destinationDirectory;
	private String fileName;

	@Override
	public boolean mediate(MessageContext context) {
		compactFiles(context);
		return false;
	}

	private void compactFiles(MessageContext context) {
		try {
			log.debug("\nVALUES OF CONTEXT \n - Home Directory: " + getHomeDirectory() + "\n- Destination Directory: "
					+ getDestinationDirectory() + "\n- File name: " + getFileName());

			if (fileName == null || fileName.equals("")) {
				zipFolder(getHomeDirectory(), getDestinationDirectory() + ".zip");
			} else {
				zipFolder(getHomeDirectory() + File.separator + getFileName(),
						getDestinationDirectory() + File.separator + getFileName());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static public void zipFolder(String srcFolder, String destZipFile) throws Exception {
		ZipOutputStream zip = null;
		FileOutputStream fileWriter = null;

		fileWriter = new FileOutputStream(destZipFile);
		zip = new ZipOutputStream(fileWriter);

		addFolderToZip("", srcFolder, zip);
		zip.flush();
		zip.close();
	}

	static private void addFileToZip(String path, String srcFile, ZipOutputStream zip) throws Exception {

		File folder = new File(srcFile);
		if (folder.isDirectory()) {
			addFolderToZip(path, srcFile, zip);
		} else {
			byte[] buf = new byte[1024];
			int len;
			FileInputStream in = new FileInputStream(srcFile);
			zip.putNextEntry(new ZipEntry(path + File.separator + folder.getName()));
			while ((len = in.read(buf)) > 0) {
				zip.write(buf, 0, len);
			}
		}
	}

	static private void addFolderToZip(String path, String srcFolder, ZipOutputStream zip) throws Exception {
		File folder = new File(srcFolder);

		for (String fileName : folder.list()) {
			if (path.equals("")) {
				addFileToZip(folder.getName(), srcFolder + File.separator + fileName, zip);
			} else {
				addFileToZip(path + File.separator + folder.getName(), srcFolder + File.separator + fileName, zip);
			}
		}
	}

	public String getHomeDirectory() {
		return homeDirectory;
	}

	public void setHomeDirectory(String homeDirectory) {
		this.homeDirectory = homeDirectory;
	}

	public String getDestinationDirectory() {
		return destinationDirectory;
	}

	public void setDestinationDirectory(String destinationDirectory) {
		this.destinationDirectory = destinationDirectory;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
