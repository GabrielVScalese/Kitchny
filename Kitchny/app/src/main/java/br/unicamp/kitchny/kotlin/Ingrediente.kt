package br.unicamp.kitchny.kotlin

import kotlin.Exception

class Ingrediente (nomeIngrediente: String, quantidade: String, modoDePreparo: String) : Cloneable
{
    var nomeIngrediente: String = ""
        set(value)
        {
            if(value.isBlank())
                throw Exception("nomeIngrediente inválido")

            var temLetra = false
            for(c in value)
            {
                if(c.isLetter())
                {
                    temLetra = true
                    break
                }
            }
            if(!temLetra)
                throw Exception("nomeIngrediente inválido - não há letras")

            field = value
        }


    var quantidade: String = ""
        set(value)
        {
            field = value
        }

    var modoDePreparo: String = ""
        set(value)
        {
            field = value
        }


    init
    {
        this.nomeIngrediente = nomeIngrediente
        this.quantidade = quantidade
        this.modoDePreparo = modoDePreparo
    }

    constructor(nomeIngrediente: String) : this(nomeIngrediente, "", "")
    constructor(ingrediente: Ingrediente) : this(ingrediente.nomeIngrediente, ingrediente.quantidade, ingrediente.modoDePreparo)

    override fun equals(other: Any?): Boolean
    {
        val outra: Ingrediente = other as Ingrediente

        return (this.nomeIngrediente == outra.nomeIngrediente && this.quantidade == outra.quantidade)
    }

    override fun hashCode(): Int
    {
        var ret = 17

        ret = ret * 17 + this.nomeIngrediente.hashCode()
        ret = ret * 17 + this.quantidade.hashCode()

        if (ret < 0)
            ret = -ret

        return ret
    }

    override fun toString(): String
    {
        return "nomeIngrediente: ${this.nomeIngrediente} | quantidade: ${this.quantidade}"
    }

    override fun clone(): Any
    {
        var ret: Ingrediente? = null
        try
        {
            ret = Ingrediente (this)
        }
        catch (ex: Exception)
        {}

        return ret as Any
    }
}