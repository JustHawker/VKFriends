package checkers;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class InternetConnectionChecker extends AsyncTask<Void, Void, Void> {
    private Response response;
    private Boolean isAvailable;

    private final String GOOGLE_PUBLIC_DNS = "8.8.8.8";
    private final int PORT = 53;
    private final int TIMEOUT_MS = 1500;

    public InternetConnectionChecker(Response response) {
        isAvailable=false;
        this.response = response;
        execute();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            Socket sock = new Socket();
            sock.connect(new InetSocketAddress(GOOGLE_PUBLIC_DNS, PORT), TIMEOUT_MS);
            sock.close();
            isAvailable= true;
        } catch (IOException e) {
            isAvailable= false;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        response.onResponseReceived(isAvailable) ;
    }
}