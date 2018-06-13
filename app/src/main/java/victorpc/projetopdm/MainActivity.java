package victorpc.projetopdm;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    String cityName = null;
    String longitude = null;
    String latitude = null;
    private LocationManager locationManager = null;
    private Boolean flag = false;
    TextView cidadeAtual;

    RadioGroup radioGroup;
    Integer contador = 1;

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

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, MyLocationListener);


        cidadeAtual = (TextView) findViewById(R.id.cityView);

        radioGroup = (RadioGroup) findViewById(R.id.radioButtonGroup);

        Button botao1 = (Button) findViewById(R.id.set_time);

        Button botao2 = (Button) findViewById(R.id.set_wallpaper);

        Button botao3 = (Button) findViewById(R.id.button);


        botao1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int selectedId = radioGroup.getCheckedRadioButtonId();

                Toast.makeText(MainActivity.this,
                        "Tempo alterado", Toast.LENGTH_SHORT)
                        .show();
            }
        });

        botao2.setOnClickListener(new View.OnClickListener() {
            //Os métodos de setResource podem receber as imagens porém ele ainda dá a warning mesmo assim
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {
                WallpaperManager myWallpaperManager = WallpaperManager
                        .getInstance(MainActivity.this);
                flag = displayGpsStatus();
                if(flag){
                if (contador == 1){
                    contador++;
                    try {

                        myWallpaperManager.setResource(R.drawable.wallpaper1);

                        Toast.makeText(MainActivity.this,
                                "Wallpaper alterado com sucesso", Toast.LENGTH_SHORT)
                                .show();

                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                    }
                } else if(contador == 2){
                    try {
                        contador--;
                        myWallpaperManager.setResource(R.drawable.wallpaper2);

                        Toast.makeText(MainActivity.this,
                                "Wallpaper alterado com sucesso", Toast.LENGTH_SHORT)
                                .show();

                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                    }
                }
                } else {
                    Toast.makeText(MainActivity.this,
                            "GPS NOT ACTIVATED", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

    }

    //Pegar as Coordenadas pelo GPS
   private final LocationListener MyLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location loc) {

            longitude = "Longitude: " +loc.getLongitude();
            latitude = "Latitude: " +loc.getLatitude();

           //Pega Nome da Cidade pelas coordenadas
            Geocoder gcd = new Geocoder(getBaseContext(),
                    Locale.getDefault());
            List<Address>  addresses;
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
                    Toast.makeText(MainActivity.this, "GPS Provider update", Toast.LENGTH_SHORT).show();
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

    public void toggleGPSUpdates(View view) {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 2 * 60 * 1000, 10, MyLocationListener);
        }
    }





