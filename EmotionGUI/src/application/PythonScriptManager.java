package application;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class PythonScriptManager{

	private BufferedReader pythonScriptReader;
	private StringBuffer inputBuffer;
	private String line;
	private String pythonScript;
	
	//Constructor for the PythonScriptManager class
	public PythonScriptManager(String pythonScript) {
		this.pythonScript = pythonScript;
	}
	
	//Method to load the Python Script
	public void loadPythonScript(String pythonScript) {
		this.pythonScript = pythonScript;
	}
	
	//Method for changing a Python script
	public void changePythonScript(String currentLine, String newLine) throws IOException {
	pythonScriptReader = new BufferedReader(new FileReader(this.pythonScript));
	inputBuffer = new StringBuffer();
	//Replace the currentLine being search for with newLine
	searchAndReplaceInScript(pythonScriptReader,currentLine,newLine);
	//Write to the Python Script
	writeToFile();
	}
	
	//Method to write lines to a Python script
	public void writeToFile() throws IOException {
		FileOutputStream writeToPythonFile = new FileOutputStream(this.pythonScript);
		writeToPythonFile.write(inputBuffer.toString().getBytes());
		writeToPythonFile.close();
	}
	
	//Method to search and replace a line in a Python script
	public void searchAndReplaceInScript(BufferedReader pythonScriptReader, String currentLine, String newLine) throws IOException {
		//Reads through the entire Python script
		while((line = pythonScriptReader.readLine()) != null) {
			//When a line in the Python script matches currentLine it is replaced with newLine
			if(line.contains(currentLine)) {
				line = newLine;
			}
			//Rewrites the same line in the script if currentLine is not found.
			inputBuffer.append(line);
			inputBuffer.append('\n');
		}
		pythonScriptReader.close();
	}
	
}
	
