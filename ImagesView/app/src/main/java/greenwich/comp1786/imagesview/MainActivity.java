package greenwich.comp1786.imagesview;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    Button btnNext, btnPrevious;
    int currentIndex = 0;

    int [] images = {
            R.drawable.f22,
            R.drawable.su35,
            R.drawable.su57,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        imageView = findViewById(R.id.imageView);
        btnNext = findViewById(R.id.btnNext);
        btnPrevious = findViewById(R.id.btnPrevious);

        imageView.setImageResource(images[currentIndex]);

        btnPrevious.setOnClickListener(v -> {
            currentIndex --;
            if (currentIndex < 0) {
                currentIndex = images.length - 1;
            }
            imageView.setImageResource(images[currentIndex]);
        });

        btnNext.setOnClickListener(v -> {
           currentIndex ++;
           if (currentIndex > images.length -1) {
               currentIndex = 0;
           }
           imageView.setImageResource((images[currentIndex]));
        });
    }
}