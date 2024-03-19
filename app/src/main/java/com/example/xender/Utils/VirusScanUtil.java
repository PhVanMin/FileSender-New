package com.example.xender.Utils;

import com.kanishka.virustotal.dto.ScanInfo;
import com.kanishka.virustotal.exception.APIKeyNotFoundException;
import com.kanishka.virustotal.exception.UnauthorizedAccessException;
import com.kanishka.virustotalv2.VirusTotalConfig;
import com.kanishka.virustotalv2.VirustotalPublicV2;
import com.kanishka.virustotalv2.VirustotalPublicV2Impl;

import java.io.File;
import java.io.UnsupportedEncodingException;

public class VirusScanUtil {
    public static void scanFile(String filePath) {
        try {
            VirusTotalConfig.getConfigInstance().setVirusTotalAPIKey("4ae0176f319cdb95bc384f4c85b7679d1a28f391e4d47f482a4b67f26a487bc9");
            VirustotalPublicV2 virusTotalRef = new VirustotalPublicV2Impl();

            ScanInfo scanInformation = virusTotalRef.scanFile(new File(filePath));

            System.out.println("___SCAN INFORMATION___");
            System.out.println("MD5 :\t" + scanInformation.getMd5());
            System.out.println("Perma Link :\t" + scanInformation.getPermalink());
            System.out.println("Resource :\t" + scanInformation.getResource());
            System.out.println("Scan Date :\t" + scanInformation.getScanDate());
            System.out.println("Scan Id :\t" + scanInformation.getScanId());
            System.out.println("SHA1 :\t" + scanInformation.getSha1());
            System.out.println("SHA256 :\t" + scanInformation.getSha256());
            System.out.println("Verbose Msg :\t" + scanInformation.getVerboseMessage());
            System.out.println("Response Code :\t" + scanInformation.getResponseCode());
            System.out.println("done.");
        } catch (APIKeyNotFoundException ex) {
            System.err.println("API Key not found! " + ex.getMessage());
        } catch (UnsupportedEncodingException ex) {
            System.err.println("Unsupported Encoding Format!" + ex.getMessage());
        } catch (UnauthorizedAccessException ex) {
            System.err.println("Invalid API Key " + ex.getMessage());
        } catch (Exception ex) {
            System.err.println("Something Bad Happened! " + ex.getMessage());
        }
    }
}
