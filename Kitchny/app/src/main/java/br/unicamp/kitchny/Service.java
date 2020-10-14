package br.unicamp.kitchny;

import java.util.List;

import br.unicamp.kitchny.kotlin.Usuario;
import retrofit.Call;
import retrofit.http.*;

public interface Service {

    /*@GET("get")
    Call<List<Receitas>> getAll ();*/

    /*@GET("getNome/{nome}")
    Call<Produto> getNome (@Path("nome") String nome);*/

    @POST("autenticateUsuario")
    Call<Status> autenticarUsuario (@Body Usuario usuario);

    @POST("insertUsuario")
    Call<Status> insertUsuario (@Body Usuario usuario);

    @GET("usuario/{email}")
    Call<Usuario> getUsuario (@Path("email") String email);
}
