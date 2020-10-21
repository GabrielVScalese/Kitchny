package br.unicamp.kitchny.kotlin

class Compra(nomeIngrediente: String, quantidade: String)
{
    var nomeIngrediente = ""
    set(value)
    {
        if(value.isBlank())
            throw Exception("Parâmetro estava em branco")

        field = value
    }

    var quantidade = ""
    set(value)
    {
        if(value.isBlank())
            throw Exception("Parâmetro estava em branco")
        

        field = value
    }
}