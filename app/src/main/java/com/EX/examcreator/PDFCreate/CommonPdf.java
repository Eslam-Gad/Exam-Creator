package com.EX.examcreator.PDFCreate;

import android.content.Context;
import android.os.Build;
import android.os.Environment;

import com.EX.examcreator.R;

import java.io.File;

public class CommonPdf {

    public static String getApppath(Context context)
    {

        File dir;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
        {

            dir = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
                    + File.separator
                    + context.getResources().getString(R.string.app_name)
                    + File.separator
            );

        }else {
             dir = new File(android.os.Environment.getExternalStorageDirectory()
                    + File.separator
                    + context.getResources().getString(R.string.app_name)
                    + File.separator
            );
        }

        if (!dir.exists())
            dir.mkdir();

        return dir.getPath() +File.separator;
    }


}
