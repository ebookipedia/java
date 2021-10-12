package com.softalks.ebookipedia;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class Output {

	private Output() {
		// SONAR mandatory
	}
	
	private static final File IO = new File("out");
	
	public static void url(String content) {
		print(new File(IO, content), content);
	}
	
	private static void print(File file, String content) {
		file.mkdirs();
		try (OutputStream out = new FileOutputStream(file)) {
			out.write(content.getBytes(StandardCharsets.UTF_8));
		} catch (IOException e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}
	
}
