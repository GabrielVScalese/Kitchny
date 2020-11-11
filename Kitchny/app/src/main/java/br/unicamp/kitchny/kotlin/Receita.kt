package br.unicamp.kitchny.kotlin

import kotlin.Exception

class Receita (nome: String, rendimento: String, modoDePreparo: String, imagem: String, avaliacao: Float) : Cloneable
{
    var nome: String = ""
        set(value)
        {
            if(value.isBlank())
                throw Exception("Nome inválido")

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
                throw Exception("Nome inválido - não há letras")

            field = value
        }



    var rendimento: String = ""
        set(value)
        {
            field = value
        }

    var modoDePreparo: String = ""
        set(value)
        {
            field = value
        }

    var imagem: String = ""
        set(value)
        {
            field = value
        }

    var avaliacao: Float = 0.0F
        set(value)
        {
            field = value
        }


    init
    {
        this.nome = nome
        this.imagem = imagem
        this.rendimento = rendimento
        this.modoDePreparo = modoDePreparo
        this.avaliacao = avaliacao
    }

    constructor(nome: String) : this(nome, "", "", "", 0.0F)
    constructor(receita: Receita) : this(receita.nome, receita.rendimento, receita.modoDePreparo, receita.imagem, receita.avaliacao)

    override fun equals(other: Any?): Boolean
    {
        val outra: Receita = other as Receita

        return (this.nome == outra.nome && this.imagem == outra.imagem && this.rendimento == outra.rendimento && this.modoDePreparo == outra.modoDePreparo && this.avaliacao == outra.avaliacao)
    }

    override fun hashCode(): Int
    {
        var ret = 17

        ret = ret * 17 + this.nome.hashCode()
        ret = ret * 17 + this.imagem.hashCode()
        ret = ret * 17 + this.rendimento.hashCode()
        ret = ret * 17 + this.modoDePreparo.hashCode()
        ret = ret * 17 + this.avaliacao.hashCode()

        if (ret < 0)
            ret = -ret

        return ret
    }

    override fun toString(): String
    {
        return "Nome: ${this.nome} | Imagem: ${this.imagem} | Rendimento: ${this.rendimento} | Modo de Preparo: ${this.modoDePreparo} | Avaliacao: ${this.avaliacao}"
    }

    override fun clone(): Any
    {
        var ret: Receita? = null
        try
        {
            ret = Receita(this)
        }
        catch (ex: Exception)
        {}

        return ret as Any
    }
}