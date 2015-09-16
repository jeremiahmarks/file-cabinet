package hello;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.lang.Character;
import java.lang.System;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FileUploadController {

    @RequestMapping(value="/upload", method=RequestMethod.GET)
    public @ResponseBody String provideUploadInfo() {
        return "You can upload a file by posting to this same URL.";
    }

    @RequestMapping(value="/upload", method=RequestMethod.POST)
    public @ResponseBody String handleFileUpload(@RequestParam("appname") String appname,
                                                 @RequestParam("file") MultipartFile file){
        if (!file.isEmpty()) {
            //Pattern :: check for 13-16 consecutive ints \b\d{13,16}\b OR \b[\d -]{13,}\b checks for split up digits 13+
            Pattern regexp = Pattern.compile("(([0-9]{4})[-|\\s| ]([0-9]{4})[-|\\s| ]([0-9]{4})[-|\\s| ]([0-9]{4}))|(([0-9]{4})[-|\\s| ]([0-9]{6})[-|\\s| ]([0-9]{5}))");
            String ts =
                    new java.text.SimpleDateFormat("yyyy-MM-dd-hh-mm-ssa").format(new Date()); //Date stamp
            String thisfilename = file.getOriginalFilename(); //Get file name
            //String line = null;
            try {
                //read file and match against regex
                //inputStream = new FileInputStream(file);
                //char ch[] = Character.valueOf(inputStream.read());
                //Matcher m = regexp.matcher(file); //Matcher

                byte[] bytes = file.getBytes();
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(new File(appname + "_" + ts  + "_" + thisfilename)));
                stream.write(bytes);
                stream.close();
                return "Thank you for your upload";
            } catch (Exception e) {
                return "You failed to upload " + appname + " => " + e.getMessage();
            }
        } else {
            return "failed upload because the file was empty.";
        }
    }

}
