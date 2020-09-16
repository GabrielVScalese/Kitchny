package br.unicamp.kitchny.kotlin

import kotlin.Exception

class Usuario (email: String, nome: String, senha: String) : Cloneable
{
    private var email: String = ""
    set(value)
    {
        if(value.isBlank() || !value.contains('@'))
            throw Exception("Email inválido")

        field = value
    }

    private var nome: String = ""
    set(value)
    {
        if(value.isBlank())
            throw Exception("Nome inválido")

        var temLetra = false
        for(c: Char in value)
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

    private var senha: String = ""
    set(value)
    {
        if(value.isBlank() || value.length < 8)
            throw Exception("Senha inválida")

        field = value
    }


    init
    {
        this.nome = nome
        this.email = email
        this.senha = senha
    }

    constructor(email: String, senha: String) : this(email, "UsuarioProvisorio12345", senha)
    constructor(usuario: Usuario) : this(usuario.email, usuario.nome, usuario.senha)

    override fun equals(other: Any?): Boolean
    {
        val outro: Usuario = other as Usuario

        return (this.nome == outro.nome && this.email == outro.email && this.senha == outro.senha)
    }

    override fun hashCode(): Int
    {
        var ret = 17

        ret = ret * 17 + this.nome.hashCode()
        ret = ret * 17 + this.email.hashCode()
        ret = ret * 17 + this.senha.hashCode()

        if (ret < 0)
            ret = -ret

        return ret
    }

    override fun toString(): String
    {
        return "Email: ${this.email} | Nome: ${this.nome} | Senha: ${this.senha}"
    }

    override fun clone(): Any
    {
        var ret: Usuario? = null
        try
        {
            ret = Usuario(this)
        }
        catch (ex: Exception)
        {}

        return ret as Any
    }
}