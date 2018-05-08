package victorpc.projetopdm;

import android.app.WallpaperManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    RadioGroup radioGroup;
    RadioButton radioSexButton;
    Integer contador = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        radioGroup = (RadioGroup) findViewById(R.id.radioButtonGroup);

        Button botao1 = (Button) findViewById(R.id.set_time);

        Button botao2 = (Button) findViewById(R.id.set_wallpaper);

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
            @Override
            public void onClick(View view) {
                WallpaperManager myWallpaperManager = WallpaperManager
                        .getInstance(MainActivity.this);
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
            }
        });

    }
}
