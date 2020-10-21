package br.unicamp.kitchny.kotlin

class Compra(nomeIngrediente: String, quantidade: String)
{
    var nomeIngrediente = ""
    set(value)
    {
        if(value.isBlank())
            throw Exception("Parâmetro estava em branco")
        for(c in value)
        {
            if (!c.isLetter())
            {
                throw Exception("Um ingrediente não pode conter números")
            }
        }

        field = value
    }

    var quantidade = ""
    set(value)
    {
        if(value.isBlank())
            throw Exception("Parâmetro estava em branco")
        for(char in value)
        {
            if(char.isLetter())
                throw Exception("Quantidade não possui letras")
        }

        field = value
    }

    init
    {
        this.nomeIngrediente = nomeIngrediente
        this.quantidade = quantidade
    }
}