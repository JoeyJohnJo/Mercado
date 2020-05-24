# Mercado
Aplicativo para gerenciamento de mercados, feito em colaboração com https://github.com/SrDiegoRocha. O programa consiste em dois modos:
Modo Vendas e Modo Administração.

### Instruções
Colocar o arquivo SQLite em uma pasta "mercado" na sua pasta de usuario. Exemplo: C:\Users\joeyj\mercado/bancodedasdos.sbd.db

Colocar o arquivo config.properties na mesma pasta "mercado" que o arquivo SQLite.

Colocar a pasta lib no mesmo diretório que o jar final.

Caso não rode simplesmente clickando no jar, utilize o arquivo .bat para inicialização

### Modo Vendas
Formado por uma tela onde são inseridos os produtos que serão vendidos para o cliente, listando-os e atualizando o preço final da compra
conforme mais produtos são adicionados. Ao final da listagem de produtos, é carregado uma tela mostrando o preço final e uma caixa de texto
onde será inserido a quantidade de dinheiro que o comprador entregou, para assim mostrar quanto deve ser entregue de troco. Quando uma
compra é completada, atualiza o banco de dados com as novas quantidades dos produtos em estoque, e o histórico de vendas.

### Modo ADM
Contém diferentes telas com diversas funções para auxiliar no manejamento dos produtos do mercado, incluindo cadastro e visualização de
produtos e clientes, histórico de todas as vendas feitas e resumos periódicos de renda e gastos. Notificações aparecem na tela inicial
caso a quantidade de algum produto em estoque esteja abaixo do estipulado nas configurações.

### Configurações
A tela de configurações permite o usuario a definir seus próprios atalhos para acessar cada tela usando apenas o teclado. Mostra as
opções de temas dispiníveis (único finalizado é o __escuro - roxo__), e estipular a quantidade mínima no estoque antes que apareça
as notificações de aviso na tela inicial.

### Desenvolvimento
Desenvolvido com JavaFX e SQLite, este projeto contém uma estruturação mais organizada que os passados, seguindo o modelo MVC, e uso de
FXML para o GUI. Algumas janelas de popup customizadas foram feitas usando Java. Há estrutura para adicionar mais temas ao aplicativo, 
porém o unico tema finalizado é o __escuro - roxo__.
