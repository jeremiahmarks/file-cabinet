package com.fileupload.inf;

import java.io.*;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.Character;
import java.lang.String;
import java.lang.System;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.corba.se.spi.orbutil.fsm.Input;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import org.apache.commons.validator.routines.CreditCardValidator;

@Controller
public class FileUploadController {

    private final String CREDIT_CARD_REGEX = "(([0-9]{4})[-|\\s| ]([0-9]{4})[-|\\s| ]([0-9]{4})[-|\\s| ]([0-9]{4}))|(([0-9]{4})[-|\\s| ]([0-9]{6})[-|\\s| ]([0-9]{5}))";
    private final String SOCIAL_SECURITY_REGEX = "^(?!000|666)[0-8][0-9]{2}-(?!00)[0-9]{2}-(?!0000)[0-9]{4}$";
    private final Pattern creditCardPattern = Pattern.compile(CREDIT_CARD_REGEX);
    private final Pattern socialSecurityPattern = Pattern.compile(SOCIAL_SECURITY_REGEX);

    @RequestMapping(value = "/failedupload", method = RequestMethod.GET)
    public String failedpage() {
        return "failedpage";
    }

    private void foundBadData(MultipartFile file) {
        InputStream inputStream = new ByteArrayInputStream(file.getBytes());
        Scanner scan = new Scanner(inputStream, "UTF-8");

        boolean hasCreditCardInfo = false;
        boolean hasSSN = false;
        String line;
        Matcher creditCardMatcher;
        Matcher socialSecurityMatcher;

        while ((!hasCreditCardInfo || !hasSSN) && scan.hasNextLine()) {
            line = scan.nextLine();
            //System.out.println(line); //verify line data visually
            creditCardMatcher = creditCardPattern.matcher(line);
            socialSecurityMatcher = socialSecurityPattern.matcher(line);
            if (!hasCreditCardInfo && creditCardMatcher.find()) {
                hasCreditCardInfo = true;
                url = "failedpage";
                //add message condition for type of data that is passed in
                //add multiple file upload
            }
            if (!hasSSN && socialSecurityMatcher.find()) {
                hasSSN = true;
                url = "failedpage";
            }
        }
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String handleFileUpload(@RequestParam("appname") String appname,
                            @RequestParam("file") MultipartFile[] files) {
        String result = "error";
        for (MultipartFile file : files) {

            String url = "";
            if (!file.isEmpty()) {
                //String dateUploadedStamp =
                //        new java.text.SimpleDateFormat("yyyy-MM-dd-hh-mm-ssa").format(new Date()); //Date stamp
                String uploadedFile = file.getOriginalFilename(); //Get file name
                try {
                    foundBadData(file);
                    if (hasCreditCardInfo == false && hasSSN == false) {
                        String storage = System.getProperty("user.home");
                        File fileNew = new File(storage + "\\customerdata\\" + appname + "_" + uploadedFile);
                        BufferedOutputStream stream =
                                new BufferedOutputStream(new FileOutputStream(fileNew));
                        if (!fileNew.exists()) {
                            stream.write(inputStream.read());
                            stream.close();
                        }
                        result = "success";
                    }
                } catch (Exception e) {
                    System.out.println(e);
                    result = "error";
                }
            } else {
                result = "failedpage";
            }
            result = url;
        }
    }
    return result;
}