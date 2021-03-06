package org.telosys.tools.commons.io;

import java.io.File;
import java.io.IOException;

import junit.env.telosys.tools.commons.TestsEnv;
import junit.framework.TestCase;

import org.telosys.tools.commons.io.DefaultOverwriteChooser;
import org.telosys.tools.commons.io.OverwriteChooser;
import org.telosys.tools.commons.io.ResourcesCopier;

public class ResourcesCopierTest extends TestCase {

	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
//		System.out.println("===== [ SETUP ] =====");
//		System.out.println("Loading configuration from folder : " + TestsFolders.getTestsRootFolder() );
	}

	//----------- File to File ------------------
	public void testCopyFileToFile1()  {
		int n = copy (	getOriginFile("file1.txt"), 
						getDestinationFile("file1-copy.txt"), 
						OverwriteChooser.YES);
		assertEquals(1, n);
	}

	public void testCopyFileToFile2() {
		int n = copy (	getOriginFile("file1.txt"), 
						getDestinationFile("file1-copy.txt"), 
						OverwriteChooser.NO);
		assertEquals(0, n);
	}

	//----------- Folder to Folder ------------------
	public void testCopyFolderToFolder1() {
		int n = copy (	getOriginFolder("foo"), 
						getExistingDestinationFolder("foo2"), 
						OverwriteChooser.YES);
		assertTrue(n > 0 );
	}
	
	public void testCopyFolderToFolder2()  {
		int n = copy (	getOriginFolder("foo"), 
						getExistingDestinationFolder("foo3"), 
						OverwriteChooser.YES);
		assertTrue(n > 0 );
	}

	public void testCopyFolderToFolder3() {
		int n = copy (	getOriginFolder("foo/bar"), 
						getExistingDestinationFolder("foo-bar"), 
						OverwriteChooser.YES);
		assertTrue(n > 0 );
	}
	
	public void testCopyFolderToFolder4()  {
		int n = copy (	getOriginFolder("foo/bar"), 
						getExistingDestinationFolder("foo-bar"), 
						OverwriteChooser.NO);
		assertTrue(n == 0 );
	}
	
	//----------- File to Folder ------------------
	public void testCopyFileToFolder1()  {
		createFileIfNotExists(getDestinationFile("mydir/fileA.txt"));
		int n = copy (	getOriginFile("foo/fileA.txt"), 
						getExistingDestinationFolder("mydir"), 
						OverwriteChooser.YES);
		assertTrue(n == 1 ); // 1 because "overwrite"
	}
	public void testCopyFileToFolder1bis() {
		createFileIfNotExists(getDestinationFile("mydir/fileA.txt"));
		int n = copy (	getOriginFile("foo/fileA.txt"), 
						getExistingDestinationFolder("mydir"), 
						OverwriteChooser.NO);
		assertTrue(n == 0 ); // 0 because "do not overwrite"
	}
	
	public void testCopyFileToFolder2()  {
		createFileIfNotExists(getDestinationFile("mydir/dest-A/fileA.txt"));
		int n = copy (	getOriginFile("foo/fileA.txt"), 
						getExistingDestinationFolder("mydir/dest-A"), 
						OverwriteChooser.YES);
		assertTrue(n == 1 ); // 1 because "overwrite"
	}
	public void testCopyFileToFolder2bis() {
		createFileIfNotExists(getDestinationFile("mydir/dest-A/fileA.txt"));
		int n = copy (	getOriginFile("foo/fileA.txt"), 
						getExistingDestinationFolder("mydir/dest-A"), 
						OverwriteChooser.NO);
		assertTrue(n == 0 ); // 0 because "do not overwrite" 
	}
	
	//----------------------------------------------
	private File getOriginFile(String fileOrFolderName ) {
		//return new File(TestsFolders.getFullFileName("resources-origin/" + fileOrFolderName));
		//return new File("src/test/resources/resources-origin/" + fileOrFolderName );
		return TestsEnv.getTestFile("resources-origin/" +fileOrFolderName);
	}
	
	private File getOriginFolder(String folderName ) {
		//return new File(TestsFolders.getFullFileName("resources-origin/" + fileOrFolderName));
		//return new File("src/test/resources/resources-origin/" + fileOrFolderName );
		return TestsEnv.getTestFolder("resources-origin/" +folderName);
	}
	
	private File getDestinationFile(String fileOrFolderName ) {
		return new File("target/tests-tmp/resources-destination/" + fileOrFolderName);
	}
	
	private File getExistingDestinationFolder(String folderName ) {
		return TestsEnv.getTmpExistingFolder("/resources-destination/" + folderName);
	}
	
	/**
	 * Creates a void file if it doesn't exist (in order to be sure this file exists)
	 */
	private void createFileIfNotExists(File file ) {
		System.out.println("createFileIfNotExists( " + file.getAbsolutePath() + ")" );
		if ( ! file.exists() ) {
			System.out.println("Creating file " + file.getAbsolutePath() );
			//(works for both Windows and Linux)
			file.getParentFile().mkdirs(); 
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException("Cannot create file " + file.getAbsolutePath() );
			}
			System.out.println("File " + file.getAbsolutePath() + " created." );
		}
	}
	
	private int copy(File source, File destination, int choice ) {
		int n = 0 ;
		System.out.println("===== COPY ");
		System.out.println("  from : " + source  );
		System.out.println("  to   : " + destination );
		System.out.println("  choice = " + choice );
		ResourcesCopier copier = new ResourcesCopier(new DefaultOverwriteChooser(choice), new CopyHandlerLogger() );
		try {
			n = copier.copy(source, destination);
			System.out.println(n + " file(s) copied");
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Cannot copy file");
		}
		return n ;
	}
	
}
