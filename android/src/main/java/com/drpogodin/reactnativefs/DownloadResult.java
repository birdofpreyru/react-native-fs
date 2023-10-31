package com.drpogodin.reactnativefs;
import java.util.Map;

public class DownloadResult {
  public int statusCode;
  public long bytesWritten;
  public Exception exception;
  public Map<String, String> headers;
}
