package com.adeskmath.backend.shop.utilities;

import com.adeskmath.backend.shop.service.FileService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class CLIOperator implements ApplicationRunner {

    public CLIOperator(FileService fileService) {
        this.fileService = fileService;
    }

    private final FileService fileService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String[] params;
        if (args.getSourceArgs().length != 0) {
            params = args.getSourceArgs();
        } else return;
        if (params.length != 3 || params[1].equals(params[2])) {
            System.out.println("error: wrong or missed parameters");
            return;
        }
        String inputFile = params[1];
        String outputFile = params[2];
        if (params[0].equals("search")) {
            fileService.search(inputFile, outputFile);
            return;
        }
        if (params[0].equals("stat")) {
            fileService.stat(inputFile, outputFile);
            return;
        }
        System.out.println("error: 1st parameter must be 'search' or 'stat'");
    }

}
