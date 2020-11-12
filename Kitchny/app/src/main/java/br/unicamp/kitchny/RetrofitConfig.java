package br.unicamp.kitchny;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

public class RetrofitConfig {
    private Retrofit retrofit;

    public RetrofitConfig ()
    {
        //Scalese
        /*this.retrofit = new Retrofit.Builder().baseUrl("http://192.168.0.28:3000/api/").
                addConverterFactory(GsonConverterFactory.create()).build();*/
        //Felis
        this.retrofit = new Retrofit.Builder().baseUrl("http://192.168.6.1:3000/api/").
            addConverterFactory(GsonConverterFactory.create()).build();
    }

    public Service getService ()
    {
        return this.retrofit.create(Service.class);
    }
}
