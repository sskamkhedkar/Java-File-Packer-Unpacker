import java.io.*;
import java.util.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;

class Main
{
	public static void main(String args[])
	{
		Scanner sobj = new Scanner(System.in);
		while(true)
		{
		System.out.println("--------------------------------");
		System.out.println("Enter your choice:");
		System.out.println("1. Packing");
		System.out.println("2. Unpacking");
		System.out.println("3. Exit");
		System.out.println("--------------------------------");
		
		String Dir,FileName;
		int choice = 0;
		choice = sobj.nextInt();
		switch(choice)
		{
			case 1:
				System.out.println("Enter directory name:");
				Dir = sobj.next();
				
				System.out.println("Enter file name:");
				FileName = sobj.next();
				Packer pobj = new Packer(Dir,FileName);
				break;
			
			case 2:
				System.out.println("Enter packed file name");
				String fname = sobj.next();
				Unpacker obj = new Unpacker(fname);
				break;
				
			case 3:
				System.out.println("--------------------------------");
				System.out.println("Thank you for using packer unpacker application...");
				System.out.println("--------------------------------");
				System.exit(0);
				break;
				
			default:
				System.out.println("Wrong choice");
		}
		}
	}
	
}

class Packer
{
	//reference to write in output stream - characteristic
	public FileOutputStream outstream = null;
	//parameterised constructor - create file inside constructor
	public Packer(String FolderName, String FileName)
	{
		try
		{
			System.out.println("Inside packer constructor");
			//creates new file for packing
			File outfile = new File(FileName);
			outstream = new FileOutputStream(FileName);
			
			//Set the current working directory for folder traversal
			System.setProperty("user.dir",FolderName);
			
			TravelDirectory(FolderName);
		}
		catch(Exception obj)
		{
			System.out.println(obj);
		}
	}
	
	public void TravelDirectory(String path)
	{
		File directoryPath = new File(path);
		
		//get all file names from directory
		File arr[] = directoryPath.listFiles();
		
		for(File filename : arr)
		{
			//System.out.println(filename.getName());
			//System.out.println(filename.getAbsolutePath());
			if(filename.getName().endsWith(".txt"))
			{
				PackFile(filename.getAbsolutePath());
			}
		}
	}
	
	public void PackFile(String FilePath)
	{
		//packing logic
		System.out.println("FileName received: "+FilePath);
		byte Header[] = new byte[100];
		byte Buffer[] = new byte[1024];
		int length = 0;
		
		
		FileInputStream istream = null;
		
		File fobj = null;
		fobj = new File(FilePath);
		
		String temp = FilePath+" "+fobj.length();
		
		System.out.println("Header: "+temp.length());
		
		//create header of 100 bytes
		for(int i=temp.length();i<100;i++)
		{
			temp = temp + " ";
		}
		System.out.println("Header: "+temp.length());
		//convert string (array of characters) to bytes
		Header = temp.getBytes();
		
		try
		{
			//open file for reading
			istream = new FileInputStream(FilePath);
			
			outstream.write(Header,0,Header.length);
			while((length = istream.read(Buffer)) > 0)
			{
				outstream.write(Buffer,0,length);
			}
			
			istream.close();
		}
		catch(Exception obj)
		{}
		
		//System.out.println("Header: "+temp.length());
		
	}
}

//class
class Unpacker
{
	//object to write
	public FileOutputStream outstream = null;
	//constructor
	public Unpacker(String src)
	{
		//System.out.println("Inside unpacker");
		unpackFile(src);
	}
	
	public void unpackFile(String FilePath)
	{
		try
		{
			//file open
			FileInputStream instream = new FileInputStream(FilePath);
			//local variables
			byte Header[] = new byte[100];
			int length = 0;
			int counter = 0;
		
			while((length = instream.read(Header,0,100)) > 0)
			{
				//convert byte to string
				String str = new String(Header);
				System.out.println(str);
				//extract last extension
				String ext = str.substring(str.lastIndexOf("\\"));
				//get file name - remove first letter
				ext = ext.substring(1);
				
				//word[0] - filename, word[1] - file length
				//handle space
				String words[] = ext.split("\\s");
				String name = words[0];
				//convert file length to int
				int size = Integer.parseInt(words[1]);
				
				//create array of bytes of size = file size
				byte arr[] = new byte[size];
				instream.read(arr,0,size);
				
				//new file gets created
				FileOutputStream fout = new FileOutputStream(name);
				//write data into newly created file
				fout.write(arr,0,size);
				counter++;
			}
			
			System.out.println("Successfully unpacked files: "+counter);
			//instream.read(Header,0,100);
			//System.out.println(Header.toString());
		}
		catch(Exception obj)
		{}
	}
}
