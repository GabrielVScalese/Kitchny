"use strict";

const express = require("express");
const app = express();
const bodyParser = require("body-parser");
const port = 3000;
const connStr =
  "Server=regulus.cotuca.unicamp.br;Database=BD19171;User Id=BD19171;Password=COTUCA78911INFO;";
const sql = require("mssql");

const fs = require("fs");
const { exec } = require("child_process");
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
router.get("/api/receita/:nomeReceita", async (req, res) => {
  try {
    let nomeReceita = req.params.nomeReceita;

    const receita = await getReceitaFromNomeReceita(nomeReceita);

    if (receita === undefined)
        return res.status(404).send({status: "Receita não encontrada!"});
    
	let objReceita = {nome: receita.nome, rendimento: receita.rendimento, modoDePreparo: receita.modoDePreparo, imagem: receita.imagem, avaliacao: receita.avaliacao}
	
    return res.json(objReceita);
  } catch (error) {
	  console.log(error);
    return res.status(500).send({ status: "Erro na busca de uma receita!" });
  }
});

async function getReceitaFromNomeReceita (nomeReceita)
{
  const response = await execSQL(
    "SELECT * FROM KITCHNY.DBO.RECEITAS WHERE NOME LIKE '%" + nomeReceita + "%'"
  );
  
  if (response.rowsAffected === 0)
  {
    return undefined;
  }
  else
     return response.recordset[0];
}

// Retorna receitas a partir de uma pesquisa
router.get("/api/receitaFromPesquisa/:pesquisa", async(req, res) => {
  try{
    let pesquisa = req.params.pesquisa

    let idReceita = await getIdReceitaFromNomeIngrediente(pesquisa);
    let receita1;

    if (idReceita === undefined)
        receita1 = undefined
    else
        receita1 = await getReceitaFromIdReceita (idReceita)

    let receita2 = await getReceitaFromNomeReceita(pesquisa);

    let receitas = [];

    if (receita1 !== undefined)
        receitas.push(receita1);

    if (receita2 !== undefined)
        receitas.push(receita2);

    return res.json(receitas);
  }
  catch (error) {
    return res.status(500).send({ status: "Erro na busca de receitas!" });
  }
})

async function getIdReceitaFromNomeIngrediente (nomeIngrediente)
{
  const response = await execSQL("SELECT RECEITA FROM KITCHNY.DBO.INGREDIENTES " +
      "WHERE NOME LIKE '%" + nomeIngrediente + "%'")

  if (response.recordset.length === 0)
      return undefined

  return response.recordset[0].RECEITA;
}

async function getReceitaFromIdReceita (idReceita)
{
  const response = await execSQL("SELECT * FROM KITCHNY.DBO.RECEITAS WHERE ID = " + idReceita)

  return response.recordset[0]
}

async function getIdReceitaFromNomeReceita(nomeReceita)
{
  const response = await execSQL("SELECT ID FROM KITCHNY.DBO.RECEITAS WHERE NOME LIKE '%" + nomeReceita + "%'");

  if(response.recordset.length === 0)
    return undefined;

  return response.recordset[0].ID;
}

// Retorna todos os ingredientes de uma receita
router.get("/api/ingredientesReceita/:id?", async (req, res) => {
  try {
    let nomeReceita = req.params.id;
    const id = await getIdReceitaFromNomeReceita(nomeReceita);
    const response = await execSQL(
      "SELECT * FROM KITCHNY.DBO.INGREDIENTES WHERE RECEITA = " + id
    );
	
	let ingredientes = []
	
	for (let i = 0; i < response.recordset.length; i++)
	{
		let obj = {nomeIngrediente: response.recordset[i].nome, quantidade: response.recordset[i].quantidade}
		ingredientes.push(obj)
	}
	
    return res.json(ingredientes);
  } catch (error) {
    return res
      .status(500)
      .send({ status: "Erro na busca de ingredienes de uma receita" });
  }
});

// Atualiza a avaliação de uma receita
router.post("/api/updateAvaliacao", async (req, res) => {
  try{
    let avaliacao = req.body.avaliacao;
    let nomeReceita = req.body.nome;
    let notaAtual = await getAvaliacaoFromNomeReceita(nomeReceita);
    let media = ((notaAtual + avaliacao) / 2.0);
    

    await execSQL("UPDATE KITCHNY.DBO.RECEITAS SET AVALIACAO = " + media);

    return res.status(200).send({ status: "Avaliação alterada com sucesso!" });
  }
  catch(error){
    return res.status(500).send({ status: "Erro ao alterar avaliação!" });
  }
})

async function getAvaliacaoFromNomeReceita(nomeReceita)
{
  const response = await execSQL("SELECT AVALIACAO FROM KITCHNY.DBO.RECEITAS WHERE NOME LIKE '%" + nomeReceita + "%'");

  if(response.recordset.length === 0)
    return undefined;

  

  return response.recordset[0].AVALIACAO;
}

// Insere uma receita
router.post("/api/insertReceita", async (req, res) => {
  try {
    let nome = req.body.nome;
    let rendimento = req.body.rendimento;
    let modoDePreparo = req.body.modoDePreparo;
    let imagem = req.body.imagem;
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
        "'" + 
        imagem +
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
      let nomeReceita = receitas[i].nome;
      let rendimento = "";
      let imagem = "";
      if (receitas[i].secao[1] != undefined)
        var modoDePreparo = obterModoDePreparo(receitas[i]);
      else continue;
      let avaliacao = null;
      await execSQL(
        "INSERT INTO KITCHNY.DBO.RECEITAS (NOME, RENDIMENTO, MODODEPREPARO, IMAGEM, AVALIACAO) VALUES (" +
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
          imagem +
          "'" +
          "," +
          avaliacao +
          ")"
      );
    }

    return res.status(200).send({ status: "Receitas incluídas!" });
  } catch (error) {
    console.log(error);
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

/*// Registra os ingredientes do JSON no BD (não funciona corretamente)
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
      "SELECT ID FROM KITCHNY.DBO.RECEITAS WHERE NOME LIKE '%" +
        receita.nome +
        "%'"
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
}*/

router.post("/api/insertIngredientes/:nomeReceita", async(req, res) => {
  try {
    let ingredientes = req.body;
    let nomeReceita = req.params.nomeReceita;
    
    let idReceita = await getIdReceitaFromNomeReceita(nomeReceita);

    for (let i = 0; i < ingredientes.length; i++) {
      await execSQL(
        "INSERT INTO KITCHNY.DBO.INGREDIENTES VALUES (" +
          "'" +
          ingredientes[i].nomeIngrediente +
          "'" +
          "," +
          idReceita +
          "," +
          "'" +
          ingredientes[i].quantidade +
          "'" +
          ")"
      );
    }

    return res.status(200).send({ status: "Ingredientes incluídos!" });
  }
  catch (error){
    return res.status(500).send({ status: "Erro na inserção de ingredientes!" });
  }
})

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

router.post("/api/updateAprovacao/:email", async (req, res) => {
  try{
    let email = req.params.email;
    const qtdReceitasAprovadasAtual = await getQtdReceitasAprovadas(email)
    await updateQtdReceitasAprovadas(qtdReceitasAprovadasAtual + 1)

    return res.status(200).send({ status: "Avaliacao alterada!" });
  }
  catch (error){
    return res.status(500).send({ status: "Erro na atualizacao de quantidades de receitas aprovadas" });
  }
})

async function getQtdReceitasAprovadas (email)
{
  const response = await execSQL("SELECT QTDRECEITASAPROVADAS * FROM KITCHNY.DBO.USUARIOS WHERE EMAIL = '" + email + "'")

  return response.recordset[0];
}

async function updateQtdReceitasAprovadas(qtdReceitasAprovadas)
{
  await execSQL("UPDATE KITCHNY.DBO.USUARIOS SET QTDRECEITASAPROVADAS = " + qtdReceitasAprovadas)
}

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
