"use strict";

const express = require("express");
const app = express();
const bodyParser = require("body-parser");
const port = 3000;
const connStr =
  "Server=regulus.cotuca.unicamp.br;Database=BD19171;User Id=BD19171;Password=;";
const sql = require("mssql");

const fs = require("fs");
const { execFile, exec } = require("child_process");
const { RSA_NO_PADDING } = require("constants");
var result = fs.readFileSync("receitas.json");
var receitas = JSON.parse(result);

sql
  .connect(connStr)
  .then((conn) => (global.conn = conn))
  .catch((err) => console.log("erro: " + err));

app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());
const router = express.Router();

app.listen(port);
console.log("API funcionando!");

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
app.use("/", router);

/////////////////////////////////////////////////////////////////////////////// Receitas

// Retorna todas as receitas
router.get("/api/receitas", async (req, res) => {
  let query = "SELECT * FROM KITCHNY.DBO.RECEITAS";
  let response = await execSQL(query);
  return res.json(response.recordset);
});

// Retorna uma receita com um determinado nome
router.get("/api/receita/:id?", async (req, res) => {
  try {
    let nomeReceita = req.params.id;
    let query =
      "SELECT * FROM KITCHNY.DBO.RECEITAS WHERE NOME = " +
      "'" +
      nomeReceita +
      "'";
    const response = await execSQL(query);
    return res.json(response.recordset[0]);
  } catch (error) {
    console.log(error);
    res.json(404);
  }
});

// Retorna todos os ingredientes de uma receita
router.get("/api/ingredientesReceita/:id?", async (req, res) => {
  try {
    let nomeReceita = req.params.id;
    const id = await getIdReceita(nomeReceita);
    let query = "SELECT * FROM KITCHNY.DBO.INGREDIENTES WHERE RECEITA = " + id;
    const response = await execSQL(query);
    return res.json(response.recordset);
  } catch (error) {
    console.log(error);
    return res.json(404);
  }
});

async function getIdReceita(nomeReceita) {
  try {
    let query =
      "SELECT * FROM KITCHNY.DBO.RECEITAS WHERE NOME = " +
      "'" +
      nomeReceita +
      "'";
    const response = await execSQL(query);
    return response.recordset[0].id;
  } catch (error) {
    console.log(error);
  }
}

// Insere uma receita
router.post("/api/insertReceita", async (req, res) => {
  try {
    let nome = req.body.nome;
    let rendimento = req.body.rendimento;
    let modoDePreparo = req.body.modoDePreparo;
    let avaliacao = 6;

    let query =
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
      ")";

    await execSQL(query);
    return res.json(200);
  } catch (error) {
    console.log(error);
    res.json(404);
  }
});

// Registra 1300 receitas no BD
router.post("/api/insertReceitas", async function (req, res) {
  try {
    for (let i = 0; i < receitas.length; i++) {
      let receita = receitas[i];
      console.log(receita);
      let nomeReceita = receita.nome;
      let rendimento = "";
      if (receita.secao[1] != undefined)
        var modoDePreparo = obterModoDePreparo(receita);
      else continue;
      let avaliacao = 6;
      let query =
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
        ")";

      await execSQL(query);
    }

    return res.json(200);
  } catch (error) {
    console.log(error);
    res.json(404);
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
router.delete("api/deleteReceita/:id?", async (req, res) => {
  try {
    let nomeReceita = req.params.id;
    let query = "DELETE FROM KITCHNY.DBO.RECEITAS WHERE NOME = " + nomeReceita;
    await execSQL(query);
    return res.json(200);
  } catch (error) {
    console.log(error);
    res.json(404);
  }
});

/////////////////////////////////////////////////////////////////////////////// Ingredientes

// Retorna todos os ingredientes
router.get("/api/ingredientes", async (req, res) => {
  try {
    let query = "SELECT * FROM KITCHNY.DBO.INGREDIENTES";
    const response = await execSQL(query);
    return res.json(response.recordset);
  } catch (error) {
    console.log(error);
    res.json(404);
  }
});

// Retorna um ingrediente com um determinado nome
router.get("/api/ingrediente/:id?", async (req, res) => {
  try {
    let nome = req.params.id;
    let query =
      "SELECT * FROM KITCHNY.DBO.INGREDIENTES WHERE NOME = " + "'" + nome + "'";
    const response = await execSQL(query);
    return res.json(response.recordset[0]);
  } catch (error) {
    console.log(error);
    res.json(404);
  }
});

// Registra os ingredientes do JSON no BD (não funciona corretamente)
router.post("/api/insertIngredientes", async (req, res) => {
  try {
    var receita = receitas[0];
    let ingredientes = await obterIngrediente(receita);
    console.log(ingredientes);
    //await registrarIngredientes(ingredientes);
    /*for (let i = 0; i < receitas.length; i++) {
      //console.log(receita);
      var ingredientes = await obterIngrediente(receita);
      console.log(ingredientes);
      await registrarIngredientes(ingredientes);
    }*/

    return res.json(200);
  } catch (error) {
    //console.log(error);
    res.json(404);
  }
});

// Registra um grupo de ingredientes (não funciona corretamente)
async function registrarIngredientes(ingredientes) {
  try {
    for (let i = 0; i < ingredientes.length; i++) {
      var obj = ingredientes[i];
      let query =
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
        ")";
      await execSQL(query);
    }
  } catch (error) {
    //console.log(error);
  }
}

// Retorna um conjunto de ingredientes de uma receita (não funciona corretamente)
async function obterIngrediente(receita) {
  try {
    var ingredientes = [];
    var obj = {
      idReceita: 0,
      ingrediente: "",
      quantidade: "",
    };
    for (let i = 0; i < receita.secao[0].conteudo.length; i++) {
      var aux = await execSQL(
        "SELECT ID FROM KITCHNY.DBO.RECEITAS WHERE NOME = " +
          "'" +
          receita.nome +
          "'"
      );
      obj.idReceita = aux.recordset[0].ID;
      //obj.ingrediente = receita.secao[0].conteudo[i];
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
  } catch (error) {
    //console.log(error);
  }
}

/////////////////////////////////////////////////////////////////////////////// Usuários

// Retorna todos os usuários
router.get("/api/usuarios", async (req, res) => {
  try {
    let query = "SELECT * FROM KITCHNY.DBO.USUARIOS";
    const response = await execSQL(query);
    return res.json(response.recordset);
  } catch (error) {
    console.log(error);
    return res.json(404);
  }
});

// Retorna um usuário com um determinado email
router.get("/api/usuario/:id?", async (req, res) => {
  try {
    let email = req.params.id;
    let query =
      "SELECT * FROM KITCHNY.DBO.USUARIOS WHERE EMAIL = " + "'" + email + "'";
    const response = await execSQL(query);
    return res.json(response.recordset[0]);
  } catch (error) {
    console.log(error);
    return res.json(404);
  }
});

// Insere um usuário
router.post("/api/insertUsuario", async (req, res) => {
  try {
    let usuario = req.body;
    let query =
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
      ");";
    await execSQL(query);
    return res.json(200);
  } catch (error) {
    console.log(error);
    return res.json(404);
  }
});

// Autentica um usuário a partir da senha
router.post("/api/autenticateUsuario", async (req, res) => {
  try {
    let email = req.body.email;
    let senha = req.body.senha;
    let query =
      "SELECT SENHA FROM KITCHNY.DBO.USUARIOS WHERE EMAIL = " +
      "'" +
      email +
      "'";
    const responseSenha = await execSQL(query);
    if (responseSenha.recordset[0].SENHA == senha) return res.json(true);
    else return res.json(false);
  } catch (error) {
    console.log(error);
    return res.json(404);
  }
});

/////////////////////////////////////////////////////////////////////////////// ListaDeCompras

// Retorna uma lista de compras de um determinado usuário (email)
router.get("/api/listaDeCompras/:id?", async (req, res) => {
  try {
    let email = req.params.id;
    let queryInicial =
      "SELECT ID FROM KITCHNY.DBO.USUARIOS WHERE EMAIL = " + "'" + email + "'";
    const responseInicial = await execSQL(queryInicial);
    let obj = responseInicial.recordset[0];
    let queryFinal =
      "SELECT * FROM KITCHNY.DBO.LISTADECOMPRAS WHERE IDUSUARIO = " + obj.ID;
    const responseFinal = await execSQL(queryFinal);
    return res.json(responseFinal.recordset);
  } catch (error) {
    console.log(error);
    return res.json(404);
  }
});

// Insere uma lista de compras
router.post("/api/insertListaDeCompras", async (req, res) => {
  try {
    let idUsuario = req.body.idUsuario;
    let idIngrediente = req.body.idIngrediente;
    let quantidade = req.body.quantidade;
    let query =
      "INSERT INTO KITCHNY.DBO.LISTADECOMPRAS VALUES (" +
      idUsuario +
      "," +
      idIngrediente +
      "," +
      "'" +
      quantidade +
      "'" +
      ")";
    await execSQL(query);
    return res.json(200);
  } catch (error) {
    console.log(error);
    return res.json(404);
  }
});
