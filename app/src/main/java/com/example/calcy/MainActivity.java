package com.example.calcy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.calcy.databinding.ActivityMainBinding;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import org.apache.commons.lang3.StringUtils;

public class MainActivity extends AppCompatActivity {
ActivityMainBinding binding;
    boolean lastnumeric= false;
    boolean statError= false;
    boolean lastDot= false;
    Expression expression;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    public void onClearClick(View view) {

        binding.dataTv.setText("");
        lastnumeric=false;
    }

    public void onbackspaceClick(View view) {

        String temp = StringUtils.chop(binding.dataTv.getText().toString());
        binding.dataTv.setText(temp);

        try {
            String temp2 = binding.dataTv.getText().toString();
            String lastchar= String.valueOf(temp2.charAt(temp2.length()-1));
            if (StringUtils.isNumeric(lastchar)){
                onEqual();
            }else{
                String noOperatorString=StringUtils.chop(temp);
                onEqual(noOperatorString);

            }

        }catch (Exception e){
            binding.resultTv.setText("");
            binding.resultTv.setVisibility(View.GONE);
            System.out.println(e);

        }

    }

    public void onOperatorClick(View view) {
        Button button = (Button) view;
        if (!statError && lastnumeric ){

            binding.dataTv.append(button.getText().toString());
            lastDot=false;
            lastnumeric=false;
            onEqual();
        }

    }

    public void onEqualClick(View view) {

        onEqual();
        binding.dataTv.setText(binding.resultTv.getText().toString().replaceAll("=",""));

    }

    public void onAllclearClick(View view) {


        binding.dataTv.setText("");
        binding.resultTv.setText("");
        statError=false;
        lastDot=false;
        lastnumeric=false;
        binding.resultTv.setVisibility(View.GONE);
    }

    public void ondigitclick(View view) {

        try {

            Button button = (Button) view;

            if (statError) {

                binding.dataTv.setText(button.getText().toString());
                statError = false;

            } else {
                binding.dataTv.append(button.getText().toString());
            }

            lastnumeric = true;
            onEqual();

        }catch (Exception e){
            System.out.println(e);
        }
    }

    private void onEqual(){
    if (lastnumeric && !statError) {
        String txt = binding.dataTv.getText().toString();
        if (txt.contains("%")) {
            String val = txt.replaceAll("%", "/100*");
            onEqual(val);
        }
        else {
            onEqual(txt);
        }
    }


    }
    private void onEqual(String str){
        if (lastnumeric && !statError){
            expression = new ExpressionBuilder(str).build();
            try {
                String result = String.valueOf(expression.evaluate());
                if(result.endsWith(".0")){
                    result=result.replace(".0","");
                }
                binding.resultTv.setVisibility(View.VISIBLE);
                binding.resultTv.setText("="+result);
            }catch (ArithmeticException arithmeticException){
                System.out.println(arithmeticException);
                binding.resultTv.setText("Error");
                statError=true;
                lastnumeric=false;
            }
        }


    }
    }
//done