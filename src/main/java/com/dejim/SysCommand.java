package com.dejim;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.charset.StandardCharsets;

public class SysCommand {

	public SysCommand() {

	}

	boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");

	public static Process process;

	public String process(String command, String path) throws IOException {
		StringBuffer response = new StringBuffer();
		String line;

		System.out.print(command);
		if (command.equals("azure")) {
			Path wrapperPath = Paths.get("/opt/mule/mule-4.3.0/conf/wrapper.conf");
	        appendToFile(wrapperPath, "wrapper.java.additional.101=-javaagent:" + path + "applicationinsights-agent-3.0.2.jar");
	        command = "/opt/mule/mule-4.3.0/bin/mule restart";
		}

		try {
			if (isWindows) {
				process = Runtime.getRuntime().exec(String.format(command));
			} else {
				process = Runtime.getRuntime().exec(String.format(command));
			}
			process.waitFor();

			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			BufferedReader error = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			while ((line = reader.readLine()) != null) {
				response.append(line + "\n");
				System.out.print(line + "\n");
			}
			while ((line = error.readLine()) != null) {
				response.append(line + "\n");
				System.out.print(line + "\n");
			}

			process.destroy();
			process.getInputStream().close();
			process.getOutputStream().close();
			process.getErrorStream().close();

		} catch (IOException e1) {
		} catch (InterruptedException e2) {
		}

		return response.toString();
	}

	public String stopProcess(String command) throws IOException {
		process.destroy();
		process.getInputStream().close();
		process.getOutputStream().close();
		process.getErrorStream().close();
		return "Repose Stopped";
	}

	private static void appendToFile(Path path, String content) throws IOException {

		Files.write(path, content.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE,
				StandardOpenOption.APPEND);

	}
}
