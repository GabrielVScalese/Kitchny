create table Kitchny.dbo.Ingredientes
(
    id int identity primary key not null,
    nome varchar(100) not null,
    receita int not null,
    quantidade varchar(20) not null,
    constraint fkReceita foreign key(receita) references Receitas(id)
)

select * from Kitchny.dbo.Ingredientes

drop table Kitchny.dbo.Ingredientes

drop table Kitchny.dbo.ListaDeCompras

create table Kitchny.dbo.ListaDeCompras
(
    primary key(idUsuario, idIngrediente),
    idUsuario int not null,
    idIngrediente int not null,
    quantidade varchar(20) not null,
    constraint fkIdIngrediente foreign key(idIngrediente) references Ingredientes(id),
    constraint fkIdUsuario foreign key(idUsuario) references Usuarios(id)
)

create table Kitchny.dbo.Receitas
(
    id int identity primary key not null,
    nome varchar(100) not null,
    rendimento varchar(50) not null,
    modoDePreparo varchar(100000) not null,
    avaliacao float null
)

drop table Kitchny.dbo.Receitas


select * from Kitchny.dbo.Receitas

