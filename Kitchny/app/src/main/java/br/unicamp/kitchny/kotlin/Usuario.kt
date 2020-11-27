package br.unicamp.kitchny.kotlin

import kotlin.Exception

class Usuario (email: String, nome: String, senha: String, qtdReceitasAprovadas: Int, qtdReceitasReprovadas: Int, qtdReceitasPublicadas: Int, notaMediaReceitas: Float) : Cloneable
{
    var email: String = ""
        set(value)
        {
            /*if(value.isBlank() || !value.contains('@'))
                throw Exception("Email inválido")*/

            field = value
        }

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

    var qtdReceitasAprovadas: Int = 0
        set(value)
        {
            if(value < 0)
                throw Exception("Quantidade inválida")

            field = value
        }

    var qtdReceitasReprovadas: Int = 0
        set(value)
        {
            if(value < 0)
                throw Exception("Quantidade inválida")

            field = value
        }

    var senha: String = ""
        set(value)
        {
            /*if(value.isBlank() || value.length < 8)
                throw Exception("Senha inválida")*/

            field = value
        }

    var notaMediaReceitas: Float = 0.0F
        set(value)
        {
            if(value < 0.0F)
                throw Exception("Nota inválida")

            field = value
        }

    var qtdReceitasPublicadas: Int = 0
        set(value)
        {
            if(value < 0)
                throw Exception("Quantidade inválida")

            field = value
        }


    init
    {
        this.nome = nome
        this.email = email
        this.senha = senha
        this.qtdReceitasAprovadas = qtdReceitasAprovadas
        this.qtdReceitasReprovadas = qtdReceitasReprovadas
        this.notaMediaReceitas = notaMediaReceitas
        this.qtdReceitasPublicadas = qtdReceitasPublicadas
    }

    constructor(email: String, senha: String) : this(email, "UsuarioProvisorio12345", senha, 0, 0, 0, 0.0F)
    constructor(usuario: Usuario) : this(usuario.email, usuario.nome, usuario.senha, usuario.qtdReceitasAprovadas, usuario.qtdReceitasReprovadas, usuario.qtdReceitasPublicadas, usuario.notaMediaReceitas)
    constructor(email: String, nome: String, senha: String) : this(email, nome, senha, 0, 0, 0, 0.0F)

    override fun equals(other: Any?): Boolean
    {
        val outro: Usuario = other as Usuario

        return (this.nome == outro.nome && this.email == outro.email && this.senha == outro.senha && this.qtdReceitasAprovadas == outro.qtdReceitasAprovadas && this.qtdReceitasReprovadas == outro.qtdReceitasReprovadas && this.notaMediaReceitas == outro.notaMediaReceitas && this.qtdReceitasPublicadas == outro.qtdReceitasPublicadas)
    }

    override fun hashCode(): Int
    {
        var ret = 17

        ret = ret * 17 + this.nome.hashCode()
        ret = ret * 17 + this.email.hashCode()
        ret = ret * 17 + this.senha.hashCode()
        ret = ret * 17 + this.qtdReceitasAprovadas.hashCode()
        ret = ret * 17 + this.qtdReceitasReprovadas.hashCode()
        ret = ret * 17 + this.notaMediaReceitas.hashCode()
        ret = ret * 17 + this.qtdReceitasPublicadas.hashCode()

        if (ret < 0)
            ret = -ret

        return ret
    }

    override fun toString(): String
    {
        return "Email: ${this.email} | Nome: ${this.nome} | Senha: ${this.senha} | Quantidade de Receitas Aprovadas: ${this.qtdReceitasAprovadas} | Quantidade de Receitas Reprovadas: ${this.qtdReceitasReprovadas} | Nota Média das Receitas: ${this.notaMediaReceitas} | Quantidade de Receitas Publicadas: ${this.qtdReceitasPublicadas}"
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