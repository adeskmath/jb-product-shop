/*** Example with CommandLineRunner & ApplicationContext
 * I used ApplicationRunner instead (CLIOperator.java)
package com.adeskmath.backend.shop;


import com.adeskmath.backend.shop.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class CLIApplication implements CommandLineRunner {

	private static ApplicationContext context;

	@Autowired
	public void setContext(ApplicationContext context) {
		this.context = context;
	}
	public static void main(String[] args) {
		SpringApplication.run(CLIApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		if (args.length != 3 || args[1].equals(args[2])) {
			System.out.println("error: wrong or missed parameters");
			return;
		}
		String operation = args[0];
		String inputFile = args[1];
		String outputFile = args[2];

		FileService fileService = context.getBean(FileService.class);

		switch (operation) {
			case "search":
				fileService.search(inputFile, outputFile);
				break;

			case "stat":
				fileService.stat(inputFile, outputFile);
				break;

			default:
				System.out.println("error: 1st parameter must be 'search' or 'stat'");
		}
	}
}
*/
