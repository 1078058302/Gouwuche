package soexample.umeng.com.gouwuche;

import android.os.AsyncTask;

import com.google.common.io.CharStreams;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/*
Author:樊卓璞
date:2018/9/21
*/public class HttpHelper {

    public HttpHelper() {
    }

    public HttpHelper get(String url) {
        MyAsyncTask myAsyncTask = new MyAsyncTask(url, "GET");
        myAsyncTask.execute();
        return this;
    }

    class MyAsyncTask extends AsyncTask<String, Integer, String> {
        private String murl;
        private String method;

        public MyAsyncTask(String murl, String method) {
            this.murl = murl;
            this.method = method;
        }

        @Override
        protected String doInBackground(String... strings) {
            String s = null;
            try {
                URL url = new URL(murl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod(method);
                connection.setConnectTimeout(3000);
                int code = connection.getResponseCode();
                if (code == HttpURLConnection.HTTP_OK) {
                    InputStream is = connection.getInputStream();
                    s = CharStreams.toString(new InputStreamReader(is));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return s;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            listener.success(s);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }

    private HelperListener listener;

    public void result(HelperListener listener) {
        this.listener = listener;
    }

    public interface HelperListener {
        void success(String data);

        void error();
    }
}
