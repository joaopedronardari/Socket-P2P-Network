EP 1 - DE REDES

Grupo:
Adriano dos Santos Rodrigues da Silva - 8623910 
Guilherme Hernandes - 8598631
João Pedro Nardari - 8623865

=====================================================
1 - Classes
2 - Como compilar
3 - Como executar
4 - Observações
=====================================================

1 - Classes

1.1 - entity.User
	Nesta classe estão agrupadas todos os atributos de cada usuário, como seu nome, senha, IP, porta em que espera conexões e sua lista de amigos. 

1.2 - gui.WindowLogin
	Esta classe representa a tela de login, contendo os campos Usuário (nome), senha e ip do servidor. Todos os dados são informados pelo usuário 
	e só realiza o logon quando o usuário dgitar um usuário e senha válido e um ip de servidor que esteja rodando e esperando conexão na porta 10000.

1.3 - gui.WindowListFriends
	Aqui temos a lista dos usuários que são amigos do usuário que está logado no cliente. A lista exibe todos os contatos e informa quais estão 
	online e quais estão offline. Só é possível conversar com os que estão online, clicando na lista. Algumas vezes há uma certa demora em atulizar o status,
	porém isto é devido ao método de atualização do componente que usamos (JList) da classe Swing não ser thread-safe, e portanto há necessidade da thread que
	atualiza esperar em uma fila. Contudo, sempre em menos de (30 segundos)
	 
1.4 - gui.WindowTalk
	Esta tela implementa a conversa, com o usuário digitando as mensagens que deseja enviar para o outro e exibindo todas as mensagens da conversa 
	(recebidas e enviadas). Caso ocorra um erro pelo usuário estar offline e a lista ainda não estar atualizada, a mensagem não é enviada.

1.5 - socket.client.KeepAlive
	Aqui temos a thread do cliente que fica enviando mensagens de se o cliente está no ar. Enviamos a cada 0,5 segundo uma requisição, de maneira que o 
	cliente	não fique por muito tempo desatualizado com o status dos amigos. A cada requisição abrimos uma nova conexão, para não deixar a conexão aberta 
	por muito tempo, já que o servidor só atualiza o status como offline depois de não receber 10 mensagens de keep alive (5 segundos),e manter muitas
	conexões simultâneas pode acarretar em perda de performance.

1.6 - socket.client.ReceiveMsg
	Esta é a thread responsável por receber todas as mensagens enviadas pelos outros peers. Ela possui uma lista com todas as janelas de chat abertas e
	redireciona para a janela correta. Caso não houver janela entre estes peers, esta thread abre uma nova janela de conversa.

1.7 - socket.PutUserInactive
	Esta é a thread do servidor que verifica há quanto tempo os clientes não enviaram uma mensagem de keep alive, removendo-o da lista de usuários online

1.8 - socket.server.RequestType
	Esta classe é só um enumerador para cada tipo de mensagem, somente para abstrair parte do protocolo. 
	
1.9 - socket.server.Server
	Este é o servidor, responsável por enviar para os usuários e amigos em qual porta escutar/enviar as mensagens, receber as conexões dos usuários e enviar
	os amigos online/offline de cada peer.
	
4 - Observações
	- A atulização da lista pode demorar alguns segundos (cerca de menos de 15 segundos) devido ao método utilizado da classe Swing não ser thread-safe
	- No envio das mensagens, favor não pressionar o enter, pois ficará com uma linha vazia e sua mensagem (antes do enter) não será enviada para o outro
	peer, pois o programa interpreta que é uma linha vazia, já que pega somente  última linha (podendo acarretar em outros problemas).
	- Acontece problemas quando há mais de um servidor rodando no mesmo ip, já que a porta é fixa, sendo lançada uma exceção por duas aplicações escutando 
	na mesma porta
	- Rodar o .bat do servidor e o jar da aplicação no diretório que foi enviado, já que pode acarretar em problemas caso tentar rodar fora, principalmente
	o .bat que compila os arquivos antes de rodar.