package br.unicamp.kitchny.kotlin

import kotlin.Exception

class Status (status: String) : Cloneable
{
    var status: String = ""
        set(value)
        {
            status = value
        }


    init
    {
        this.status = status
    }

    constructor(esse: Status) : this(esse.status)

    override fun equals(other: Any?): Boolean
    {
        val outra: Status = other as Status

        return (this.status == outra.status)
    }

    override fun hashCode(): Int
    {
        var ret = 17

        ret = ret * 17 + this.status.hashCode()

        if (ret < 0)
            ret = -ret

        return ret
    }

    override fun toString(): String
    {
        return "status: ${this.status}"
    }

    override fun clone(): Any
    {
        var ret: Status? = null
        try
        {
            ret = Status (this)
        }
        catch (ex: Exception)
        {}

        return ret as Any
    }
}