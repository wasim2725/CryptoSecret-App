package com.example.cryptosecret;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {

    EditText keytext;
    EditText normaltext;
    EditText ciphertext;
    Button copy_normal;
    Button copy_cipher;
    Button encrypt;
    Button decrypt;
    Button delete_normal;
    Button delete_cipher;
    TextView char_count;
    TextView char_count2;
    Context c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.parseColor("#2a9db7"));
        }
        c = MainActivity.this;
        normaltext = findViewById(R.id.normaltext);
        keytext = findViewById(R.id.key);
        ciphertext = findViewById(R.id.ciphertext);
        copy_cipher = findViewById(R.id.copy_cipher);
        copy_normal = findViewById(R.id.copy_normal);
        encrypt = findViewById(R.id.encrypt);
        decrypt = findViewById(R.id.decrypt);
        delete_normal = findViewById(R.id.delete_normal);
        delete_cipher = findViewById(R.id.delete_cipher);
        char_count = findViewById(R.id.char_count);
        char_count2 = findViewById(R.id.char_count2);

        encrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(normaltext.getText().toString().matches("") || keytext.getText().toString().matches("")){
                    App.ToastMaker(c, "Enter Input Text and Key");
                }
                else if(keytext.getText().toString().length() != 8){
                    App.ToastMaker(c, "Enter Key of 8 characters");
                }
                else {
                    ciphertext.setText(encrypt(normaltext.getText().toString(),c));
                }
            }
        });
        decrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(normaltext.getText().toString().matches("") || keytext.getText().toString().matches("")){
                    App.ToastMaker(c, "Enter Encrypted Text and Key");
                }
                else if(keytext.getText().toString().length() != 8){
                    App.ToastMaker(c, "Enter Key of 8 characters");
                }
                else {
                    ciphertext.setText(decrypt(normaltext.getText().toString(),c));
                }
            }
        });
        copy_normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("cipher text", normaltext.getText().toString());
                clipboard.setPrimaryClip(clip);
                App.ToastMaker(c,"Input Text Copied");
            }
        });
        copy_cipher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("cipher text", ciphertext.getText().toString());
                clipboard.setPrimaryClip(clip);
                App.ToastMaker(c,"Encrypted Text Copied");
            }
        });
        delete_normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                normaltext.setText("");
            }
        });
        delete_cipher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ciphertext.setText("");
            }
        });
        normaltext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                char_count.setText(normaltext.getText().toString().length()+"");
            }
        });
        ciphertext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                char_count2.setText(ciphertext.getText().toString().length()+"");
            }
        });
    }

    public String decrypt(String value, Context c){
        String coded;
        String result = null;
        if(value.startsWith("code==")){
            coded = value.substring(6, value.length()).trim();
        }
        else {
            coded = value.trim();
        }
        try{
            byte[] bytesDecoded = Base64.decode(coded.getBytes("UTF-8"),Base64.DEFAULT);
            SecretKeySpec key = new SecretKeySpec(keytext.getText().toString().getBytes(),"DES");
            Cipher cipher = Cipher.getInstance("DES/ECB/ZeroBytePadding");
            cipher.init(Cipher.DECRYPT_MODE,key);
            byte[] textDecrypted = cipher.doFinal(bytesDecoded);
            result = new String(textDecrypted);
        }
        catch (NoSuchAlgorithmException e){
            e.printStackTrace();
            App.DialogMaker(c, "Encrypt Error", "Error" + "\n" + e.getMessage());
            return "Encrypt Error";
        }
        catch (NoSuchPaddingException e){
            e.printStackTrace();
            App.DialogMaker(c, "Encrypt Error", "Error" + "\n" + e.getMessage());
            return "Encrypt Error";
        }
        catch (IllegalBlockSizeException e){
            e.printStackTrace();
            App.DialogMaker(c, "Encrypt Error", "Error" + "\n" + e.getMessage());
            return "Encrypt Error";
        }
        catch (BadPaddingException e){
            e.printStackTrace();
            App.DialogMaker(c, "Encrypt Error", "Error" + "\n" + e.getMessage());
            return "Encrypt Error";
        }
        catch (InvalidKeyException e){
            e.printStackTrace();
            App.DialogMaker(c, "Encrypt Error", "Error" + "\n" + e.getMessage());
            return "Encrypt Error";
        }
        catch (UnsupportedEncodingException e){
            e.printStackTrace();
            App.DialogMaker(c, "Encrypt Error", "Error" + "\n" + e.getMessage());
            return "Encrypt Error";
        }
        catch (Exception e){
            e.printStackTrace();
            App.DialogMaker(c, "Encrypt Error", "Error" + "\n" + e.getMessage());
            return "Encrypt Error";
        }
        return result;
    }

    public String encrypt(String value, Context c){
        String crypted = "";
        try{
            byte[] cleartext = value.getBytes("UTF-8");
            SecretKeySpec key = new SecretKeySpec(keytext.getText().toString().getBytes(), "DES");
            Cipher cipher = Cipher.getInstance("DES/ECB/ZeroBytePadding");
            cipher.init(Cipher.ENCRYPT_MODE,key);
            crypted = Base64.encodeToString(cipher.doFinal(cleartext), Base64.DEFAULT);
        }
        catch (NoSuchAlgorithmException e){
            e.printStackTrace();
            App.DialogMaker(c, "Encrypt Error", "Error" + "\n" + e.getMessage());
            return "Encrypt Error";
        }
        catch (NoSuchPaddingException e){
            e.printStackTrace();
            App.DialogMaker(c, "Encrypt Error", "Error" + "\n" + e.getMessage());
            return "Encrypt Error";
        }
        catch (IllegalBlockSizeException e){
            e.printStackTrace();
            App.DialogMaker(c, "Encrypt Error", "Error" + "\n" + e.getMessage());
            return "Encrypt Error";
        }
        catch (BadPaddingException e){
            e.printStackTrace();
            App.DialogMaker(c, "Encrypt Error", "Error" + "\n" + e.getMessage());
            return "Encrypt Error";
        }
        catch (InvalidKeyException e){
            e.printStackTrace();
            App.DialogMaker(c, "Encrypt Error", "Error" + "\n" + e.getMessage());
            return "Encrypt Error";
        }
        catch (UnsupportedEncodingException e){
            e.printStackTrace();
            App.DialogMaker(c, "Encrypt Error", "Error" + "\n" + e.getMessage());
            return "Encrypt Error";
        }
        catch (Exception e){
            e.printStackTrace();
            App.DialogMaker(c, "Encrypt Error", "Error" + "\n" + e.getMessage());
            return "Encrypt Error";
        }
        return crypted;
    }
}