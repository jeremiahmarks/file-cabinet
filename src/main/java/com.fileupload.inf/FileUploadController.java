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

    /*@RequestMapping(value="/upload", method=RequestMethod.GET)
    public @ResponseBody String provideUploadInfo() {
        return "You can upload a file by posting to this same URL.";
    }

    @RequestMapping(value="/reupload", method=RequestMethod.GET)
    public String index() {
        return "index";
    }
*/
    @RequestMapping(value = "/failedupload", method = RequestMethod.GET)
    public String failedpage() {
        return "failedpage";
    }

    //@RequestMapping(value="/fail", method=RequestMethod.GET)
    //public String uploadfailed() {
    //     return "redirect:failpage.html";
    // }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public
    String handleFileUpload(@RequestParam("appname") String appname,
                            @RequestParam("file") MultipartFile file) {
        String url = "";
        if (!file.isEmpty()) {
            String dateUploadedStamp =
                    new java.text.SimpleDateFormat("yyyy-MM-dd-hh-mm-ssa").format(new Date()); //Date stamp
            String uploadedFile = file.getOriginalFilename(); //Get file name
            try {
                InputStream inputStream = new ByteArrayInputStream(file.getBytes());
                Scanner scan = new Scanner(inputStream, "UTF-8");
                Pattern regccnum = Pattern.compile("(([0-9]{4})[-|\\s| ]([0-9]{4})[-|\\s| ]([0-9]{4})[-|\\s| ]([0-9]{4}))|(([0-9]{4})[-|\\s| ]([0-9]{6})[-|\\s| ]([0-9]{5}))");
                Pattern regssn = Pattern.compile("^(?!000|666)[0-8][0-9]{2}-(?!00)[0-9]{2}-(?!0000)[0-9]{4}$");
                boolean hasCreditCardInfo = false;
                boolean hasSSN = false;
                while (scan.hasNextLine()) {
                    String line = scan.nextLine();
                    System.out.println(line); //verify line data visually
                    Matcher m = regccnum.matcher(line);
                    Matcher n = regssn.matcher(line);
                    if (m.find()) {
                        hasCreditCardInfo = true;
                         url = "failedpage";
                    } else if (n.find()) {
                        hasSSN = true;
                         url = "failedpage";
                    }
                }
                if (hasCreditCardInfo == false && hasSSN == false) {
                    BufferedOutputStream stream =
                            new BufferedOutputStream(new FileOutputStream(new File(appname + "_" + dateUploadedStamp + "_" + uploadedFile)));
                    stream.write(inputStream.read());
                    stream.close();
                    return "success";
                }
            } catch (Exception e) {
                return "You failed to upload the file";
            }
        } else {
            return "failed upload because the file was empty.";
        }
        return url;
    }

}