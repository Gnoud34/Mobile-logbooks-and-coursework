package greenwich.comp1786.calsim;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    TextView tvResult;
    String current = "", operator = "";
    Double first = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvResult = findViewById(R.id.tvResult);

        setNumberClickListeners();
        setOperatorListeners();
    }

    void setNumberClickListeners() {
        int[] ids = {R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
                R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9, R.id.btnDot};
        View.OnClickListener listener = v -> {
            Button b = (Button) v;
            current += b.getText().toString();
            tvResult.setText(current);
        };

        for (int id : ids) {
            findViewById(id).setOnClickListener(listener);
        }

        findViewById(R.id.btnClear).setOnClickListener(v -> {
            current = "";
            first = null;
            operator = "";
            tvResult.setText("0");
        });
    }

    void setOperatorListeners() {
        findViewById(R.id.btnAdd).setOnClickListener(v -> setOperator("+"));
        findViewById(R.id.btnSubtract).setOnClickListener(v -> setOperator("-"));
        findViewById(R.id.btnMultiply).setOnClickListener(v -> setOperator("*"));
        findViewById(R.id.btnDivide).setOnClickListener(v -> setOperator("/"));

        findViewById(R.id.btnEquals).setOnClickListener(v -> calculate());
    }

    void setOperator(String op) {
        if (!current.isEmpty()) {
            try {
                first = Double.parseDouble(current);
                operator = op;
                current = "";
                tvResult.setText(op);
            } catch (Exception e) {
                tvResult.setText("Invalid");
            }
        }
    }

    void calculate() {
        if (first != null && !current.isEmpty()) {
            try {
                double second = Double.parseDouble(current);
                double result = 0;
                switch (operator) {
                    case "+": result = first + second; break;
                    case "-": result = first - second; break;
                    case "*": result = first * second; break;
                    case "/":
                        if (second == 0) {
                            tvResult.setText("Cannot divide by 0");
                            return;
                        }
                        result = first / second;
                        break;
                }
                current = String.valueOf(result);
                tvResult.setText(current);
                first = null;
            } catch (Exception e) {
                tvResult.setText("Error");
            }
        }
    }
}