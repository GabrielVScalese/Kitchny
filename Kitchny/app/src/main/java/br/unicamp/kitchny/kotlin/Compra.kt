package br.unicamp.kitchny.kotlin

class Compra(nomeIngrediente: String, quantidade: String) : Cloneable
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

    constructor(compra: Compra) : this(compra.nomeIngrediente, compra.quantidade)

    override fun equals(other: Any?): Boolean
    {
        val compra = other as Compra
        if(this.nomeIngrediente == compra.nomeIngrediente && this.quantidade == compra.quantidade)
            return true

        return false
    }

    override fun hashCode(): Int
    {
        var ret = 17

        ret = ret * 17 + this.nomeIngrediente.hashCode()
        ret = ret * 17 + this.quantidade.hashCode()

        if(ret < 0)
            ret = -ret

        return ret
    }

    override fun toString(): String
    {
        return "Nome: ${this.nomeIngrediente} | Quantidade: ${this.quantidade}"
    }

    override fun clone(): Any
    {
        var ret: Compra? = null
        try
        {
            ret = Compra(this)
        }
        catch(ignored: Exception){}

        return ret as Compra
    }
}