package com.example.xender.utils;

import android.util.Log;

import com.kanishka.virustotal.dto.FileScanReport;
import com.kanishka.virustotal.dto.ScanInfo;
import com.kanishka.virustotal.dto.VirusScanInfo;
import com.kanishka.virustotal.exception.APIKeyNotFoundException;
import com.kanishka.virustotal.exception.UnauthorizedAccessException;
import com.kanishka.virustotalv2.VirusTotalConfig;
import com.kanishka.virustotalv2.VirustotalPublicV2;
import com.kanishka.virustotalv2.VirustotalPublicV2Impl;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class VirusScanUtil {
    public static String scanFile(File fileToScan) {
        try {
            VirusTotalConfig.getConfigInstance().setVirusTotalAPIKey("4ae0176f319cdb95bc384f4c85b7679d1a28f391e4d47f482a4b67f26a487bc9");
            VirustotalPublicV2 virusTotalRef = new VirustotalPublicV2Impl();

            ScanInfo scanInformation = virusTotalRef.scanFile(fileToScan);

            Log.d("Scan file", "___SCAN INFORMATION___");
            Log.d("Scan file", "MD5 :\t" + scanInformation.getMd5());
            Log.d("Scan file", "Perma Link :\t" + scanInformation.getPermalink());
            Log.d("Scan file", "Resource :\t" + scanInformation.getResource());
            Log.d("Scan file", "Scan Date :\t" + scanInformation.getScanDate());
            Log.d("Scan file", "Scan Id :\t" + scanInformation.getScanId());
            Log.d("Scan file", "SHA1 :\t" + scanInformation.getSha1());
            Log.d("Scan file", "SHA256 :\t" + scanInformation.getSha256());
            Log.d("Scan file", "Verbose Msg :\t" + scanInformation.getVerboseMessage());
            Log.d("Scan file", "Response Code :\t" + scanInformation.getResponseCode());
            Log.d("Scan file", "done.");

            return scanInformation.getResource();
        } catch (APIKeyNotFoundException ex) {
            Log.d("Error", "1");
        } catch (UnsupportedEncodingException ex) {
            Log.d("Error", "2");
        } catch (UnauthorizedAccessException ex) {
            Log.d("Error", "3");
        } catch (Exception ex) {
            Log.d("Error","4");
        }
        return null;
    }

    public static void getFileScanReport(String resource) {
        try {
            VirusTotalConfig.getConfigInstance().setVirusTotalAPIKey("4ae0176f319cdb95bc384f4c85b7679d1a28f391e4d47f482a4b67f26a487bc9");
            VirustotalPublicV2 virusTotalRef = new VirustotalPublicV2Impl();

            FileScanReport report = virusTotalRef.getScanReport(resource);

            Log.d("Scan file", "Positives :\t" + report.getPositives());
        } catch (APIKeyNotFoundException ex) {
            Log.d("Scan file", "API Key not found! " + ex.getMessage());
        } catch (UnsupportedEncodingException ex) {
            Log.d("Scan file", "Unsupported Encoding Format!" + ex.getMessage());
        } catch (UnauthorizedAccessException ex) {
            Log.d("Scan file", "Invalid API Key " + ex.getMessage());
        } catch (Exception ex) {
            Log.d("Scan file", "Something Bad Happened! " + ex.getMessage());
        }
    }
}