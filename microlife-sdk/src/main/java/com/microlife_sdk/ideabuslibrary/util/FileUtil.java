package com.microlife_sdk.ideabuslibrary.util;

import android.content.ContentUris;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.os.Build.VERSION;
import android.provider.DocumentsContract;
import android.provider.MediaStore.Images.Media;
import java.io.IOException;
import java.io.InputStream;

public class FileUtil {
   public static String getAbsolutePath(Context context, Uri uri) {
      if (VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context, uri)) {
         String docId;
         String[] split;
         String type;
         if (isExternalStorageDocument(uri)) {
            docId = DocumentsContract.getDocumentId(uri);
            split = docId.split(":");
            type = split[0];
            if ("primary".equalsIgnoreCase(type)) {
               return Environment.getExternalStorageDirectory() + "/" + split[1];
            }
         } else {
            if (isDownloadsDocument(uri)) {
               docId = DocumentsContract.getDocumentId(uri);
               Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
               return getDataColumn(context, contentUri, (String)null, (String[])null);
            }

            if (isMediaDocument(uri)) {
               docId = DocumentsContract.getDocumentId(uri);
               split = docId.split(":");
               type = split[0];
               Uri contentUri = null;
               if ("image".equals(type)) {
                  contentUri = Media.EXTERNAL_CONTENT_URI;
               } else if ("video".equals(type)) {
                  contentUri = android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
               } else if ("audio".equals(type)) {
                  contentUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
               }

               String selection = "_id=?";
               String[] selectionArgs = new String[]{split[1]};
               return getDataColumn(context, contentUri, "_id=?", selectionArgs);
            }
         }
      } else {
         if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, (String)null, (String[])null);
         }

         if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
         }
      }

      return null;
   }

   public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
      Cursor cursor = null;
      String column = "_data";
      String[] projection = new String[]{"_data"};

      try {
         cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, (String)null);
         if (cursor != null && cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow("_data");
            String var8 = cursor.getString(column_index);
            return var8;
         }
      } finally {
         if (cursor != null) {
            cursor.close();
         }

      }

      return null;
   }

   public static boolean isExternalStorageDocument(Uri uri) {
      return "com.android.externalstorage.documents".equals(uri.getAuthority());
   }

   public static boolean isDownloadsDocument(Uri uri) {
      return "com.android.providers.downloads.documents".equals(uri.getAuthority());
   }

   public static boolean isMediaDocument(Uri uri) {
      return "com.android.providers.media.documents".equals(uri.getAuthority());
   }

   public static int getBitmapDegree(String path) {
      short degree = 0;

      try {
         ExifInterface exifInterface = new ExifInterface(path);
         int orientation = exifInterface.getAttributeInt("Orientation", 1);
         switch(orientation) {
         case 3:
            degree = 180;
            break;
         case 6:
            degree = 90;
            break;
         case 8:
            degree = 270;
         }
      } catch (IOException var4) {
         var4.printStackTrace();
      }

      return degree;
   }

   public static Bitmap decodeBitmapFromRid(Resources resources, int rid, int inSampleSize) {
      if (rid == 0) {
         return null;
      } else {
         Options options = getBitmapOptions(inSampleSize);
         InputStream is = resources.openRawResource(rid);
         return BitmapFactory.decodeStream(is, (Rect)null, options);
      }
   }

   public static Bitmap decodeBitmapFromByteArray(byte[] byteArray, int inSampleSize) {
      if (byteArray == null) {
         return null;
      } else {
         Options options = getBitmapOptions(inSampleSize);
         return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, options);
      }
   }

   private static Options getBitmapOptions(int inSampleSize) {
      Options options = new Options();
      options.inPreferredConfig = Config.RGB_565;
      options.inPurgeable = true;
      options.inInputShareable = true;
      options.inSampleSize = inSampleSize;
      return options;
   }
}
