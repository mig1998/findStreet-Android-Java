package com.example.request;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class endereco extends AppCompatActivity {


private EditText UF,Cidade,Rua;
private Button BuscarCep,Buscar;
private ListView lista;
private ArrayList<String> ceps;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endereco);

        //edit
        UF=findViewById(R.id.editUF);
        Cidade=findViewById(R.id.editCidade);
        Rua=findViewById(R.id.editEndereco);

        //btn
        BuscarCep=findViewById(R.id.btnBuscarCep);
        Buscar=findViewById(R.id.btnCompleto);





        //lista
       lista=findViewById(R.id.lista);


        getSupportActionBar().hide();







        Buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
    if(UF.getText().length()<2 || UF.getText().length()>2){

        Toast.makeText(getApplicationContext(),"insira o UF valido",Toast.LENGTH_SHORT).show();



    }

    if(Rua.getText().toString().equals("") || Cidade.getText().toString().equals("") ){

        Toast.makeText(getApplicationContext(),"Voce deixou algum Campo Vazio",Toast.LENGTH_SHORT).show();
                }else

                    {

        MyTask task=new MyTask();

            String uf=UF.getText().toString();
            String cidade=Cidade.getText().toString();
            String rua=Rua.getText().toString();


            String viaCep="https://viacep.com.br/ws/"+uf+"/"+cidade+"/"+rua+"/json/";

            task.execute(viaCep);


    }

            }
        });


        BuscarCep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);

                startActivity(intent);


            }
        });



    }



    class MyTask extends AsyncTask<String,Void,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();



        }


        @Override
        protected String doInBackground(String... strings) {

          String stringUrl=strings[0];

            InputStream inputStream=null;
            InputStreamReader streamReader=null;
            StringBuffer buffer=null;


          try {
              URL url=new URL(stringUrl);


              HttpURLConnection conexao=(HttpURLConnection) url.openConnection();

              inputStream=conexao.getInputStream();

              streamReader=new InputStreamReader(inputStream);

              BufferedReader reader=new BufferedReader(streamReader);


              buffer=new StringBuffer();


              String linha="";

              while( (linha=reader.readLine()) !=null){
                  buffer.append(linha);


              }




          } catch (MalformedURLException e) {
              e.printStackTrace();
          } catch (IOException e) {
              e.printStackTrace();
          }


            return buffer.toString();



        }




        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            String cep=null;
            String logradouro=null;
            String bairro=null;
            String complemento=null;



            try {
                //json array, pegar array inteiro
             JSONArray jsonArray=new JSONArray(s);
              //  Cep = jsonArray.getString(0);

                //pegar apenas um vvalor
             //   JSONObject object = jsonArray.getJSONObject(0);
              //  Cep = object.getString("cep");




                List<String> list = new ArrayList<String>();
                ceps=new ArrayList<String>();


              ArrayAdapter<String>  itensAdaptador = new ArrayAdapter<String>(getApplicationContext(),
                       android.R.layout.simple_list_item_2,
                        android.R.id.text2,
                        list);

              lista.setAdapter(itensAdaptador);

                for (int i=0; i<jsonArray.length(); i++) {

                    JSONObject object=jsonArray.getJSONObject(i);
                    logradouro=object.getString("logradouro");
                    cep=object.getString("cep");
                    bairro=object.getString("bairro");
                    complemento=object.getString("complemento");



                    list.add("Endereço: "+logradouro+"; Cep: "+cep+"; Bairro: "+bairro+"; Complemento: " +complemento);

                    ceps.add(object.getString("cep"));


                }




                    lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            //chamando metodo que remove
                            String Valor=  ceps.get(position);
                            Intent intent=new Intent(getApplicationContext(),MapsActivity.class);

                            intent.putExtra("cep",Valor);

                            startActivity(intent);

                            Toast.makeText(getApplicationContext(),"Cep: "+Valor,Toast.LENGTH_LONG).show();
                        }
                    });




            } catch (JSONException e) {
                e.printStackTrace();
            }






        }






    }



    public void clear(View view){
        Rua.getText().clear();
        UF.getText().clear();
        Cidade.getText().clear();
    }

}