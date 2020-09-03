package  com.example.request;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private Button recuperar,voltar;
    private TextView Logradouro,Bairro,Cidade,UF;
    private EditText cep;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    recuperar=findViewById(R.id.btnRecuperar);
    voltar=findViewById(R.id.btnVoltar);

    Logradouro=findViewById(R.id.txtEnd);
    Bairro=findViewById(R.id.txtBairro);
    Cidade=findViewById(R.id.txtCidade);
    UF=findViewById(R.id.txtUF);


    cep=findViewById(R.id.editCep);

    getSupportActionBar().hide();


    recuperar.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (cep.getText().length()<8 || cep.getText().length()>8) {
            Toast.makeText(getApplicationContext(),"Insira um CEP Valido!",Toast.LENGTH_SHORT).show();

            } else {


                //a classe
                MyTask task = new MyTask();


                String viaCep = cep.getText().toString();

                //cep
                String urlCep = "https://viacep.com.br/ws/" + viaCep + "/json/";

                //executar
                task.execute(urlCep);

            }

        }

    });



    voltar.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(getApplicationContext(),endereco.class);

            startActivity(intent);
        }
    });




    }

    //instanciar a classe extendida e implementar os metodos
    class MyTask extends AsyncTask<String,Void,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();




        }



        @Override
        protected String doInBackground(String... strings) {

            //recupera em string
        String stringUrl= strings[0];

        //instanciando vazio\/
        InputStream inputStream=null;
        InputStreamReader inputStreamReader=null;
        StringBuffer buffer=null;

        try {

            URL url = new URL(stringUrl);

        //abrindo a conexao
          HttpURLConnection conexao =(HttpURLConnection)  url.openConnection();


          //recupera os dados em bytes
          inputStream=conexao.getInputStream();

          //le os dados em bytes e decodifica para caracters
          inputStreamReader=new InputStreamReader(inputStream);


          //le os caracters
            BufferedReader reader= new BufferedReader(inputStreamReader);

            //stringbuffer
            buffer=new StringBuffer();

            String linha="";


            while( (linha=reader.readLine()) !=null){
             buffer.append(linha);


            }




        }catch (MalformedURLException e) {
            e.printStackTrace();

        }catch (IOException e){
            e.printStackTrace();
        }

        //retornando
        return buffer.toString();

        }






        @Override
        protected void onPostExecute(String results) {
            super.onPostExecute(results);


            String logradouro=null;
            String bairro=null;
            String cidade=null;
            String uf=null;



            try {

            //api cep
                JSONObject jsonObject=new JSONObject(results);




	//api cep
                logradouro=jsonObject.getString("logradouro");
                bairro=jsonObject.getString("bairro");
                cidade=jsonObject.getString("localidade");
                uf=jsonObject.getString("uf");






            } catch (JSONException e) {
                e.printStackTrace();
            }

                if(logradouro==null || bairro==null || cidade==null || uf==null)
                {
                    Toast.makeText(getApplicationContext(),"Cep não Existe!",Toast.LENGTH_SHORT).show();
                    Logradouro.setText("");
                    Bairro.setText("");
                    Cidade.setText("");
                    UF.setText("");

                }else {

                    Logradouro.setText("Endereco: " + logradouro);
                    Bairro.setText("Bairro: " + bairro);
                    Cidade.setText("Cidade: " + cidade);
                    UF.setText("UF: " + uf);

                }

        }




    }


}