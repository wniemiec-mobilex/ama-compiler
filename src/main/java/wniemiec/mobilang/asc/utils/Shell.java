package wniemiec.mobilang.asc.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;

public class Shell {

    private Path workingDirectory;
    private String output = "";
    private String errorOutput = "";

    public Shell() {
        this(Path.of(System.getProperty("user.dir")));
    }

    public Shell(Path workingDirectory) {
        this.workingDirectory = workingDirectory;
    }

    public void exec(String... commands) throws IOException {
        //System.out.println(System.getProperty("user.dir"));
        //ProcessBuilder pb = new ProcessBuilder(commands);
        //Process process = pb.start();
        StringBuilder parsedCommand = new StringBuilder();

        for (String command : commands) {
            parsedCommand.append(command);
            parsedCommand.append(File.pathSeparatorChar);
        }

        if (parsedCommand.length() > 0) {
            parsedCommand.deleteCharAt(parsedCommand.length()-1);
        }

        System.out.println(parsedCommand.toString());
        
        Process process = Runtime.getRuntime().exec(parsedCommand.toString(), null, workingDirectory.toFile());
        
        //ProcessBuilder pb = new ProcessBuilder(commands);
        //pb.directory(workingDirectory.toAbsolutePath().toFile());
        //Process process = pb.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        StringBuilder builder = new StringBuilder();
        StringBuilder errorBuilder = new StringBuilder();
        
        String line = null;
        
        while ( (line = reader.readLine()) != null) {
            builder.append(line);
            builder.append(System.getProperty("line.separator"));
        }
        String errorLine = null;

        while ( (errorLine = errorReader.readLine()) != null) {
            errorBuilder.append(errorLine);
            errorBuilder.append(System.getProperty("line.separator"));
        }

        output = builder.toString();
        errorOutput = errorBuilder.toString(); 
    }

    public String getOutput() {
        return output;
    }

    public String getErrorOutput() {
        return errorOutput;
    }

    public boolean hasError() {
        return !errorOutput.isBlank();
    }
}
