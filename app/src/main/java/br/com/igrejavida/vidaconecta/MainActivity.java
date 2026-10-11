package br.com.igrejavida.vidaconecta;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.webkit.*;
import android.widget.*;
import android.view.View;

public class MainActivity extends Activity {
 private static final String HOME = "https://vida-conecta-production.up.railway.app/";
 private WebView web;
 private ValueCallback<Uri[]> fileCallback;
 private static final int FILE_REQUEST=12;
 @Override public void onCreate(Bundle b) {
  super.onCreate(b);
  web = new WebView(this);
  setContentView(web);
  web.getSettings().setJavaScriptEnabled(true);
  web.getSettings().setDomStorageEnabled(true);
  web.getSettings().setAllowFileAccess(false);
  web.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_NEVER_ALLOW);
  web.setWebViewClient(new WebViewClient() {
   @Override public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
    Uri uri=request.getUrl();
    if ("https".equals(uri.getScheme()) && "vida-conecta-production.up.railway.app".equals(uri.getHost())) return false;
    try { startActivity(new Intent(Intent.ACTION_VIEW, uri)); } catch(Exception ignored) { Toast.makeText(MainActivity.this,"Não foi possível abrir o link",Toast.LENGTH_SHORT).show(); }
    return true;
   }
  });
  web.setWebChromeClient(new WebChromeClient() {
   @Override public boolean onShowFileChooser(WebView view, ValueCallback<Uri[]> cb, FileChooserParams params) {
    if(fileCallback!=null) fileCallback.onReceiveValue(null);
    fileCallback=cb;
    try { startActivityForResult(params.createIntent(),FILE_REQUEST); return true; }
    catch(Exception e) { fileCallback=null; return false; }
   }
  });
  if(b==null) web.loadUrl(HOME); else web.restoreState(b);
 }
 @Override protected void onActivityResult(int req,int result,Intent data) {
  super.onActivityResult(req,result,data);
  if(req==FILE_REQUEST && fileCallback!=null) { fileCallback.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(result,data)); fileCallback=null; }
 }
 @Override protected void onSaveInstanceState(Bundle out){ web.saveState(out); super.onSaveInstanceState(out); }
 @Override public void onBackPressed(){ if(web.canGoBack())web.goBack(); else super.onBackPressed(); }
}
