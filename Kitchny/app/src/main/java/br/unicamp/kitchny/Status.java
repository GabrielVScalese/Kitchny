package br.unicamp.kitchny;

public class Status
{
    private String status;

    public Status (String status) throws Exception
    {
        setStatus(status);
    }

    public void setStatus (String status) throws Exception
    {
        if (status == null || status.equals(""))
            throw new Exception ("Status invalido");

        this.status = status;
    }
}
