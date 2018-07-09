package victorpc.projetopdm;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    List<String> trends;
    String cityName = null;
    String longitude = null;
    String latitude = null;
    private LocationManager locationManager = null;
    private Boolean flag = false;
    TextView cidadeAtual;
    String query = "https://api.flickr.com/services/rest/?method=flickr.photos.search" + "&api_key=a00db9ea6312361a62c4c8828845f13b" + "&tags=" + cityName + "&format=json" + "&nojsoncallback=1" + "&extras=url_m";
    Bitmap wallpaper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationManager = (LocationManager) MainActivity.this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        cidadeAtual = (TextView) findViewById(R.id.cityView);

    }

    //Pegar as Coordenadas pelo GPS
    private final LocationListener MyLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location loc) {

            longitude = "Longitude: " + loc.getLongitude();
            latitude = "Latitude: " + loc.getLatitude();

            //Pega Nome da Cidade pelas coordenadas
            Geocoder gcd = new Geocoder(getBaseContext(),
                    Locale.getDefault());
            List<Address> addresses;
            try {
                addresses = gcd.getFromLocation(loc.getLatitude(), loc
                        .getLongitude(), 1);
                if (addresses.size() > 0) {
                    cityName = addresses.get(0).getLocality();
                } else {
                    Toast.makeText(MainActivity.this, "Not Locale", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    cidadeAtual.setText(cityName);
                    Toast.makeText(MainActivity.this, "GPS Provider update", Toast.LENGTH_LONG).show();
                    new LoadImagesFromFlickrTask().execute(query);
                }
            });

        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    //Verificar se o GPS ta ativo ou não
    private Boolean displayGpsStatus() {
        ContentResolver contentResolver = getBaseContext()
                .getContentResolver();
        boolean gpsStatus = Settings.Secure
                .isLocationProviderEnabled(contentResolver,
                        LocationManager.GPS_PROVIDER);
        if (gpsStatus) {
            return true;

        } else {
            return false;
        }
    }

    @SuppressLint("MissingPermission")
    public void toggleGPSUpdates(View view) {
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 1000, 10, MyLocationListener);


    }

    public void setWallpaper(View view){
        WallpaperManager myWallpaperManager = WallpaperManager
                .getInstance(MainActivity.this);
        flag = displayGpsStatus();
        if (flag && trends != null) {
                new DownloadImage().execute(trends.get(1));
                try {
                    if(wallpaper != null) {
                        myWallpaperManager.setBitmap(wallpaper);

                        Toast.makeText(MainActivity.this,
                                "Wallpaper alterado com sucesso", Toast.LENGTH_SHORT)
                                .show();
                    }
                } catch (IOException e) {}
        } else {
            Toast.makeText(MainActivity.this,
                    "GPS não está ativado ou lista vazia", Toast.LENGTH_SHORT)
                    .show();
        }
    }

     class DownloadImage extends AsyncTask<String, Void, Bitmap> {
        private String TAG = "DownloadImage";
        ProgressDialog dialog;
         protected void onPreExecute() {
             super.onPreExecute();
             dialog = ProgressDialog.show(MainActivity.this, "Aguarde",
                     "Baixando imagens, Por Favor Aguarde...");
         }

        private Bitmap downloadImageBitmap(String sUrl) {
            Bitmap bitmap = null;
            try {
                InputStream inputStream = new URL(sUrl).openStream();   // Download Image from URL
                bitmap = BitmapFactory.decodeStream(inputStream);       // Decode Bitmap
                inputStream.close();
            } catch (Exception e) {
                Log.d(TAG, "Exception 1, Something went wrong!");
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return downloadImageBitmap(params[0]);
        }

        protected void onPostExecute(Bitmap result) {
             dialog.dismiss();
            wallpaper = result;
        }
    }

    class LoadImagesFromFlickrTask extends AsyncTask<String, Integer, List> {
        ProgressDialog dialog;
        String json = null;

        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(MainActivity.this, "Aguarde",
                    "Buscando opções de imagens, Por Favor Aguarde...");
        }

        @Override
        protected void onPostExecute(List result) {
            super.onPostExecute(result);
            dialog.dismiss();
            Toast.makeText(MainActivity.this,
                    json, Toast.LENGTH_LONG)
                    .show();

        }

        @Override
        protected List doInBackground(String... params) {
            String urlString = params[0];

            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(urlString);


            try {
                HttpResponse response = httpclient.execute(httpget);

                HttpEntity entity = response.getEntity();

                if (entity != null) {
                    InputStream instream = entity.getContent();

                    json = getStringFromInputStream(instream);
                    instream.close();

                    trends = getImages(json);

                    return trends;
                }
            } catch (Exception e) {
                Log.e("DEVMEDIA", "Falha ao acessar Web service", e);
            }
            return null;
        }

        private List<String> getImages(String jsonString) {

            List<String> urls = new ArrayList();

            try {

                JSONObject imagesList = new JSONObject(jsonString);
                JSONArray lista = new JSONArray(imagesList.getJSONArray("photo"));

                JSONObject image;

                for (int i = 0; i < lista.length(); i++) {
                    image = new JSONObject(lista.getString(i));

                    urls.add(image.optString("url_m"));

                }
            } catch (JSONException e) {
                Log.e("DEVMEDIA", "Erro no parsing do JSON", e);
            }

            return urls;
        }
    }

    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }
}
