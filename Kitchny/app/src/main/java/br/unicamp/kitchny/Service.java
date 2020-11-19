package br.unicamp.kitchny;

import java.util.List;

import br.unicamp.kitchny.kotlin.Compra;
import br.unicamp.kitchny.kotlin.Ingrediente;
import br.unicamp.kitchny.kotlin.Receita;
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

    @GET("listaDeCompras/{email}")
    Call<List<Compra>> getCestaDeCompras (@Path("email") String email);

    @GET("receitas")
    Call<List<Receita>> getReceitas();

    @GET("receita/{nomeReceita}")
    Call<Receita> getReceita (@Path("nomeReceita") String nomeReceita);

    @GET("receitaFrom/{pesquisa}")
    Call<List<Receita>> getReceitaFromPesquisa (@Path("pesquisa") String pesquisa);

    @GET("ingredientesReceita/{nomeReceita}")
    Call<List<Ingrediente>> getIngredientesFromNomeReceita (@Path("nomeReceita") String nomeReceita);
}
