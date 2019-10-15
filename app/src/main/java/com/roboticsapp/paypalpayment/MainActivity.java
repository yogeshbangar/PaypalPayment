package com.roboticsapp.paypalpayment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.math.BigDecimal;

import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "paypalExample";
    public static final String PAYPAL_KEY = "AShz5KyWM6B5Ogxb_yOp5G5-fK42G5y0Jvq4ooiVrAkSwvhJSr2IH654dv_7BzKnlrVVgEF2WWLZ2Dk_";

    private static final int REQUEST_CODE_PAYMENT = 1;
    private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;
    private static final String CONFIG_ENVIROMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;

    private static PayPalConfiguration config;

    PayPalPayment thingstoBuy;
    Button Order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Order = findViewById(R.id.order);

        Order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MakePayment();
            }
        });
        configPayPal();
    }

    private void configPayPal() {
        config  = new PayPalConfiguration()
                .environment(CONFIG_ENVIROMENT)
                .clientId(PAYPAL_KEY)
                .merchantName("yogeshbangar@gmail.com")
                .merchantPrivacyPolicyUri(Uri.parse("https://hututusoftwares.com/privacy.php"))
                .merchantUserAgreementUri(Uri.parse("https://hututusoftwares.com/privacy.php"));
    }


    public void MakePayment(){
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        startService(intent);

        thingstoBuy = new PayPalPayment(new BigDecimal(String.valueOf("10.45")),"USD","Payment",PayPalPayment.PAYMENT_INTENT_SALE);
        Intent payment = new Intent(this, PaymentActivity.class);
        payment.putExtra(PaymentActivity.EXTRA_PAYMENT,thingstoBuy);
        payment.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        startActivityForResult(payment,REQUEST_CODE_PAYMENT);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_PAYMENT){
            if(resultCode == Activity.RESULT_OK){
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if(confirm != null){
                    try {
                        System.out.println("~~~~~"+confirm.toJSONObject().toString());
                        System.out.println("~~~~~"+confirm.getPayment().toJSONObject().toString(4));
                        Toast.makeText(this,"Payment has been Sussesfull.",Toast.LENGTH_LONG).show();
                    }catch (Exception e){
                        Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();
                    }
                }
            }else if (resultCode == Activity.RESULT_CANCELED){
                Toast.makeText(this,"Payment has been Cancled.",Toast.LENGTH_LONG).show();
            }else if(resultCode == PaymentActivity.RESULT_EXTRAS_INVALID){
                Toast.makeText(this,"RESULT_EXTRAS_INVALID.",Toast.LENGTH_LONG).show();
            }
        }
        if(requestCode == REQUEST_CODE_FUTURE_PAYMENT){
            if(resultCode == Activity.RESULT_OK){
                PayPalAuthorization authorization = data.getParcelableExtra(PayPalFuturePaymentActivity.EXTRA_RESULT_AUTHORIZATION);
                if(authorization != null){
                    try {
                        String AuthCode = authorization.getAuthorizationCode();
                        System.out.println(AuthCode+"~~REQUEST_CODE_FUTURE_PAYMENT~~~"+authorization.toJSONObject().toString());

                    }catch (Exception e){
                        Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();
                    }
                }
            }else if (resultCode == Activity.RESULT_CANCELED){
                Toast.makeText(this,"Payment has been Cancled.",Toast.LENGTH_LONG).show();
            }else if(resultCode == PaymentActivity.RESULT_EXTRAS_INVALID){
                Toast.makeText(this,"RESULT_EXTRAS_INVALID.",Toast.LENGTH_LONG).show();
            }
        }
    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == REQUEST_CODE_PAYMENT){
//            if(resultCode == Activity.RESULT_OK){
//                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
//                if(confirm != null){
//                    try {
//                        System.out.println("~~~~~"+confirm.toJSONObject().toString());
//                        System.out.println("~~~~~"+confirm.getPayment().toJSONObject().toString(4));
//                    }catch (Exception e){
//                        Toast.makeText()
//                    }
//                }
//            }
//        }
//    }
}




