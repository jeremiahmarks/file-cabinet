package com.fileupload.inf;

//Create my FUVR method in another class
// filename, type of data, contains?
//String, ssn ccn, bool
//bool valid
//string errormessage

public class FileUploadValidationResult(filename, containsCreditCards, containsSocialSecurity, valid, errorMessage) {
        String this.filename = filename;
        boolean this.containsCreditCards = containsCreditCards;
        boolean this.containsSocialSecurity = containsSocialSecurity;
        boolean this.valid = valid;
        String this.errorMessage = errorMessage;

        valid = !containsCreditCards && !containsSocialSecurity;
        }

        List<FileUploadValidationResult> results = new ArrayList() {
        for(file : files) {
        results.add(foundBadData(files){
        }
