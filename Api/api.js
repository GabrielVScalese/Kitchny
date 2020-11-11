"use strict";

const express = require("express");
const app = express();
const bodyParser = require("body-parser");
const port = 3000;
const connStr =
  "Server=regulus.cotuca.unicamp.br;Database=BD19171;User Id=BD19171;Password=COTUCA78911INFO;";
const sql = require("mssql");

const fs = require("fs");
var receitas = JSON.parse(fs.readFileSync("receitas.json"));

sql
  .connect(connStr)
  .then((conn) => (global.conn = conn))
  .catch((err) => console.log("erro: " + err));

app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());

app.listen(port);
console.log("API funcionando!");

const router = express.Router();

// Executa alguma query
async function execSQL(sqlQry) {
  try {
    return await global.conn.request().query(sqlQry);
  } catch (error) {
    console.log(error);
  }
}

// Rota raiz
router.get("/", (req, res) => {
  res.json({ mensagem: "API funcionando" });
});

/////////////////////////////////////////////////////////////////////////////// Receitas

// Retorna todas as receitas
router.get("/api/receitas", async (req, res) => {
  try {
    let response = await execSQL("SELECT * FROM KITCHNY.DBO.RECEITAS");

    let receitas = [];
    for(let i = 0; i < response.recordset.length; i++)
    {
      let receita = {nome: response.recordset[i].nome, rendimento: response.recordset[i].rendimento, modoDePreparo: response.recordset[i].modoDePreparo,
                    imagem: response.recordset[i].imagem, avaliacao: response.recordset[i].avaliacao};

      receitas.push(receita);
    }
    return res.json(receitas);
  } catch (error) {
    return res.status(500).send({ status: "Erro na busca de receitas!" });
  }
});

// Retorna uma receita com um determinado nome
router.get("/api/receita/:id?", async (req, res) => {
  try {
    let nomeReceita = req.params.id;

    const receita = await getReceita(nomeReceita);
    
    return res.json(receita);
  } catch (error) {
    return res.status(500).send({ status: "Erro na busca de uma receita!" });
  }
});

async function getReceita (receita)
{
  const response = await execSQL(
    "SELECT * FROM KITCHNY.DBO.RECEITAS WHERE NOME = " +
      "'" +
      receita +
      "'"
  );
  
  if (response.rowsAffected == 0)
  {
    return undefined;
  }
  else
     return response.recordset[0];
}

// Retorna receitas associadas aos ingredientes
router.get("/api/receitasFromIngredientes", async (req, res) => {
  try {
    let ingredientes = req.body.ingredientes;
    let receitas = [];

    for (let i = 0; i < ingredientes.length; i++) {
      let idReceita = await idReceitaFromIngrediente(ingredientes[i]);
      let nomeReceita = await nomeReceitaFromIngrediente(idReceita);
      receitas.push(nomeReceita);
    }

    let obj = { receitas };

    return res.json(obj);
  } catch (error) {
    return res.status(500).send({ status: "Erro na busca de receitas!" });
  }
});

async function nomeReceitaFromIngrediente(idReceita) {
  const response = await execSQL(
    "SELECT NOME FROM KITCHNY.DBO.RECEITAS WHERE ID = " + idReceita
  );

  return response.recordset[0];
}

// Retorna o idDaReceita
async function idReceitaFromIngrediente(ingrediente) {
  const response = await execSQL(
    "SELECT RECEITA FROM KITCHNY.DBO.INGREDIENTES WHERE NOME = " +
      "'" +
      ingrediente +
      "'"
  );

  return response.recordset[0].RECEITA;
}

// Retorna todos os ingredientes de uma receita
router.get("/api/ingredientesReceita/:id?", async (req, res) => {
  try {
    let nomeReceita = req.params.id;
    const id = await getIdReceita(nomeReceita);
    const response = await execSQL(
      "SELECT * FROM KITCHNY.DBO.INGREDIENTES WHERE RECEITA = " + id
    );

    return res.json(response.recordset);
  } catch (error) {
    return res
      .status(500)
      .send({ status: "Erro na busca de ingredienes de uma receita" });
  }
});

async function getIdReceita(nomeReceita) {
  const response = await execSQL(
    "SELECT * FROM KITCHNY.DBO.RECEITAS WHERE NOME = " + "'" + nomeReceita + "'"
  );

  return response.recordset[0].id;
}

// Insere uma receita
router.post("/api/insertReceita", async (req, res) => {
  try {
    let nome = req.body.nome;
    let rendimento = req.body.rendimento;
    let modoDePreparo = req.body.modoDePreparo;
    let avaliacao = null;

    await execSQL(
      "INSERT INTO KITCHNY.DBO.RECEITAS VALUES (" +
        "'" +
        nome +
        "'" +
        "," +
        "'" +
        rendimento +
        "'" +
        "," +
        "'" +
        modoDePreparo +
        "'" +
        "," +
        avaliacao +
        ")"
    );

    return res.status(200).send({ status: "Receita incluída!" });
  } catch (error) {
    return res.status(500).send({ status: "Erro na inclusão de receita" });
  }
});

// Registra 1300 receitas no BD
router.post("/api/insertReceitas", async function (req, res) {
  try {
    for (let i = 0; i < receitas.length; i++) {
      let nomeReceita = receita[i];
      let rendimento = "";
      if (receita[i].secao[1] != undefined)
        var modoDePreparo = obterModoDePreparo(receita[i]);
      else continue;
      let avaliacao = null;
      await execSQL(
        "INSERT INTO KITCHNY.DBO.RECEITAS (NOME, RENDIMENTO, MODODEPREPARO, AVALIACAO) VALUES (" +
          "'" +
          nomeReceita +
          "'" +
          "," +
          "'" +
          rendimento +
          "'" +
          "," +
          "'" +
          modoDePreparo +
          "'" +
          "," +
          "'" +
          avaliacao +
          "'" +
          ")"
      );
    }

    return res.status(200).send({ status: "Receitas incluídas!" });
  } catch (error) {
    return res.status(500).send({ status: "Erro na inclusão de receitas" });
  }
});

// Retorna o modo de preparo de uma receita
function obterModoDePreparo(receita) {
  let modoDePreparo = "";
  for (let i = 0; i < receita.secao[1].conteudo.length; i++)
    modoDePreparo += receita.secao[1].conteudo[i] + "\n";

  return modoDePreparo;
}

// Exclui uma receita
router.delete("/api/deleteReceita/:id?", async (req, res) => {
  try {
    let nomeReceita = req.params.id;

    if (await getReceita(nomeReceita) == undefined)
        return res.status(404).send({status: "Receita inexistente!"});

    console.log("a");
    await execSQL(
      "DELETE FROM KITCHNY.DBO.RECEITAS WHERE NOME = " + "'" + nomeReceita + "'"
    );

    return res.status(200).send({ status: "Receita excluída!" });
  } catch (error) {
    return res.status(500).send({ status: "Erro na exclusão de receita" });
  }
});

// Exclui todas as receitas
router.delete("/api/deleteReceitas", async (req, res) => {
  try {
    await execSQL("DELETE FROM KITCHNY.DBO.RECEITAS");

    return res.status(200).send({ status: "Receitas excluída!" });
  } catch (error) {
    return res.status(500).send({ status: "Erro na exclusão de receitas" });
  }
});

/////////////////////////////////////////////////////////////////////////////// Ingredientes

// Retorna todos os ingredientes
router.get("/api/ingredientes", async (req, res) => {
  try {
    const response = await execSQL("SELECT * FROM KITCHNY.DBO.INGREDIENTES");

    return res.json(response.recordset);
  } catch (error) {
    return res.status(500).send({ status: "Erro na busca de ingredientes!" });
  }
});

// Retorna um ingrediente com um determinado nome
router.get("/api/ingrediente/:id?", async (req, res) => {
  try {
    let nome = req.params.id;
    const response = await execSQL(
      "SELECT * FROM KITCHNY.DBO.INGREDIENTES WHERE NOME = " + "'" + nome + "'"
    );

    return res.json(response.recordset[0]);
  } catch (error) {
    return res.status(500).send({ status: "Erro na busca de ingrediente!" });
  }
});

// Registra os ingredientes do JSON no BD (não funciona corretamente)
router.post("/api/insertIngredientes", async (req, res) => {
  try {
    for (let i = 0; i < receitas.length; i++) {
      var ingredientes = await obterIngrediente(receitas[i]);
      await registrarIngredientes(ingredientes);
    }

    return res.status(200).send({ status: "Ingredientes incluídos!" });
  } catch (error) {
    return res.status(500).send({ status: "Erro na inclusão de ingredientes" });
  }
});

// Registra um grupo de ingredientes (não funciona corretamente)
async function registrarIngredientes(ingredientes) {
  for (let i = 0; i < ingredientes.length; i++) {
    await execSQL(
      "INSERT INTO KITCHNY.DBO.INGREDIENTES VALUES (" +
        "'" +
        ingredientes[i].ingrediente +
        "'" +
        "," +
        ingredientes[i].idReceita +
        "," +
        "'" +
        ingredientes[i].quantidade +
        "'" +
        ")"
    );
  }
}

// Retorna um conjunto de ingredientes de uma receita (não funciona corretamente)
async function obterIngrediente(receita) {
  let ingredientes = [];
  for (let i = 0; i < receita.secao[0].conteudo.length; i++) {
    var obj = {
      idReceita: 0,
      ingrediente: "",
      quantidade: "",
    };

    var aux = await execSQL(
      "SELECT ID FROM KITCHNY.DBO.RECEITAS WHERE NOME = " +
        "'" +
        receita.nome +
        "'"
    );

    obj.idReceita = aux.recordset[0].ID;
    var linha = receita.secao[0].conteudo[i];

    if (linha.indexOf("g")) {
      linha = receita.secao[0].conteudo[i].split("de");
      obj.quantidade = linha[0];
      obj.ingrediente = linha[1];
    } else if (linha.indexOf("ml")) {
      linha = receita.secao[0].conteudo[i].split("de");
      obj.quantidade = linha[0];
      obj.ingrediente = linha[1];
    } else {
      if (linha.match(/(\d+)/)) {
        linha = receita.secao[0].conteudo[i];
        let matches = linha.match(/(\d+)/);
        let cadeia = receita.secao[0].conteudo[i].split(matches[0]);
        obj.quantidade = matches;
        obj.ingrediente = linha[0];
      } else {
        obj.ingrediente = linha;
        obj.quantidade = "";
      }
    }

    ingredientes.push(obj);
  }

  return ingredientes;
}

/////////////////////////////////////////////////////////////////////////////// Usuários

// Retorna todos os usuários
router.get("/api/usuarios", async (req, res) => {
  try {
    const response = await execSQL("SELECT * FROM KITCHNY.DBO.USUARIOS");

    return res.json(response.recordset);
  } catch (error) {
    return res.status(500).send({ status: "Erro na busca de alunos!" });
  }
});

// Retorna um usuário com um determinado email
router.get("/api/usuario/:id?", async (req, res) => {
  try {
    let email = req.params.id;
    const response = await execSQL(
      "SELECT * FROM KITCHNY.DBO.USUARIOS WHERE EMAIL = " + "'" + email + "'"
    );

    return res.json(response.recordset[0]);
  } catch (error) {
    return res.status(500).send({ status: "Erro na busca de usuário!" });
  }
});


async function getUsuario (email)
{
  const response = await execSQL(
    "SELECT * FROM KITCHNY.DBO.USUARIOS WHERE EMAIL = " + "'" + email + "'"
  );

  if (response == undefined)
      return undefined;
  else
      return response.recordset[0];
}

// Insere um usuário
router.post("/api/insertUsuario", async (req, res) => {
  try {
    let usuario = req.body;

    await execSQL(
      "INSERT INTO KITCHNY.DBO.USUARIOS VALUES (" +
        "'" +
        usuario.email +
        "'" +
        "," +
        "'" +
        usuario.nome +
        "'" +
        "," +
        "'" +
        usuario.senha +
        "'" +
        "," 
        +
        usuario.qtdReceitasAprovadas +
        "," +
        usuario.qtdReceitasReprovadas +
        "," +
        usuario.qtdReceitasPublicadas +
        "," +
        usuario.notaMediaReceitas +
        ")"
    );

    return res.status(200).send({ status: "Aluno incluído!" });
  } catch (error) {
    return res.status(500).send({ status: "Erro na inserção de usuário!" });
  }
});

// Altera o campo nome e senha do usuario
router.put("/api/updateUsuario", async (req, res) => {
  try {
    const usuario = req.body;
    
    if (getUsuario(usuario.email) == undefined)
        return res.status(404).send({status: "Usuário inexistente"});

    await execSQL(
      "UPDATE KITCHNY.DBO.USUARIOS \n SET NOME = " +
        "'" +
        usuario.nome +
        "', " +
        "SENHA = " +
        "'" +
        usuario.senha +
        "' WHERE EMAIL = " +
        "'" +
        usuario.email +
        "';"
    );

    return res.status(200).send({ status: "Usuário alterado!" });
  } catch (error) {
    return res.status(500).send({ status: "Erro na alteração de usuário" });
  }
});

// Autentica um usuário a partir da senha
router.post("/api/autenticateUsuario", async (req, res) => {
  try {
    const usuario = req.body;
    const responseSenha = await execSQL(
      "SELECT SENHA FROM KITCHNY.DBO.USUARIOS WHERE EMAIL = " +
        "'" +
        usuario.email +
        "'"
    );
    if (responseSenha.recordset[0].SENHA == usuario.senha)
      return res.status(200).send({ status: "Senha correta!" });
    else return res.status(404).send({ status: "Senha incorreta!" });
  } catch (error) {
    return res.status(500).send({ status: "Erro na busca de usuário" });
  }
});

router.delete("/api/deleteUsuario/:id?", async (req, res) => {
  try
  {
    const email = req.params.id;
    await execSQL ("DELETE FROM KITCHNY.DBO.USUARIOS WHERE EMAIL = '" + email + "'");
    
    return res.status(200).send({status: "Usuário excluído!"});
  }
  catch (error)
  {
    return res.status(500).send({status: "Erro na exclusão de usuário!"});
  }
})

/////////////////////////////////////////////////////////////////////////////// ListaDeCompras

async function getIngrediente(id)
{
  const res = await execSQL("SELECT * FROM KITCHNY.DBO.INGREDIENTES WHERE ID = " + id);
  const ingrediente = res.recordset[0];
  
  return ingrediente;
}

// Retorna uma lista de compras de um determinado usuário (email)
router.get("/api/listaDeCompras/:id?", async (req, res) => {
  try {
    let email = req.params.id;
    const usuario = await getUsuario(email)
    const response = await execSQL(
      "SELECT * FROM KITCHNY.DBO.LISTADECOMPRAS WHERE IDUSUARIO = " + usuario.id
    );
    
    let listaDeCompras = response.recordset;

    for(let i = 0; i < listaDeCompras.length; i++)
    {
      const ingrediente = await getIngrediente(response.recordset[0].idIngrediente);
      listaDeCompras[i] = {nomeIngrediente: ingrediente.nome, quantidade: response.recordset[0].quantidade};
    }
    
    return res.json(listaDeCompras);
  } catch (error) {
    return res
      .status(500)
      .send({ status: "Erro na busca de lista de compras!" });
  }
});

async function getIdUsuario(email) {
  const responseInicial = await execSQL(
    "SELECT ID FROM KITCHNY.DBO.USUARIOS WHERE EMAIL = " + "'" + email + "'"
  );

  return responseInicial.recordset[0].ID;
}

// Insere uma lista de compras (arrumar)
router.post("/api/insertListaDeCompras", async (req, res) => {
  try {
    const listaDeCompras = req.body;
    await execSQL(
      "INSERT INTO KITCHNY.DBO.LISTADECOMPRAS VALUES (" +
        listaDeCompras.idUsuario +
        "," +
        listaDeCompras.idIngrediente +
        "," +
        "'" +
        listaDeCompras.quantidade +
        "'" +
        ")"
    );

    return res.status(200).send({ status: "Lista de compras inserida!" });
  } catch (error) {
    return res
      .status(500)
      .send({ status: "Erro na inclusão de lista de compras!" });
  }
});


app.use(router);
